package ru.home.application.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.home.logging.annotation.Logged;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/*
 * @created 24.04.2023
 * @author alexander
 */
@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Logged
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public List<?> getCustomers() {
        return List.of();
    }

    @GetMapping(path = "/test", produces = APPLICATION_JSON_VALUE)
    public List<?> getCustomersTest() {
        return List.of();
    }
}