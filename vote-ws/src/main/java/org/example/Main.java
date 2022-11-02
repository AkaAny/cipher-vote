package org.example;

import org.teavm.classlib.java.io.TConsole;
import org.teavm.jso.JSObject;
import org.teavm.jso.browser.Window;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Main {
    public static void main(String[] args) throws Throwable {
        JSConsole.log("java call js func");
        MessageDigest.getInstance("MD5");
        System.out.println("Hello world!");
    }
}