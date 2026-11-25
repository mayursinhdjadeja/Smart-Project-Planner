package com.planner.dao;

import com.planner.db.DBConnection;
import com.planner.exception.PlannerException;
import com.planner.model.Project;
import com.planner.model.ProjectStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// <Project>. Repository now used for Project
public class ProjectDAO implements Repository<Project> 
{
    // INSERT (add new project)
    @Override
    public void add(Project p) throws PlannerException 
    {
        String sql = "INSERT INTO projects (name, description, start_date, due_date, status) VALUES (?, ?, ?, ?, ?)";
        
        // try-with-resources: close connection and statement automatically
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) 
        {
            ps.setString(1, p.getName());
            ps.setString(2, p.getDescription());
            ps.setString(3, p.getStartDate());
            ps.setString(4, p.getDueDate());
            ps.setString(5, p.getStatus().name()); // change enum into string
            
            ps.executeUpdate(); // Run The Query
        } 
        catch (SQLException e) 
        {
            throw new PlannerException("Database error while adding project: " + e.getMessage());
        }
    }

    // SELECT ALL 
    @Override
    public List<Project> getAll() throws PlannerException 
    {
        List<Project> list = new ArrayList<>();
        String sql = "SELECT * FROM projects ORDER BY id DESC"; 

        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) 
        {
            while (rs.next()) 
            { 
                // loop run while data present
                list.add(mapResultSetToProject(rs)); // Change data into Project object
            }
        } 
        catch (SQLException e) 
        {
            throw new PlannerException("Error fetching projects: " + e.getMessage());
        }
        return list;
    }

    // SELECT BY ID 
    @Override
    public Project getById(int id) throws PlannerException 
    {
        String sql = "SELECT * FROM projects WHERE id = ?";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) 
        {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) 
            {
                if (rs.next()) 
                {
                    return mapResultSetToProject(rs);
                }
            }
        } 
        catch (SQLException e) 
        {
            throw new PlannerException("Error finding project: " + e.getMessage());
        }
        return null; 
    }

    // 4. UPDATE 
    @Override
    public void update(Project p) throws PlannerException 
    {
        String sql = "UPDATE projects SET name=?, description=?, start_date=?, due_date=?, status=? WHERE id=?";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) 
        {
            ps.setString(1, p.getName());
            ps.setString(2, p.getDescription());
            ps.setString(3, p.getStartDate());
            ps.setString(4, p.getDueDate());
            ps.setString(5, p.getStatus().name());
            ps.setInt(6, p.getId()); // WHERE id = ?
            
            ps.executeUpdate();
        } 
        catch (SQLException e) 
        {
            throw new PlannerException("Error updating project: " + e.getMessage());
        }
    }

    // DELETE 
    @Override
    public void delete(int id) throws PlannerException 
    {
        String sql = "DELETE FROM projects WHERE id = ?";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) 
        {
            ps.setInt(1, id);
            ps.executeUpdate();
            
        } 
        catch (SQLException e) 
        {
            throw new PlannerException("Error deleting project: " + e.getMessage());
        }
    }

    // HELPER METHOD (convert DB RAW DATA (ResultSet) into our project object)
    private Project mapResultSetToProject(ResultSet rs) throws SQLException 
    {
        Project p = new Project();
        p.setId(rs.getInt("id"));
        p.setName(rs.getString("name"));
        p.setDescription(rs.getString("description"));
        p.setStartDate(rs.getString("start_date"));
        p.setDueDate(rs.getString("due_date"));
        
        // change string status into enum
        p.setStatus(ProjectStatus.valueOf(rs.getString("status")));
        p.setCreatedAt(rs.getString("created_at"));
        return p;
    }
}