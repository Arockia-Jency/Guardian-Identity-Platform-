package com.jency.guardian.common.controller;


import com.jency.guardian.common.dto.response.HealthResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class HealthController {

    @GetMapping("/health")
    public HealthResponse health(){
        return new HealthResponse(
                "up",
               "Guardian Identity Platform",
                "1.0.0"
        );
    }
}
