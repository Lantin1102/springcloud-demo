package com.itheima.consumer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("consumer")
public class ConsumerController {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    DiscoveryClient discoveryClient;
    /*
        restTemplate直接http请求方法

    @RequestMapping("{username}")
    public Map searchUser(@PathVariable("username") String username) {

        return restTemplate.getForObject("http://localhost:8081/user/" + username, Map.class);
    }
     */
    /**
     * @description 通过eureka注册中心调用服务
     *
     * @param username
     * @return Map
     */
    @RequestMapping("{username}")
    public Map searchUser(@PathVariable("username") String username) {
        /*
        List<ServiceInstance> instances = discoveryClient.getInstances("user");

         if (!instances.isEmpty()) {

            ServiceInstance serviceInstance = instances.get(0);
            String host = serviceInstance.getHost();
            int port = serviceInstance.getPort();
            System.out.println("instanceId:"+serviceInstance.getInstanceId());

            System.out.println("url:"+serviceInstance.getUri());
            System.out.println("Scheme:"+serviceInstance.getScheme());

            return restTemplate.getForObject("http://"+host+":"+port+"/user/" + username, Map.class);

        }*/
        //开启复制均衡后,具体调用时 直接用服务名称调用
        return restTemplate.getForObject("http://user/user/" + username, Map.class);
    }
}
