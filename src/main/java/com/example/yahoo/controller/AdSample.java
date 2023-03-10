/**
 * Copyright (C) 2022 Yahoo Japan Corporation. All Rights Reserved.
 */
package com.example.yahoo.controller;

import com.example.yahoo.biddingstrategy.BiddingStrategyServiceSample;
import com.example.yahoo.repository.ValuesRepositoryFacade;
import com.example.yahoo.util.ApiUtils;
import com.example.yahoo.util.ValuesHolder;
import jp.co.yahoo.adssearchapi.v10.YahooJapanAdsApiClient;
import jp.co.yahoo.adssearchapi.v10.api.*;
import jp.co.yahoo.adssearchapi.v10.model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * example Ad operation and Utility method collection.
 */
public class AdSample {

  private static final YahooJapanAdsApiClient yahooJapanAdsApiClient = ApiUtils.getYahooJapanAdsApiClient();
  private static final AdGroupAdServiceApi adGroupAdService = new AdGroupAdServiceApi(yahooJapanAdsApiClient);
  private static final AdGroupBidMultiplierServiceApi adGroupBidMultiplierService = new AdGroupBidMultiplierServiceApi(yahooJapanAdsApiClient);
  private static final AdGroupCriterionServiceApi adGroupCriterionService = new AdGroupCriterionServiceApi(yahooJapanAdsApiClient);
  private static final AdGroupServiceApi adGroupService = new AdGroupServiceApi(yahooJapanAdsApiClient);
  private static final BiddingStrategyServiceApi biddingStrategyService = new BiddingStrategyServiceApi(yahooJapanAdsApiClient);
  private static final CampaignCriterionServiceApi campaignCriterionService = new CampaignCriterionServiceApi(yahooJapanAdsApiClient);
  private static final CampaignServiceApi campaignService = new CampaignServiceApi(yahooJapanAdsApiClient);
  private static final CampaignTargetServiceApi campaignTargetService = new CampaignTargetServiceApi(yahooJapanAdsApiClient);
  private static final FeedItemServiceApi feedItemService= new FeedItemServiceApi(yahooJapanAdsApiClient);

