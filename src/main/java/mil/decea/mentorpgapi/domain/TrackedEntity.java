package mil.decea.mentorpgapi.domain;

import jakarta.persistence.MappedSuperclass;
/**
 * <br>
 * <b>ATENÇÃO: SE A CLASSE NÃO ESTIVER ANOTADA COM <i>@TrackOnlySelectedFields</i> AS MUDANÇAS SOMENTE SERÃO COMPUTADAS SE A REFERIDA
 * CLASSE FOR ATRIBUTO DE UMA OUTRA CLASSE QUE POSSUA ESSA ANOTAÇÃO</b>
 * <br> <br>
 * Método que todas as entidades devem implementar caso seja necessário rastrear suas alterações. No caso onde não
 * se deseja rastrear ou os dados são verificados pelo método de updateValues de outro objeto, recomenda-se retornar
 * uma List vazia.
 * @param record objeto DTO com os valores a serem alterados na entidade ao qual se refere.
 * @return a lista de mudanças caso tenham ocorrido ou um array sempre vazio quando não houver controle de mudanças
 */

public interface TrackedEntity extends DomainEntity{


    /**
     * Descritor simples que resume ou sintetiza em uma única string o que foi alterado de importante no objeto.
     * Extremamente importante nos objetos que pertençam a alguma coleção de dados (List/Set), para representar
     * o que foi incluído ou removido. Pode ser aplicado em qualquer cenário no qual não é necessário saber cada
     * alteração dos valores da entidade.
     * @return a descrição resumida da mudança
     */
     String getEntityDescriptor();
     TrackedEntity getParentObject();


}
