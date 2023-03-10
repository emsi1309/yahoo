/**
 * Copyright (C) 2022 Yahoo Japan Corporation. All Rights Reserved.
 */
package com.example.yahoo.repository;

import com.example.yahoo.util.ValuesHolder;

/**
 * Utility method collection for Java Sample Program.
 */
public class ValuesRepositoryFacade {

    private ValuesHolder valuesHolder;
    private FeedValuesRepository feedFolderValueRepository;
    private CampaignValuesRepository campaignValuesRepository;
    private BiddingStrategyValuesRepository biddingStrategyValuesRepository;
    private AdGroupValuesRepository adGroupValuesRepository;
    private AdGroupAdValuesRepository adGroupAdValuesRepository;
    private AdGroupCriterionValuesRepository adGroupCriterionValuesRepository;
    private FeedItemValuesRepository feedItemValuesRepository;
    private PageFeedAssetSetValuesRepository pageFeedAssetSetValuesRepository;

    /**
     * ValuesRepositoryFacade constructor.
     *
     * @param valuesHolder ValuesHolder
     */
    public ValuesRepositoryFacade(ValuesHolder valuesHolder) {
        this.valuesHolder = valuesHolder;
        this.feedFolderValueRepository = new FeedValuesRepository(this.valuesHolder);
        this.campaignValuesRepository = new CampaignValuesRepository(this.valuesHolder);
        this.biddingStrategyValuesRepository = new BiddingStrategyValuesRepository(this.valuesHolder);
        this.adGroupValuesRepository = new AdGroupValuesRepository(this.valuesHolder);
        this.adGroupAdValuesRepository = new AdGroupAdValuesRepository(this.valuesHolder);
        this.adGroupCriterionValuesRepository = new AdGroupCriterionValuesRepository(this.valuesHolder);
        this.feedItemValuesRepository = new FeedItemValuesRepository(this.valuesHolder);
        this.pageFeedAssetSetValuesRepository = new PageFeedAssetSetValuesRepository(this.valuesHolder);
    }

    /**
     * @return ValuesHolder
     */
    public ValuesHolder getValuesHolder() {
        return this.valuesHolder;
    }

    /**
     * @return FeedValueRepository
     */
    public FeedValuesRepository getFeedValueRepository() {
        return this.feedFolderValueRepository;
    }

    /**
     * @return BiddingStrategyValuesRepository
     */
    public BiddingStrategyValuesRepository getBiddingStrategyValuesRepository() {
        return this.biddingStrategyValuesRepository;
    }

    /**
     * @return CampaignValuesRepository
     */
    public CampaignValuesRepository getCampaignValuesRepository() {
        return this.campaignValuesRepository;
    }

    /**
     * @return AdGroupValuesRepository
     */
    public AdGroupValuesRepository getAdGroupValuesRepository() {
        return this.adGroupValuesRepository;
    }

    /**
     * @return AdGroupAdValuesRepository
     */
    public AdGroupAdValuesRepository getAdGroupAdValuesRepository() {
        return this.adGroupAdValuesRepository;
    }

    /**
     * @return AdGroupCriterionValuesRepository
     */
    public AdGroupCriterionValuesRepository getAdGroupCriterionValuesRepository() {
        return this.adGroupCriterionValuesRepository;
    }

    /**
     * @return FeedItemValuesRepository
     */
    public FeedItemValuesRepository getFeedItemValuesRepository() {
        return this.feedItemValuesRepository;
    }

    /**
     * @return PageFeedAssetSetValuesRepository
     */
    public PageFeedAssetSetValuesRepository getPageFeedAssetSetValuesRepository() {
        return this.pageFeedAssetSetValuesRepository;
    }

}
