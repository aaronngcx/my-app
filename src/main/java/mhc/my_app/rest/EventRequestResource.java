package mhc.my_app.rest;

import jakarta.validation.Valid;
import java.util.List;
import mhc.my_app.model.EventRequestDTO;
import mhc.my_app.service.EventRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/eventRequests", produces = MediaType.APPLICATION_JSON_VALUE)
public class EventRequestResource {

    private final EventRequestService eventRequestService;

    public EventRequestResource(final EventRequestService eventRequestService) {
        this.eventRequestService = eventRequestService;
    }

    @GetMapping
    public ResponseEntity<List<EventRequestDTO>> getAllEventRequests() {
        return ResponseEntity.ok(eventRequestService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventRequestDTO> getEventRequest(
            @PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(eventRequestService.get(id));
    }

    @PostMapping
    public ResponseEntity<Long> createEventRequest(
            @RequestBody @Valid final EventRequestDTO eventRequestDTO) {
        final Long createdId = eventRequestService.create(eventRequestDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateEventRequest(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final EventRequestDTO eventRequestDTO) {
        eventRequestService.update(id, eventRequestDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEventRequest(@PathVariable(name = "id") final Long id) {
        eventRequestService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
