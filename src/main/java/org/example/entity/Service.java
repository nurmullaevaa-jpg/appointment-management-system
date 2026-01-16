package org.example.entity;

import java.math.BigDecimal;
import java.util.Objects;

public class Service {
    private Integer id;
    private String name;
    private Integer duration;
    private BigDecimal price;

    public Service() {
    }

    public Service(String name, Integer duration, BigDecimal price) {
        this.name = name;
        this.duration = duration;
        this.price = price;
    }

    public Service(Integer id, String name, Integer duration, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.price = price;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Service service = (Service) o;
        return Objects.equals(id, service.id) &&
               Objects.equals(name, service.name) &&
               Objects.equals(duration, service.duration) &&
               Objects.equals(price, service.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, duration, price);
    }

    @Override
    public String toString() {
        return "Service{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", duration=" + duration +
               ", price=" + price +
               '}';
    }
}

