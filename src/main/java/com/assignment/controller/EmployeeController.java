package com.assignment.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.assignment.dto.BonusResponse;
import com.assignment.entities.Employee;
import com.assignment.helper.ApiResponse;
import com.assignment.services.EmployeeService;
/**
 * The type Employee controller.
 */
@RestController
@RequestMapping("/tci")
public class EmployeeController {
    
    @Autowired
   private EmployeeService employeeService;

    /**
     * Save employees response entity.
     *
     * @param employees the employees
     * @return the response entity
     */

    @PostMapping("/employee-bonus")
   public ResponseEntity<ApiResponse> saveEmployees(@RequestBody Map<String, List<Employee>> employees){
        try {
            List<Employee> listOfEmployees = employees.get("employees");
            employeeService.saveEmployees(listOfEmployees);
            return new ResponseEntity<>(ApiResponse.builder().message("List of employees saved successfully").success(true).build(), HttpStatusCode.valueOf(201));
        } catch (Exception e) {
            e.printStackTrace();
           return new ResponseEntity<>(ApiResponse.builder().message(e.getMessage()).success(false).build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
   }

   /**
     * Gets eligible employees.
     *
     * @param date the date
     * @return the eligible employees
     */
   @GetMapping("/employee-bonus")
   public ResponseEntity<BonusResponse> getEligibleEmployees(@RequestParam("date") String date){
        BonusResponse bonusResponse = employeeService.eligibleEmployees(date);
        if(!bonusResponse.getErrorMessage().isEmpty()){
            return new ResponseEntity<>(bonusResponse, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(bonusResponse, HttpStatus.valueOf(200));
   }
}
