package com.planner.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.planner.dao.ProjectDAO;
import com.planner.dao.TaskDAO;
import com.planner.exception.PlannerException;
import com.planner.model.Task;
import com.planner.model.TaskStatus;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class StatsHandler implements HttpHandler 
{
    // DAOs to fetch data
    private ProjectDAO projectDAO = new ProjectDAO();
    private TaskDAO taskDAO = new TaskDAO();

    @Override
    public void handle(HttpExchange exchange) throws IOException 
    {
        String response = "";
        int statusCode = 200;

        try 
        {
            // 1. Calculate Total Projects
            int totalProjects = projectDAO.getAll().size();
            
            // 2. Calculate Task Stats
            List<Task> tasks = taskDAO.getAll();
            int totalTasks = tasks.size();
            int completedTasks = 0;
            int pendingTasks = 0;

            for (Task t : tasks) 
            {
                if (t.getStatus() == TaskStatus.DONE) 
                {
                    completedTasks++;
                } 
                else 
                {
                    pendingTasks++;
                }
            }

            // 3. Build JSON Response
            response = "{\"totalProjects\":" + totalProjects + 
                       ",\"totalTasks\":" + totalTasks + 
                       ",\"completedTasks\":" + completedTasks + 
                       ",\"pendingTasks\":" + pendingTasks + "}";

        } 
        catch (PlannerException e) 
        {
            statusCode = 500;
            response = "{\"error\": \"" + e.getMessage() + "\"}";
        }

        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}