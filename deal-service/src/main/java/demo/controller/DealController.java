package demo.controller;

import demo.domain.Product;
import demo.dto.DealInfo;
import demo.service.DealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by jiaxu on 8/9/17.
 */
@RestController
public class DealController {

    // Default value for pageable
    private static final String DEFAULT_PAGE_NUMBER = "0";
    private static final String DEFAULT_PAGE_SIZE = "15";
    private static final String DEFAULT_SORT_BY = "discountPercent";

    private DealService dealService;

    @Autowired
    public DealController(DealService dealService) {
        this.dealService = dealService;
    }

    @RequestMapping(value = "/deals", method = RequestMethod.GET)
    @ResponseBody
    public List<DealInfo> getDeals(@RequestParam(value = "q", required = false, defaultValue = "") String q,
                                  @RequestParam(value = "page", required = false, defaultValue = DEFAULT_PAGE_NUMBER) int page,
                                  @RequestParam(value = "size", required = false, defaultValue = DEFAULT_PAGE_SIZE) int size,
                                  @RequestParam(value = "sortBy", required = false, defaultValue = DEFAULT_SORT_BY) String sortBy) {
        if(q.length() == 0) {
            return this.dealService.findAllDealsOrderBy(page, size, sortBy);
        }
        return this.dealService.searchDealsByTitle(q, sortBy);
    }

    @RequestMapping(value = "/deals/{categoryId}", method = RequestMethod.GET)
    @ResponseBody
    public List<DealInfo> getDealsByCategoryId(@PathVariable("categoryId") int categoryId,
                                   @RequestParam(value = "page", required = false, defaultValue = DEFAULT_PAGE_NUMBER) int page,
                                   @RequestParam(value = "size", required = false, defaultValue = DEFAULT_PAGE_SIZE) int size,
                                   @RequestParam(value = "sortBy", required = false, defaultValue = DEFAULT_SORT_BY) String sortBy) {
        return this.dealService.findDealsByCategoryIdOrderBy(categoryId, page, size, sortBy);
    }
}
