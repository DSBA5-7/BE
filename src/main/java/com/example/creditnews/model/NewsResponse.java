package com.example.creditnews.model;

import lombok.Data;

@Data
public class NewsResponse {
    private String title;
    private String body;
    private String keywords;
    private String category;
    private double trustScore;
}
