package mil.decea.mentorpgapi.domain;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.*;

import java.io.Serializable;
import java.lang.reflect.Field;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class BaseEntity implements Serializable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    boolean ativo = true;
    public void updateValues(Record dados){

        for(Field f : dados.getClass().getDeclaredFields()){
            String methodRadix = f.getName().substring(0,1).toUpperCase() + f.getName().substring(1);
            if (f.getType().isRecord()){
                String m_name = "get" + methodRadix;
                try {
                    Object obj = getClass().getMethod(m_name).invoke(this);
                    if (obj instanceof BaseEntity){
                        ((BaseEntity) obj).updateValues((Record) f.get(dados));
                    }
                }catch (Exception ignore){}
            }else{
                String m_name = "set" + methodRadix;
                try {
                    getClass().getMethod(m_name).invoke(this, f.get(dados));
                }catch (Exception ignore){}
            }
        }

    }

}
