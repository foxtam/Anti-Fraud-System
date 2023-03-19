package net.foxtam.antifraudsystem.service;

public class WrongRoleException extends Exception {
    public WrongRoleException(String msg) {
        super(msg);
    }
}
