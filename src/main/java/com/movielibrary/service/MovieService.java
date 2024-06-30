

package com.movielibrary.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.movielibrary.entity.Movie;
import com.movielibrary.entity.User;
import com.movielibrary.repository.MovieRepository;
import com.movielibrary.repository.UserRepository;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private UserRepository userRepository;

    public Movie addMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    public void deleteMovie(Long id) {
        movieRepository.deleteById(id);
    }

    public List<Movie> getAllMovies(Authentication authentication) {
        if (authentication == null) {
            return movieRepository.findAll().stream()
                .map(movie -> new Movie(movie.getId(), movie.getName(), null, null, null, movie.getPrice()))
                .collect(Collectors.toList());
        }

        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        boolean isAdmin = user.getRole().equals("ADMIN");

        if (isAdmin) {
            return movieRepository.findAll();
        } else {
            return movieRepository.findAll().stream()
                .map(movie -> {
                    if (user.getPurchasedMovies().contains(movie.getId())) {
                        return movie;
                    } else {
                        return new Movie(movie.getId(), movie.getName(), null, null, null, movie.getPrice());
                    }
                })
                .collect(Collectors.toList());
        }
    }

    public Movie getMovieById(Long movieId, Authentication authentication) {
        Optional<Movie> movieOpt = movieRepository.findById(movieId);

        if (!movieOpt.isPresent()) {
            return null;
        }

        Movie movie = movieOpt.get();

        if (authentication == null) {
            return new Movie(movie.getId(), movie.getName(), null, null, null, movie.getPrice());
        }

        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        boolean isAdmin = user.getRole().equals("ADMIN");

        if (isAdmin || user.getPurchasedMovies().contains(movie.getId())) {
            return movie;
        } else {
            return new Movie(movie.getId(), movie.getName(), null, null, null, movie.getPrice());
        }
    }
    
    public boolean purchaseMovie(String username, Long movieId) {
        User user = userRepository.findByUsername(username);
        Optional<Movie> movieOptional = movieRepository.findById(movieId);

        if (user == null || !movieOptional.isPresent()) {
            return false;
        }

        Movie movie = movieOptional.get();
        
        // Simulate purchase logic
        if (!user.getPurchasedMovies().contains(movieId)) {
            user.getPurchasedMovies().add(movieId);
            userRepository.save(user);
            return true;
        }

        return false;
    }
    
    public List<Movie> getPurchasedMovies(String username) {
        User user = userRepository.findByUsername(username);
        return movieRepository.findAllById(user.getPurchasedMovies());
    }
}
