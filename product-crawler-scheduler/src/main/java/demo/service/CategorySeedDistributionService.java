package demo.service;

import demo.domain.CategorySeed;

/**
 * Created by jiaxu on 8/12/17.
 */
public interface CategorySeedDistributionService {

    boolean sendCategorySeed(String crawlerLocation, CategorySeed categorySeed);

}
