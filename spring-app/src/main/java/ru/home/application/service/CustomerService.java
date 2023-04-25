package ru.home.application.service;

import org.springframework.stereotype.Service;
import ru.home.application.model.dto.CustomerDTO;
import ru.home.logging.annotation.Logged;

import java.util.List;
import java.util.stream.Collectors;

/*
 * @created 26.04.2023
 * @author alexander
 */
@Service
public class CustomerService extends BaseCustomerService {
    @Logged
    public List<CustomerDTO> getCustomers() {
        return List.of(
                new CustomerDTO().setName("test1")
        ).stream()
                .peek(c -> c.setAge(this.getAge()))
                .collect(Collectors.toList());
    }
}
