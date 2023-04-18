/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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

@SuppressWarnings("unused")
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Endereco {

    @Column(columnDefinition = "TEXT")
    private String rua;

    @Column(columnDefinition = "TEXT")
    private String numero;

    @Column(columnDefinition = "TEXT")
    private String complemento;

    @Column(columnDefinition = "TEXT")
    private String bairro;

    @Column(columnDefinition = "TEXT")
    private String cidade;

    @Column(columnDefinition = "TEXT")
    private String uf;
    @Column(columnDefinition = "TEXT")
    private String cep;

    public void setUf(String uf) {
        this.uf = uf != null ? uf.toUpperCase() : null;
    }


}
