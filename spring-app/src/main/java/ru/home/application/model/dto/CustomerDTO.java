package ru.home.application.model.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

/*
 * @created 26.04.2023
 * @author alexander
 */
@Data
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerDTO {
    String name;
    Integer age;
}
