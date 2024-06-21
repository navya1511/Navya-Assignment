package com.assignment.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.assignment.entities.Employee;
/**
 * The interface Employee repository.
 */
public interface EmployeeRepository extends JpaRepository<Employee, Integer>{
    /**
     * Find by joining date before and exit date after list.
     *
     * @param beforeDate the before date
     * @param afterDate  the after date
     * @return the list
     */
    @Query
    public List<Employee> findByJoiningDateBeforeAndExitDateAfter(Date beforeDate, Date afterDate);
}
