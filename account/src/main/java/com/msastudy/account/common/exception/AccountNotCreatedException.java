package com.msastudy.account.common.exception;

public class AccountNotCreatedException extends RuntimeException {
    private String username;
    private String message;

    public AccountNotCreatedException(String message, String username) {
        super("Account of username: " + username + " was not created");
        this.username = username;
        this.message = message;
    }

    public AccountNotCreatedException(String username) {
        super();
        this.username = username;
    }
}
