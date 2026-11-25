package com.planner.model;

public class Project extends BaseEntity 
{ 
    
    private String name;
    private String description;
    private String startDate;
    private String dueDate;
    private ProjectStatus status;

    public Project() 
    {
        super(); // CONSTRUCTOR CALLING (PARENT CLASS BASE-ENTITY)
    }

    public Project(int id, String name, String description, String startDate, String dueDate, ProjectStatus status, String createdAt) 
    {
        super(id, createdAt);
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.dueDate = dueDate;
        this.status = status;
    }

    // Getters and Setters
    public String getName() 
    { 
        return name; 
    }
    public void setName(String name) 
    { 
        this.name = name; 
    }

    public String getDescription() 
    { 
        return description; 
    }
    public void setDescription(String description) 
    { 
        this.description = description; 
    }

    public String getStartDate() 
    { 
        return startDate; 
    }
    public void setStartDate(String startDate) 
    { 
        this.startDate = startDate; 
    }

    public String getDueDate() 
    { 
        return dueDate; 
    }
    public void setDueDate(String dueDate) 
    { 
        this.dueDate = dueDate; 
    }

    public ProjectStatus getStatus() 
    { 
        return status; 
    }
    public void setStatus(ProjectStatus status) 
    { 
        this.status = status; 
    }

    @Override
    public String toString() 
    {
        return "Project [" + super.toString() + ", Name: " + name + ", Status: " + status + "]";
    }
}