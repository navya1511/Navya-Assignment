package com.assignment.helper;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiResponse {
    String message;
    boolean success;

}
