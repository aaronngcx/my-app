package mhc.my_app.rest;

import jakarta.validation.Valid;
import java.util.List;
import mhc.my_app.model.VendorDTO;
import mhc.my_app.service.VendorService;
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
@RequestMapping(value = "/api/vendors", produces = MediaType.APPLICATION_JSON_VALUE)
public class VendorResource {

    private final VendorService vendorService;

    public VendorResource(final VendorService vendorService) {
        this.vendorService = vendorService;
    }

    @GetMapping
    public ResponseEntity<List<VendorDTO>> getAllVendors() {
        return ResponseEntity.ok(vendorService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VendorDTO> getVendor(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(vendorService.get(id));
    }

    @PostMapping
    public ResponseEntity<Long> createVendor(@RequestBody @Valid final VendorDTO vendorDTO) {
        final Long createdId = vendorService.create(vendorDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateVendor(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final VendorDTO vendorDTO) {
        vendorService.update(id, vendorDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVendor(@PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = vendorService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        vendorService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
