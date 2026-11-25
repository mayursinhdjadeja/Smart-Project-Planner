package com.planner.model;

// abstract means direct object banse nay, just for inherite it
public abstract class BaseEntity 
{
    private int id;
    private String createdAt;

    // Default constructor 
    public BaseEntity() 
    {

    }

    // Constructor 
    public BaseEntity(int id, String createdAt) 
    {
        this.id = id;
        this.createdAt = createdAt;
    }

    // Getter and Setter methods
    public int getId() 
    {
        return id;
    }

    public void setId(int id) 
    {
        this.id = id;
    }

    public String getCreatedAt() 
    {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) 
    {
        this.createdAt = createdAt;
    }

    // toString() method (Method Overriding)
    @Override
    public String toString() 
    {
        return "ID: " + id + ", Created At: " + createdAt;
    }
}