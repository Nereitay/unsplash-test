package com.example.test.repository;

import com.example.test.model.Unsplash;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UnsplashRepository extends JpaRepository<Unsplash, String>, JpaSpecificationExecutor<Unsplash> {
}
