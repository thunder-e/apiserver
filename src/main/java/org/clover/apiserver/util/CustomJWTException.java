package org.clover.apiserver.util;

public class CustomJWTException extends RuntimeException{
    
    //사용자 정의 예외
    public CustomJWTException(String message) {
        super(message);
    }
}
