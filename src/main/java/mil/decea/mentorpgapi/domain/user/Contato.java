
package mil.decea.mentorpgapi.domain.user;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Força Aérea Brasileira - (FAB)
 * Istituto Tecnológico de Aeronáutica - (ITA)
 * Programa de Pós-Graduação em Aplicações Operacionais - (PPGAO)
 * Author: Dennys Wallace Duncan Imbassahy
 * (Ten Cel Av Duncan)
 * Email: dennysduncan@gmail.com  /  dwduncan@ita.br
 * Date: 02/03/2021
 * Time: 12:44
 */
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Contato {

    public static final int EMAIL_PESSOAL = 1;
    public static final int EMAIL_INSTITUCIONAL = 2;


    @Column(columnDefinition = "TEXT")
    private String emailPessoal;

    @Column(columnDefinition = "TEXT")
    private String emailInstitucional;

    private int emailPreferencial;

    @Column(columnDefinition = "TEXT")
    private String telCelular;

    @Column(columnDefinition = "TEXT")
    private String telResidencial;

    @Column(columnDefinition = "TEXT")
    private String telTrabalho;

    @Column(columnDefinition = "TEXT")
    private String ramal;


    public String getDefautEmail(){
        return emailPreferencial == EMAIL_PESSOAL && emailPessoal != null && !emailPessoal.isEmpty() ? emailPessoal : emailInstitucional;
    }

}
