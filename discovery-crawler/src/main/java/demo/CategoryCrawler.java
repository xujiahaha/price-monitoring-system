package demo;

import demo.domain.Category;
import demo.domain.CategorySeed;
import demo.service.CategorySeedService;
import demo.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.*;

/**
 * Created by jiaxu on 8/4/17.
 */
@Component
@Slf4j
public class CategoryCrawler {

    private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.95 Safari/537.36";
    private final String AUTH_USER = "bittiger";
    private final String AUTH_PASSWORD = "cs504";
    private static final String AMAZON_URL = "https://www.amazon.com/s/ref=nb_sb_noss?url=search-alias=aps&field-keywords=-4321";
    private static final String PRODUCT_LIST_URL = "https://www.amazon.com/s/ref=nb_sb_noss?url=<url>&field-keywords=-4321";
    private static final int CRAWLER_TIMEOUT = 80000; // in ms
    private List<String> proxyList;
    private String PROXY_FILE_PATH = "proxylist.txt";

    private CategoryService categoryService;
    private CategorySeedService categorySeedService;

    @Autowired
    public CategoryCrawler(CategoryService categoryService, CategorySeedService categorySeedService) {
        this.categoryService = categoryService;
        this.categorySeedService = categorySeedService;
    }

    public void init() {
        initProxyList(PROXY_FILE_PATH);
    }

    private int categoryId = 1;
    public List<Category> crawling() {
        setProxyHost();
        List<Category> result = new ArrayList<>();
        Map<String, String> headers = getHeaders();
        try {
            Document doc = Jsoup.connect(AMAZON_URL).maxBodySize(0).headers(headers).userAgent(USER_AGENT).timeout(CRAWLER_TIMEOUT).get();
            int i = 2;
            while(true) {
                String selector = "#searchDropdownBox > option:nth-child(" + i++ + ")";
                Element element = doc.select(selector).first();
                if(element == null) {
                    log.info("Retrieved all categories!");
                    break;
                }
                String categoryTitle = element.text();
                String value = element.attr("value");
                // eg. value = "search-alias=alexa-skills"
                String searchAlias = value.substring(value.indexOf("=")+1);
                String productListUrl = PRODUCT_LIST_URL.replace("<url>", value);
                Category category = new Category(categoryId, categoryTitle);
                this.categoryService.saveCategory(category);
                CategorySeed categorySeed = new CategorySeed(category);
                categorySeed.setSearchAlias(searchAlias);
                categorySeed.setProductListUrl(productListUrl);
                this.categorySeedService.saveCategorySeed(categorySeed);
                System.out.println(categoryId + " : " + element.text());
                categoryId++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
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
