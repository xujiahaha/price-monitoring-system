package demo.service.impl;

import demo.domain.Product;
import demo.domain.ProductRepository;
import demo.dto.DealInfo;
import demo.service.DealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiaxu on 8/9/17.
 */
@Service
public class DealServiceImpl implements DealService {

    private ProductRepository productRepository;

    @Autowired
    public DealServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<DealInfo> findAllDealsOrderBy(int page, int size, String sortBy) {
        Pageable pageable = new PageRequest(page, size, Sort.Direction.DESC, sortBy);
        return createDealInfo(this.productRepository.findByDiscountPercentGreaterThan(0, pageable));
    }

    @Override
    public List<DealInfo> findDealsByCategoryIdOrderBy(int categoryId, int page, int size, String sortBy) {
        Pageable pageable = new PageRequest(page, size, Sort.Direction.DESC, sortBy);
        return createDealInfo(this.productRepository.findByCategoryIdAndDiscountPercentGreaterThan(categoryId,0, pageable));
    }

    @Override
    public List<DealInfo> searchDealsByTitle(String query, String sortBy) {
        List<DealInfo> res = new ArrayList<>();
        query = query.replace(",", " ");
        String[] keywords = query.split("\\s+");
        StringBuilder regex = new StringBuilder();
        int i = 0;
        for(; i < keywords.length; i++) {
            if (keywords[i] != null && keywords[i].length() > 0) {
                regex.append(keywords[i]);
            }
            if(i != keywords.length-1) {
                regex.append("|");
            }
        }
        System.out.println(regex.toString());
        List<Product> products = this.productRepository.findByTitleRegex(regex.toString(), sortBy);
        for(Product product : products) {
            res.add(new DealInfo(product));
        }
        return res;
    }

    private List<DealInfo> createDealInfo(Page<Product> products) {
        List<Product> productContents = products.getContent();
        List<DealInfo> dealInfoList = new ArrayList<>();
        for(Product product : productContents) {
            dealInfoList.add(new DealInfo(product));
        }
        return dealInfoList;
    }
}
