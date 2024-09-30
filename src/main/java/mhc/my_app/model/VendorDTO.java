package mhc.my_app.model;

import jakarta.validation.constraints.Size;


public class VendorDTO {

    private Long id;

    @Size(max = 255)
    private String name;

//    private Long event;

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

//    public Long getEvent() {
//        return event;
//    }

//    public void setEvent(final Long event) {
//        this.event = event;
//    }

}
