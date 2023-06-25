package mil.decea.mentorpgapi.util;

import mil.decea.mentorpgapi.domain.user.UserRecord;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.temporal.Temporal;
import java.util.*;

public class ReflectionUtils {


    public static Class<?> getElementType(Field field){

        if (!Collection.class.isAssignableFrom(field.getType())){
            throw new IllegalArgumentException("This method only accepts fields that is a Collection");
        }

        Type genericFieldType = field.getGenericType();

        if(genericFieldType instanceof ParameterizedType){
            ParameterizedType aType = (ParameterizedType) genericFieldType;
            Type[] fieldArgTypes = aType.getActualTypeArguments();
            return (Class) fieldArgTypes[0];
            /*for(Type fieldArgType : fieldArgTypes){
                Class fieldArgClass = (Class) fieldArgType;
                System.out.println("fieldArgClass = " + fieldArgClass);
            }*/
        }

        return Object.class;
    }

    public static Class<?> getElementTypeOrNull(Field field){

        if (!Collection.class.isAssignableFrom(field.getType())){
            return null;
        }

        Type genericFieldType = field.getGenericType();

        if(genericFieldType instanceof ParameterizedType){
            ParameterizedType aType = (ParameterizedType) genericFieldType;
            Type[] fieldArgTypes = aType.getActualTypeArguments();
            return (Class) fieldArgTypes[0];
        }

        return null;
    }

    public static Class<?> getElementType(Method method){

        if (!Collection.class.isAssignableFrom(method.getReturnType())){
            throw new IllegalArgumentException("This method only accepts fields that are Collections");
        }

        Type genericFieldType = method.getGenericReturnType();

        if(genericFieldType instanceof ParameterizedType){
            ParameterizedType aType = (ParameterizedType) genericFieldType;
            Type[] fieldArgTypes = aType.getActualTypeArguments();
            return (Class) fieldArgTypes[0];
            /*for(Type fieldArgType : fieldArgTypes){
                Class fieldArgClass = (Class) fieldArgType;
                System.out.println("fieldArgClass = " + fieldArgClass);
            }*/
        }

        return Object.class;
    }

    public static Field getFieldByNameRecursively(Class<?> _class, String _fieldName){

        Class<?> actual = _class;

        while (actual != Object.class){
            try {
                return actual.getDeclaredField(_fieldName);
            } catch (NoSuchFieldException e) {
                actual = actual.getSuperclass();
            }

            if (actual == null) break;
        }

        return null;
    }

    public static boolean isToStringReadable(Class<?> _class){
        return  _class.isPrimitive()
                || String.class.isAssignableFrom(_class)
                || Number.class.isAssignableFrom(_class)
                || Boolean.class.isAssignableFrom(_class)
                || Temporal.class.isAssignableFrom(_class);
    }

    public static Set<Field> getAllNonCollectionsFields(Class<?> _class){

        Class<?> actual = _class;
        Set<Field> fields = new HashSet<>(_class.getDeclaredFields().length);

        while (actual != Object.class){
            fields.addAll(Arrays.stream(_class.getDeclaredFields()).filter(f->!Collection.class.isAssignableFrom(f.getType())).toList());
            actual = actual.getSuperclass();
            if (actual == null) break;
        }

        return fields;
    }

    public static Set<Field> getAllFields(Class<?> _class, boolean excludeCollectionsFields, String[] fieldsNames){

        boolean emptyArgs =  (fieldsNames == null || fieldsNames.length == 0);

        if (emptyArgs && excludeCollectionsFields){
            return getAllNonCollectionsFields(_class);
        }

        Class<?> actual = _class;
        Set<Field> fields = new HashSet<>(_class.getDeclaredFields().length);

        List<String> _fieldsNames = emptyArgs ? new ArrayList<>() : Arrays.stream(fieldsNames).toList();

        while (actual != Object.class){
            fields.addAll(Arrays.stream(_class.getDeclaredFields())
                    .filter(f -> emptyArgs || _fieldsNames.contains(f.getName()))
                    .filter(f-> !excludeCollectionsFields || !Collection.class.isAssignableFrom(f.getType()))
                    .toList());
            actual = actual.getSuperclass();
            if (actual == null) break;
        }

        return fields;
    }

    public static void main(String ...args) throws NoSuchFieldException {
        System.out.println(getElementType(UserRecord.class.getDeclaredField("documents")).getSimpleName());
    }

}
