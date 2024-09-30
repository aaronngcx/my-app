package mhc.my_app.repos;

import mhc.my_app.domain.Company;
import mhc.my_app.domain.Event;
import mhc.my_app.domain.EventRequest;
import mhc.my_app.domain.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EventRequestRepository extends JpaRepository<EventRequest, Long> {

    EventRequest findFirstByEvent(Event event);

    EventRequest findFirstByVendor(Vendor vendor);

    EventRequest findFirstByCompany(Company company);

}
