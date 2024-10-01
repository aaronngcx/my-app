package mhc.my_app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.List;

import mhc.my_app.domain.Company;
import mhc.my_app.domain.Event;
import mhc.my_app.domain.Vendor;
import mhc.my_app.model.EventRequestDTO;
import mhc.my_app.model.Status;
import mhc.my_app.repos.CompanyRepository;
import mhc.my_app.repos.EventRepository;
import mhc.my_app.repos.VendorRepository;
import mhc.my_app.service.EventRequestService;
import mhc.my_app.util.CustomCollectors;
import mhc.my_app.util.WebUtils;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;

@Controller
@RequestMapping("/eventRequests")
public class EventRequestController {
    private static final Logger logger = LoggerFactory.getLogger(EventRequestController.class);
    private final EventRequestService eventRequestService;
    private final ObjectMapper objectMapper;
    private final EventRepository eventRepository;
    private final VendorRepository vendorRepository;
    private final CompanyRepository companyRepository;

    public EventRequestController(final EventRequestService eventRequestService,
                                  final ObjectMapper objectMapper, final EventRepository eventRepository,
                                  final VendorRepository vendorRepository, final CompanyRepository companyRepository) {
        this.eventRequestService = eventRequestService;
        this.objectMapper = objectMapper;
        this.eventRepository = eventRepository;
        this.vendorRepository = vendorRepository;
        this.companyRepository = companyRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("statusValues", Status.values());
        model.addAttribute("eventValues", eventRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Event::getId, Event::getId)));
        model.addAttribute("vendorValues", vendorRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Vendor::getId, Vendor::getId)));
        model.addAttribute("companyValues", companyRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Company::getId, Company::getId)));
    }

    @GetMapping
    public String list(final Model model) {
        List<EventRequestDTO> eventRequests = eventRequestService.findAll();

        model.addAttribute("eventRequests", eventRequests);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String role = "UNKNOWN"; // Default role

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            role = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .findFirst()
                    .orElse("UNKNOWN");
        }

        model.addAttribute("userRole", role);
        return "eventRequest/list"; // Your Thymeleaf template for listing events
    }


    @GetMapping("/add")
    public String add(Model model) {
        EventRequestDTO eventRequestDTO = new EventRequestDTO();
        model.addAttribute("eventRequest", eventRequestDTO);

        return "eventRequest/add"; // Thymeleaf template at eventRequest/add.html
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("eventRequest") @Valid final EventRequestDTO eventRequestDTO,
                      final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(error -> {
                String errorMessage;
                String fieldName;

                if (error instanceof FieldError) {
                    FieldError fieldError = (FieldError) error;
                    errorMessage = fieldError.getDefaultMessage(); // Get error message
                    fieldName = fieldError.getField(); // Get specific field name
                } else {
                    // Handle non-field errors (e.g., object-level validations)
                    errorMessage = error.getDefaultMessage();
                    fieldName = error.getObjectName(); // Object-level validation error
                }

                // Print the error message and the field name
                System.out.println("Validation error in field '" + fieldName + "': " + errorMessage);
            });
            return "eventRequest/add";
        }

        eventRequestService.create(eventRequestDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("eventRequest.create.success"));
        return "redirect:/eventRequests";
    }


    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id, final Model model) {
        model.addAttribute("eventRequest", eventRequestService.get(id));
        return "eventRequest/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id,
                       @ModelAttribute("eventRequest") @Valid final EventRequestDTO eventRequestDTO,
                       final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "eventRequest/edit";
        }
        eventRequestService.update(id, eventRequestDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("eventRequest.update.success"));
        return "redirect:/eventRequests";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final Long id,
                         final RedirectAttributes redirectAttributes) {
        eventRequestService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("eventRequest.delete.success"));
        return "redirect:/eventRequests";
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<String> approve(@PathVariable Long id, @RequestParam String approvedDate) {
        try {
            System.out.println("Reach");

            eventRequestService.approveEventRequest(id, approvedDate);
            return ResponseEntity.ok("Event approved successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to approve event: " + e.getMessage());
        }
    }
}
