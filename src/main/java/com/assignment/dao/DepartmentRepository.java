package com.assignment.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.assignment.entities.Department;

/**
 * The interface Department repository.
 */
public interface DepartmentRepository extends JpaRepository<Department, Integer>{
    /**
     * Find by name department.
     *
     * @param name the name
     * @return the department
     */
    public Department findByName(String name);
    
} 