package com.example.myapplication.Utils.Tools;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.Assert.*;

public class ToolsTest {

    @Test
    public void testURLEncoder() {
        String expectedString = "https%3A%2F%2Fjellymaps.com%2Fapi%2Fgetmarkers";
        String actualString = Tools.encodeString("https://jellymaps.com/api/getmarkers");

        assertEquals(expectedString, actualString);
    }

    @Test
    public void testPixelsToDP() {
        int expectedValue = 10000;
        int actualValue = Tools.pixelsToDP(100, 100);

        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testReadStream() {
        InputStream inputStream = new ByteArrayInputStream("Bytes".getBytes());
        String expectedStream = "Bytes";
        String actualStream = Tools.readStream(inputStream);

        assertEquals(expectedStream, actualStream);
    }
}