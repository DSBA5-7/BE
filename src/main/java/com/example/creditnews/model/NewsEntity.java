package com.example.creditnews.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "news")
public class NewsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 1000)
    private String body;

    private String keywords;

    private String category;

    private double trustScore;

    private String url;
}

