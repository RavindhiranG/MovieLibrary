package com.movielibrary.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.movielibrary.entity.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long> {
}
