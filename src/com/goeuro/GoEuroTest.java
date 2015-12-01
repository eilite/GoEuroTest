package com.goeuro;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by elie on 01/12/15.
 *
 */
public class GoEuroTest {
    private String city;
    /**
     * Base URL of the API
     */
    private String url="http://api.goeuro.com/api/v2/position/suggest/en/";


    /**
     * Constructor of the class, takes a city in parameter
     * @param city
     */
    public GoEuroTest(String city) {
        this.city = city;
    }

    /**
     * This functions makes the rest API call with the city
     * @return The response of the API call, JSON format.
     * @throws IOException
     */
    private JSONArray restCall() throws IOException, JSONException {
        String cityUrl=url+this.city;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(cityUrl)
                .build();

        Response response = client.newCall(request).execute();
        return new JSONArray(response.body().string());
    }

    /**
     * This function returns the csv line corresponding to an element of the JSON Array returned by the API call.
     * @param element an element of the JSON Array returned by the API call.
     * @return a csv line with the id, name, type, latitude and longitude of the city.
     */
    private String getCsvLine(JSONObject element) throws JSONException {
        int id = element.getInt("_id");
        String name = element.getString("name");
        String type = element.getString("type");
        double latitude = element.getJSONObject("geo_position").getDouble("latitude");
        double longitude = element.getJSONObject("geo_position").getDouble("longitude");

        return id+","+name+","+type+","+latitude+","+longitude;
    }

    /**
     * This function creates cities.csv and writes each line corresponding to each element of the API response.
     * @param response API response
     */
    private void writeInCsvFile(JSONArray response){
        int responseLength = response.length();
        Path newFile = Paths.get("cities.csv");
        //create the file or remove it if it already exists
        try {
            Files.deleteIfExists(newFile);
            newFile = Files.createFile(newFile);
        } catch (IOException e) {
            System.err.println("An error occured while creating the file");
            return;
        }
        //Write in the file for each city
        try(BufferedWriter writer = Files.newBufferedWriter(newFile, Charset.defaultCharset())){
            for(int i=0;i<responseLength;i++){
                writer.append(getCsvLine(response.getJSONObject(i)));
                writer.newLine();
            }
            writer.flush();
        }catch(Exception exception){
            System.err.println("An error occured while writing in the file.");
            return;
        }

    }

    /**
     * This function makes the API call, and writes a new line in the csv file for each element of the response.
     */
    public void run(){
        JSONArray response;
        try {
            response = restCall();
            if(response.length()==0){
                System.out.println("No city found");
            }else{
                writeInCsvFile(response);
            }
        } catch (Exception e) {
            System.err.println("An error occured while getting the informations about"+city);
            return;
        }
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
