
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
 * Time: 19:19
 */

@SuppressWarnings("unused")
public enum Sexo {
    MASCULINO("Masculino","Masc"),
    FEMININO("Feminino","Fem");

    private String desc;
    private String sigla;

    Sexo(String desc, String sigla) {
        this.desc = desc;
        this.sigla = sigla;
    }
    public String getDescricao() {
        return desc;
    }


    public static Sexo getPorDescricao(final String valor){
        Optional<Sexo> ts = Arrays.stream(Sexo.values()).filter(t->t.getDescricao().equals(valor)).findAny();
        return ts.orElse(null);
    }


    public static List<String> getStringLista(){
        return Arrays.stream(Sexo.values()).map(Sexo::getDescricao).collect(Collectors.toList());
    }

    public String getSigla() {
        return sigla;
    }
}
