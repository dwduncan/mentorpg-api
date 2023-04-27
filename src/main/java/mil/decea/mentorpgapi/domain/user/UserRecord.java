package mil.decea.mentorpgapi.domain.user;
import java.time.LocalDate;
import java.util.List;

public record UserRecord(
	Long id,
	String cpf,
	Titulacao titulacao,
	Posto posto,
	String quadro,
	String especialidade,
	String nomeGuerra,
	String nomeCompleto,
	Sexo sexo,
	ForcaSingular forcaSingular,
	LocalDate ultimaPromocao,
	boolean pttc,
	int antiguidadeRelativa,
	String senha,
	String roles,
	Endereco endereco,
	Contato contato,
	UserImage photo,
	String identidade,
	String passaporte,
	String nacionalidade,
	String naturalidade,
	LocalDate dataNascimento,
	LocalDate validadePassaporte,
	LocalDate dataPraca,
	LocalDate proximaPromocao,
	String banco,
	String agencia,
	String conta,
	String saram,
	String observacoes,
	List documents){
	public UserRecord(User obj){
		this(obj.getId(), obj.getCpf(), obj.getTitulacao(), obj.getPosto(), obj.getQuadro(), obj.getEspecialidade(), obj.getNomeGuerra(), obj.getNomeCompleto(), obj.getSexo(), obj.getForcaSingular(), obj.getUltimaPromocao(), obj.isPttc(), obj.getAntiguidadeRelativa(), obj.getSenha(), obj.getRoles(), obj.getEndereco(), obj.getContato(), obj.getPhoto(), obj.getIdentidade(), obj.getPassaporte(), obj.getNacionalidade(), obj.getNaturalidade(), obj.getDataNascimento(), obj.getValidadePassaporte(), obj.getDataPraca(), obj.getProximaPromocao(), obj.getBanco(), obj.getAgencia(), obj.getConta(), obj.getSaram(), obj.getObservacoes(), obj.getDocuments());
	}
}