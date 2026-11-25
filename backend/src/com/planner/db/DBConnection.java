package com.planner.db;

import java.sql.*;

public class DBConnection 
{
    // db (smartProjectPlanner) address (URL)
    private static final String URL = "jdbc:mysql://localhost:3306/smartProjectPlanner?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    
    //MySQL username
    private static final String USER = "root";
    
    // MySQL password
    private static final String PASS = "tnvi00@BA"; 

    // Static Block Work only one when class load
    static 
    {
        try 
        {
            // load mysql driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL Driver Loaded Successfully.");
        } 
        catch (ClassNotFoundException e) 
        {
            System.out.println("Driver not found! Check your lib folder.");
            e.printStackTrace();
        }
    }

    // method to get connection
    public static Connection getConnection() throws SQLException 
    {
        // connect with my sql
        return DriverManager.getConnection(URL, USER, PASS);
    }
    
}