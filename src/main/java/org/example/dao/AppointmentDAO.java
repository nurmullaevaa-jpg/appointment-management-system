package org.example.dao;

import org.example.entity.Appointment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {
    private static final String INSERT = "INSERT INTO appointments (client_id, service_id, appointment_date, appointment_time, status) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_BY_ID = "SELECT * FROM appointments WHERE id = ?";
    private static final String SELECT_ALL = "SELECT * FROM appointments";
    private static final String SELECT_BY_CLIENT = "SELECT * FROM appointments WHERE client_id = ?";
    private static final String SELECT_BOOKED_BY_DATE_TIME = "SELECT * FROM appointments WHERE appointment_date = ? AND appointment_time = ? AND status = 'booked'";
    private static final String UPDATE_STATUS = "UPDATE appointments SET status = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM appointments WHERE id = ?";

    public Integer create(Appointment appointment) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, appointment.getClientId());
            stmt.setInt(2, appointment.getServiceId());
            stmt.setDate(3, appointment.getAppointmentDate());
            stmt.setTime(4, appointment.getAppointmentTime());
            stmt.setString(5, appointment.getStatus().name().toLowerCase());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return null;
    }

    public Appointment findById(Integer id) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAppointment(rs);
                }
            }
        }
        return null;
    }

    public List<Appointment> findAll() throws SQLException {
        List<Appointment> appointments = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                appointments.add(mapResultSetToAppointment(rs));
            }
        }
        return appointments;
    }

    public List<Appointment> findByClientId(Integer clientId) throws SQLException {
        List<Appointment> appointments = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_CLIENT)) {
            stmt.setInt(1, clientId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    appointments.add(mapResultSetToAppointment(rs));
                }
            }
        }
        return appointments;
    }

    public Appointment findBookedByDateTime(Date date, Time time) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BOOKED_BY_DATE_TIME)) {
            stmt.setDate(1, date);
            stmt.setTime(2, time);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAppointment(rs);
                }
            }
        }
        return null;
    }

    public boolean updateStatus(Integer id, Appointment.Status status) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_STATUS)) {
            stmt.setString(1, status.name().toLowerCase());
            stmt.setInt(2, id);
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

    private Appointment mapResultSetToAppointment(ResultSet rs) throws SQLException {
        String statusStr = rs.getString("status").toUpperCase();
        Appointment.Status status = Appointment.Status.valueOf(statusStr);
        return new Appointment(
            rs.getInt("id"),
            rs.getInt("client_id"),
            rs.getInt("service_id"),
            rs.getDate("appointment_date"),
            rs.getTime("appointment_time"),
            status
        );
    }
}

