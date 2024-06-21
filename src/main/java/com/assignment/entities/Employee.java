package com.assignment.entities;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Employee.
 */

@Entity
@Data
@NoArgsConstructor
@Table(name = "employees")
public class Employee {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private int id;
    private String empName;

    private String department;
    private int amount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM-dd-yyyy")
    private String currency;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM-dd-yyyy")
    private Date joiningDate;

    private Date exitDate;

   
}

