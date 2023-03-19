package net.foxtam.antifraudsystem.service;

public class UsernameAlreadyExistsException extends Exception {
    public UsernameAlreadyExistsException(String msg) {
        super(msg);
    }
}
