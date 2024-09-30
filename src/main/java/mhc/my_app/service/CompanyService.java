package mhc.my_app.service;

import java.util.List;
import mhc.my_app.domain.Company;
import mhc.my_app.domain.EventRequest;
import mhc.my_app.domain.User;
import mhc.my_app.model.CompanyDTO;
import mhc.my_app.repos.CompanyRepository;
import mhc.my_app.repos.EventRequestRepository;
import mhc.my_app.repos.UserRepository;
import mhc.my_app.util.NotFoundException;
import mhc.my_app.util.ReferencedWarning;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final EventRequestRepository eventRequestRepository;

    public CompanyService(final CompanyRepository companyRepository,
                          final UserRepository userRepository,
                          final EventRequestRepository eventRequestRepository) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
        this.eventRequestRepository = eventRequestRepository;
    }

    public List<CompanyDTO> findAll() {
        final List<Company> companies = companyRepository.findAll(Sort.by("id"));
        return companies.stream()
                .map(company -> mapToDTO(company, new CompanyDTO()))
                .toList();
    }

    public CompanyDTO get(final Long id) {
        return companyRepository.findById(id)
                .map(company -> mapToDTO(company, new CompanyDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final CompanyDTO companyDTO) {
        final Company company = new Company();
        mapToEntity(companyDTO, company);
        return companyRepository.save(company).getId();
    }

    public void update(final Long id, final CompanyDTO companyDTO) {
        final Company company = companyRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(companyDTO, company);
        companyRepository.save(company);
    }

    public void delete(final Long id) {
        companyRepository.deleteById(id);
    }

    private CompanyDTO mapToDTO(final Company company, final CompanyDTO companyDTO) {
        companyDTO.setId(company.getId());
        companyDTO.setName(company.getName());
        return companyDTO;  // Added return statement
    }

    private Company mapToEntity(final CompanyDTO companyDTO, final Company company) {
        company.setName(companyDTO.getName());
        return company; // Added return statement
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Company company = companyRepository.findById(id)
                .orElseThrow(NotFoundException::new);

        User companyUser = userRepository.findFirstByCompany(company);
        if (companyUser != null) {
            referencedWarning.setKey("company.user.company.referenced");
            referencedWarning.addParam(companyUser.getId());
            return referencedWarning;
        }

        EventRequest companyEventRequest = eventRequestRepository.findFirstByCompany(company);
        if (companyEventRequest != null) {
            referencedWarning.setKey("company.eventRequest.company.referenced");
            referencedWarning.addParam(companyEventRequest.getId());
            return referencedWarning;
        }

        return null; // This return indicates no references found
    }
}
