package demo.service;

import demo.domain.Product;
import demo.dto.DealInfo;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by jiaxu on 8/9/17.
 */
public interface DealService {

    List<DealInfo> findAllDealsOrderBy(int page, int size, String sortBy);

    List<DealInfo> findDealsByCategoryIdOrderBy(int categoryId, int page, int size, String sortBy);

    List<DealInfo> searchDealsByTitle(String query, String sortBy);
}
