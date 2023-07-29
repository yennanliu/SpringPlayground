package com.yen.SpringReddit.exceptions;

public class SubredditNotFoundException extends RuntimeException {

    public SubredditNotFoundException(String message) {
        super(message);
    }

}
