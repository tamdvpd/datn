package com.fashionstore.fashionstore.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fashionstore.fashionstore.service.StatisticService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/statistic")
@RequiredArgsConstructor
public class StatisticController {
    private final StatisticService statisticService;;

    @GetMapping("/user")
    public ResponseEntity<Map<String, Long>> statisticUser() {
        return ResponseEntity.ok(statisticService.statisticUser());
    }
}
