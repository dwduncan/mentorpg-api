package mil.decea.mentorpgapi.domain.user;
import jakarta.validation.constraints.NotNull;
import java.lang.Long;
import jakarta.validation.constraints.Size;

public record AuthUserRecord(
@NotNull(message="O id n\u00e3o pode ser nulo", payload={}, groups={})
Long id,
	DataAuthorityRecord dataAuthorityRecord,
String senhaAntiga,
@Size(groups={}, min=8, message="A senha deve possuir no m\u00ednimo 8 caract\u00e9res", payload={}, max=2147483647)
String senha,
boolean ativo,
@NotNull(message="O cpf n\u00e3o pode ser nulo", payload={}, groups={})
String cpf,
String role) {
	public AuthUserRecord(AuthUser obj) {
		this(obj.getId(),
			new DataAuthorityRecord(obj.getDataAuthority()),
			obj.getSenhaAntiga(),
			obj.getSenha(),
			obj.isAtivo(),
			obj.getCpf(),
			obj.getRole());
	}

	public AuthUserRecord(Long id, String cpf, String role, String senha, boolean ativo) {
		this(id,
			null,
			null,
			senha,
			ativo,
			cpf,
			role);
	}
}