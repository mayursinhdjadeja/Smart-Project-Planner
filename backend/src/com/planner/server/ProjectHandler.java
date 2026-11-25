package com.planner.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.planner.model.Project;
import com.planner.model.ProjectStatus;
import com.planner.service.ProjectService;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ProjectHandler implements HttpHandler 
{
    private ProjectService projectService = new ProjectService();

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
                List<Project> projects = projectService.getAllProjects();
                StringBuilder json = new StringBuilder("[");
                for (int i = 0; i < projects.size(); i++) 
                {
                    Project p = projects.get(i);
                    json.append("{\"id\":").append(p.getId())
                        .append(",\"name\":\"").append(p.getName())
                        .append("\",\"description\":\"").append(p.getDescription())
                        .append("\",\"startDate\":\"").append(p.getStartDate())
                        .append("\",\"dueDate\":\"").append(p.getDueDate())
                        .append("\",\"status\":\"").append(p.getStatus())
                        .append("\"}");
                    if (i < projects.size() - 1) json.append(",");
                }
                json.append("]");
                response = json.toString();
            } 
            else if (method.equals("POST")) 
            {
                String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                String name = getValue(body, "name");
                String desc = getValue(body, "description");
                String start = getValue(body, "startDate");
                String due = getValue(body, "dueDate");

                Project p = new Project();
                p.setName(name);
                p.setDescription(desc);
                p.setStartDate(start);
                p.setDueDate(due);
                p.setStatus(ProjectStatus.TODO);

                projectService.addProject(p);
                response = "{\"message\": \"Project added successfully!\"}";
            }
            else if (method.equals("PUT")) 
            {
                String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                int id = Integer.parseInt(getValue(body, "id"));
                String name = getValue(body, "name");
                String desc = getValue(body, "description");
                String start = getValue(body, "startDate");
                String due = getValue(body, "dueDate");
                String statusStr = getValue(body, "status");

                Project p = projectService.getProjectById(id);
                if (p != null) 
                {
                    p.setName(name);
                    p.setDescription(desc);
                    p.setStartDate(start);
                    p.setDueDate(due);
                    p.setStatus(ProjectStatus.valueOf(statusStr));
                    
                    projectService.updateProject(p);
                    response = "{\"message\": \"Project updated successfully!\"}";
                }
                else 
                {
                    statusCode = 404;
                    response = "{\"error\": \"Project not found\"}";
                }
            }
            else if (method.equals("DELETE")) 
            {
                String query = exchange.getRequestURI().getQuery();
                if (query != null && query.contains("id=")) 
                {
                    int id = Integer.parseInt(getValue(query, "id"));
                    projectService.deleteProject(id);
                    response = "{\"message\": \"Project deleted successfully!\"}";
                } 
                else 
                {
                    statusCode = 400;
                    response = "{\"error\": \"Project ID is required for deletion\"}";
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