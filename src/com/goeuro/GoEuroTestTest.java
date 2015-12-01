package com.goeuro;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.*;

/**
 * Created by elie on 01/12/15.
 */
public class GoEuroTestTest {

    GoEuroTest goEuroTest = new GoEuroTest("city");

    /**
     * Should throw a JSONException because the JSON object doesn't have the required format.
     * @throws JSONException
     */
    @Test(expected = JSONException.class)
    public void getCsvLineTest() throws JSONException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        JSONObject jsonobject = new JSONObject("[]");
        Method method = GoEuroTest.class.getDeclaredMethod("getCsvLine", JSONObject.class);
        method.setAccessible(true);
        method.invoke(goEuroTest,new JSONObject(jsonobject));
    }

    /**
     * Test with an element of the JSON API result for Berlin.
     * @throws JSONException
     */
    @Test
    public void getCsvLineTest2() throws JSONException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String jsonString = "{\"_id\":376217,\"key\":null,\"name\":\"Berlin\",\"fullName\":\"Berlin, Germany\",\"iata_airport_code\":null," +
                "\"type\":\"location\",\"country\":\"Germany\",\"geo_position\":{\"latitude\":52.52437,\"longitude\":13.41053}," +
                "\"locationId\":8384,\"inEurope\":true,\"countryCode\":\"DE\",\"coreCountry\":true,\"distance\":null}";
        //Method to call private methods.
        Method method = GoEuroTest.class.getDeclaredMethod("getCsvLine", JSONObject.class);
        method.setAccessible(true);
        assertEquals(method.invoke(goEuroTest,new JSONObject(jsonString)),"376217,Berlin,location,52.52437,13.41053");
    }

}