package com.example.creditnews.repository;

import com.example.creditnews.model.NewsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends JpaRepository<NewsEntity, Long> {
    // 추가 쿼리 메서드 필요 시 정의 가능
}

