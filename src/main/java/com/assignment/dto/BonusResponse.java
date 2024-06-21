package com.assignment.dto;

import java.util.List;

import lombok.Data;
/**
 * The type Bonus response.
 */
@Data
public class BonusResponse {
    private String errorMessage;
    private List<CurrencyGroup> data;
}
