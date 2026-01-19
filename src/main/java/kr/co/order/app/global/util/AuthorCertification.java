package kr.co.order.app.global.util;

public class AuthorCertification {
    public static boolean certification(String username, String author){
        return author.equals(username);
    }
}
