package com.jd.hazelcast.controller;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.jd.hazelcast.vo.GetVO;
import com.jd.hazelcast.vo.PutVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HazelcastController {

    @Autowired
    private HazelcastInstance hzInstance;

    @PostMapping(value = "/put")
    public String put(@RequestBody PutVO request) {

        IMap<Long, String> map = hzInstance.getMap("myCache");
        map.put(request.getKey(), request.getValue());

        return map.toString();
    }

    @PostMapping(value = "/get")
    public String get(@RequestBody GetVO request) {

        IMap<Long, String> map = hzInstance.getMap("myCache");

        return "Value: " + map.get(request.getKey()) ;
    }
}
