package mil.decea.mentorpgapi.domain.user;

public record EnderecoRecord(
	String rua,
	String numero,
	String complemento,
	String bairro,
	String cidade,
	String uf,
	String cep) {
	public EnderecoRecord(Endereco obj){
		this(obj.getRua(),
			obj.getNumero(),
			obj.getComplemento(),
			obj.getBairro(),
			obj.getCidade(),
			obj.getUf(),
			obj.getCep());
	}
}