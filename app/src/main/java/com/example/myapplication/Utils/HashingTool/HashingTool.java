package com.example.myapplication.Utils.HashingTool;

import com.google.common.hash.Hashing;

import java.nio.charset.Charset;

public class HashingTool {

    public static String HashString(String input){
        return Hashing.sha256().hashString(input, Charset.defaultCharset()).toString();
    }

}
