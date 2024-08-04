package com.buildout1.coderhack.exceptions;

public class userNotFound extends RuntimeException{
    public userNotFound(String message) {
        super(message);
    }
}