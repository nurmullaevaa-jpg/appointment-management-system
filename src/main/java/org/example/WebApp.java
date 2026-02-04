package org.example;

import org.example.dao.AppointmentDAO;
import org.example.dao.ClientDAO;
import org.example.dao.ServiceDAO;
import org.example.entity.Appointment;
import org.example.entity.Client;
import org.example.entity.Service;
import org.example.service.AppointmentService;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WebApp {

    private static final ClientDAO clientDAO = new ClientDAO();
    private static final ServiceDAO serviceDAO = new ServiceDAO();
    private static final AppointmentDAO appointmentDAO = new AppointmentDAO();
    private static final AppointmentService appointmentService = new AppointmentService();

    public static void main(String[] args) {
        Javalin app = Javalin.create(config -> {
            config.staticFiles.add(staticFiles -> {
                staticFiles.hostedPath = "/";
                staticFiles.directory = "/static";
                staticFiles.location = io.javalin.http.staticfiles.Location.CLASSPATH;
            });
        }).start(7000);

        app.get("/", ctx -> ctx.redirect("/index.html"));

        app.get("/api/clients", WebApp::getClients);
        app.post("/api/clients", WebApp::createClient);

        app.get("/api/services", WebApp::getServices);
        app.post("/api/services", WebApp::createService);

        app.get("/api/appointments", WebApp::getAppointments);
        app.post("/api/appointments", WebApp::createAppointment);
        app.post("/api/appointments/{id}/cancel", WebApp::cancelAppointment);
        app.post("/api/appointments/{id}/complete", WebApp::completeAppointment);

        app.exception(Exception.class, (e, ctx) -> {
            ctx.status(400).json(Map.of("success", false, "error", e.getMessage()));
        });

        System.out.println("Server: http://localhost:7000");
    }

    private static void getClients(Context ctx) throws SQLException {
        List<Client> clients = clientDAO.findAll();
        ctx.json(clients);
    }

    private static void createClient(Context ctx) throws SQLException {
        Client client = ctx.bodyAsClass(Client.class);
        Integer id = clientDAO.create(client);
        Map<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("success", id != null);
        ctx.json(result);
    }

    private static void getServices(Context ctx) throws SQLException {
        List<Service> services = serviceDAO.findAll();
        ctx.json(services);
    }

    private static void createService(Context ctx) throws SQLException {
        Service service = ctx.bodyAsClass(Service.class);
        Integer id = serviceDAO.create(service);
        Map<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("success", id != null);
        ctx.json(result);
    }

    private static void getAppointments(Context ctx) throws SQLException {
        List<Appointment> appointments = appointmentDAO.findAll();
        List<Map<String, Object>> list = appointments.stream()
            .map(a -> {
                Map<String, Object> m = new HashMap<>();
                m.put("id", a.getId());
                m.put("clientId", a.getClientId());
                m.put("serviceId", a.getServiceId());
                m.put("appointmentDate", a.getAppointmentDate() != null ? a.getAppointmentDate().toString() : null);
                m.put("appointmentTime", a.getAppointmentTime() != null ? a.getAppointmentTime().toString() : null);
                m.put("status", a.getStatus().name());
                return m;
            })
            .collect(Collectors.toList());
        ctx.json(list);
    }

    private static void createAppointment(Context ctx) throws SQLException {
        Map<String, Object> body = ctx.bodyAsClass(Map.class);
        int clientId = ((Number) body.get("clientId")).intValue();
        int serviceId = ((Number) body.get("serviceId")).intValue();
        Date date = Date.valueOf((String) body.get("appointmentDate"));
        Time time = Time.valueOf((String) body.get("appointmentTime"));
        Integer id = appointmentService.bookAppointment(clientId, serviceId, date, time);
        Map<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("success", id != null);
        ctx.json(result);
    }

    private static void cancelAppointment(Context ctx) throws SQLException {
        int id = Integer.parseInt(ctx.pathParam("id"));
        appointmentService.cancelAppointment(id);
        ctx.json(Map.of("success", true));
    }

    private static void completeAppointment(Context ctx) throws SQLException {
        int id = Integer.parseInt(ctx.pathParam("id"));
        appointmentService.completeAppointment(id);
        ctx.json(Map.of("success", true));
    }
}
