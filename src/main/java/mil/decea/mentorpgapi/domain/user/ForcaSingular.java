
package mil.decea.mentorpgapi.domain.user;

import mil.decea.mentorpgapi.domain.changewatch.InneValueChange;

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
 * Time: 19:40
 */
@InneValueChange("getSigla()")
public enum ForcaSingular {

    CIV("Civil", "Civil",3),
    FAB("FAB","Força Aérea Brasileira",0),
    EB("EB","Exército Brasileiro",1),
    MB("MB","Marinha do Brasil",2);


    private final String sigla;
    private final String extenso;
    private int prioridade;

    ForcaSingular(String sigla, String extenso, int prioridade) {
        this.sigla = sigla;
        this.extenso = extenso;
        this.prioridade = prioridade;
    }

    public String getSigla() {
        return sigla;
    }

    public String getExtenso() {
        return extenso;
    }

    @Override
    public String toString() {
        return sigla;
    }


    public static ForcaSingular getPorTipoSigla(final String sigla){
        Optional<ForcaSingular> ts = Arrays.stream(ForcaSingular.values()).filter(t->t.getSigla().equals(sigla)).findAny();
        return ts.orElse(null);
    }

    public static List<String> getStringLista(){
        return Arrays.stream(ForcaSingular.values()).map(ForcaSingular::getSigla).collect(Collectors.toList());
    }
/*

    public String getChave() {
        return sigla;
    }
*/

    public String getRotulo() {
        return sigla;
    }

    public int getPrioridade() {
        return prioridade;
    }

    public ForcaSingular convert(String s) {
        return Arrays.stream(ForcaSingular.values()).filter(p->getSigla().equalsIgnoreCase(s)).findAny().orElse(null);
    }
}
