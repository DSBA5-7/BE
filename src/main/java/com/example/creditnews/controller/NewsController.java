package com.example.creditnews.controller;

import com.example.creditnews.model.NewsRequest;
import com.example.creditnews.model.NewsResponse;
import com.example.creditnews.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    @Autowired
    private NewsService newsService;

    @PostMapping("/analyze")
    public ResponseEntity<List<NewsResponse>> analyzeNews(@RequestBody NewsRequest request) {
        List<NewsResponse> responses = newsService.analyzeNews(request);
        return ResponseEntity.ok(responses);
    }
}
