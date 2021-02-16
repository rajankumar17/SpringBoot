package io.javabrains.moviecatalogservice.resources;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import io.javabrains.moviecatalogservice.models.CatalogItem;
import io.javabrains.moviecatalogservice.models.Movie;
import io.javabrains.moviecatalogservice.models.Rating;
import io.javabrains.moviecatalogservice.models.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
//import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class CatalogResource {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private CatalogResourceFallback catalogResourceFallback;

    /*@Autowired
    WebClient.Builder webClientBuilder;*/

    @RequestMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {

        //Basic Design
       /* return Collections.singletonList(
                new CatalogItem("100" ,"Title1", "Description1", 3)
        );*/

        //Using Rest Template
        /*UserRating userRating = restTemplate.getForObject("http://localhost:8083/ratingsdata/user/" + userId, UserRating.class);
        return userRating.getRatings().stream()
                .map(rating -> {
                    Movie movie = restTemplate.getForObject("http://localhost:8082/movies/" + rating.getMovieId(), Movie.class);
                    return new CatalogItem(movie.getMovieId() ,movie.getName(), movie.getDescription(), rating.getRating());
                })
                .collect(Collectors.toList());*/

        //Final Version - Using Service Discovery
        UserRating userRating = catalogResourceFallback.getRatingsData(userId);

        return userRating.getRatings().stream()
                .map(rating -> {
                    Movie movie = catalogResourceFallback.getMovieData(rating);
                    return new CatalogItem(movie.getMovieId() ,movie.getName(), movie.getDescription(), rating.getRating());
                })
                .collect(Collectors.toList());

    }


}

/*
Alternative WebClient way
Movie movie = webClientBuilder.build().get().uri("http://localhost:8082/movies/"+ rating.getMovieId())
.retrieve().bodyToMono(Movie.class).block();
*/