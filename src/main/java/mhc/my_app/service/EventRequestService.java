package mhc.my_app.service;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

import mhc.my_app.domain.Event;
import mhc.my_app.domain.EventRequest;
import mhc.my_app.domain.Vendor;
import mhc.my_app.model.EventRequestDTO;
import mhc.my_app.repos.EventRepository;
import mhc.my_app.repos.EventRequestRepository;
import mhc.my_app.util.NotFoundException;
import org.springframework.stereotype.Service;

@Service
public class EventRequestService {

    private final EventRequestRepository eventRequestRepository;
    private final EventRepository eventRepository;

    public EventRequestService(final EventRequestRepository eventRequestRepository, final EventRepository eventRepository) {
        this.eventRequestRepository = eventRequestRepository;
        this.eventRepository = eventRepository;
    }

    public List<EventRequestDTO> findAll() {
        // Fetch all EventRequest entities with their associated Event
        final List<EventRequest> eventRequests = eventRequestRepository.findAllWithEvent();

        // Convert the list of EventRequest entities to EventRequestDTOs
        return eventRequests.stream().map(eventRequest -> mapToDTO(eventRequest, new EventRequestDTO())) // Ensure mapToDTO is correct
                .toList(); // Use .toList() for Java 16+, or use Collectors.toList() for earlier versions
    }

    public EventRequestDTO get(final Long id) {
        return eventRequestRepository.findById(id).map(eventRequest -> mapToDTO(eventRequest, new EventRequestDTO())).orElseThrow(NotFoundException::new);
    }

    public Long create(final EventRequestDTO eventRequestDTO) {
        final EventRequest eventRequest = new EventRequest();
        mapToEntity(eventRequestDTO, eventRequest); // This will set the Event object
        return eventRequestRepository.save(eventRequest).getId();
    }

    public void update(final Long id, final EventRequestDTO eventRequestDTO) {
        final EventRequest eventRequest = eventRequestRepository.findById(id).orElseThrow(NotFoundException::new);
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
        eventRequestDTO.setDateCreated(OffsetDateTime.now());

        // Set the Event object directly in DTO
        eventRequestDTO.setEvent(eventRequest.getEvent());
        eventRequestDTO.setVendor(eventRequest.getVendor());

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

        final Event event = eventRequestDTO.getEvent() == null ? null : eventRequestDTO.getEvent();
        final Vendor vendor = eventRequestDTO.getVendor() == null ? null : eventRequestDTO.getVendor();

        eventRequest.setEvent(event);
        eventRequest.setVendor(vendor);

        return eventRequest;
    }
}
