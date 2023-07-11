package mil.decea.mentorpgapi.domain.academic;


import mil.decea.mentorpgapi.domain.user.User;

public interface Autor {
    User getUsuario();

    void setUsuario(User usuario);

    <T extends AbstractPublicacao> T getPublicacao();

}
