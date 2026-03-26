package com.yen.bookingSystem.dto;

import com.yen.bookingSystem.entity.Resource;
import jakarta.validation.constraints.NotBlank;

public class ResourceDto {

    private String id;

    @NotBlank(message = "name is required")
    private String name;

    private String type;
    private Integer capacity;
    private Boolean active;

    public static ResourceDto from(Resource resource) {
        ResourceDto dto = new ResourceDto();
        dto.setId(resource.getId());
        dto.setName(resource.getName());
        dto.setType(resource.getType());
        dto.setCapacity(resource.getCapacity());
        dto.setActive(resource.getActive());
        return dto;
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
