package com.example.exception.custom;

public class ExceedLimitException extends CustomException{
    public ExceedLimitException(String reason){
        super(reason);
    }
}
