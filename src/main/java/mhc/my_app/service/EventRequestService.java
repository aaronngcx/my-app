package mhc.my_app.service;

import java.util.List;
import mhc.my_app.domain.Company;
import mhc.my_app.domain.Event;
import mhc.my_app.domain.EventRequest;
import mhc.my_app.domain.Vendor;
import mhc.my_app.model.EventRequestDTO;
import mhc.my_app.repos.CompanyRepository;
import mhc.my_app.repos.EventRepository;
import mhc.my_app.repos.EventRequestRepository;
import mhc.my_app.repos.VendorRepository;
import mhc.my_app.util.NotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class EventRequestService {

    private final EventRequestRepository eventRequestRepository;
    private final EventRepository eventRepository;
    private final VendorRepository vendorRepository;
    private final CompanyRepository companyRepository;

    public EventRequestService(final EventRequestRepository eventRequestRepository,
            final EventRepository eventRepository, final VendorRepository vendorRepository,
            final CompanyRepository companyRepository) {
        this.eventRequestRepository = eventRequestRepository;
        this.eventRepository = eventRepository;
        this.vendorRepository = vendorRepository;
        this.companyRepository = companyRepository;
    }

    public List<EventRequestDTO> findAll() {
        final List<EventRequest> eventRequests = eventRequestRepository.findAll(Sort.by("id"));
        return eventRequests.stream()
                .map(eventRequest -> mapToDTO(eventRequest, new EventRequestDTO()))
                .toList();
    }

    public EventRequestDTO get(final Long id) {
        return eventRequestRepository.findById(id)
                .map(eventRequest -> mapToDTO(eventRequest, new EventRequestDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final EventRequestDTO eventRequestDTO) {
        final EventRequest eventRequest = new EventRequest();
        mapToEntity(eventRequestDTO, eventRequest);
        return eventRequestRepository.save(eventRequest).getId();
    }

    public void update(final Long id, final EventRequestDTO eventRequestDTO) {
        final EventRequest eventRequest = eventRequestRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(eventRequestDTO, eventRequest);
        eventRequestRepository.save(eventRequest);
    }

    public void delete(final Long id) {
        eventRequestRepository.deleteById(id);
    }

    private EventRequestDTO mapToDTO(final EventRequest eventRequest,
            final EventRequestDTO eventRequestDTO) {
        eventRequestDTO.setId(eventRequest.getId());
        eventRequest.setProposedDate1(eventRequest.getProposedDate1());
        eventRequest.setProposedDate2(eventRequest.getProposedDate2());
        eventRequest.setProposedDate3(eventRequest.getProposedDate3());
        eventRequestDTO.setPostalCode(eventRequest.getPostalCode());
        eventRequestDTO.setStreetName(eventRequest.getStreetName());
        eventRequestDTO.setRemarks(eventRequest.getRemarks());
        eventRequestDTO.setConfirmedDate(eventRequest.getConfirmedDate());
        eventRequestDTO.setStatus(eventRequest.getStatus());
        eventRequestDTO.setEvent(eventRequest.getEvent() == null ? null : eventRequest.getEvent().getId());
        eventRequestDTO.setVendor(eventRequest.getVendor() == null ? null : eventRequest.getVendor().getId());
        eventRequestDTO.setCompany(eventRequest.getCompany() == null ? null : eventRequest.getCompany().getId());
        return eventRequestDTO;
    }

    private EventRequest mapToEntity(final EventRequestDTO eventRequestDTO,
            final EventRequest eventRequest) {
        eventRequest.setProposedDate1(eventRequest.getProposedDate1());
        eventRequest.setProposedDate2(eventRequest.getProposedDate2());
        eventRequest.setProposedDate3(eventRequest.getProposedDate3());
        eventRequest.setPostalCode(eventRequestDTO.getPostalCode());
        eventRequest.setStreetName(eventRequestDTO.getStreetName());
        eventRequest.setRemarks(eventRequestDTO.getRemarks());
        eventRequest.setConfirmedDate(eventRequestDTO.getConfirmedDate());
        eventRequest.setStatus(eventRequestDTO.getStatus());
        final Event event = eventRequestDTO.getEvent() == null ? null : eventRepository.findById(eventRequestDTO.getEvent())
                .orElseThrow(() -> new NotFoundException("event not found"));
        eventRequest.setEvent(event);
        final Vendor vendor = eventRequestDTO.getVendor() == null ? null : vendorRepository.findById(eventRequestDTO.getVendor())
                .orElseThrow(() -> new NotFoundException("vendor not found"));
        eventRequest.setVendor(vendor);
        final Company company = eventRequestDTO.getCompany() == null ? null : companyRepository.findById(eventRequestDTO.getCompany())
                .orElseThrow(() -> new NotFoundException("company not found"));
        eventRequest.setCompany(company);
        return eventRequest;
    }

}
