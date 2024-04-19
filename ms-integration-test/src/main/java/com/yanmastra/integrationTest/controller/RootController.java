package com.yanmastra.integrationTest.controller;

import jakarta.annotation.security.PermitAll;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

    @PermitAll
    @GetMapping("public")
    String returnPublic() {
        return "Hello world";
    }
}
