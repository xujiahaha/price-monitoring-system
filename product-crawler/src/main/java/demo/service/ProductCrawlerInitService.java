package demo.service;

import java.util.List;

/**
 * Created by jiaxu on 8/12/17.
 */
public interface ProductCrawlerInitService {

    void init(String proxyFilePath);

    List<String> getProxyList();

    int getNumOfProxy();

}
