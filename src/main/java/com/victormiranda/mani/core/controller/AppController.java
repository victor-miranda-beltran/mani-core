package com.victormiranda.mani.core.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AppController {

    @RequestMapping("/")
    public Map hi() {
        final Map hi = new HashMap<>();
        hi.put("hello", "horld");

        return hi;
    }
}
