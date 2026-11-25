package com.planner.thread;

import com.planner.dao.TaskDAO;
import com.planner.exception.PlannerException;
import com.planner.model.Task;
import com.planner.model.TaskStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Implements Runnable
public class ReminderService implements Runnable 
{
    private TaskDAO taskDAO = new TaskDAO();
    
    // Synchronized list to store overdue tasks (Thread Safety)
    private static List<Task> overdueTasks = new ArrayList<>();

    // Synchronized method to read list safely
    public static synchronized List<Task> getOverdueTasks() 
    {
        return overdueTasks;
    }

    @Override
    public void run() 
    {
        try 
        {
            List<Task> allTasks = taskDAO.getAll();
            List<Task> currentOverdue = new ArrayList<>();
            LocalDate today = LocalDate.now();

            for (Task t : allTasks) 
            {
                // If task is not DONE and due date is before today
                if (t.getStatus() != TaskStatus.DONE) 
                {
                    LocalDate dueDate = LocalDate.parse(t.getDueDate());
                    if (dueDate.isBefore(today)) 
                    {
                        currentOverdue.add(t);
                    }
                }
            }

            // Update the static list safely
            synchronized (ReminderService.class) 
            {
                overdueTasks.clear();
                overdueTasks.addAll(currentOverdue);
            }
            
            System.out.println("Background Check: Found " + currentOverdue.size() + " overdue tasks.");

        } 
        catch (PlannerException e) 
        {
            System.out.println("Error in background check: " + e.getMessage());
        }
    }
}