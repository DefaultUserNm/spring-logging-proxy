package ru.home.application.service;

import ru.home.logging.annotation.Logged;

/*
 * @created 26.04.2023
 * @author alexander
 */
public class BaseCustomerService {
    @Logged
    public Integer getAge() {
        return 25;
    }
}