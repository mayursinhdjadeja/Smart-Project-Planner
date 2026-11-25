package com.planner.exception;

// this is a chekd exception because we extend the EXCEPTION from java.lang
public class PlannerException extends Exception 
{
    public PlannerException(String message) 
    {
        super(message); //Send msg to parent class EXCEPTION
    }
}