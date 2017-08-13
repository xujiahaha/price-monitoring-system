package demo.service.impl;

import demo.service.ProductCrawlerInitService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by jiaxu on 8/12/17.
 */
@Service
public class ProductCrawlerInitServiceImpl implements ProductCrawlerInitService {

    private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.95 Safari/537.36";
    private final String AUTH_USER = "bittiger";
    private final String AUTH_PASSWORD = "cs504";
    private static final int CRAWLER_TIMEOUT = 10000; // in ms
    private List<String> proxyList;

    @Override
    public void init(String proxyFilePath) {
        initProxyList(proxyFilePath);
        testProxy();
    }

    @Override
    public List<String> getProxyList() {
        return this.proxyList;
    }

    @Override
    public int getNumOfProxy() {
        return this.proxyList.size();
    }

    private void initProxyList(String proxyFilePath) {
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
        System.out.println("proxy list has been initialized.");
    }

    private void testProxy() {

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

}
