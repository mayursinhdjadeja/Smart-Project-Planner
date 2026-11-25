package com.planner.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.planner.model.Task;
import com.planner.thread.ReminderService;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class ReminderHandler implements HttpHandler 
{
    @Override
    public void handle(HttpExchange exchange) throws IOException 
    {
        String response = "";
        int statusCode = 200;

        try 
        {
            // Get overdue tasks from the synchronized list
            List<Task> overdue = ReminderService.getOverdueTasks();
            
            StringBuilder json = new StringBuilder("[");
            for (int i = 0; i < overdue.size(); i++) 
            {
                Task t = overdue.get(i);
                json.append("{\"id\":").append(t.getId())
                    .append(",\"title\":\"").append(t.getTitle())
                    .append("\",\"dueDate\":\"").append(t.getDueDate())
                    .append("\"}");
                
                if (i < overdue.size() - 1) 
                {
                    json.append(",");
                }
            }
            json.append("]");
            response = json.toString();

        } 
        catch (Exception e) 
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