package io.javabrains.discoveryserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

//http://localhost:8761/
//http://localhost:8083/ratingsdata/user/foo
//http://localhost:8083/ratingsdata/movies/100
/*
WebService Design:
Ratings Data Service - W1: Will provide the rating data
Input - UserId
Output - MovieId and Ratings
http://localhost:8083/ratingsdata/movies/100  --- Rating of specific movie id
http://localhost:8083/ratingsdata/user/foo --- Ratings for specific user

Movie Info Service - W2:Wil provide the movie info
Input - MovieId
Output - Movie Details
http://localhost:8082/movies/100   --- Title and Description of movie based on movie id

Movie Catalog Service - W3: Will utilise the above W1 and W2 to display the combined details
Input - UserId
Output - Movie list with details and ratings
http://localhost:8081/catalog/foo  --- Title ,Description , Rating

Hystrix Dashboard
http://localhost:8081/hystrix


 */

@SpringBootApplication
@EnableEurekaServer
public class DiscoveryServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(DiscoveryServerApplication.class, args);
	}

}

