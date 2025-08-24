package com.fashionstore.fashionstore.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.fashionstore.fashionstore.repository.UserRepository;
import com.fashionstore.fashionstore.service.StatisticService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService {
    private final UserRepository userRepository;

    @Override
    public Map<String, Long> statisticUser() {
        Map<String, Long> map = new HashMap<>();
        map.put("day", userRepository.countUsersToday());
        map.put("week", userRepository.countUsersThisWeek());
        map.put("month", userRepository.countUsersThisMonth());
        map.put("year", userRepository.countUsersThisYear());
        map.put("countAllAccounts", userRepository.countAllAccounts());
        map.put("countUserAccounts", userRepository.countUserAccounts());
        map.put("countAdminAccounts", userRepository.countAdminAccounts());
        map.put("countActiveAccounts", userRepository.countActiveAccounts());
        map.put("countInactiveAccounts", userRepository.countInactiveAccounts());
        return map;
    }

}
