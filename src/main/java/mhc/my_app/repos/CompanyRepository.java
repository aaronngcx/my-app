package mhc.my_app.repos;

import mhc.my_app.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CompanyRepository extends JpaRepository<Company, Long> {
}
