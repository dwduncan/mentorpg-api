
package mil.decea.mentorpgapi.domain.user;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Força Aérea Brasileira - (FAB)
 * Istituto Tecnológico de Aeronáutica - (ITA)
 * Programa de Pós-Graduação em Aplicações Operacionais - (PPGAO)
 * Author: Dennys Wallace Duncan Imbassahy
 * (Ten Cel Av Duncan)
 * Email: dennysduncan@gmail.com  /  dwduncan@ita.br
 * Date: 03/03/2021
 * Time: 19:24
 */
public enum Posto{

    TB("Ten Brig","Tenente Brigadeiro",2, true, false, false),
    MB("Maj Brig","Major Brigadeiro",3, true, false, false),
    BR("Brig","Brigadeiro",4, true, true, false),
    CEL("Cel","Coronel",5, true, true, false),
    CMG("CMG","Capitão de Mar e Guerra",5, false, false, true),
    TCEL("Ten Cel","Tenente Coronel",6, true, true, false),
    CF("CF","Capitão de Fragata",6, false, false, true),
    MAJ("Maj","Major",7, true, true, false),
    CC("CC","Capitão de Corveta",7, false, false, true),
    CAP("Cap","Capitão",8, true, true, false),
    CT("CT","Capitão Tenente",8, false, false, true),
    TEN1("1º Ten","Primeiro Tenente",9, true, true, true),
    TEN2("2º Ten","Segundo Tenente",10, true, true, true),
    CIV("CV","Civil",10, true, true, true),
    ASP("Asp","Aspirante a Oficial",11, true, true, false),
    SO("SO","Suboficial",12, true, true, true),
    SGT1("1º Sgt","Primero Sargento",13, true, true, true),
    SGT2("2º Sgt","Segundo Sargento",14, true, true, true),
    SGT3("3º Sgt","Terceiro Sargento",15, true, true, true),
    CB("Cb","Cabo",16, true, true, true),
    S1("S1","Soldado de 1º Classe",17, true, true, true),
    S2("S2","Soldado de 2º Classe",18, true, true, true),
    NIL("","",50, true, true, true);

    private final String sigla;
    private final String extenso;
    private final int antiguidade;

    private final boolean fab;
    private final boolean eb;
    private final boolean mb;

    Posto(String sigla, String extenso, int antiguidade, boolean fab, boolean eb, boolean mb) {
        this.sigla = sigla;
        this.extenso = extenso;
        this.antiguidade = antiguidade;
        this.fab = fab;
        this.eb = eb;
        this.mb = mb;
    }



    public String getSigla() {
        return sigla;
    }

    public String getExtenso() {
        return extenso;
    }

    public int getAntiguidade() {
        return antiguidade;
    }

    @Override
    public String toString() {
        return  sigla;
    }


    public static Posto getPorSigla(final String valor){
        Optional<Posto> ts = Arrays.stream(Posto.values()).filter(t->t.getSigla().equals(valor)).findAny();
        return ts.orElse(null);
    }

    public static List<Posto> getPostosForcaSingular(final ForcaSingular forca){
        switch (forca) {
            case FAB: return Arrays.stream(Posto.values()).filter(Posto::isFab).collect(Collectors.toList());
            case EB: return Arrays.stream(Posto.values()).filter(Posto::isEb).collect(Collectors.toList());
            case MB: return Arrays.stream(Posto.values()).filter(Posto::isMb).collect(Collectors.toList());
            default: return null;
        }
    }

    public boolean isFab() {
        return fab;
    }

    public boolean isEb() {
        return eb;
    }

    public boolean isMb() {
        return mb;
    }
}
