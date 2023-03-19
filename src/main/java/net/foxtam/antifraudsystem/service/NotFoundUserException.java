package net.foxtam.antifraudsystem.service;

public class NotFoundUserException extends Exception {
    public NotFoundUserException(String msg) {
        super(msg);
    }
}
