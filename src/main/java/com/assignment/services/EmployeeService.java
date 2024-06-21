package com.assignment.services;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.assignment.dao.DepartmentRepository;
import com.assignment.dao.EmployeeRepository;
import com.assignment.dto.BonusResponse;
import com.assignment.dto.CurrencyGroup;
import com.assignment.dto.EmployeeBonus;
import com.assignment.entities.Department;
import com.assignment.entities.Employee;
/**
 * The type Employee service.
 */
@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;
    /**
     * Save employees.
     *
     * @param employees the employees
     */
    public void saveEmployees(List<Employee> employees){
        
        for (Employee employee : employees) {
            Department department = departmentRepository.findByName(employee.getDepartment());
            if(department == null){
                department = new Department();
                department.setName(employee.getDepartment());
                departmentRepository.save(department);
            }
            employee.setDepartment(department.getName());
            employeeRepository.save(employee);
        }
        
    }
     /**
     * Eligible employees bonus response.
     *
     * @param date the date
     * @return the bonus response
     */
    public BonusResponse eligibleEmployees(String date){
        SimpleDateFormat formatter = new SimpleDateFormat("MMM-dd-yyyy");

        try {
        Date parsedDate = formatter.parse(date);
        List<Employee> eligibleEmployees = employeeRepository.findByJoiningDateBeforeAndExitDateAfter(parsedDate, parsedDate);
        Map<String, List<Employee>> groupCurrency = eligibleEmployees.stream().collect(Collectors.groupingBy(Employee::getCurrency));
        List<CurrencyGroup> currencyGroups = new ArrayList<>();
        for(Map.Entry<String, List<Employee>> entry : groupCurrency.entrySet()){
            CurrencyGroup currencyGroup = new CurrencyGroup();
            currencyGroup.setCurrency(entry.getKey());

            List<EmployeeBonus> bonusResponses = entry.getValue().stream().map(emp -> 
            {
                EmployeeBonus bonus = new EmployeeBonus();
               bonus.setEmpName(emp.getEmpName());
               bonus.setAmount(emp.getAmount());
               return bonus;

            }).collect(Collectors.toList());

            currencyGroup.setEmployees(bonusResponses);
            currencyGroups.add(currencyGroup);

        }

        BonusResponse bonusResponse = new BonusResponse();
        bonusResponse.setErrorMessage("");
        bonusResponse.setData(currencyGroups);
        return bonusResponse;

        
    } catch (ParseException ex) {
        ex.printStackTrace();
        BonusResponse bonusResponse = new BonusResponse();
        bonusResponse.setErrorMessage("\"Invalid date format. Please use 'MMM-dd-yyyy'.\"" + ex.getMessage());
        return bonusResponse;
    }
    catch (Exception e) {
        e.printStackTrace();
        BonusResponse bonusResponse = new BonusResponse();
        bonusResponse.setErrorMessage("Something went Wrong, Try again!!");
        return bonusResponse;
    }
    }
  
}
