package com.planner.model;

public class Task extends BaseEntity 
{ 
    
    private int projectId; //FK
    private String title;
    private String description;
    private Priority priority;
    private TaskStatus status;
    private String dueDate;

    public Task() 
    {
        super();
    }

    public Task(int id, int projectId, String title, String description, Priority priority, TaskStatus status, String dueDate, String createdAt) 
    {
        super(id, createdAt);
        this.projectId = projectId;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.status = status;
        this.dueDate = dueDate;
    }

    // Getters and Setters
    public int getProjectId() 
    { 
        return projectId; 
    }
    public void setProjectId(int projectId) 
    { 
        this.projectId = projectId; 
    }


    public String getTitle() 
    { 
        return title; 
    }
    public void setTitle(String title) 
    { 
        this.title = title; 
    }


    public String getDescription() 
    { 
        return description; 
    }
    public void setDescription(String description) 
    { 
        this.description = description; 
    }


    public Priority getPriority() 
    { 
        return priority; 
    }
    public void setPriority(Priority priority) 
    { 
        this.priority = priority; 
    }


    public TaskStatus getStatus() 
    { 
        return status; 
    }
    public void setStatus(TaskStatus status) 
    { 
        this.status = status; 
    }


    public String getDueDate() 
    { 
        return dueDate; 
    }
    public void setDueDate(String dueDate) 
    { 
        this.dueDate = dueDate; 
    }

    
    @Override
    public String toString() 
    {
        return "Task [" + super.toString() + ", Title: " + title + ", Priority: " + priority + "]";
    }
}