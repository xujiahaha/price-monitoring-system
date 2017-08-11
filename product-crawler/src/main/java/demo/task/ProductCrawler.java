package demo.task;

import demo.ProductSender;
import demo.domain.CategorySeed;
import demo.model.ProductInfo;
import demo.service.CategorySeedService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.print.DocFlavor;
import java.io.*;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URLDecoder;
import java.util.*;

/**
 * Created by jiaxu on 8/5/17.
 */
@Component
public class ProductCrawler {

    private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.95 Safari/537.36";
    private final String AUTH_USER = "bittiger";
    private final String AUTH_PASSWORD = "cs504";
    private static final int CRAWLER_TIMEOUT = 80000; // in ms
    private List<String> proxyList;
    private String PROXY_FILE_PATH = "proxylist.txt";
    private static final int MAX_PAGE_OF_PRODUCT = 2;

    private CategorySeedService categorySeedService;
    private ProductSender productSender;

    @Autowired
    public ProductCrawler(CategorySeedService categorySeedService, ProductSender productSender) {
        this.categorySeedService = categorySeedService;
        this.productSender = productSender;
    }

    public void init() {
        initProxyList(PROXY_FILE_PATH);
        testProxy();
    }

    private static final String[] TITLE_SELECTORS = {
            "#result_{INDEX} > div > div > div > div.a-fixed-left-grid-col.a-col-right > div.a-row.a-spacing-small > div:nth-child(1) > a",
            "#result_{INDEX} > div > div:nth-child(3) > div.a-row.a-spacing-none.a-spacing-top-mini > a"
    };

    private static final String[] PRICE_SELECTORS = {
           "#result_{INDEX} > div > div.a-row.a-spacing-top-small > div:nth-child(2) > a > span"
    };

    private static final String[] THUMBNAIL_SELECTORS_1= {
            "#result_{INDEX} > div > div > div > div.a-fixed-left-grid-col.a-col-left > div > div > a > img",
            "#result_{INDEX} > div > div:nth-child(2) > div > div > a > img",
    };

    private static final String[] THUMBNAIL_SELECTORS_2= {
            "#rot-{ASIN} > div > a > div.s-card.s-card-group-rot-{ASIN}.s-active > img"
    };



