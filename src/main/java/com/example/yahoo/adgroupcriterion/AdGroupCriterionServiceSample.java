/**
 * Copyright (C) 2022 Yahoo Japan Corporation. All Rights Reserved.
 */
package com.example.yahoo.adgroupcriterion;

import jp.co.yahoo.adssearchapi.sample.basic.adgroup.AdGroupServiceSample;
import jp.co.yahoo.adssearchapi.sample.repository.ValuesRepositoryFacade;
import jp.co.yahoo.adssearchapi.sample.util.ApiUtils;
import jp.co.yahoo.adssearchapi.sample.util.ValuesHolder;
import jp.co.yahoo.adssearchapi.v10.api.AdGroupCriterionServiceApi;
import jp.co.yahoo.adssearchapi.v10.model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * example AdGroupCriterionService operation and Utility method collection.
 */
public class AdGroupCriterionServiceSample {

  private static final AdGroupCriterionServiceApi adGroupCriterionService = new AdGroupCriterionServiceApi(ApiUtils.getYahooJapanAdsApiClient());

  /**
   * main method for AdGroupCriterionServiceSample
   *
   * @param args command line arguments
   */
  public static void main(String[] args) throws Exception {

    // =================================================================
    // Setting
    // =================================================================
    ValuesHolder valuesHolder = new ValuesHolder();
    long accountId = ApiUtils.ACCOUNT_ID;

    try {
      // =================================================================
      // check & create upper service object.
      // =================================================================
      valuesHolder = setup();
      ValuesRepositoryFacade valuesRepositoryFacade = new ValuesRepositoryFacade(valuesHolder);
      Long campaignId = valuesRepositoryFacade.getCampaignValuesRepository().findCampaignId(CampaignServiceType.STANDARD);
      Long adGroupId = valuesRepositoryFacade.getAdGroupValuesRepository().findAdGroupId(campaignId);

      // =================================================================
      // AdGroupCriterionService ADD
      // =================================================================
      // create request.
      AdGroupCriterionServiceOperation addRequest = buildExampleMutateRequest(accountId, new ArrayList<AdGroupCriterion>() {{
        add(createExampleBiddableAdGroupCriterion(campaignId, adGroupId));
      }});

      // run
      List<AdGroupCriterionServiceValue> addResponse = adGroupCriterionService.adGroupCriterionServiceAddPost(addRequest).getRval().getValues();
      valuesRepositoryFacade.getValuesHolder().setAdGroupCriterionServiceValueList(addResponse);

      // =================================================================
      // AdGroupCriterionService GET
      // =================================================================
      // create request.
      AdGroupCriterionServiceSelector getRequest = buildExampleGetRequest(accountId, AdGroupCriterionServiceUse.BIDDABLE, valuesRepositoryFacade.getAdGroupCriterionValuesRepository().getAdGroupCriterions());

      // run
      adGroupCriterionService.adGroupCriterionServiceGetPost(getRequest);

      // =================================================================
      // AdGroupCriterionService SET
      // =================================================================
      // create request.
      AdGroupCriterionServiceOperation setRequest =
          buildExampleMutateRequest(accountId, createExampleSetRequest(valuesRepositoryFacade.getAdGroupCriterionValuesRepository().getAdGroupCriterions()));

      // run
      adGroupCriterionService.adGroupCriterionServiceSetPost(setRequest);

      // =================================================================
      // AdGroupCriterionService REMOVE
      // =================================================================
      // create request.
      AdGroupCriterionServiceOperation removeRequest = buildExampleMutateRequest(accountId, valuesRepositoryFacade.getAdGroupCriterionValuesRepository().getAdGroupCriterions());

      // run
      adGroupCriterionService.adGroupCriterionServiceRemovePost(removeRequest);

    } catch (Exception ex) {
      ex.printStackTrace();
      throw ex;
    } finally {
      cleanup(valuesHolder);
    }
  }

  /**
   * example mutate request.
   */
  public static AdGroupCriterionServiceOperation buildExampleMutateRequest(long accountId, List<AdGroupCriterion> operand) {
    AdGroupCriterionServiceOperation operation = new AdGroupCriterionServiceOperation();
    operation.setAccountId(accountId);
    operation.getOperand().addAll(operand);

    return operation;
  }

