package mil.decea.mentorpgapi.domain.daoservices.repositories;

import mil.decea.mentorpgapi.domain.documents.DocumentType;
import mil.decea.mentorpgapi.domain.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface DocumentTypeRepository extends JpaRepository<DocumentType, Long>, JpaSpecificationExecutor<DocumentType> {

    List<DocumentType> findAllByAtivoTrue();

}
