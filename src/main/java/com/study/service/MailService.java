package com.study.service;

public interface MailService {

    void sendMailForActivationAccount(String activationUrl, String mail);
}
