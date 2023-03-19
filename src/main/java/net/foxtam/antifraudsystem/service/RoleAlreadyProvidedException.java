package net.foxtam.antifraudsystem.service;

public class RoleAlreadyProvidedException extends Exception {
    public RoleAlreadyProvidedException(String msg) {
        super(msg);
    }
}
