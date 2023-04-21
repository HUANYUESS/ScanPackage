package com.sugoos.scanpackage.factory;

import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class RestTemplateFactory {

    private static RestTemplate restTemplate;

    private RestTemplateFactory() {}

    public static synchronized RestTemplate getInstance() {
        if (restTemplate == null) {
            restTemplate = new RestTemplate(simpleClientHttpRequestFactory());
        }
        return restTemplate;
    }

    public static ClientHttpRequestFactory simpleClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();//默认的是JDK提供http连接，需要的话可以//通过setRequestFactory方法替换为例如Apache HttpComponents、Netty或//OkHttp等其它HTTP library。
        factory.setReadTimeout(5000);//单位为ms
        factory.setConnectTimeout(5000);//单位为ms
        return factory;
    }

}
