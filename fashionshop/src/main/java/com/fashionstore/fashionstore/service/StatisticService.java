package com.fashionstore.fashionstore.service;

import java.util.Map;

public interface StatisticService {
    Map<String, Long> statisticUser();

    Map<String, Long> statisticRevenue();

    Map<String, Long> statisticOrder();
}
