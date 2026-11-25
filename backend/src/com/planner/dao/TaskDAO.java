package com.planner.dao;

import com.planner.db.DBConnection;
import com.planner.exception.PlannerException;
import com.planner.model.Task;
import com.planner.model.TaskStatus;
import com.planner.model.Priority;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// <Task>. Repository now used for Task
public class TaskDAO implements Repository<Task> 
{
    // INSERT (add new task)
    @Override
    public void add(Task t) throws PlannerException 
    {
        String sql = "INSERT INTO tasks (project_id, title, description, priority, status, due_date) VALUES (?, ?, ?, ?, ?, ?)";
        
        // try-with-resources: close connection and statement automatically
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) 
        {
            ps.setInt(1, t.getProjectId());
            ps.setString(2, t.getTitle());
            ps.setString(3, t.getDescription());
            ps.setString(4, t.getPriority().name()); // change enum into string
            ps.setString(5, t.getStatus().name());   // change enum into string
            ps.setString(6, t.getDueDate());
            
            ps.executeUpdate(); // Run The Query
        } 
        catch (SQLException e) 
        {
            throw new PlannerException("Database error while adding task: " + e.getMessage());
        }
    }

    // SELECT ALL 
    @Override
    public List<Task> getAll() throws PlannerException 
    {
        List<Task> list = new ArrayList<>();
        String sql = "SELECT * FROM tasks ORDER BY id DESC"; 

        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) 
        {
            while (rs.next()) 
            { 
                // loop run while data present
                list.add(mapResultSetToTask(rs)); // Change data into Task object
            }
        } 
        catch (SQLException e) 
        {
            throw new PlannerException("Error fetching tasks: " + e.getMessage());
        }
        return list;
    }

    // SELECT BY ID 
    @Override
    public Task getById(int id) throws PlannerException 
    {
        String sql = "SELECT * FROM tasks WHERE id = ?";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) 
        {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) 
            {
                if (rs.next()) 
                {
                    return mapResultSetToTask(rs);
                }
            }
        } 
        catch (SQLException e) 
        {
            throw new PlannerException("Error finding task: " + e.getMessage());
        }
        return null; 
    }

    // 4. UPDATE 
    @Override
    public void update(Task t) throws PlannerException 
    {
        String sql = "UPDATE tasks SET project_id=?, title=?, description=?, priority=?, status=?, due_date=? WHERE id=?";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) 
        {
            ps.setInt(1, t.getProjectId());
            ps.setString(2, t.getTitle());
            ps.setString(3, t.getDescription());
            ps.setString(4, t.getPriority().name());
            ps.setString(5, t.getStatus().name());
            ps.setString(6, t.getDueDate());
            ps.setInt(7, t.getId()); // WHERE id = ?
            
            ps.executeUpdate();
        } 
        catch (SQLException e) 
        {
            throw new PlannerException("Error updating task: " + e.getMessage());
        }
    }

    // DELETE 
    @Override
    public void delete(int id) throws PlannerException 
    {
        String sql = "DELETE FROM tasks WHERE id = ?";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) 
        {
            ps.setInt(1, id);
            ps.executeUpdate();
            
        } 
        catch (SQLException e) 
        {
            throw new PlannerException("Error deleting task: " + e.getMessage());
        }
    }

    // HELPER METHOD (convert DB RAW DATA (ResultSet) into our task object)
    private Task mapResultSetToTask(ResultSet rs) throws SQLException 
    {
        Task t = new Task();
        t.setId(rs.getInt("id"));
        t.setProjectId(rs.getInt("project_id"));
        t.setTitle(rs.getString("title"));
        t.setDescription(rs.getString("description"));
        
        // change string priority and status into enum
        t.setPriority(Priority.valueOf(rs.getString("priority")));
        t.setStatus(TaskStatus.valueOf(rs.getString("status")));
        
        t.setDueDate(rs.getString("due_date"));
        t.setCreatedAt(rs.getString("created_at"));
        return t;
    }
}