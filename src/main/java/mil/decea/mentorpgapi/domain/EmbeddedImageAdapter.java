package mil.decea.mentorpgapi.domain;


import mil.decea.mentorpgapi.domain.AbstractEntityDTOAdapter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;import java.lang.Long;
import mil.decea.mentorpgapi.util.DateTimeAPIHandler;


@NoArgsConstructor
@Service
public class EmbeddedImageAdapter extends AbstractEntityDTOAdapter<EmbeddedImage, EmbeddedImageRecord> {


	@Override
	public EmbeddedImageRecord generateRecord() { return new EmbeddedImageRecord(getEntity());}

	public EmbeddedImage updateEntity() {
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