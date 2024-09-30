package mhc.my_app.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public class EventDTO {

    private Long id;

    @Size(max = 255)
    private String name;

    @Size(max = 255)
    private String description;

    @NotNull
    private Long vendor;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public Long getVendor() {
        return vendor;
    }

    public void setVendor(final Long vendor) {
        this.vendor = vendor;
    }

}
