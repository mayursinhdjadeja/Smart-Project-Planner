package com.planner.thread;

// Extends Thread 
public class DeadlineCheckerThread extends Thread 
{
    @Override
    public void run() 
    {
        ReminderService reminderService = new ReminderService();
        
        while (true) 
        {
            try 
            {
                // Run the check
                reminderService.run();
                
                // Sleep for 30 seconds (30000 milliseconds)
                Thread.sleep(30000);
            } 
            catch (InterruptedException e) 
            {
                System.out.println("Thread was interrupted.");
                break;
            }
        }
    }
}