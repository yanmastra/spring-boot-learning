package com.yanmastra.integrationTest.controller;

import jakarta.annotation.security.PermitAll;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@PermitAll
@RestController
public class RootController {

    @GetMapping("")
    String returnEmpty() {
        return "{\"msg\":\"empty\"}";
    }

    @GetMapping("public")
    String returnPublic() {
        return "Hello world";
    }
}
