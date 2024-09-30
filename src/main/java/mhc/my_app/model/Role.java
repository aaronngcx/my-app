package mhc.my_app.model;


public enum Role {
    ROLE_HR,
    ROLE_VENDOR;

    public String getAuthority() {
        return this.name();
    }
}
