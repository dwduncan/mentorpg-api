package mil.decea.mentorpgapi.util;

import mil.decea.mentorpgapi.domain.user.UserImageRecord;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ReactMethodDeclaration {

    public static Map<Class<?>, ReactMethodDeclaration> declaredMethodsForClass= new HashMap();
    private final Class<?> ownerClass;
    private final Map<String,String> methods = new HashMap<>();

    static {
        declaredMethodsForClass.put(UserImageRecord.class,
                new ReactMethodDeclaration.ReactClassMethodBuilder(UserImageRecord.class)
                        .addMethod("src: () => string,", """
                                                                
                                    src(): string {
                                         if (this){
                                             if (this.base64Data) return this.base64Data;
                                             return !!this.arquivoUrl ? this.arquivoUrl : '/assets/images/simpleface.svg';
                                         }
                                         return '/assets/images/simpleface.svg';
                                    }                                                                
                                """)
                        .build());
    }

    private ReactMethodDeclaration(Class<?> ownerClass) {
        this.ownerClass = ownerClass;
    }

    public static Map<String,String> getDeclaredMethodsForClass(Class<?> ownerClass){
        ReactMethodDeclaration r = declaredMethodsForClass.getOrDefault(ownerClass, null);
        return r != null ? r.getMethods() : null;
    }

    /**
     * The key is a function declaration as expected in a typescript interface, meanwhile the value is the function implementation as expected inside a typescript class
     */
    public Map<String, String> getMethods() {
        return methods;
    }

    public Class<?> getOwnerClass() {
        return ownerClass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReactMethodDeclaration that = (ReactMethodDeclaration) o;
        return Objects.equals(ownerClass, that.ownerClass);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ownerClass);
    }

    public static class ReactClassMethodBuilder{
        private final ReactMethodDeclaration rmd;

        public ReactClassMethodBuilder(Class<?> targetClass){
            rmd = new ReactMethodDeclaration(targetClass);
        }

        public ReactClassMethodBuilder addMethod(String methodInterfaceDeclaration, String methodClassImplementation){
            rmd.methods.put(methodInterfaceDeclaration,methodClassImplementation);
            return this;
        }

        public ReactMethodDeclaration build(){
            return rmd;
        }
    }
}
