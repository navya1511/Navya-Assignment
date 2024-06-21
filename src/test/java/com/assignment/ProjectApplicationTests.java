package com.assignment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.assignment.dao.DepartmentRepository;
import com.assignment.dao.EmployeeRepository;
import com.assignment.dto.BonusResponse;
import com.assignment.dto.CurrencyGroup;
import com.assignment.dto.EmployeeBonus;
import com.assignment.entities.Department;
import com.assignment.entities.Employee;
import com.assignment.services.EmployeeService;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class ProjectApplicationTests {

	@Test
	void contextLoads() {
	}


	@Mock
    private EmployeeRepository mockEmployeeRepository;
    @Mock
    private DepartmentRepository mockDepartmentRepository;

    @InjectMocks
    private EmployeeService employeeServiceUnderTest;

    @Test
    void testSaveEmployees() {
        // Setup
        final Employee employee = new Employee();
        employee.setId(0);
        employee.setEmpName("empName");
        employee.setDepartment("department");
        employee.setAmount(0);
        employee.setCurrency("currency");
        final List<Employee> employees = List.of(employee);

        // Configure DepartmentRepository.findByName(...).
        final Department department = new Department();
        department.setId(0);
        department.setName("department");
        when(mockDepartmentRepository.findByName("department")).thenReturn(department);

        // Run the test
        employeeServiceUnderTest.saveEmployees(employees);

        // Verify the results
        // Confirm EmployeeRepository.save(...).
        final Employee entity = new Employee();
        entity.setId(0);
        entity.setEmpName("empName");
        entity.setDepartment("department");
        entity.setAmount(0);
        entity.setCurrency("currency");
        verify(mockEmployeeRepository).save(entity);
    }

    @Test
    void testSaveEmployees_DepartmentRepositoryFindByNameReturnsNull() {
        // Setup
        final Employee employee = new Employee();
        employee.setId(0);
        employee.setEmpName("empName");
        employee.setDepartment("department");
        employee.setAmount(0);
        employee.setCurrency("currency");
        final List<Employee> employees = List.of(employee);
        when(mockDepartmentRepository.findByName("department")).thenReturn(null);

        // Run the test
        employeeServiceUnderTest.saveEmployees(employees);

        // Verify the results
        // Confirm DepartmentRepository.save(...).
        final Department entity = new Department();
        entity.setId(0);
        entity.setName("department");
        verify(mockDepartmentRepository).save(entity);

        // Confirm EmployeeRepository.save(...).
        final Employee entity1 = new Employee();
        entity1.setId(0);
        entity1.setEmpName("empName");
        entity1.setDepartment("department");
        entity1.setAmount(0);
        entity1.setCurrency("currency");
        verify(mockEmployeeRepository).save(entity1);
    }

    @Test
    void testEligibleEmployees_ExceptionHandling() {
        when(mockEmployeeRepository.findByJoiningDateBeforeAndExitDateAfter(any(), any()))
                .thenThrow(new RuntimeException("Simulated error from repository"));

        String validDateString = "Jan-01-2020";
        BonusResponse result = employeeServiceUnderTest.eligibleEmployees(validDateString);

        String expectedErrorMessage = "Something went Wrong, Try again!!";
        assertEquals(expectedErrorMessage, result.getErrorMessage(), "Error message should indicate a general error");
        assertNull(result.getData(), "Data should be null when an exception occurs");
    }


    @Test
    void test_correctly_parses_valid_date_and_returns_eligible_employees_grouped_by_currency() {

        final BonusResponse expectedResult = new BonusResponse();
        expectedResult.setErrorMessage("");
        final CurrencyGroup currencyGroup = new CurrencyGroup();
        currencyGroup.setCurrency("USD");
        final EmployeeBonus employeeBonus = new EmployeeBonus();
        employeeBonus.setEmpName("John Doe");
        employeeBonus.setAmount(1000);
        currencyGroup.setEmployees(List.of(employeeBonus));
        expectedResult.setData(List.of(currencyGroup));

        final Employee employee = new Employee();
        employee.setId(1);
        employee.setEmpName("John Doe");
        employee.setDepartment("IT");
        employee.setAmount(1000);
        employee.setCurrency("USD");
        employee.setJoiningDate(new GregorianCalendar(2019, Calendar.JANUARY, 1).getTime());
        employee.setExitDate(new GregorianCalendar(2021, Calendar.JANUARY, 1).getTime());
        final List<Employee> employees = List.of(employee);
        when(mockEmployeeRepository.findByJoiningDateBeforeAndExitDateAfter(
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime())).thenReturn(employees);

        // Run the test
        final BonusResponse result = employeeServiceUnderTest.eligibleEmployees("Jan-01-2020");

        // Verify the results
        assertEquals(expectedResult, result);
    }
}
