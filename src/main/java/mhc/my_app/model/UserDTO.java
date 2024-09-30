package mhc.my_app.model;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserDTO {

    private Long id;

    @Size(max = 255)
    private String name;

    @Size(max = 255)
    private String username;

    @Size(max = 255)
    private String password;

    @NotNull
    private Role role;

    private Long company;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) { // Corrected parameter name
        this.username = username; // Changed to match the field name
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(final Role role) {
        this.role = role;
    }

    public Long getCompany() {
        return company;
    }

    public void setCompany(final Long company) {
        this.company = company;
    }

    public Long getVendor() {
        return vendor;
    }

    public void setVendor(final Long vendor) {
        this.vendor = vendor;
    }
}
