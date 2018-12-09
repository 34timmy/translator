package com.tf.translator.service;


public interface TranslatorService {

    String translate(String input, String from, String to);

    void changeKey(String key);
}
