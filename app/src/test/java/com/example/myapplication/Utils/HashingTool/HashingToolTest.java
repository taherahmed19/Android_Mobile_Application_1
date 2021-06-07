package com.example.myapplication.Utils.HashingTool;

import org.junit.Test;

import static org.junit.Assert.*;

public class HashingToolTest {

    @Test
    public void testSHA256Hashing() {
        String input = "data";
        String expectedHash = "3a6eb0790f39ac87c94f3856b2dd2c5d110e6811602261a9a923d3bb23adc8b7";
        String actualHash = HashingTool.HashString(input);

        assertEquals(expectedHash, actualHash);
    }
}