package com.movielibrary.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.movielibrary.entity.Movie;
import com.movielibrary.service.MovieService;

@RestController
@RequestMapping("/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @PostMapping("/admin/add")
    public Movie addMovie(@RequestBody Movie movie) {
        return movieService.addMovie(movie);
    }

    @DeleteMapping("/admin/delete/{id}")
    public void deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
    }

    @GetMapping
    public List<Movie> getAllMovies(Authentication authentication) {
        return movieService.getAllMovies(authentication);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable Long id, Authentication authentication) {
        Movie movie = movieService.getMovieById(id, authentication);
        if (movie != null) {
            return ResponseEntity.ok(movie);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/purchase/{movieId}")
    public ResponseEntity<String> purchaseMovie(@PathVariable Long movieId, Authentication authentication) {
        String username = authentication.getName();
        boolean success = movieService.purchaseMovie(username, movieId);
        if (success) {
            return ResponseEntity.ok("Movie purchased successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Purchase failed. You may not be authorized to buy this movie.");
        }
    }
}
