package com.sltc.sa;
//jdk version 1.8

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.Endpoint;
import java.io.BufferedReader;
import java.io.FileReader;

//json-simple-1.1.1.jar
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
@WebService
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT)
public class WS
{
    @WebMethod
    public Float converter(String sourceCurrency, String targetCurrency, float amountInSourceCurrency){
        float amount = 0;
        //Read values from JSON
        float readSourceCurrency = JsonParser(sourceCurrency);
        float readTargetCurrency = JsonParser(targetCurrency);


        try{
            //convert the currency
            amount = ((amountInSourceCurrency / readSourceCurrency) * readTargetCurrency);
            System.out.println("amount " + amount);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return amount;
    }

    //read json file
    public String getJSONFromFile(String filename) {
        StringBuilder jsonText = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                jsonText.append(line).append("\n");
            }
            bufferedReader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonText.toString();
    }

    //assign a value of a country codes
    public float JsonParser(String countryCode){
        float countrySalary = 0;
        try{
            JSONParser parser = new JSONParser();

            Object object = parser.parse(getJSONFromFile("./rates.json"));

            JSONObject mainJsonObject = (JSONObject) object;
            JSONObject jsonObject = (JSONObject) mainJsonObject.get("rates");

            try {
                countrySalary = Float.parseFloat(jsonObject.get(countryCode).toString());
                System.out.println("country code " + countryCode);
            }catch(Exception e) {
                countrySalary = 0;
            }

        }catch(Exception e) {
            e.printStackTrace();
        }
        return countrySalary;
    }

    public static void main(String[] args){
        System.out.println("Server Started!");
        Endpoint.publish("http://localhost:8888/WebService/", new WS());
    }
}
