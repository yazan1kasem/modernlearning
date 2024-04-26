package modern.learning.modernlearning;

import entities.U_user;

import java.util.List;
import java.util.stream.Collectors;

public abstract class Currentuser {
    static private String user;
     public static void setUsername(String usernamen){
        user = usernamen;
    }
    public static String getUsername(){
     return user;
     }

}
