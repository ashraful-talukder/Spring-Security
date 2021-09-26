package com.ash.springsecurity.email;

public interface EmailSender {

    void send(String to, String email);
}
