package com.example.yahoo.controller;


import jp.co.yahoo.adsdisplayapi.v10.model.Campaign;

public class main {

    public static void main(String[] args) throws Exception {
        Campaign campaign = new Campaign();
        campaign.accountId(12456L);
        campaign.campaignId(1234L);

    }
}
