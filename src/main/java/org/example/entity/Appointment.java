package org.example.entity;

import java.sql.Date;
import java.sql.Time;
import java.util.Objects;

public class Appointment {
    public enum Status {
        BOOKED, CANCELLED, COMPLETED
    }

    private Integer id;
    private Integer clientId;
    private Integer serviceId;
    private Date appointmentDate;
    private Time appointmentTime;
    private Status status;

    public Appointment() {
    }

    public Appointment(Integer clientId, Integer serviceId, Date appointmentDate, Time appointmentTime, Status status) {
        this.clientId = clientId;
        this.serviceId = serviceId;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.status = status;
    }

    public Appointment(Integer id, Integer clientId, Integer serviceId, Date appointmentDate, Time appointmentTime, Status status) {
        this.id = id;
        this.clientId = clientId;
        this.serviceId = serviceId;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public Time getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(Time appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Appointment that = (Appointment) o;
        return Objects.equals(id, that.id) &&
               Objects.equals(clientId, that.clientId) &&
               Objects.equals(serviceId, that.serviceId) &&
               Objects.equals(appointmentDate, that.appointmentDate) &&
               Objects.equals(appointmentTime, that.appointmentTime) &&
               status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, clientId, serviceId, appointmentDate, appointmentTime, status);
    }

    @Override
    public String toString() {
        return "Appointment{" +
               "id=" + id +
               ", clientId=" + clientId +
               ", serviceId=" + serviceId +
               ", appointmentDate=" + appointmentDate +
               ", appointmentTime=" + appointmentTime +
               ", status=" + status +
               '}';
    }
}

