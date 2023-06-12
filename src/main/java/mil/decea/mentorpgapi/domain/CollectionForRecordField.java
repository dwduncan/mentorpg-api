package mil.decea.mentorpgapi.domain;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collection;
import java.util.List;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface CollectionForRecordField {
    Class<?> elementsOfType();
    Class<? extends Collection> collectionOfType() default List.class;
}
