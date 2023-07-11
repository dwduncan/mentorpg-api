package mil.decea.mentorpgapi.domain.agenda;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;

public interface Periodicidade {

    Map<DayOfWeek, Integer> mapaDiasSemana = Map.of(DayOfWeek.MONDAY,1, DayOfWeek.TUESDAY,2,DayOfWeek.WEDNESDAY,4, DayOfWeek.THURSDAY,8,DayOfWeek.FRIDAY,16,DayOfWeek.SATURDAY,32,DayOfWeek.SUNDAY,64);

    int SEGUNDA = 1;
    int TERCA = 2;
    int QUARTA = 4;
    int QUINTA = 8;
    int SEXTA = 16;
    int SABADO = 32;
    int DOMINGO = 64;

    int getDiasSemana();
    CICLO getCiclo();


    default int calcularValorCompostoDiasSemana(List<DayOfWeek> diasDaSemana){
        if (diasDaSemana != null) return diasDaSemana.stream().mapToInt(mapaDiasSemana::get).sum();
        return 0;
    }

}
