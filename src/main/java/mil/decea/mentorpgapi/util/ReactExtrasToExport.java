package mil.decea.mentorpgapi.util;

import mil.decea.mentorpgapi.domain.user.AuthUserRecord;

import java.util.HashMap;
import java.util.Map;

public class ReactExtrasToExport {

    private static final Map<Class<?>, String> mapFunctions = new HashMap<>();
    private static final Map<Class<?>, String> mapImports = new HashMap<>();


    static {
        mapImports.put(AuthUserRecord.class,"import {Roles} from './Roles';\r\n" +
                "import {DataAuthorityRecord, IDataAuthorityRecord} from './IDataAuthorityRecord';\r\nimport IUserRecord from \"./IUserRecord\";\r\n\r\n");
        mapFunctions.put(AuthUserRecord.class,
                 """
                         \r\n
                         export function addRole(role: Roles, usr: IAuthUserRecord){
                          	if (!usr?.role || usr.role.trim() === '') usr.role = role.name;
                          	else{
                          		if (!usr.role.includes(role.name)) usr.role = usr.role.concat(' ',role.name);
                          	}
                         }           
                          
                         export function removeRole(role: Roles, usr: IAuthUserRecord){
                           	if (!!usr?.role && usr.role.includes(role.name)) {
                           		usr.role = usr.role.replace(role.name,'').replaceAll('\s\s+'," ").trim();
                           	}
                         }             
                           
                          export function removeUserRole(role: string, usr: IUserRecord){
                             if (!!usr?.role && usr.role.includes(role)) {
                                usr.role = usr.role.replace(role,'').replaceAll('\s\s+'," ").trim();
                             }
                          }
                           
                           
                         export function addUserRole(role: string, usr: IUserRecord){
                           	 if (!usr?.role || usr.role.trim() === '') usr.role = role;
                           	 else{
                           		if (!usr.role.includes(role)) usr.role = usr.role.concat(' ',role);
                           	 }
                         }
                           
                         export function checkRoles(user_roles: string | string[] | Roles | Roles[],
                                                      required_roles: string | string[] | Roles | Roles[]) {
                           
                             if (!required_roles) return true;
                             if (!user_roles) return false;
                           
                             const owneds = Array.isArray(user_roles) ? Object.values(user_roles) : [user_roles];
                             const requireds = Array.isArray(required_roles) ?  Object.values(required_roles) : [required_roles];
                           
                             return owneds.some(own=> requireds.includes(own));
                         }                                                 
                         """);
    }


    /**
     *
     * @param classe
     * @return uma String com todas as funções que serão anexadas após a classe ou null se não houver nenhuma função adicional para a classe solicitada
     */
    public static String getFunctions(Class<?> classe){
        return mapFunctions.get(classe);
    }
    public static String getImports(Class<?> classe){
        return mapImports.getOrDefault(classe,"");
    }


}
