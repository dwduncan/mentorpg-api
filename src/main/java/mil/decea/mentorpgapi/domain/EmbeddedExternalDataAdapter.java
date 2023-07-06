package mil.decea.mentorpgapi.domain;

import lombok.NoArgsConstructor;
import mil.decea.mentorpgapi.util.DateTimeAPIHandler;
import org.springframework.stereotype.Service;

@NoArgsConstructor
@Service
public class EmbeddedExternalDataAdapter extends AbstractEntityDTOAdapter<EmbeddedExternalData, EmbeddedExternalDataRecord> {


	@Override
	public EmbeddedExternalDataRecord generateRecord() { return new EmbeddedExternalDataRecord(getEntity());}

	public EmbeddedExternalData updateEntity() {
		getEntity().setId(getIdentifiedRecord().id());
		getEntity().setPreviousFileName(getIdentifiedRecord().previousFileName());
		getEntity().setNomeArquivo(getIdentifiedRecord().nomeArquivo());
		getEntity().setLastUpdate(DateTimeAPIHandler.converterStringDate(getIdentifiedRecord().lastUpdate()));
		getEntity().setBase64Data(getIdentifiedRecord().base64Data());
		getEntity().setFormato(getIdentifiedRecord().formato());
		getEntity().setArquivoUrl(getIdentifiedRecord().arquivoUrl());
		getEntity().setTamanho(getIdentifiedRecord().tamanho());
		return getEntity();
	}
}