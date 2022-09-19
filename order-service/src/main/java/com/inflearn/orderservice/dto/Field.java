package com.inflearn.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Field { // Kafka가 가지고 있는 형태
    private String type;
    private boolean optional;
    private String field;
}
