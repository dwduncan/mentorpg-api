package mil.decea.mentorpgapi.domain.user;

public record ContatoRecord(
	String emailPessoal,
	String emailInstitucional,
	int emailPreferencial,
	String telCelular,
	String telResidencial,
	String telTrabalho,
	String ramal) {
	public ContatoRecord(Contato obj) {
		this(obj.getEmailPessoal(),
			obj.getEmailInstitucional(),
			obj.getEmailPreferencial(),
			obj.getTelCelular(),
			obj.getTelResidencial(),
			obj.getTelTrabalho(),
			obj.getRamal());
	}
}