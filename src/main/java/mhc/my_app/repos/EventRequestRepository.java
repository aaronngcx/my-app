package mhc.my_app.repos;

import mhc.my_app.domain.Company;
import mhc.my_app.domain.Event;
import mhc.my_app.domain.EventRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface EventRequestRepository extends JpaRepository<EventRequest, Long> {

    EventRequest findFirstByEvent(Event event);

    EventRequest findFirstByCompany(Company company);

    @Query("SELECT er FROM EventRequest er JOIN FETCH er.event")
    List<EventRequest> findAllWithEvent();
}
