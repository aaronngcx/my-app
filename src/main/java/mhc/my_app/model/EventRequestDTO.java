package mhc.my_app.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public class EventRequestDTO {

    private Long id;

    @NotNull
    private LocalDate proposedDate1;

    @NotNull
    private LocalDate proposedDate2;

    @NotNull
    private LocalDate proposedDate3;

    @Size(max = 255)
    private String postalCode;

    @Size(max = 255)
    private String streetName;

    @Size(max = 255)
    private String remarks;

    @Size(max = 255)
    private String confirmedDate;

    private Status status;

    @NotNull
    private Long event;

    private Long vendor;

    private Long company;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public LocalDate getProposedDate1() {
        return proposedDate1;
    }

    public void setProposedDate1(LocalDate proposedDate1) {
        this.proposedDate1 = proposedDate1;
    }

    public LocalDate getProposedDate2() {
        return proposedDate2;
    }

    public void setProposedDate2(LocalDate proposedDate2) {
        this.proposedDate2 = proposedDate2;
    }

    public LocalDate getProposedDate3() {
        return proposedDate3;
    }

    public void setProposedDate3(LocalDate proposedDate3) {
        this.proposedDate3 = proposedDate3;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(final String postalCode) {
        this.postalCode = postalCode;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(final String streetName) {
        this.streetName = streetName;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

    public String getConfirmedDate() {
        return confirmedDate;
    }

    public void setConfirmedDate(final String confirmedDate) {
        this.confirmedDate = confirmedDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(final Status status) {
        this.status = status;
    }

    public Long getEvent() {
        return event;
    }

    public void setEvent(final Long event) {
        this.event = event;
    }

    public Long getVendor() {
        return vendor;
    }

    public void setVendor(final Long vendor) {
        this.vendor = vendor;
    }

    public Long getCompany() {
        return company;
    }

    public void setCompany(final Long company) {
        this.company = company;
    }

}
