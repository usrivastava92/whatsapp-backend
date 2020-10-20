package app.whatsapp.apigateway.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    @Value("<h1>${spring.application.name:Hello World!!}</h1>")
    private String applicationName;

    @RequestMapping("/")
    public String index() {
        return applicationName;
    }

}
