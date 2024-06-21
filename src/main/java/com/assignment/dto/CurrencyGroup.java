package com.assignment.dto;

import java.util.List;

import lombok.Data;
/**
 * The type Currency group.
 */
@Data
public class CurrencyGroup {
    private String currency;
    private List<EmployeeBonus> employees; 
}
