package mhc.my_app.service;

import java.util.List;
import mhc.my_app.domain.Event;
import mhc.my_app.domain.EventRequest;
import mhc.my_app.domain.Vendor;
import mhc.my_app.model.EventDTO;
import mhc.my_app.repos.EventRepository;
import mhc.my_app.repos.EventRequestRepository;
import mhc.my_app.repos.VendorRepository;
import mhc.my_app.util.NotFoundException;
import mhc.my_app.util.ReferencedWarning;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final VendorRepository vendorRepository;
    private final EventRequestRepository eventRequestRepository;

    public EventService(final EventRepository eventRepository,
                        final VendorRepository vendorRepository,
                        final EventRequestRepository eventRequestRepository) {
        this.eventRepository = eventRepository;
        this.vendorRepository = vendorRepository;
        this.eventRequestRepository = eventRequestRepository;
    }

    public List<EventDTO> findAll() {
        final List<Event> events = eventRepository.findAll(Sort.by("id"));
        return events.stream()
                .map(event -> mapToDTO(event, new EventDTO()))
                .toList();
    }

    public EventDTO get(final Long id) {
        return eventRepository.findById(id)
                .map(event -> mapToDTO(event, new EventDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final EventDTO eventDTO) {
        final Event event = new Event();
        mapToEntity(eventDTO, event);
        return eventRepository.save(event).getId();
    }

    public void update(final Long id, final EventDTO eventDTO) {
        final Event event = eventRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(eventDTO, event);
        eventRepository.save(event);
    }

    public void delete(final Long id) {
        eventRepository.deleteById(id);
    }

    private EventDTO mapToDTO(final Event event, final EventDTO eventDTO) {
        eventDTO.setId(event.getId());
        eventDTO.setName(event.getName());
        eventDTO.setDescription(event.getDescription());

        // If Event has a relationship with Vendor, ensure it’s set properly
        if (event.getVendor() != null) {
            eventDTO.setVendor(event.getVendor().getId());
        }

        return eventDTO;
    }

    private Event mapToEntity(final EventDTO eventDTO, final Event event) {
        event.setName(eventDTO.getName());
        event.setDescription(eventDTO.getDescription());

        // Get the Vendor based on the id provided in the DTO
        if (eventDTO.getVendor() != null) {
            final Vendor vendor = vendorRepository.findById(eventDTO.getVendor())
                    .orElseThrow(() -> new NotFoundException("Vendor not found"));
            event.setVendor(vendor);
        } else {
            event.setVendor(null); // Ensure Vendor is null if no vendor is provided
        }

        return event;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Event event = eventRepository.findById(id)
                .orElseThrow(NotFoundException::new);

        // Remove the line that checks for Vendor from Event
        // Update this section if you still want to validate other references
        final EventRequest eventRequest = eventRequestRepository.findFirstByEvent(event);
        if (eventRequest != null) {
            referencedWarning.setKey("event.eventRequest.event.referenced");
            referencedWarning.addParam(eventRequest.getId());
            return referencedWarning;
        }

        return null;
    }
}
