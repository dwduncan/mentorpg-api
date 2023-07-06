package mil.decea.mentorpgapi.domain.documents;

import lombok.NoArgsConstructor;
import mil.decea.mentorpgapi.domain.AbstractEntityDTOAdapter;
import mil.decea.mentorpgapi.util.DateTimeAPIHandler;
import org.springframework.stereotype.Service;

@NoArgsConstructor
@Service
public class UserDocumentAdapter extends AbstractEntityDTOAdapter<UserDocument, UserDocumentRecord> {

    @Override
    public UserDocumentRecord generateRecord() {
        return new UserDocumentRecord(getEntity());
    }

    @Override
    public UserDocument updateEntity() {
        getEntity().setPreviousFileName(getEntity().getNomeArquivo());
        getEntity().setArquivoUrl(this.getIdentifiedRecord().arquivoUrl());
        getEntity().setTipoDocumentacao(new DocumentType(this.getIdentifiedRecord().tipoDocumentacao()));
        getEntity().setMotivoRecusa(this.getIdentifiedRecord().motivoRecusa());
        getEntity().setIdExigencia(this.getIdentifiedRecord().idExigencia());
        getEntity().setObrigatorio(this.getIdentifiedRecord().obrigatorio());
        getEntity().setStatusDocumento(this.getIdentifiedRecord().statusDocumento());
        getEntity().setAtivo(this.getIdentifiedRecord().ativo());
        getEntity().setNomeArquivo(this.getIdentifiedRecord().nomeArquivo());
        getEntity().setFormato(this.getIdentifiedRecord().formato());
        getEntity().setTamanho(this.getIdentifiedRecord().tamanho());
        getEntity().setLastUpdate(DateTimeAPIHandler.converterStringDate(this.getIdentifiedRecord().dataHoraUpload()));
        getEntity().setBase64Data(this.getIdentifiedRecord().base64Data());
        return getEntity();
    }
}
