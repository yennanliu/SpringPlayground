package com.yen.bookingSystem.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "resource")
public class Resource {

    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    private String type;

    private Integer capacity = 1;

    private Boolean active = true;

    public Resource() {}

    public Resource(String id, String name, String type, Integer capacity, Boolean active) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.capacity = capacity;
        this.active = active;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}
