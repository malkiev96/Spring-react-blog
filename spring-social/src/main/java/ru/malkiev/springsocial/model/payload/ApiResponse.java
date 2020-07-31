package ru.malkiev.springsocial.model.payload;

import lombok.Data;

@Data
public class ApiResponse {

    private final boolean success;
    private final String message;
}