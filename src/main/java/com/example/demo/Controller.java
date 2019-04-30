package com.example.demo;

import io.spring.guides.gs_producing_web_service.Result;
import org.springframework.stereotype.Component;

@Component
public class Controller {
    public Result findNumbrer(int number){
        Result res = new Result();
        res.setCode("1");
        res.setError("e");
        return res;
    }
}
