package mhc.my_app.repos;

import mhc.my_app.domain.Event;
import mhc.my_app.domain.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EventRepository extends JpaRepository<Event, Long> {

    Event findFirstByVendor(Vendor vendor);

}
