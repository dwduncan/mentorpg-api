package mil.decea.mentorpgapi.domain.administrativo;


import java.io.Serializable;

public class IdiomaAdapter implements Serializable {

    private static final long serialVersionUID = 1L;

    final UnarioWrapper unarioWrapper;
    public static final int IDIOMA_LE = 1;
    public static final int IDIOMA_FALA = 2;
    public static final int IDIOMA_ESCREVE = 4;
    public static final int IDIOMA_ENTENDE = 8;
    private int valorOriginal;

    public IdiomaAdapter(UnarioWrapper unarioWrapper) {
        this.unarioWrapper = unarioWrapper;
        this.setValorOriginal(unarioWrapper.getValorUnario());
    }

    public boolean isLeitura() {
        return isAtribuido(IDIOMA_LE);
    }

    public void setLeitura(boolean le) {
        setAtribuido(le, IDIOMA_LE);
    }

    public boolean isFala() {
        return isAtribuido(IDIOMA_FALA);
    }

    public void setFala(boolean fala) {
        setAtribuido(fala, IDIOMA_FALA);
    }

    public boolean isEscreve() {
        return isAtribuido(IDIOMA_ESCREVE);
    }

    public void setEscreve(boolean escreve) {
        setAtribuido(escreve, IDIOMA_ESCREVE);
    }

    public boolean isEntende() {
        return isAtribuido(IDIOMA_ENTENDE);
    }

    public void setEntende(boolean entende) {
        setAtribuido(entende, IDIOMA_ENTENDE);
    }

    private boolean isAtribuido(int valor) {
        return (getValorOriginal() & valor) != 0;
    }

    private void setAtribuido(boolean atribuir, int valor) {
        boolean check = isAtribuido(valor);
        if (atribuir != check) {
            if (atribuir) setValorOriginal(getValorOriginal() + valor);
            else setValorOriginal(getValorOriginal() - valor);
        }
        unarioWrapper.setValorUnario(valorOriginal);
    }

    public int getValorOriginal() {
        return valorOriginal;
    }

    public void setValorOriginal(Integer valorOriginal) {
        this.valorOriginal = valorOriginal != null ? valorOriginal : 0;
    }

    public String getNomeCampo(){
        return unarioWrapper.getNomeCampo();
    }
}
