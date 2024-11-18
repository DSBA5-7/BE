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

    private String title;             // 기사 제목
    @Column(length = 1000)
    private String body;              // 기사 본문
    private String keywords;          // 추출된 키워드
    private String category;          // 기사 분야 (예: 경제, IT 등)
    private String sentiment;         // 긍정/부정 판별
    private String positiveNegative;  // 양성/음성 판별
    private double similarityScore;   // 유사도 점수
    private String url;               // 기사 URL
}
