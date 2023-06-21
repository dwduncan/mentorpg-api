package mil.decea.mentorpgapi.domain.daoservices.repositories;

import mil.decea.mentorpgapi.domain.documents.DocumentType;
import mil.decea.mentorpgapi.domain.documents.UserDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface UserDocumentRepository extends JpaRepository<UserDocument, Long>, JpaSpecificationExecutor<UserDocument> {



}
