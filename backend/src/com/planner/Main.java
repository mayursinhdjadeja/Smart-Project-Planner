package com.planner;

import com.planner.server.PlannerServer;

public class Main 
{
    public static void main(String[] args) 
    {
        try 
        {
            System.out.println("Starting Smart Project Planner...");
            
            // Create server object and start it
            PlannerServer server = new PlannerServer();
            server.start();
            
        } 
        catch (Exception e) 
        {
            System.out.println("Failed to start server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}