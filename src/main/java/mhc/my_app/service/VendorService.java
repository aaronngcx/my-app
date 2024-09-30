package mhc.my_app.service;

import java.util.List;
import mhc.my_app.domain.User;
import mhc.my_app.domain.Vendor;
import mhc.my_app.model.VendorDTO;
import mhc.my_app.repos.UserRepository;
import mhc.my_app.repos.VendorRepository;
import mhc.my_app.util.NotFoundException;
import mhc.my_app.util.ReferencedWarning;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class VendorService {

    private final VendorRepository vendorRepository;
    private final UserRepository userRepository;

    public VendorService(final VendorRepository vendorRepository, final UserRepository userRepository) {
        this.vendorRepository = vendorRepository;
        this.userRepository = userRepository;
    }

    public List<VendorDTO> findAll() {
        final List<Vendor> vendors = vendorRepository.findAll(Sort.by("id"));
        return vendors.stream()
                .map(vendor -> mapToDTO(vendor, new VendorDTO()))
                .toList();
    }

    public VendorDTO get(final Long id) {
        return vendorRepository.findById(id)
                .map(vendor -> mapToDTO(vendor, new VendorDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final VendorDTO vendorDTO) {
        final Vendor vendor = new Vendor();
        mapToEntity(vendorDTO, vendor);
        return vendorRepository.save(vendor).getId();
    }

    public void update(final Long id, final VendorDTO vendorDTO) {
        final Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(vendorDTO, vendor);
        vendorRepository.save(vendor);
    }

    public void delete(final Long id) {
        vendorRepository.deleteById(id);
    }

    private VendorDTO mapToDTO(final Vendor vendor, final VendorDTO vendorDTO) {
        vendorDTO.setId(vendor.getId());
        vendorDTO.setName(vendor.getName());
        return vendorDTO;
    }

    private Vendor mapToEntity(final VendorDTO vendorDTO, final Vendor vendor) {
        vendor.setName(vendorDTO.getName());
        return vendor;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final User vendorUser = userRepository.findFirstByVendor(vendor);
        if (vendorUser != null) {
            referencedWarning.setKey("vendor.user.vendor.referenced");
            referencedWarning.addParam(vendorUser.getId());
            return referencedWarning;
        }
        return null;
    }

}
