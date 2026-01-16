package org.example.service;

import org.example.dao.AppointmentDAO;
import org.example.dao.ServiceDAO;
import org.example.entity.Appointment;
import org.example.entity.Service;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;

public class AppointmentService {
    private final AppointmentDAO appointmentDAO;
    private final ServiceDAO serviceDAO;

    public AppointmentService() {
        this.appointmentDAO = new AppointmentDAO();
        this.serviceDAO = new ServiceDAO();
    }

    public Integer bookAppointment(Integer clientId, Integer serviceId, Date date, Time time) 
            throws SQLException, IllegalArgumentException {
        Service service = serviceDAO.findById(serviceId);
        if (service == null) {
            throw new IllegalArgumentException("Service not found");
        }

        Appointment existingAppointment = appointmentDAO.findBookedByDateTime(date, time);
        if (existingAppointment != null) {
            throw new IllegalArgumentException("This time slot is already booked");
        }

        Appointment appointment = new Appointment(clientId, serviceId, date, time, Appointment.Status.BOOKED);
        return appointmentDAO.create(appointment);
    }

    public boolean cancelAppointment(Integer appointmentId) throws SQLException, IllegalArgumentException {
        Appointment appointment = appointmentDAO.findById(appointmentId);
        if (appointment == null) {
            throw new IllegalArgumentException("Appointment not found");
        }

        if (appointment.getStatus() == Appointment.Status.CANCELLED) {
            throw new IllegalArgumentException("Appointment is already cancelled");
        }

        if (appointment.getStatus() == Appointment.Status.COMPLETED) {
            throw new IllegalArgumentException("Cannot cancel a completed appointment");
        }

        return appointmentDAO.updateStatus(appointmentId, Appointment.Status.CANCELLED);
    }

    public boolean completeAppointment(Integer appointmentId) throws SQLException, IllegalArgumentException {
        Appointment appointment = appointmentDAO.findById(appointmentId);
        if (appointment == null) {
            throw new IllegalArgumentException("Appointment not found");
        }

        if (appointment.getStatus() != Appointment.Status.BOOKED) {
            throw new IllegalArgumentException("Only booked appointments can be marked as completed");
        }

        return appointmentDAO.updateStatus(appointmentId, Appointment.Status.COMPLETED);
    }

    public Appointment getAppointment(Integer appointmentId) throws SQLException {
        return appointmentDAO.findById(appointmentId);
    }
}

