package com.planner.service;

import com.planner.dao.ProjectDAO;
import com.planner.exception.PlannerException;
import com.planner.exception.ValidationException;
import com.planner.model.Project;

import java.util.List;

public class ProjectService 
{
    // make DAO object to talk with DB
    private ProjectDAO projectDAO;

    public ProjectService() 
    {
        this.projectDAO = new ProjectDAO();
    }

    // ADD PROJECT 
    public void addProject(Project p) throws PlannerException 
    {
        // Rule 1: Name can not be empty
        if (p.getName() == null || p.getName().trim().isEmpty()) 
        {
            throw new ValidationException("Project name cannot be empty.");
        }

        // Rule 2: Dates can not be empty
        if (p.getStartDate() == null || p.getDueDate() == null) 
        {
            throw new ValidationException("Start date and Due date are required.");
        }

        // Rule 3: Due date, Start date (difrence cannot be negative)
        if (p.getDueDate().compareTo(p.getStartDate()) < 0) 
        {
            throw new ValidationException("Due date cannot be before Start date.");
        }

        // if all rules are followed then send to DAO
        projectDAO.add(p);
    }

    // 2. GET ALL PROJECTS
    public List<Project> getAllProjects() throws PlannerException 
    {
        return projectDAO.getAll();
    }

    // 3. GET PROJECT BY ID
    public Project getProjectById(int id) throws PlannerException 
    {
        Project p = projectDAO.getById(id);
        if (p == null) 
        {
            throw new PlannerException("Project with ID " + id + " not found.");
        }
        return p;
    }

    // 4. UPDATE PROJECT 
    public void updateProject(Project p) throws PlannerException 
    {
        if (p.getName() == null || p.getName().trim().isEmpty()) 
        {
            throw new ValidationException("Project name cannot be empty.");
        }
        
        if (p.getDueDate().compareTo(p.getStartDate()) < 0) 
        {
            throw new ValidationException("Due date cannot be before Start date.");
        }

        projectDAO.update(p);
    }

    // 5. DELETE PROJECT
    public void deleteProject(int id) throws PlannerException 
    {
        projectDAO.delete(id);
    }
}