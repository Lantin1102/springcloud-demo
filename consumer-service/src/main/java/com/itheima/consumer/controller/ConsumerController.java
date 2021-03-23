package com.itheima.consumer.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("consumer")
@Slf4j
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
    @HystrixCommand(fallbackMethod = "searchUserFallback")
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
        if ("itheima".equals(username)) {
            throw new RuntimeException("itheima");
        }
        //开启复制均衡后,具体调用时 直接用服务名称调用
        return restTemplate.getForObject("http://user/user/" + username, Map.class);
    }

    public Map searchUserFallback(@PathVariable("username") String username) {
        log.info("服务降级触发，username ：{}",username);
        HashMap<Object, Object> map = new HashMap<>();
        map.put("msg","操作异常，稍后重试");
        map.put("code","503");
        return map;
    }

}
