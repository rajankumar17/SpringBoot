package io.javabrains.moviecatalogservice.resources;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import io.javabrains.moviecatalogservice.models.Movie;
import io.javabrains.moviecatalogservice.models.Rating;
import io.javabrains.moviecatalogservice.models.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Service
public class CatalogResourceFallback {

    @Autowired
    private RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "getFallbackMovieData",commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value="2000"),
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold",value="5"),
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage",value="50"),
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds",value="5000"),
    })
    public Movie getMovieData(Rating rating) {
        return restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(), Movie.class);
    }

    private Movie getFallbackMovieData(Rating rating) {
        return new Movie("0","Movie name not found" , "");
    }

    @HystrixCommand(fallbackMethod = "getFallbackRating")
    public UserRating getRatingsData(String userId) {
        return restTemplate.getForObject("http://ratings-data-service/ratingsdata/user/" + userId, UserRating.class);
    }

    private UserRating getFallbackRating(String userId) {
        UserRating userRating = new UserRating();
        userRating.setUserId(userId);
        userRating.setRatings(Arrays.asList(new Rating("0",0)));
        return userRating;
    }
}
