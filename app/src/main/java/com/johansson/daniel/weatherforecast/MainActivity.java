package com.johansson.daniel.weatherforecast;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    static final String OPEN_URL = "http://api.openweathermap.org/data/2.5/weather?id=";
    static final String API = "&appid=ad675dc59d5154fe8d57bda9253df777";
    static final String MODE = "&mode=xml";
    static final String UNITS = "&units=imperial";

    private parseXML parse;

    String urlLocation, url, city, weatherCondition;
    String athens = "Athens, AL";
    String degerfors = "Degerfors, Sweden";
    String[] cities = {degerfors, athens};
    Spinner citiesWeather;

    static TextView cityTitle, conditions, temps, humid, wind, windDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityTitle = findViewById(R.id.cityTitle);
        conditions = findViewById(R.id.conditions);
        temps = findViewById(R.id.temps);
        humid = findViewById(R.id.humid);
        wind = findViewById(R.id.windData);
        windDir = findViewById(R.id.windDir);

        //Create spinner using the cities array
        ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(this, R.layout.spinner, cities);
        citiesWeather = findViewById(R.id.city_weather);

        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        citiesWeather.setAdapter(cityAdapter);
        citiesWeather.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id){

        url = null;
        //String city uses the value from the selected item from spinner
        city = citiesWeather.getSelectedItem().toString();

        //Use if statement to set location using the city selected from spinner
        if (city == cities[0]){

            urlLocation = "2717884";

        } else if (city == cities[1]){

            urlLocation = "4830668";
        }

        //Create a complete URL adding the selected location
        try {
            url = OPEN_URL + urlLocation + MODE + UNITS + API;

            //Use url to parse XML from the parse class
            parse = new parseXML(url);
            parse.fetchXML();
            Log.i("MyActivity", "XML Parser View " + parse);

            while(parse.parsingComplete);
            Log.i("MyActivity", "XML Parsing Complete " + parse.parsingComplete);

            //Assign values from the XML document to TextViews
            cityTitle.setText(parse.getCity());
            weatherCondition = parse.getWeather().substring(0,1).toUpperCase() + parse.getWeather().substring(1).toLowerCase();
            conditions.setText(weatherCondition);
            temps.setText(parse.getTemperature() + "°F");
            humid.setText(parse.getHumidity() + "%");
            wind.setText(parse.getWind());
            windDir.setText(parse.getWindDirection());

        } catch (Exception e) {
            conditions.setText(e.getMessage());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent){
        conditions.setText("Select a city");

    }
}
