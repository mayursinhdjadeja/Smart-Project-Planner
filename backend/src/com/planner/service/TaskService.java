package com.planner.service;

import com.planner.dao.TaskDAO;
import com.planner.exception.PlannerException;
import com.planner.exception.ValidationException;
import com.planner.model.Task;

import java.util.List;

public class TaskService 
{
    // make DAO object to talk with DB
    private TaskDAO taskDAO;

    public TaskService() 
    {
        this.taskDAO = new TaskDAO();
    }

    // ADD TASK 
    public void addTask(Task t) throws PlannerException 
    {
        // Rule 1: Title can not be empty
        if (t.getTitle() == null || t.getTitle().trim().isEmpty()) 
        {
            throw new ValidationException("Task title cannot be empty.");
        }

        // Rule 2: Project ID must be valid (greater than 0)
        if (t.getProjectId() <= 0) 
        {
            throw new ValidationException("Task must belong to a valid Project.");
        }

        // Rule 3: Due date can not be empty
        if (t.getDueDate() == null || t.getDueDate().trim().isEmpty()) 
        {
            throw new ValidationException("Task due date is required.");
        }

        // if all rules are followed then send to DAO
        taskDAO.add(t);
    }

    // 2. GET ALL TASKS
    public List<Task> getAllTasks() throws PlannerException 
    {
        return taskDAO.getAll();
    }

    // 3. GET TASK BY ID
    public Task getTaskById(int id) throws PlannerException 
    {
        Task t = taskDAO.getById(id);
        if (t == null) 
        {
            throw new PlannerException("Task with ID " + id + " not found.");
        }
        return t;
    }

    // 4. UPDATE TASK 
    public void updateTask(Task t) throws PlannerException 
    {
        if (t.getTitle() == null || t.getTitle().trim().isEmpty()) 
        {
            throw new ValidationException("Task title cannot be empty.");
        }
        
        if (t.getDueDate() == null || t.getDueDate().trim().isEmpty()) 
        {
            throw new ValidationException("Task due date is required.");
        }

        taskDAO.update(t);
    }

    // 5. DELETE TASK
    public void deleteTask(int id) throws PlannerException 
    {
        taskDAO.delete(id);
    }
}