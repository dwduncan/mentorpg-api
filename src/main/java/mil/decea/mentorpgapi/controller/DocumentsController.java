package mil.decea.mentorpgapi.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import mil.decea.mentorpgapi.domain.daoservices.minio.ClientMinioImplemantationException;
import mil.decea.mentorpgapi.domain.daoservices.minio.ClienteMinio;
import mil.decea.mentorpgapi.domain.documents.ViewDocumentRecord;
import mil.decea.mentorpgapi.domain.documents.UserDocumentRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/docs")
@SecurityRequirement(name = "bearer-key")
public class DocumentsController {
    private final ClienteMinio clienteMinio;

    @Autowired
    public DocumentsController(ClienteMinio clienteMinio) {
        this.clienteMinio = clienteMinio;
    }

    @PostMapping("/view")
    @Transactional
    public ResponseEntity<?> viewDocument(@RequestBody @Valid UserDocumentRecord doc) throws ClientMinioImplemantationException {
        String url = clienteMinio.createSasUrl(doc.bucket(), doc.storageDestinationPath());
        return ResponseEntity.ok(new ViewDocumentRecord(doc.id(),url, clienteMinio.expiraEmSegundos, doc.storageDestinationPath(),doc.bucket(),doc.nomeArquivo()));
    }



}
