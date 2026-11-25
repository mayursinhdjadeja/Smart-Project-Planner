package com.planner.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.planner.model.Task;
import com.planner.model.TaskStatus;
import com.planner.model.Priority;
import com.planner.service.TaskService;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TaskHandler implements HttpHandler 
{
    private TaskService taskService = new TaskService();

    @Override
    public void handle(HttpExchange exchange) throws IOException 
    {
        String method = exchange.getRequestMethod(); 
        String response = "";
        int statusCode = 200;

        try 
        {
            if (method.equals("GET")) 
            {
                List<Task> tasks = taskService.getAllTasks();
                StringBuilder json = new StringBuilder("[");
                for (int i = 0; i < tasks.size(); i++) 
                {
                    Task t = tasks.get(i);
                    json.append("{\"id\":").append(t.getId())
                        .append(",\"projectId\":").append(t.getProjectId())
                        .append(",\"title\":\"").append(t.getTitle())
                        .append("\",\"description\":\"").append(t.getDescription())
                        .append("\",\"priority\":\"").append(t.getPriority())
                        .append("\",\"status\":\"").append(t.getStatus())
                        .append("\",\"dueDate\":\"").append(t.getDueDate())
                        .append("\"}");
                    if (i < tasks.size() - 1) json.append(",");
                }
                json.append("]");
                response = json.toString();
            } 
            else if (method.equals("POST")) 
            {
                String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                String projectIdStr = getValue(body, "projectId");
                String title = getValue(body, "title");
                String desc = getValue(body, "description");
                String priorityStr = getValue(body, "priority");
                String due = getValue(body, "dueDate");

                int projectId = 0;
                if (projectIdStr != null && !projectIdStr.isEmpty()) 
                {
                    projectId = Integer.parseInt(projectIdStr);
                }

                Task t = new Task();
                t.setProjectId(projectId);
                t.setTitle(title);
                t.setDescription(desc);
                t.setDueDate(due);
                
                if (priorityStr != null && !priorityStr.isEmpty()) 
                {
                    t.setPriority(Priority.valueOf(priorityStr));
                } 
                else 
                {
                    t.setPriority(Priority.MEDIUM); 
                }
                
                t.setStatus(TaskStatus.TODO);

                taskService.addTask(t);
                response = "{\"message\": \"Task added successfully!\"}";
            }
            else if (method.equals("PUT")) 
            {
                String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                int id = Integer.parseInt(getValue(body, "id"));
                String newStatus = getValue(body, "status");

                Task t = taskService.getTaskById(id);
                if (t != null) 
                {
                    t.setStatus(TaskStatus.valueOf(newStatus));
                    taskService.updateTask(t);
                    response = "{\"message\": \"Task status updated successfully!\"}";
                } 
                else 
                {
                    statusCode = 404;
                    response = "{\"error\": \"Task not found\"}";
                }
            }
            else if (method.equals("DELETE")) 
            {
                String query = exchange.getRequestURI().getQuery();
                if (query != null && query.contains("id=")) 
                {
                    int id = Integer.parseInt(getValue(query, "id"));
                    taskService.deleteTask(id);
                    response = "{\"message\": \"Task deleted successfully!\"}";
                } 
                else 
                {
                    statusCode = 400;
                    response = "{\"error\": \"Task ID is required for deletion\"}";
                }
            }
        } 
        catch (Exception e) 
        {
            statusCode = 400;
            response = "{\"error\": \"" + e.getMessage() + "\"}";
        }

        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*"); 
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    // FIXED: Added limit 2 to split to handle empty values
    private String getValue(String body, String key) throws Exception 
    {
        String[] pairs = body.split("&");
        for (String pair : pairs) 
        {
            String[] keyValue = pair.split("=", 2); 
            if (keyValue[0].equals(key)) 
            {
                if (keyValue.length > 1) {
                    return URLDecoder.decode(keyValue[1], "UTF-8");
                } else {
                    return "";
                }
            }
        }
        return "";
    }
}