package mil.decea.mentorpgapi.domain.user;
import java.lang.Long;

public record DataAuthorityRecord(
String name,
Long id,
String cpf) {
	public DataAuthorityRecord(DataAuthority obj) {
		this(obj.getName(),
			obj.getId(),
			obj.getCpf());
	}

	public DataAuthorityRecord(Long id, String name, String cpf) {
		this(name,
			id,
			cpf);
	}
}