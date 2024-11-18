package com.example.creditnews.service;

import com.example.creditnews.model.NewsEntity;
import com.example.creditnews.model.NewsRequest;
import com.example.creditnews.model.NewsResponse;
import com.example.creditnews.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NewsService {

    @Autowired
    private NewsRepository newsRepository;

    public NewsResponse processNews(NewsRequest request) {
        // 뉴스 데이터 크롤링 및 분석 결과 (샘플 데이터)
        String title = "Sample Title";
        String body = "Sample Body";
        String keywords = "Sample Keywords";
        String category = "Sample Category";
        double trustScore = 0.85;

        // 데이터 저장
        NewsEntity newsEntity = new NewsEntity();
        newsEntity.setTitle(title);
        newsEntity.setBody(body);
        newsEntity.setKeywords(keywords);
        newsEntity.setCategory(category);
        newsEntity.setTrustScore(trustScore);
        newsEntity.setUrl(request.getUrl());
        newsRepository.save(newsEntity);

        // 응답 생성
        NewsResponse response = new NewsResponse();
        response.setTitle(title);
        response.setBody(body);
        response.setKeywords(keywords);
        response.setCategory(category);
        response.setTrustScore(trustScore);
        return response;
    }
}