  /**
   * example biddable adGroupCriterion request.
   *
   * @param campaignId long
   * @param adGroupId  long
   * @return AdGroupCriterion
   */
  public static AdGroupCriterion createExampleBiddableAdGroupCriterion(long campaignId, long adGroupId) {

    // keyword
    AdGroupCriterionServiceKeyword keyword = new AdGroupCriterionServiceKeyword();
    keyword.setText("sample text" + AdGroupCriterionServiceUse.BIDDABLE.name());
    keyword.setKeywordMatchType(AdGroupCriterionServiceKeywordMatchType.PHRASE);

    // Set AdGroupCriterionServiceCriterion
    AdGroupCriterionServiceCriterion criterion = new AdGroupCriterionServiceCriterion();
    criterion.setCriterionType(AdGroupCriterionServiceCriterionType.KEYWORD);
    criterion.setKeyword(keyword);

    // bid
    AdGroupCriterionServiceBid bit = new AdGroupCriterionServiceBid();
    bit.setCpc((long) 100);

    // customParameters
    AdGroupCriterionServiceCustomParameter parameter = new AdGroupCriterionServiceCustomParameter();
    parameter.setKey("id1");
    parameter.setValue("1234");
    AdGroupCriterionServiceCustomParameters customParameters = new AdGroupCriterionServiceCustomParameters();
    customParameters.setParameters(Collections.singletonList(parameter));

    // Set BiddableAdGroupCriterion
    AdGroupCriterionServiceBiddableAdGroupCriterion biddableAdGroupCriterion = new AdGroupCriterionServiceBiddableAdGroupCriterion();
    biddableAdGroupCriterion.setUserStatus(AdGroupCriterionServiceUserStatus.ACTIVE);
    biddableAdGroupCriterion.setTrackingUrl("http://www.yahoo.co.jp/?url={lpurl}&amp;a={creative}&amp;pid={_id1}");
    biddableAdGroupCriterion.setBid(bit);
    biddableAdGroupCriterion.setFinalUrl("http://www.yahoo.co.jp");
    biddableAdGroupCriterion.setSmartphoneFinalUrl("http://www.yahoo.co.jp/mobile");
    biddableAdGroupCriterion.setCustomParameters(customParameters);

    AdGroupCriterion adGroupCriterion = new AdGroupCriterion();
    adGroupCriterion.setCampaignId(campaignId);
    adGroupCriterion.setAdGroupId(adGroupId);
    adGroupCriterion.setUse(AdGroupCriterionServiceUse.BIDDABLE);
    adGroupCriterion.setCriterion(criterion);
    adGroupCriterion.setBiddableAdGroupCriterion(biddableAdGroupCriterion);

    return adGroupCriterion;
  }

  /**
   * example get request.
   *
   * @param accountId           long
   * @param adGroupCriterionUse AdGroupCriterionUse
   * @param adGroupCriterions   AdGroupCriterion
   */
  public static AdGroupCriterionServiceSelector buildExampleGetRequest(long accountId, AdGroupCriterionServiceUse adGroupCriterionUse, List<AdGroupCriterion> adGroupCriterions) {

    // Set Selector
    AdGroupCriterionServiceSelector selector = new AdGroupCriterionServiceSelector();
    selector.setAccountId(accountId);
    selector.setUse(adGroupCriterionUse);

    List<Long> campaignIds = new ArrayList<>();
    List<Long> adGroupIds = new ArrayList<>();
    List<Long> criterionIds = new ArrayList<>();
    for (AdGroupCriterion adGroupCriterion : adGroupCriterions) {
      campaignIds.add(adGroupCriterion.getCampaignId());
      adGroupIds.add(adGroupCriterion.getAdGroupId());
      criterionIds.add(adGroupCriterion.getCriterion().getCriterionId());
    }
    selector.setCampaignIds(campaignIds);
    selector.setAdGroupIds(adGroupIds);
    selector.setCriterionIds(criterionIds);

    selector.setUserStatuses(Arrays.asList(AdGroupCriterionServiceUserStatus.ACTIVE, AdGroupCriterionServiceUserStatus.PAUSED));
    selector.setApprovalStatuses(Arrays.asList( //
        AdGroupCriterionServiceApprovalStatus.APPROVED, //
        AdGroupCriterionServiceApprovalStatus.APPROVED_WITH_REVIEW, //
        AdGroupCriterionServiceApprovalStatus.REVIEW, //
        AdGroupCriterionServiceApprovalStatus.PRE_DISAPPROVED, //
        AdGroupCriterionServiceApprovalStatus.POST_DISAPPROVED //
    ));

    selector.setStartIndex(1);
    selector.setNumberResults(20);

    return selector;
  }

