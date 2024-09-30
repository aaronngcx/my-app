package mhc.my_app.rest;

import jakarta.validation.Valid;
import java.util.List;
import mhc.my_app.model.EventDTO;
import mhc.my_app.service.EventService;
import mhc.my_app.util.ReferencedException;
import mhc.my_app.util.ReferencedWarning;
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
@RequestMapping(value = "/api/events", produces = MediaType.APPLICATION_JSON_VALUE)
public class EventResource {

    private final EventService eventService;

    public EventResource(final EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public ResponseEntity<List<EventDTO>> getAllEvents() {
        return ResponseEntity.ok(eventService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> getEvent(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(eventService.get(id));
    }

    @PostMapping
    public ResponseEntity<Long> createEvent(@RequestBody @Valid final EventDTO eventDTO) {
        final Long createdId = eventService.create(eventDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateEvent(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final EventDTO eventDTO) {
        eventService.update(id, eventDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = eventService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        eventService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