  /**
   * main method for AdSample
   *
   * @param args command line arguments
   */
  public static void main(String[] args) throws Exception {

    // =================================================================
    // Setting
    // =================================================================
    ValuesHolder valuesHolder = new ValuesHolder();
    ValuesRepositoryFacade valuesRepositoryFacade = new ValuesRepositoryFacade(valuesHolder);
    Long feedId = valuesRepositoryFacade.getPageFeedAssetSetValuesRepository().findPageFeedAssetSetId();

    long accountId = ApiUtils.ACCOUNT_ID;

    List<Long> feedIds = new ArrayList<>();
    feedIds.add(feedId);

    try {

      // =================================================================
      // BiddingStrategyService
      // =================================================================
      // ADD
      BiddingStrategyServiceOperation addRequestBiddingStrategy = BiddingStrategyServiceSample.buildExampleMutateRequest(
          accountId,
          Arrays.asList(BiddingStrategyServiceSample.createExampleMaximizeClicksBidding())
      );
      List<BiddingStrategyServiceValue> addResponseBiddingStrategy = biddingStrategyService.biddingStrategyServiceAddPost(addRequestBiddingStrategy).getRval().getValues();
      valuesRepositoryFacade.getValuesHolder().setBiddingStrategyServiceValueList(addResponseBiddingStrategy);

      Long biddingStrategyId = valuesRepositoryFacade.getBiddingStrategyValuesRepository().findBiddingStrategyId(BiddingStrategyServiceType.MAXIMIZE_CLICKS);

      // sleep 30 second.
      System.out.println("\n***** sleep 30 seconds *****\n");
      Thread.sleep(30000);

      // SET
      BiddingStrategyServiceOperation setRequestBiddingStrategy = BiddingStrategyServiceSample.buildExampleMutateRequest(
          accountId,
          BiddingStrategyServiceSample.createExampleSetRequest(valuesRepositoryFacade.getBiddingStrategyValuesRepository().getBiddingStrategies())
      );
      biddingStrategyService.biddingStrategyServiceSetPost(setRequestBiddingStrategy);

      // GET
      BiddingStrategyServiceSelector getRequestBiddingStrategy = BiddingStrategyServiceSample.buildExampleGetRequest(
          accountId,
          valuesRepositoryFacade.getBiddingStrategyValuesRepository().getBiddingStrategyIds()
      );
      biddingStrategyService.biddingStrategyServiceGetPost(getRequestBiddingStrategy);

      // sleep 30 second.
      System.out.println("\n***** sleep 30 seconds *****\n");
      Thread.sleep(30000);

      // =================================================================
      // CampaignService
      // =================================================================
      // ADD
      CampaignServiceOperation addRequestCampaign = CampaignServiceSample.buildExampleMutateRequest(
          accountId,
          new ArrayList<Campaign>() {{
            add(CampaignServiceSample.createExampleStandardCampaign(
                "SampleStandardCampaign_",
                feedIds,
                CampaignServiceSample.createPortfolioBiddingCampaignBiddingStrategy(biddingStrategyId))
            );
            add(CampaignServiceSample.createExampleMobileAppCampaignForIOS(
                "SampleMobileAppCampaignForIOS_",
                feedIds,
                CampaignServiceSample.createPortfolioBiddingCampaignBiddingStrategy(biddingStrategyId))
            );
            add(CampaignServiceSample.createExampleMobileAppCampaignForANDROID(
                "SampleMobileAppCampaignForANDROID_",
                feedIds,
                CampaignServiceSample.createPortfolioBiddingCampaignBiddingStrategy(biddingStrategyId))
            );
          }}
      );
      List<CampaignServiceValue> addResponseCampaign = campaignService.campaignServiceAddPost(addRequestCampaign).getRval().getValues();
      valuesRepositoryFacade.getValuesHolder().setCampaignServiceValueList(addResponseCampaign);

      Long campaignIdStandard = valuesRepositoryFacade.getCampaignValuesRepository().findCampaignId(CampaignServiceType.STANDARD);
      Long campaignIdMobileAppIOS = valuesRepositoryFacade.getCampaignValuesRepository().findCampaignId(CampaignServiceAppOsType.IOS);
      String appIdIOS = valuesRepositoryFacade.getCampaignValuesRepository().findAppId(campaignIdMobileAppIOS);
      Long campaignIdMobileAppAndroid = valuesRepositoryFacade.getCampaignValuesRepository().findCampaignId(CampaignServiceAppOsType.ANDROID);
      String appIdAndroid = valuesRepositoryFacade.getCampaignValuesRepository().findAppId(campaignIdMobileAppAndroid);

      // Check Status
      CampaignServiceSample.checkStatus(valuesRepositoryFacade.getCampaignValuesRepository().getCampaignIds());

      // SET
      CampaignServiceOperation setRequestCampaign = CampaignServiceSample.buildExampleMutateRequest(
          accountId,
          CampaignServiceSample.createExampleSetRequest(valuesRepositoryFacade.getCampaignValuesRepository().getCampaigns())
      );
      campaignService.campaignServiceSetPost(setRequestCampaign);

      // =================================================================
      // CampaignTargetService
      // =================================================================
      // ADD
      CampaignTargetServiceOperation addRequestCampaignTarget = CampaignTargetServiceSample.buildExampleMutateRequest(
          accountId,
          new ArrayList<CampaignTarget>() {{
            add(CampaignTargetServiceSample.createExampleScheduleTarget(accountId, campaignIdStandard));
            add(CampaignTargetServiceSample.createExampleLocationTarget(accountId, campaignIdStandard));
            add(CampaignTargetServiceSample.createExampleNetworkTarget(accountId, campaignIdStandard));
          }}
      );
      List<CampaignTargetServiceValue> addResponseCampaignTarget = campaignTargetService.campaignTargetServiceAddPost(addRequestCampaignTarget).getRval().getValues();
      List<CampaignTarget> campaignTargets = new ArrayList<>();
      for (CampaignTargetServiceValue values : addResponseCampaignTarget ) {
        campaignTargets.add(values.getCampaignTarget());
      }

      // SET
      CampaignTargetServiceOperation setRequestCampaignTarget = CampaignTargetServiceSample.buildExampleMutateRequest(
          accountId,
          CampaignTargetServiceSample.createExampleSetRequest(campaignTargets)
      );
      campaignTargetService.campaignTargetServiceSetPost(setRequestCampaignTarget);

      // GET
      CampaignTargetServiceSelector getRequestCampaignTarget = CampaignTargetServiceSample.buildExampleGetRequest(accountId, campaignTargets);
      campaignTargetService.campaignTargetServiceGetPost(getRequestCampaignTarget);

      // =================================================================
      // CampaignCriterionService
      // =================================================================
      // ADD
      CampaignCriterionServiceOperation addRequestCampaignCriterion = CampaignCriterionServiceSample.buildExampleMutateRequest(
          accountId, Arrays.asList(CampaignCriterionServiceSample.createExampleNegativeCampaignCriterion(campaignIdStandard))
      );
      List<CampaignCriterionServiceValue> addResponseCampaignCriterion = campaignCriterionService.campaignCriterionServiceAddPost(addRequestCampaignCriterion).getRval().getValues();
      List<CampaignCriterion> campaignCriterions = new ArrayList<>();
      for (CampaignCriterionServiceValue values : addResponseCampaignCriterion ) {
        campaignCriterions.add(values.getCampaignCriterion());
      }

      // GET
      CampaignCriterionServiceSelector getRequestCampaignCriterion = CampaignCriterionServiceSample.buildExampleGetRequest(accountId, campaignCriterions);
      campaignCriterionService.campaignCriterionServiceGetPost(getRequestCampaignCriterion);

      // =================================================================
      // AdGroupService
      // =================================================================
      // ADD
      AdGroupServiceOperation addRequestAdGroup = AdGroupServiceSample.buildExampleMutateRequest(
          accountId,
          new ArrayList<AdGroup>() {{
            add(AdGroupServiceSample.createExampleStandardAdGroup(campaignIdStandard));
            add(AdGroupServiceSample.createExampleMobileAppIOSAdGroup(campaignIdMobileAppIOS));
            add(AdGroupServiceSample.createExampleMobileAppANDROIDAdGroup(campaignIdMobileAppAndroid));
          }}
      );
      List<AdGroupServiceValue> addResponseAdGroup = adGroupService.adGroupServiceAddPost(addRequestAdGroup).getRval().getValues();
      valuesRepositoryFacade.getValuesHolder().setAdGroupServiceValueList(addResponseAdGroup);

      Long adGroupIdStandard = valuesRepositoryFacade.getAdGroupValuesRepository().findAdGroupId(campaignIdStandard);
      Long adGroupIdMobileAppIOS = valuesRepositoryFacade.getAdGroupValuesRepository().findAdGroupId(campaignIdMobileAppIOS);
      Long adGroupIdMobileAppAndroid = valuesRepositoryFacade.getAdGroupValuesRepository().findAdGroupId(campaignIdMobileAppAndroid);

      // Check Status
      AdGroupServiceSample.checkStatus(valuesRepositoryFacade.getAdGroupValuesRepository().getAdGroups());

      // SET
      AdGroupServiceOperation setRequestAdGroup= AdGroupServiceSample.buildExampleMutateRequest(
          accountId,
          AdGroupServiceSample.createExampleSetRequest(valuesRepositoryFacade.getAdGroupValuesRepository().getAdGroups())
      );
      adGroupService.adGroupServiceSetPost(setRequestAdGroup);

      // =================================================================
      // AdGroupCriterionService
      // =================================================================
      // ADD
      AdGroupCriterionServiceOperation addRequestAdGroupCriterion = AdGroupCriterionServiceSample.buildExampleMutateRequest(
          accountId,
          new ArrayList<AdGroupCriterion>() {{
            add(AdGroupCriterionServiceSample.createExampleBiddableAdGroupCriterion(campaignIdStandard, adGroupIdStandard));
          }}
      );
      List<AdGroupCriterionServiceValue> addResponseAdGroupCriterion = adGroupCriterionService.adGroupCriterionServiceAddPost(addRequestAdGroupCriterion).getRval().getValues();
      valuesRepositoryFacade.getValuesHolder().setAdGroupCriterionServiceValueList(addResponseAdGroupCriterion);

      // GET
      AdGroupCriterionServiceSelector getRequestAdGroupCriterion = AdGroupCriterionServiceSample.buildExampleGetRequest(
          accountId, AdGroupCriterionServiceUse.BIDDABLE, valuesRepositoryFacade.getAdGroupCriterionValuesRepository().getAdGroupCriterions()
      );
      adGroupCriterionService.adGroupCriterionServiceGetPost(getRequestAdGroupCriterion);

      // SET
      AdGroupCriterionServiceOperation setRequestAdGroupCriterion = AdGroupCriterionServiceSample.buildExampleMutateRequest(
          accountId,
          AdGroupCriterionServiceSample.createExampleSetRequest(valuesRepositoryFacade.getAdGroupCriterionValuesRepository().getAdGroupCriterions())
      );
      adGroupCriterionService.adGroupCriterionServiceSetPost(setRequestAdGroupCriterion);

      // =================================================================
      // AdGroupBidMultiplierService
      // =================================================================
      // SET
      AdGroupBidMultiplierServiceOperation setRequestAdGroupBidMultiplier = AdGroupBidMultiplierServiceSample.buildExampleMutateRequest(
          accountId,
          AdGroupBidMultiplierServiceSample.createExampleSetRequest(campaignIdStandard, adGroupIdStandard)
      );
      adGroupBidMultiplierService.adGroupBidMultiplierServiceSetPost(setRequestAdGroupBidMultiplier);

      // GET
      AdGroupBidMultiplierServiceSelector getRequestAdGroupBidMultiplier = AdGroupBidMultiplierServiceSample.buildExampleGetRequest(accountId, campaignIdStandard, adGroupIdStandard);
      adGroupBidMultiplierService.adGroupBidMultiplierServiceGetPost(getRequestAdGroupBidMultiplier);

      // =================================================================
      // AdGroupAdService
      // =================================================================
      // ADD
      AdGroupAdServiceOperation addRequestAdGroupAd = AdGroupAdServiceSample.buildExampleMutateRequest(
          accountId,
          new ArrayList<AdGroupAd>() {{
            add(AdGroupAdServiceSample.createExampleAppAdIOS(campaignIdMobileAppIOS, appIdIOS, adGroupIdMobileAppIOS));
            add(AdGroupAdServiceSample.createExampleAppAdANDROID(campaignIdMobileAppAndroid, appIdAndroid, adGroupIdMobileAppAndroid));
          }}
      );
      List<AdGroupAdServiceValue> addResponseAdGroupAd = adGroupAdService.adGroupAdServiceAddPost(addRequestAdGroupAd).getRval().getValues();
      valuesRepositoryFacade.getValuesHolder().setAdGroupAdServiceValueList(addResponseAdGroupAd);

      // SET
      AdGroupAdServiceOperation setRequestAdGroupAd = AdGroupAdServiceSample.buildExampleMutateRequest(
          accountId,
          AdGroupAdServiceSample.createExampleSetRequest(valuesRepositoryFacade.getAdGroupAdValuesRepository().getAdGroupAds())
      );
      adGroupAdService.adGroupAdServiceSetPost(setRequestAdGroupAd);

      // GET
      AdGroupAdServiceSelector getRequestAdGroupAd = AdGroupAdServiceSample.buildExampleGetRequest(accountId, valuesRepositoryFacade.getAdGroupAdValuesRepository().getAdGroupAds());
      adGroupAdService.adGroupAdServiceGetPost(getRequestAdGroupAd);

    } catch (Exception e) {
      e.printStackTrace();
      throw e;

    }
  }
}
