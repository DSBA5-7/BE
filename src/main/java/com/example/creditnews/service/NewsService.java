package com.example.creditnews.service;

import com.example.creditnews.model.NewsEntity;
import com.example.creditnews.model.NewsRequest;
import com.example.creditnews.model.NewsResponse;
import com.example.creditnews.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class NewsService {

    @Autowired
    private NewsRepository newsRepository;

    private RestTemplate restTemplate = new RestTemplate();

    public List<NewsResponse> analyzeNews(NewsRequest request) {
        // 1. 모델 API 호출 (1차 분석)
        NewsResponse mainArticle = callModelAPI(request.getUrl());

        // 2. 유사 기사 크롤링
        List<String> similarArticles = crawlSimilarArticles(mainArticle.getKeywords());

        // 3. 유사 기사 분석
        List<NewsResponse> analyzedArticles = new ArrayList<>();
        analyzedArticles.add(mainArticle);
        for (String url : similarArticles) {
            NewsResponse response = callModelAPI(url);
            analyzedArticles.add(response);
        }

        // 4. 유사도 계산
        calculateSimilarityScores(analyzedArticles);

        // 5. 데이터 저장
        saveArticles(analyzedArticles);

        return analyzedArticles;
    }

    private NewsResponse callModelAPI(String url) {
        String modelApiUrl = "http://localhost:5000/analyze";
        NewsRequest request = new NewsRequest();
        request.setUrl(url);
        return restTemplate.postForObject(modelApiUrl, request, NewsResponse.class);
    }

    private List<String> crawlSimilarArticles(String keywords) {
        List<String> articles = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            articles.add("http://example.com/similar-news-" + i);
        }
        return articles;
    }

    private void calculateSimilarityScores(List<NewsResponse> articles) {
        List<String> vocabulary = new ArrayList<>();
        for (NewsResponse article : articles) {
            String[] tokens = (article.getBody() + " " + article.getKeywords()).split("\\s+");
            for (String token : tokens) {
                String word = token.toLowerCase();
                if (!vocabulary.contains(word)) {
                    vocabulary.add(word);
                }
            }
        }

        List<double[]> vectors = new ArrayList<>();
        for (NewsResponse article : articles) {
            vectors.add(textToVector(article.getBody() + " " + article.getKeywords(), vocabulary));
        }

        double[] mainVector = vectors.get(0);
        for (int i = 1; i < vectors.size(); i++) {
            double similarity = calculateCosineSimilarity(mainVector, vectors.get(i));
            articles.get(i).setSimilarityScore(similarity);
        }
    }

    private double calculateCosineSimilarity(double[] vectorA, double[] vectorB) {
        double dotProduct = 0.0, magnitudeA = 0.0, magnitudeB = 0.0;

        for (int i = 0; i < vectorA.length; i++) {
            dotProduct += vectorA[i] * vectorB[i];
            magnitudeA += Math.pow(vectorA[i], 2);
            magnitudeB += Math.pow(vectorB[i], 2);
        }

        magnitudeA = Math.sqrt(magnitudeA);
        magnitudeB = Math.sqrt(magnitudeB);

        return (magnitudeA == 0.0 || magnitudeB == 0.0) ? 0.0 : dotProduct / (magnitudeA * magnitudeB);
    }

    private double[] textToVector(String text, List<String> vocabulary) {
        double[] vector = new double[vocabulary.size()];
        String[] tokens = text.split("\\s+");

        for (String token : tokens) {
            int index = vocabulary.indexOf(token.toLowerCase());
            if (index != -1) {
                vector[index]++;
            }
        }

        return vector;
    }

    private void saveArticles(List<NewsResponse> responses) {
        for (NewsResponse response : responses) {
            NewsEntity entity = new NewsEntity();
            entity.setTitle(response.getTitle());
            entity.setBody(response.getBody());
            entity.setKeywords(response.getKeywords());
            entity.setCategory(response.getCategory());
            entity.setSentiment(response.getSentiment());
            entity.setPositiveNegative(response.getPositiveNegative());
            entity.setUrl(response.getUrl());
            newsRepository.save(entity);
        }
    }
}
