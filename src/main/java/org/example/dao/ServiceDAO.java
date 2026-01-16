package org.example.dao;

import org.example.entity.Service;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceDAO {
    private static final String INSERT = "INSERT INTO services (name, duration, price) VALUES (?, ?, ?)";
    private static final String SELECT_BY_ID = "SELECT * FROM services WHERE id = ?";
    private static final String SELECT_ALL = "SELECT * FROM services";
    private static final String UPDATE = "UPDATE services SET name = ?, duration = ?, price = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM services WHERE id = ?";

    public Integer create(Service service) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, service.getName());
            stmt.setInt(2, service.getDuration());
            stmt.setBigDecimal(3, service.getPrice());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return null;
    }

    public Service findById(Integer id) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToService(rs);
                }
            }
        }
        return null;
    }

    public List<Service> findAll() throws SQLException {
        List<Service> services = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                services.add(mapResultSetToService(rs));
            }
        }
        return services;
    }

    public boolean update(Service service) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE)) {
            stmt.setString(1, service.getName());
            stmt.setInt(2, service.getDuration());
            stmt.setBigDecimal(3, service.getPrice());
            stmt.setInt(4, service.getId());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean delete(Integer id) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    private Service mapResultSetToService(ResultSet rs) throws SQLException {
        return new Service(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getInt("duration"),
            rs.getBigDecimal("price")
        );
    }
}

