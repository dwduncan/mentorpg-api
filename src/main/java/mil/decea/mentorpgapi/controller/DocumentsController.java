package mil.decea.mentorpgapi.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import mil.decea.mentorpgapi.domain.daoservices.DocumentsService;
import mil.decea.mentorpgapi.domain.daoservices.minio.ClientMinioImplemantationException;
import mil.decea.mentorpgapi.domain.daoservices.minio.ClienteMinio;
import mil.decea.mentorpgapi.domain.documents.records.ExternalDocumentRecord;
import mil.decea.mentorpgapi.domain.documents.records.UserDocumentRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/docs")
@SecurityRequirement(name = "bearer-key")
public class DocumentsController {
    private final ClienteMinio clienteMinio;
    private final DocumentsService documentsService;

    @Autowired
    public DocumentsController(ClienteMinio clienteMinio,
                               DocumentsService documentsService) {

        this.clienteMinio = clienteMinio;
        this.documentsService = documentsService;

    }

    @PostMapping("/view")
    @Transactional
    public ResponseEntity<?> viewDocument(@RequestBody @Valid ExternalDocumentRecord doc) throws ClientMinioImplemantationException {
        String url = clienteMinio.createSasUrl(doc.bucket(), doc.storageDestinationPath());
        return ResponseEntity.ok(new ExternalDocumentRecord(doc.id(),url, clienteMinio.expiraEmSegundos, doc.storageDestinationPath(),doc.bucket(),doc.nomeArquivo()));
    }

    @PostMapping("/savepersonaldoc")
    @Transactional
    public ResponseEntity<?> savePersonalDocument(@RequestBody @Valid UserDocumentRecord doc) throws ClientMinioImplemantationException {
        return ResponseEntity.ok(documentsService.saveUserDocument(doc));
    }

}
