package mil.decea.mentorpgapi.domain.changewatch;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Utilizado quando o método toString do objeto não é a mais adequada para acompanhamento e
 * registro da alteração, como em enums ou objetos que podem através de algum campo ou método
 * interno fornecer o valor adequado para antes e depois de alterado.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface InnerValueChange {

    /**
     * Se o nome fornecido terminar com (), significa que é o nome de um método, caso contrário refere-se
     * a um campo da classe.
     * @return o nome do método ou campo existente dentro da classe desse objeto que fornece o valor desejado
     */
    String value();

}
