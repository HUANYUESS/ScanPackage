package com.sugoos.scanpackage.api;

import com.sugoos.scanpackage.factory.RestTemplateFactory;
import org.springframework.web.client.RestTemplate;

public class Http {

    private static final RestTemplate restTemplate = RestTemplateFactory.getInstance();

    public static String get(String url) {
        return restTemplate.getForObject(url, String.class);
    }

}
