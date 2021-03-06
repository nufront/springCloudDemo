package com.cloud.ribbonservice.service;

import com.cloud.ribbonservice.controller.Controller;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class RibbonService implements IRibbonService {

    private static final Logger logger = LoggerFactory.getLogger(IRibbonService.class);

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    LoadBalancerClient loadBalancerClient;

    @HystrixCommand(fallbackMethod = "portFallback")
    public String port() {
        this.loadBalancerClient.choose("SPRING-CLIENT-01");
        String info = restTemplate.getForObject("http://SPRING-CLIENT-01/getInfo/port", String.class);
        return restTemplate.getForObject("http://SPRING-CLIENT-01/getInfo/port", String.class);

    }

    private String portFallback()
    {
        logger.error("The service is Down");
        System.err.println("The service is Down");
        return "Service down";
    }

    public String portList() {

        RestTemplate restTemplate1 = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
        //HttpEntity<String> entity = new HttpEntity<>(populateCpicFreightProposalRequest(transaction), headers);
        try {
            ResponseEntity<String> responseEntity =
                    restTemplate1.exchange("http://localhost:10000/getInfo/port" ,
                            HttpMethod.GET,null,String.class, new ParameterizedTypeReference<String>() {
                            });
            return responseEntity.getBody();
        } catch (ResourceAccessException e) {
           e.printStackTrace();
        }
//        this.loadBalancerClient.choose("SPRING-CLIENT-01");
//        String info = restTemplate.getForObject("http://SPRING-CLIENT-01/port", String.class);
//        return restTemplate.getForObject("http://SPRING-CLIENT-01/port", String.class);
////        return restTemplate.getForObject("http://SPRING-CLIENT-01/getInfo/port", String.class);

        String url = "http://localhost:10000/getInfo/show";
        Map<String,String> params = new HashMap<>();
        params.put("name","yyc");
        params.put("email","12306");
        RestTemplate restTemplatel = new RestTemplate();
        String request = restTemplatel.getForObject(url,String.class);
        //String request = restTemplate.getForObject(url,String.class,"yyc","email");
        System.out.println(request);

        return request;
    }

}
