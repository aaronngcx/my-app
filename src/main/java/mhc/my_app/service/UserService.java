package mhc.my_app.service;

import java.util.List;
import mhc.my_app.domain.Company;
import mhc.my_app.domain.User; // Import your UserEntity class
import mhc.my_app.domain.Vendor;
import mhc.my_app.model.UserDTO;
import mhc.my_app.repos.CompanyRepository;
import mhc.my_app.repos.UserRepository;
import mhc.my_app.repos.VendorRepository;
import mhc.my_app.util.NotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final VendorRepository vendorRepository;

    public UserService(final UserRepository userRepository,
                       final CompanyRepository companyRepository,
                       final VendorRepository vendorRepository) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.vendorRepository = vendorRepository;
    }

    public List<UserDTO> findAll() {
        final List<User> users = userRepository.findAll(Sort.by("id")); // Use UserEntity
        return users.stream()
                .map(user -> mapToDTO(user, new UserDTO()))
                .toList();
    }

    public UserDTO get(final Long id) {
        return userRepository.findById(id)
                .map(user -> mapToDTO(user, new UserDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final UserDTO userDTO) {
        final User user = new User(); // Use UserEntity
        mapToEntity(userDTO, user);
        return userRepository.save(user).getId();
    }

    public void update(final Long id, final UserDTO userDTO) {
        final User user = userRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(userDTO, user);
        userRepository.save(user);
    }

    public void delete(final Long id) {
        userRepository.deleteById(id);
    }

    private UserDTO mapToDTO(final User user, final UserDTO userDTO) { // Use UserEntity
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setUsername(user.getUsername());
        userDTO.setPassword(user.getPassword());
        userDTO.setRole(user.getRole());
        userDTO.setCompany(user.getCompany() == null ? null : user.getCompany().getId());
        userDTO.setVendor(user.getVendor() == null ? null : user.getVendor().getId());
        return userDTO;
    }

    private User mapToEntity(final UserDTO userDTO, final User user) { // Use UserEntity
        user.setName(userDTO.getName());
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setRole(userDTO.getRole());

        // Map Company
        final Company company = userDTO.getCompany() == null ? null : companyRepository.findById(userDTO.getCompany())
                .orElseThrow(() -> new NotFoundException("company not found"));
        user.setCompany(company);

        // Map Vendor
        final Vendor vendor = userDTO.getVendor() == null ? null : vendorRepository.findById(userDTO.getVendor())
                .orElseThrow(() -> new NotFoundException("vendor not found"));
        user.setVendor(vendor);

        return user;
    }
}
