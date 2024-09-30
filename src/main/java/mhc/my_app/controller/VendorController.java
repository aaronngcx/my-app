package mhc.my_app.controller;

import jakarta.validation.Valid;
import mhc.my_app.domain.Event;
import mhc.my_app.model.VendorDTO;
import mhc.my_app.repos.EventRepository;
import mhc.my_app.service.VendorService;
import mhc.my_app.util.CustomCollectors;
import mhc.my_app.util.ReferencedWarning;
import mhc.my_app.util.WebUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/vendors")
public class VendorController {

    private final VendorService vendorService;
    private final EventRepository eventRepository;

    public VendorController(final VendorService vendorService,
            final EventRepository eventRepository) {
        this.vendorService = vendorService;
        this.eventRepository = eventRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("eventValues", eventRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Event::getId, Event::getId)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("vendors", vendorService.findAll());
        return "vendor/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("vendor") final VendorDTO vendorDTO) {
        return "vendor/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("vendor") @Valid final VendorDTO vendorDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "vendor/add";
        }
        vendorService.create(vendorDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("vendor.create.success"));
        return "redirect:/vendors";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id, final Model model) {
        model.addAttribute("vendor", vendorService.get(id));
        return "vendor/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id,
            @ModelAttribute("vendor") @Valid final VendorDTO vendorDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "vendor/edit";
        }
        vendorService.update(id, vendorDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("vendor.update.success"));
        return "redirect:/vendors";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final Long id,
            final RedirectAttributes redirectAttributes) {
        final ReferencedWarning referencedWarning = vendorService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR,
                    WebUtils.getMessage(referencedWarning.getKey(), referencedWarning.getParams().toArray()));
        } else {
            vendorService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("vendor.delete.success"));
        }
        return "redirect:/vendors";
    }

}
