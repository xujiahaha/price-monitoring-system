package demo.task;

import demo.model.CategorySeed;
import demo.service.ProductSendService;
import demo.model.ProductInfo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URLDecoder;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by jiaxu on 8/5/17.
 */
@Data
@Slf4j
public class ProductCrawler implements Runnable{

    private long id;

    private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.95 Safari/537.36";
    private static final int CRAWLER_TIMEOUT = 10000; // in ms
    private List<String> proxyList;
    private static final int MAX_PAGE_OF_PRODUCT = 5;

    private ProductSendService productSendService;
    private CategorySeed categorySeed;
    AtomicInteger proxyIndex;

    public ProductCrawler(CategorySeed categorySeed) {
        this.categorySeed = categorySeed;
    }

    @Override
    public void run() {
        this.crawling(categorySeed);
    }

    private static final String[] TITLE_SELECTORS = {
            "#result_{INDEX} > div > div > div > div.a-fixed-left-grid-col.a-col-right > div.a-row.a-spacing-small > div:nth-child(1) > a",
            "#result_{INDEX} > div > div:nth-child(3) > div.a-row.a-spacing-none.a-spacing-top-mini > a"
    };

    private static final String[] THUMBNAIL_SELECTORS_1= {
            "#result_{INDEX} > div > div > div > div.a-fixed-left-grid-col.a-col-left > div > div > a > img",
            "#result_{INDEX} > div > div:nth-child(2) > div > div > a > img",
    };

    private static final String[] THUMBNAIL_SELECTORS_2= {
            "#rot-{ASIN} > div > a > div.s-card.s-card-group-rot-{ASIN}.s-active > img"
    };


    private void crawling(CategorySeed categorySeed) {
        Map<String, String> headers = getHeaders();
        String productListUrl = categorySeed.getProductListUrl();
        log.info("Thread {} is crawling {} category", Thread.currentThread().getName(), categorySeed.getCategoryTitle());
        for(int pageNum = 1; pageNum <= MAX_PAGE_OF_PRODUCT; pageNum++) {
            String url = productListUrl + "&page=" + pageNum;
            System.out.println("url: " + url);
            try {
                setProxyHost();
                Document doc =Jsoup.connect(url).headers(headers).userAgent(USER_AGENT).maxBodySize(0).timeout(CRAWLER_TIMEOUT).get();
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
//                System.out.println("index starts from "+ startIndex);

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


                    ProductInfo productInfo = new ProductInfo(categorySeed.getCategoryId());
                    productInfo.setProductId(asin);
                    productInfo.setTitle(title);
                    productInfo.setPrice(price);
                    productInfo.setDetailUrl(detailUrl);
                    productInfo.setThumbnail(thumbnail);
                    productInfo.setBrand(getBrand(doc, index));

                    // send productMsg to category based queue
                    this.productSendService.sendProdToQueue(categorySeed.getSearchAlias(), productInfo);
                }
                Thread.sleep(3000);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
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
        Element priceWholeEle = prod.getElementsByClass("a-price-whole").first();
        Element priceFracEle = prod.getElementsByClass("a-price-fraction").first();
        if(priceWholeEle == null || priceFracEle == null) {
            priceWholeEle = prod.getElementsByClass("sx-price-whole").first();
            priceFracEle = prod.getElementsByClass("sx-price-fractional").first();
        }
        if(priceWholeEle != null && priceFracEle != null) {
            String priceWholeStr = priceWholeEle.text().replace(".", "").replace(",", "");
            String priceFracStr = priceFracEle.text();
            String priceStr = priceWholeStr+"."+priceFracStr;
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

    private void setProxyHost() {
//        Random rand = new Random();
//        System.setProperty("socksProxyHost", proxyList.get(rand.nextInt(proxyList.size()))); // set socks proxy server
        System.setProperty("socksProxyHost", proxyList.get(proxyIndex.get()));
        if (proxyIndex.get() == proxyList.size() - 1) {
            proxyIndex.set(0);
        } else {
            proxyIndex.incrementAndGet();
        }
    }

    private Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<String,String>();
        headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        headers.put("Accept-Language", "en-US,en;q=0.8");
        return headers;
    }
}
