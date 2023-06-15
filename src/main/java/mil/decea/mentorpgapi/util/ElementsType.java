package mil.decea.mentorpgapi.util;

import mil.decea.mentorpgapi.domain.user.UserRecord;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;

public class ElementsType {


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


    public static void main(String ...args) throws NoSuchFieldException {
        System.out.println(getElementType(UserRecord.class.getDeclaredField("documents")).getSimpleName());
    }

}
