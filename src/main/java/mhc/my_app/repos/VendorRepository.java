package mhc.my_app.repos;

import mhc.my_app.domain.Event;
import mhc.my_app.domain.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;


public interface VendorRepository extends JpaRepository<Vendor, Long> {


}
