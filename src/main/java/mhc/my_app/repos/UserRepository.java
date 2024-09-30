package mhc.my_app.repos;

import mhc.my_app.domain.Company;
import mhc.my_app.domain.User;  // Use UserEntity
import mhc.my_app.domain.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> { // Use UserEntity

    User findFirstByCompany(Company company);  // Adjusted to return UserEntity

    User findFirstByVendor(Vendor vendor);  // Adjusted to return UserEntity

    User findByUsername(String username);  // Correct method to find by username
}
