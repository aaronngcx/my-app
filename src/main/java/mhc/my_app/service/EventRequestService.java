package mhc.my_app.service;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;

import mhc.my_app.domain.*;
import mhc.my_app.model.EventRequestDTO;
import mhc.my_app.model.Status;
import mhc.my_app.repos.EventRepository;
import mhc.my_app.repos.CompanyRepository;
import mhc.my_app.repos.EventRequestRepository;
import mhc.my_app.repos.UserRepository;
import mhc.my_app.security.CustomUserDetails;
import mhc.my_app.util.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
public class EventRequestService {

    private final EventRequestRepository eventRequestRepository;
    private final EventRepository eventRepository;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    // Constructor for Dependency Injection
    public EventRequestService(final EventRequestRepository eventRequestRepository,
                               final EventRepository eventRepository,
                               final CompanyRepository companyRepository, UserRepository userRepository) {
        this.eventRequestRepository = eventRequestRepository;
        this.eventRepository = eventRepository;
        this.companyRepository = companyRepository; // Inject the company repository
        this.userRepository = userRepository;
    }

    public List<EventRequestDTO> findAll() {
        final List<EventRequest> eventRequests = eventRequestRepository.findAllWithEvent();

        // Sort the list by ID in ascending order
        eventRequests.sort(Comparator.comparing(EventRequest::getId));

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
        // Retrieve the Event based on the ID from the DTO
        Event event = eventRepository.findById(eventRequestDTO.getEvent().getId())
                .orElseThrow(() -> new RuntimeException("Event not found"));

        // Fetch the currently authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal(); // Cast to your CustomUserDetails

        User user = userRepository.findByUsername(userDetails.getUsername());
        System.out.println("User:" + user);

        Company company = user.getCompany();
        Vendor vendor = event.getVendor();

        final EventRequest eventRequest = new EventRequest();

        mapToEntity(eventRequestDTO, eventRequest);

        eventRequest.setCompany(company);
        eventRequest.setVendor(vendor);
        eventRequestDTO.setStatus(Status.PENDING);

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

    private EventRequestDTO mapToDTO(final EventRequest eventRequest, final EventRequestDTO eventRequestDTO) {
        eventRequestDTO.setId(eventRequest.getId());
        eventRequestDTO.setProposedDate1(eventRequest.getProposedDate1());
        eventRequestDTO.setProposedDate2(eventRequest.getProposedDate2());
        eventRequestDTO.setProposedDate3(eventRequest.getProposedDate3());
        eventRequestDTO.setPostalCode(eventRequest.getPostalCode());
        eventRequestDTO.setStreetName(eventRequest.getStreetName());
        eventRequestDTO.setRemarks(eventRequest.getRemarks());
        eventRequestDTO.setConfirmedDate(eventRequest.getConfirmedDate());
        eventRequestDTO.setStatus(eventRequest.getStatus());
        eventRequestDTO.setDateCreated(eventRequest.getDateCreated()); // Don't set this anew on DTO
        eventRequestDTO.setEvent(eventRequest.getEvent());
        eventRequestDTO.setVendor(eventRequest.getVendor());
        eventRequestDTO.setCompany(eventRequest.getCompany());

        return eventRequestDTO;
    }

    private EventRequest mapToEntity(final EventRequestDTO eventRequestDTO, final EventRequest eventRequest) {
        eventRequest.setProposedDate1(eventRequestDTO.getProposedDate1());
        eventRequest.setProposedDate2(eventRequestDTO.getProposedDate2());
        eventRequest.setProposedDate3(eventRequestDTO.getProposedDate3());
        eventRequest.setPostalCode(eventRequestDTO.getPostalCode());
        eventRequest.setStreetName(eventRequestDTO.getStreetName());
        eventRequest.setRemarks(eventRequestDTO.getRemarks());
        eventRequest.setConfirmedDate(eventRequestDTO.getConfirmedDate());
        eventRequest.setStatus(eventRequestDTO.getStatus());
        eventRequest.setDateCreated(OffsetDateTime.now());

        // Handle Event, Vendor, and Company assignments (set from DTO)
        final Event event = eventRequestDTO.getEvent() == null ? null : eventRequestDTO.getEvent();
        final Vendor vendor = eventRequestDTO.getVendor() == null ? null : eventRequestDTO.getVendor();
        final Company company = eventRequestDTO.getCompany() == null ? null : eventRequestDTO.getCompany();

        eventRequest.setEvent(event);
        eventRequest.setVendor(vendor);
        eventRequest.setCompany(company);

        return eventRequest;
    }

    public void approveEventRequest(Long id, String approvedDate) {
        EventRequest eventRequest = eventRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event request not found"));

        eventRequest.setStatus(Status.APPROVED); // Assuming you have an APPROVED status
        eventRequest.setConfirmedDate(approvedDate); // Parse the date if necessary

        eventRequestRepository.save(eventRequest);
    }
}
