package com.planner.server;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import com.planner.thread.DeadlineCheckerThread;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PlannerServer 
{
    public void start() throws IOException 
    {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // API Routes
        server.createContext("/api/projects", new ProjectHandler());
        server.createContext("/api/tasks", new TaskHandler());
        server.createContext("/api/stats", new StatsHandler());
        server.createContext("/api/reminders", new ReminderHandler());

        // Root Route (Loads HTML/CSS/JS)
        server.createContext("/", new StaticFileHandler());
        
        server.setExecutor(null); 
        server.start();

        DeadlineCheckerThread checkerThread = new DeadlineCheckerThread();
        checkerThread.start(); 
        
        System.out.println("Server Started Successfully!");
        System.out.println("Open in Browser: http://localhost:8080");
    }

    // Handler to serve HTML, CSS and JS files
    static class StaticFileHandler implements HttpHandler 
    {
        @Override
        public void handle(HttpExchange exchange) throws IOException 
        {
            String path = exchange.getRequestURI().getPath();
            
            // If root path, serve index.html
            if (path.equals("/")) 
            {
                path = "/index.html";
            }

            // Path to frontend folder (Going out of backend and then into frontend)
            Path filePath = Paths.get("../frontend" + path);

            if (Files.exists(filePath) && !Files.isDirectory(filePath)) 
            {
                byte[] fileBytes = Files.readAllBytes(filePath);
                
                // Set Content Type based on file extension
                String contentType = "text/plain";
                if (path.endsWith(".html")) contentType = "text/html";
                else if (path.endsWith(".css")) contentType = "text/css";
                else if (path.endsWith(".js")) contentType = "application/javascript";
                
                exchange.getResponseHeaders().set("Content-Type", contentType);
                exchange.sendResponseHeaders(200, fileBytes.length);
                
                OutputStream os = exchange.getResponseBody();
                os.write(fileBytes);
                os.close();
            } 
            else 
            {
                // 404 Not Found
                String response = "404 Not Found";
                exchange.sendResponseHeaders(404, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        }
    }
}