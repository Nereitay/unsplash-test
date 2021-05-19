package com.example.test.controller;

import com.example.test.config.AuthorizeProperties;
import com.example.test.model.Unsplash;
import com.example.test.service.UnsplashService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@EnableConfigurationProperties(AuthorizeProperties.class)
public class UnsplashController {

    @Autowired
    private AuthorizeProperties authorizeProperties;
    @Autowired
    private UnsplashService unsplashService;

    @GetMapping("code")
    public String getCode () throws IOException, InterruptedException {
        HttpClient.Builder builder = HttpClient.newBuilder();
        HttpClient httpClient = builder.followRedirects(HttpClient.Redirect.ALWAYS).build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://unsplash.com/oauth/authorize?client_id=ejcxMYgmWK3jRhq90P3UkVUDWWtsJ50DRCSqn4b_p10&redirect_uri=https://www.google.es&response_type=code&scope=public"))
                .build();
        HttpResponse<String> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        String path = httpResponse.request().uri().getPath();
        return path;
    }



    @GetMapping("collections")
    public ResponseEntity<String> getCollections(@RequestParam(value = "page", defaultValue = "1") int page,
                                                 @RequestParam(value = "per_page", defaultValue = "10") int per_page)
            throws IOException, InterruptedException {

        return Optional.ofNullable(unsplashService.getCollections(page, per_page))
                .map(collections -> ResponseEntity.ok().body(collections))
                .orElseGet(() ->ResponseEntity.notFound().build());
    }

    @GetMapping("collection/all")
    public ResponseEntity<List<Unsplash>> getCollectionsFilter(@RequestParam(value = "filter", required = false) String filter) {
        // get all collections to store for the first time
       // this.unsplashService.saveAllCollections();
        return Optional.ofNullable(unsplashService.findByFilter(filter))
                .map(unsplashes -> ResponseEntity.ok().body(unsplashes))
                .orElseGet(()->ResponseEntity.notFound().build());

    }


}
