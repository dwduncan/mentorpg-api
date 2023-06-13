package mil.decea.mentorpgapi.domain.daoservices;

import mil.decea.mentorpgapi.domain.daoservices.repositories.DocumentTypeRepository;
import mil.decea.mentorpgapi.domain.documents.DocumentTypeRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentsService {

    DocumentTypeRepository repository;

    @Autowired
    public DocumentsService(DocumentTypeRepository repository) {
        this.repository = repository;
    }


    public List<DocumentTypeRecord> getAllPersonalDocumentsTypes(){
        return repository.findAll().stream().map(DocumentTypeRecord::new).toList();
    }

}
