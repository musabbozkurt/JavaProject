package customcalenderandroid.dwarf.com.customcalenderandroid;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by DWARF on 20.05.2016.
 */
public class HandleXml {

    private String country = "name";
    private String temperature = "temp";
    private String humidity = "humidity";
    private String pressure = "pressure";
    private String urlString = null;
    private XmlPullParserFactory xmlFactoryObj;
    public volatile boolean parsingComplete = true;

    public HandleXml(String url){
        this.urlString = url;
    }
    public String getCountry() {
        return country;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public String getPressure() {
        return pressure;
    }

    public void parseXMLAndStoreIt(XmlPullParser myParser){
        int event;
        String text = null;
        try {
            event = myParser.getEventType();
            while(event!=XmlPullParser.END_DOCUMENT){
                String name = myParser.getName();
                switch (event){
                    case XmlPullParser.START_TAG:
                        break;
                    case XmlPullParser.TEXT:
                        text = myParser.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if(name.equals(country)){
                            country=text;
                        }else if(name.equals(humidity)){
                            humidity=myParser.getAttributeValue(null,"value");
                        }else if(name.equals(pressure)){
                            pressure=myParser.getAttributeValue(null,"value");
                        }else if(name.equals(temperature)){
                            temperature=myParser.getAttributeValue(null,"value");
                        }
                        else{

                        }
                        break;
                }
                event=myParser.next();
            }
            parsingComplete=false;
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public void fetchXML(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(urlString);
                    HttpURLConnection connect = (HttpURLConnection) url.openConnection();
                    connect.setReadTimeout(10000);
                    connect.setConnectTimeout(15000);
                    connect.setRequestMethod("GET");
                    connect.setDoInput(true);
                    connect.connect();

                    InputStream stream = connect.getInputStream();
                    xmlFactoryObj = XmlPullParserFactory.newInstance();
                    XmlPullParser myparser = xmlFactoryObj.newPullParser();
                    myparser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES,false);
                    myparser.setInput(stream,null);
                    parseXMLAndStoreIt(myparser);
                    stream.close();

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

}
