package com.example.test.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.test.config.AuthorizeProperties;
import com.example.test.model.Unsplash;
import com.example.test.repository.UnsplashRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.persistence.criteria.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Service
@EnableConfigurationProperties(AuthorizeProperties.class)
public class UnsplashService {

    @Autowired
    private AuthorizeProperties authorizeProperties;
    @Autowired
    private UnsplashRepository unsplashRepository;


    public String getCollections(int page, int per_page) throws IOException, InterruptedException {
        // get collection
        HttpClient httpClient = HttpClient.newHttpClient();
        String uri = this.authorizeProperties.getCollectionUrl() + "?page=" + page + "&per_page=" + per_page;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .setHeader("Authorization", this.authorizeProperties.getToken())
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public void saveAllCollections() throws IOException, InterruptedException {
        int page = 1;
        int per_page = 200;
        int size = 0;
        do {
            String collections = getCollections(page, per_page);
            JSONArray jsonArray = JSON.parseArray(collections);
            for (int i = 0; i < jsonArray.size(); i++) {
                Unsplash unsplash = new Unsplash();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id = jsonObject.getString("id");
                String title = jsonObject.getString("title");
                String description = jsonObject.getString("description");
                String cover_photo_id = jsonObject.getJSONObject("cover_photo").getString("id");
                unsplash.setId(id);
                unsplash.setTitle(title);
                unsplash.setDescription(description);
                unsplash.setCover_photo_id(cover_photo_id);
                this.unsplashRepository.save(unsplash);
            }
            page ++;
            size = jsonArray.size();
            System.out.println(size);
        } while (size != 0);

    }

    public List<Unsplash> findByFilter(String filter) {
        Specification<Unsplash> specification = new Specification<Unsplash>() {
            @Override
            public Predicate toPredicate(Root<Unsplash> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                if (StringUtils.isNotBlank(filter)) {
                    Path<Object> id = root.get("id");
                    Path<Object> title = root.get("title");
                    Path<Object> description = root.get("description");
                    Path<Object> cover_photo_id = root.get("cover_photo_id");
                    Predicate like1 = criteriaBuilder.like(id.as(String.class), "%" + filter + "%");
                    Predicate like2 = criteriaBuilder.like(title.as(String.class), "%" + filter + "%");
                    Predicate like3 = criteriaBuilder.like(description.as(String.class), "%" + filter + "%");
                    Predicate like4 = criteriaBuilder.like(cover_photo_id.as(String.class), "%" + filter + "%");
                    return criteriaBuilder.or(like1,like2,like3,like4);
                }
                return null;
            }
        };

        return this.unsplashRepository.findAll(specification);
    }
}