    public void crawling(int categoryId) {
        setProxyHost();
        Map<String, String> headers = getHeaders();
        CategorySeed seed = this.categorySeedService.getSeedByCategoryId(categoryId);
        String productListUrl = seed.getProductListUrl();
        for(int pageNum = 2; pageNum <= MAX_PAGE_OF_PRODUCT; pageNum++) {
            String url = productListUrl + "&page=" + pageNum;
            System.out.println("url: " + url);
            try {
                Document doc = Jsoup.connect(url).maxBodySize(0).headers(headers).userAgent(USER_AGENT).timeout(CRAWLER_TIMEOUT).get();
                if(doc == null) {
                    // log failure
                    continue;
                }
//                Elements prods = doc.getElementsByClass("s-result-item celwidget ");
                Elements prods = doc.select("li[data-asin]");
                System.out.println("number of prod: " + prods.size());
                if(prods.size() == 0) {
                    continue;
                }
                // get first result index in current page.
                String id = prods.first().attr("id");
                int startIndex = Integer.parseInt(id.substring(7));
                System.out.println("index starts from "+ startIndex);

                for(int i = 0; i < prods.size(); i++) {
                    int index = startIndex + i;
                    System.out.println("index: " + index);

                    String asin = getAsin(doc, index);
                    if(asin == null || asin.length() == 0) {
                        // log error
                        continue;
                    }

                    String title = getTitle(doc, index);
                    System.out.println("title: " + title);
                    if(title == null || title.length() == 0) {
                        // log error
                        continue;
                    }

                    double price = getPrice(doc, index);
                    System.out.println("price: " + price);
                    if(price <= 0) {
                        // log error
                        continue;
                    }

                    String detailUrl = getDetailUrl(doc, index);
                    System.out.println("detailUrl: " + detailUrl);
                    if(detailUrl == null || detailUrl.length() == 0) {
                        // log error
                        continue;
                    }

                    String thumbnail = getThumbnailUrl(doc, index, asin);
                    System.out.println("thumbnail: " + thumbnail);
                    if(thumbnail == null || thumbnail.length() == 0) {
                        // log error
                        continue;
                    }


                    ProductInfo productInfo = new ProductInfo(seed.getCategoryId());
                    productInfo.setProductId(asin);
                    productInfo.setTitle(title);
                    productInfo.setPrice(price);
                    productInfo.setDetailUrl(detailUrl);
                    productInfo.setThumbnail(thumbnail);
                    productInfo.setBrand(getBrand(doc, index));

                    // send productMsg to category based queue
                    this.productSender.sendProdToQueue(seed.getSearchAlias(), productInfo);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getAsin(Document doc, int index) {
        String queryStr = "#result_" + index;
        Element titleEle = doc.select(queryStr).first();
        if(titleEle != null) {
            return titleEle.attr("data-asin");
        } else {
            return null;
        }
    }

    private String getTitle(Document doc, int index) {
        for(String selector : TITLE_SELECTORS) {
            String queryStr= selector.replace("{INDEX}", String.valueOf(index));
            Element titleEle = doc.select(queryStr).first();
            if(titleEle != null) {
                return titleEle.attr("title");
            }
        }
        return null;
    }

    private String getDetailUrl(Document doc, int index) {
        for(String selector : TITLE_SELECTORS) {
            String queryStr= selector.replace("{INDEX}", String.valueOf(index));
            Element detailUrlEle = doc.select(queryStr).first();
            if(detailUrlEle != null) {
                String detailUrl = detailUrlEle.attr("href");
                return normalizeDetailUrl(detailUrl);
            }
        }
        return null;
    }

    private String getBrand(Document doc, int index) {
        String queryStr = "#result_" + index + " > div > div > div > div.a-fixed-left-grid-col.a-col-right > div.a-row.a-spacing-small > div:nth-child(2) > span:nth-child(2)";
        Element brandEle = doc.select(queryStr).first();
        if(brandEle != null) {
            return brandEle.text();
        } else {
            return "";
        }
    }

    private double getPrice(Document doc, int index) {
        String id = "result_" + index;
        Element prod = doc.getElementById(id);
        Element priceWholeEle = prod.getElementsByClass("sx-price-whole").first();
        Element priceFractionalEle = prod.getElementsByClass("sx-price-fractional").first();
        if(priceWholeEle != null && priceFractionalEle != null) {
            String priceStr = priceWholeEle.text().replace(",", "")+"."+priceFractionalEle.text();
            return Double.parseDouble(priceStr);
        } else {
            return 0;
        }
    }

    private String getThumbnailUrl(Document doc, int index, String asin) {
        for(String selector : THUMBNAIL_SELECTORS_1) {
            String queryStr= selector.replace("{INDEX}", String.valueOf(index));
            Element thumbnailUrlEle = doc.select(queryStr).first();
            if(thumbnailUrlEle != null) {
                return thumbnailUrlEle.attr("src");
            }
        }
        for(String selector : THUMBNAIL_SELECTORS_2) {
            String queryStr= selector.replace("{ASIN}", asin);
            Element thumbnailUrlEle = doc.select(queryStr).first();
            if(thumbnailUrlEle != null) {
                return thumbnailUrlEle.attr("src");
            }
        }
        return null;
    }

    private String normalizeDetailUrl(String url) {
        if (url.contains("url")) {
            // extract url from redirected url
            url = decodeRedirectUrl(url);
        }
        int index = url.indexOf("/ref=");
        if(index > 0) {
            return url.substring(0, index);
        }
        return url;
    }

    private String decodeRedirectUrl(String url) {
        int start = url.indexOf("url=")+4;
        try {
            return URLDecoder.decode(url.substring(start), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    public void initProxyList(String proxyFilePath) {
        proxyList = new ArrayList<String>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(proxyFilePath));
            String line;
            while((line = reader.readLine()) != null) {
                proxyList.add(line.split(",")[0]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.setProperty("socksProxyPort", "61336"); // set socks proxy port
        System.setProperty("http.proxyUser", AUTH_USER);
        System.setProperty("http.proxyPassword", AUTH_PASSWORD);
        Authenticator.setDefault(
                new Authenticator() {
                    @Override
                    public PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(
                                AUTH_USER, AUTH_PASSWORD.toCharArray());
                    }
                }
        );
    }

    public void setProxyHost() {
        Random rand = new Random();
        System.setProperty("socksProxyHost", proxyList.get(rand.nextInt(proxyList.size()))); // set socks proxy server
    }

    public void testProxy() {

        String test_url = "http://www.toolsvoid.com/what-is-my-ip-address";
        try {
            HashMap<String,String> headers = new HashMap<String,String>();
            headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            headers.put("Accept-Language", "en-US,en;q=0.8");
            Document doc = Jsoup.connect(test_url).headers(headers).userAgent(USER_AGENT).timeout(CRAWLER_TIMEOUT).get();
            String iP = doc.select("body > section.articles-section > div > div > div > div.col-md-8.display-flex > div > div.table-responsive > table > tbody > tr:nth-child(1) > td:nth-child(2) > strong").first().text(); //get used IP.
            System.out.println("IP-Address: " + iP);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<String,String>();
        headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        headers.put("Accept-Language", "en-US,en;q=0.8");
        return headers;
    }
}
