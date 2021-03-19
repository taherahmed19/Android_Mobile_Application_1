package com.example.myapplication.Utils.Distance;

import android.location.Location;

import org.junit.Test;

import static org.junit.Assert.*;

public class DistanceTest {

    @Test
    public void testCalculatePointsDistance(){
        double actual = Distance.CalculatePointsDistance(51.50814359035564, -0.12757841703333933, 51.509679991312744, -0.12818183756448506);
        double expected = 175.99;

        assertEquals("Distance between two lat/lng points ", expected, actual, 0.0001);
    }
}