  /**
   * example adGroupCriterion set request.
   *
   * @return List<AdGroupCriterion>
   */
  public static List<AdGroupCriterion> createExampleSetRequest(List<AdGroupCriterion> adGroupCriterions) {
    // create operands
    List<AdGroupCriterion> operands = new ArrayList<>();

    for (AdGroupCriterion adGroupCriterion : adGroupCriterions) {
      if (adGroupCriterion.getUse().equals(AdGroupCriterionServiceUse.BIDDABLE)) {

        // Set AdGroupCriterionServiceCriterion
        AdGroupCriterionServiceCriterion criterion = new AdGroupCriterionServiceCriterion();
        criterion.setCriterionId(adGroupCriterion.getCriterion().getCriterionId());
        criterion.setCriterionType(adGroupCriterion.getCriterion().getCriterionType());

        // bid
        AdGroupCriterionServiceBid bid = new AdGroupCriterionServiceBid();
        bid.setCpc((long) 150);

        AdGroupCriterionServiceBiddableAdGroupCriterion biddableAdGroupCriterion = new AdGroupCriterionServiceBiddableAdGroupCriterion();
        biddableAdGroupCriterion.setUserStatus(AdGroupCriterionServiceUserStatus.PAUSED);
        biddableAdGroupCriterion.setBid(bid);

        AdGroupCriterion operand = new AdGroupCriterion();
        operand.setCampaignId(adGroupCriterion.getCampaignId());
        operand.setAdGroupId(adGroupCriterion.getAdGroupId());
        operand.setUse(adGroupCriterion.getUse());
        operand.setCriterion(criterion);
        operand.setBiddableAdGroupCriterion(biddableAdGroupCriterion);

        operands.add(operand);
      }
    }

    return operands;
  }

  /**
   * check & create upper service object.
   *
   * @return ValuesHolder
   */
  private static ValuesHolder setup() throws Exception {

    return AdGroupServiceSample.create();
  }

  /**
   * cleanup service object.
   *
   * @param valuesHolder ValuesHolder
   */
  public static void cleanup(ValuesHolder valuesHolder) throws Exception {
    AdGroupServiceSample.cleanup(valuesHolder);
  }

  /**
   * create biddable AdGroupCriterion.
   *
   * @return valuesHolder ValuesHolder
   */
  public static ValuesHolder create() throws Exception {

    ValuesHolder parentValuesHolder = setup();
    ValuesRepositoryFacade parentValuesRepositoryFacade = new ValuesRepositoryFacade(parentValuesHolder);

    long accountId = ApiUtils.ACCOUNT_ID;
    Long campaignId = parentValuesRepositoryFacade.getCampaignValuesRepository().findCampaignId(CampaignServiceType.STANDARD);
    Long adGroupId = parentValuesRepositoryFacade.getAdGroupValuesRepository().findAdGroupId(campaignId);


    // create request.
    AdGroupCriterionServiceOperation addRequest = buildExampleMutateRequest( //
        accountId, Collections.singletonList(createExampleBiddableAdGroupCriterion(campaignId, adGroupId)) //
    );

    // run
    List<AdGroupCriterionServiceValue> addResponse = adGroupCriterionService.adGroupCriterionServiceAddPost(addRequest).getRval().getValues();

    ValuesHolder seflValuesHolder = new ValuesHolder();
    seflValuesHolder.setBiddingStrategyServiceValueList(parentValuesHolder.getBiddingStrategyServiceValueList());
    seflValuesHolder.setFeedServiceValueList(parentValuesHolder.getFeedServiceValueList());
    seflValuesHolder.setCampaignServiceValueList(parentValuesHolder.getCampaignServiceValueList());
    seflValuesHolder.setAdGroupServiceValueList(parentValuesHolder.getAdGroupServiceValueList());
    seflValuesHolder.setAdGroupCriterionServiceValueList(addResponse);

    return seflValuesHolder;
  }

}
