package mil.decea.mentorpgapi.domain.daoservices;

import jakarta.transaction.Transactional;
import mil.decea.mentorpgapi.domain.changewatch.ObjectChangesChecker;
import mil.decea.mentorpgapi.domain.changewatch.logs.FieldChangedWatcher;
import mil.decea.mentorpgapi.domain.changewatch.logs.ObjecCreatedLog;
import mil.decea.mentorpgapi.domain.daoservices.minio.ClientMinioImplemantationException;
import mil.decea.mentorpgapi.domain.daoservices.minio.ClienteMinio;
import mil.decea.mentorpgapi.domain.daoservices.repositories.DocumentTypeRepository;
import mil.decea.mentorpgapi.domain.daoservices.repositories.UserDocumentRepository;
import mil.decea.mentorpgapi.domain.daoservices.repositories.UserRepository;
import mil.decea.mentorpgapi.domain.documents.DocumentTypeRecord;
import mil.decea.mentorpgapi.domain.documents.UserDocument;
import mil.decea.mentorpgapi.domain.documents.UserDocumentRecord;
import mil.decea.mentorpgapi.domain.user.AuthUser;
import mil.decea.mentorpgapi.etc.exceptions.MentorValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@SuppressWarnings("unused")
@Service
public class DocumentsService {

    UserRepository userRepository;
    DocumentTypeRepository documentTypeRepository;
    ChangeLogService changeLogService;
    UserDocumentRepository userDocumentRepository;
    ClienteMinio clienteMinio;

    @Autowired
    public DocumentsService(DocumentTypeRepository documentTypeRepository,
                            ChangeLogService changeLogService,
                            UserDocumentRepository userDocumentRepository,
                            UserRepository userRepository,
                            ClienteMinio clienteMinio) {
        this.documentTypeRepository = documentTypeRepository;

        this.changeLogService = changeLogService;
        this.userDocumentRepository = userDocumentRepository;
        this.clienteMinio = clienteMinio;
        this.userRepository = userRepository;
    }


    public List<DocumentTypeRecord> getAllPersonalDocumentsTypes(){
        return documentTypeRepository.findAll().stream().map(DocumentTypeRecord::new).toList();
    }

    public List<DocumentTypeRecord> getAllPersonalDocumentsTypesActives(){
        return documentTypeRepository.findAllByAtivoTrue().stream().map(DocumentTypeRecord::new).toList();
    }
    @Transactional
    public UserDocumentRecord saveUserDocument(UserDocumentRecord dados) throws ClientMinioImplemantationException {

        if (dados.userId() == null){
            throw new MentorValidationException("É necessário vincular um usuário ao documento!");
        }

        boolean newEntity = dados.id() == null || dados.id() < 1;

        try {

            var user = userRepository.getReferenceById(dados.userId());

            UserDocument userDoc = newEntity ? new UserDocument(user) : userDocumentRepository.getReferenceById(dados.id());

            ObjectChangesChecker changes = userDoc.onValuesUpdated(dados);

            userDoc = userDocumentRepository.save(userDoc);

            clienteMinio.updateObject(userDoc);

            AuthUser ausr = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            if (newEntity) changes.setObjectAndParentIdIfEquals(userDoc.getId());

            changeLogService.insert(changes);

            return new UserDocumentRecord(userDoc);
        } catch (ClientMinioImplemantationException e) {
            throw new ClientMinioImplemantationException(e);
        } catch (Exception e){
            e.printStackTrace();
            throw new MentorValidationException("Falha ao salvar documento pessoal");
        }
    }


}
