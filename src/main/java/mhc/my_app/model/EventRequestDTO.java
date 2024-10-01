package mhc.my_app.model;

import mhc.my_app.domain.Company;
import mhc.my_app.domain.Event;
import mhc.my_app.domain.Vendor;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public class EventRequestDTO {
    private Long id;
    private LocalDate proposedDate1;
    private LocalDate proposedDate2;
    private LocalDate proposedDate3;
    private String location;
    private String remarks;
    private String confirmedDate;
    private Status status;
    private OffsetDateTime dateCreated;
    private Event event;
    private Vendor vendor;
    private Company company;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getConfirmedDate() {
        return confirmedDate;
    }

    public void setConfirmedDate(String confirmedDate) {
        this.confirmedDate = confirmedDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public OffsetDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(OffsetDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

}
