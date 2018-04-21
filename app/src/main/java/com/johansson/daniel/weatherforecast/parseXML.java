package com.johansson.daniel.weatherforecast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class parseXML {
    //Strings used to parse XML data
    private String city = "city";
    private String humidity = "humidity";
    private String temperature = "temperature";
    private String weather = "weather";
    private String wind = "wind";
    private String windDirection = "windDirection";

    private String urlString;
    private XmlPullParserFactory xmlFactoryObject;
    public volatile boolean parsingComplete = true;

    public parseXML(String url) {
        this.urlString = url;
    }

    //Get methods to assign values from the XML data
    public String getCity(){
        return city;
    }

    public String getHumidity(){
        return humidity;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getWeather() {
        return weather;
    }

    public String getWind() {
        return wind;
    }

    public String getWindDirection() {
        return windDirection;
    }

    //Search through XML file and store data to variables
    public void parseXMLAndStoreIt(XmlPullParser myParser) {
        int event;

        try {
            event = myParser.getEventType();
            //Loop through XML file until end of document
            while (event != XmlPullParser.END_DOCUMENT) {
                //String to match up with names for data in the XML document
                String name=myParser.getName();
                //Use a switch statement that uses if statements until the end tag
                switch (event){
                    case XmlPullParser.START_TAG:

                        if (name.equals("city")){
                            city = myParser.getAttributeValue(null, "name");
                        }
                        else if (name.equals("humidity")){
                            humidity = myParser.getAttributeValue(null,"value");
                        }
                        else if(name.equals("temperature")){
                            temperature = myParser.getAttributeValue(null,"value");
                        }
                        else if(name.equals("weather")){
                            weather = myParser.getAttributeValue(null,"value");
                        }
                        else if (name.equals("speed")){
                            wind = myParser.getAttributeValue(null, "name");
                        }
                        else if (name.equals("direction")){
                            windDirection = myParser.getAttributeValue(null,"name");
                        }
                        else{
                        }
                        break;
                    case XmlPullParser.END_TAG:
                }
                event = myParser.next();
            }
            parsingComplete = false;
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Method to fetch the XML file using HttpURLConnection
    public void fetchXML() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(urlString);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    conn.setReadTimeout(10000 /* milliseconds */);
                    conn.setConnectTimeout(15000 /* milliseconds */);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    conn.connect();

                    InputStream stream = conn.getInputStream();

                    xmlFactoryObject = XmlPullParserFactory.newInstance();
                    XmlPullParser myparser = xmlFactoryObject.newPullParser();

                    myparser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                    myparser.setInput(stream, null);
                    parseXMLAndStoreIt(myparser);
                    stream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}