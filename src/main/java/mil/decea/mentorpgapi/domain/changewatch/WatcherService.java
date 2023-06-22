package mil.decea.mentorpgapi.domain.changewatch;

import mil.decea.mentorpgapi.domain.IdentifiedRecord;
import mil.decea.mentorpgapi.domain.user.User;
import mil.decea.mentorpgapi.util.ElementsType;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@SuppressWarnings("unchecked")
@Service
public class WatcherService {


    public List<ZChangeLog> checkChanges(ChangeWatcher<?> previousObject, IdentifiedRecord updatedObject, User responsable){

        TrackChange tcAnnotation = previousObject.getClass().getAnnotation(TrackChange.class);

        if (tcAnnotation == null){
            throw new IllegalArgumentException("A classe precisa ser anotada com @TrackChange");
        }

        if (!Objects.equals(updatedObject.getClass(), tcAnnotation.recordClass())){
            throw new IllegalArgumentException("O objeto atualizado não corresponde ao definido pelo objeto anterior a mudança");
        }

        if (responsable == null){
            throw new IllegalArgumentException("Obrigatório o responsável por realizar as mudanças");
        }

        List<ZChangeLog> changesList = new ArrayList<>(10);

        Class<?> actual = previousObject.getClass();

        while (actual != Object.class){

            for(Field field : actual.getDeclaredFields()){
                List<ZChangeLog> _logs = checkField(responsable, previousObject, updatedObject, field);
                changesList.addAll(_logs);
            }

            actual = actual.getSuperclass();

            if (actual == null) break;
        }



        return null;
    }

    private ZChangeLog createLog(User user, ChangeWatcher<?> previousObject, IdentifiedRecord updatedObject ,Field fieldBefore){

        if (fieldBefore.isAnnotationPresent(IgnoreTrackChange.class)) return null;

        ZChangeLog zlog = createBasicLog(user, previousObject, fieldBefore);

        NoValueTrack nvTrack = fieldBefore.getAnnotation(NoValueTrack.class);
        if (nvTrack != null){
            zlog.setAfterOrMessage(nvTrack.value());
        }else{

            try{
                Field fieldAfter = updatedObject.getClass().getDeclaredField(fieldBefore.getName());
                zlog.setBefore(getFieldValue(fieldBefore, previousObject));
                zlog.setAfterOrMessage(getFieldValue(fieldAfter, updatedObject));
            }catch (Exception ex){
                ex.printStackTrace();
                return null;
            }
        }
        return zlog;
    }

    private ZChangeLog createBasicLog(User user, ChangeWatcher<?> previousObject, Field fieldBefore){
        ZChangeLog zlog = new ZChangeLog();
        zlog.setResponsableId(user.getId());
        zlog.setResponsableName(user.getNomeQualificado() + " / " + user.getNomeCompleto());
        zlog.setObjectClass(previousObject.getChangingObject().getClass().getName());
        zlog.setFieldName(fieldBefore.getName());
        zlog.setObjectId(previousObject.getId());
        zlog.setFieldType(fieldBefore.getType().getName());
        zlog.setNeverExpires(fieldBefore.isAnnotationPresent(NeverExpires.class));
        zlog.setOccurrenceTime(LocalDateTime.now());
        return zlog;
    }


    private List<ZChangeLog> checkField(User user, ChangeWatcher<?> previousObject, IdentifiedRecord updatedObject ,Field fieldBefore){

        Class<?> elementType = ElementsType.getElementTypeOrNull(fieldBefore);

        if (elementType == null) {
            ZChangeLog _log = createLog(user, previousObject, updatedObject, fieldBefore);
            return List.of(_log);
        }else{
            TrackChange elementTC = elementType.getAnnotation(TrackChange.class);
            if (elementTC != null){
                try{
                    Field fieldAfter = elementTC.recordClass().getDeclaredField(fieldBefore.getName());
                    List<? extends ChangeWatcher<?>> beforeList = (List<? extends ChangeWatcher<?>>) fieldBefore.get(previousObject);
                    List<? extends IdentifiedRecord> afterList = (List<? extends IdentifiedRecord>) fieldAfter.get(updatedObject);
                    Map<Long, IdentifiedRecord> afterMap = new HashMap<>();
                    if (afterList != null) {
                        for (IdentifiedRecord ir : afterList) {
                            if (ir.id() != null || ir.id() > 0){
                                afterMap.put(ir.id(), ir);
                            }
                        }
                    }
                    Map<Long, ? extends ChangeWatcher<?>> beforeMap = beforeList.stream().collect(Collectors.toMap(ChangeWatcher::getId, Function.identity()));

                }catch (Exception ignore){}
            }
        }

        if (fieldBefore.isAnnotationPresent(IgnoreTrackChange.class)) return null;


        return new ArrayList<>();
    }


    private String getFieldValue(Field field, Object obj) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        InneValueChange ivc = field.getType().getAnnotation(InneValueChange.class);
        if (ivc != null){
            boolean isMethod = ivc.value().endsWith("()");
            Object value;
            if (isMethod){
                value = field.getType().getDeclaredMethod(ivc.value()).invoke(obj);
            }else{
                value =field.getType().getDeclaredField(ivc.value()).get(obj);
            }
            return value != null ? value.toString() : null;
        }else{
            return field.get(obj).toString();
        }
    }

}
