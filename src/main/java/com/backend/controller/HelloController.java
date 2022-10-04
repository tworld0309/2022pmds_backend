package com.backend.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/hello")
@Api(value="HelloController Swagger", tags = "HelloController")
public class HelloController {

    @RequestMapping(value="/helloworld", method= RequestMethod.GET)
    public String helloworld(){
        return "backend - helloworld";
    }

}
