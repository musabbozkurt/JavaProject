package customcalenderandroid.dwarf.com.customcalenderandroid;

/**
 * Created by DWARF on 20.05.2016.
 */

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

public class WeatherActivity extends AppCompatActivity {

    private EditText txt1,txt2,txt3,txt4,txt5;
    private String url1 = "http://api.openweathermap.org/data/2.5/weather?q=";
    private String url2 = "&mode=xml&appid=0b232cb707aacd72506e3a25ac79ca19";
    private HandleXml obj;
    Button btnWeather;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_activity);
        btnWeather = (Button) findViewById(R.id.button3);

        txt1 = (EditText) findViewById(R.id.txtlocation);
        txt2 = (EditText) findViewById(R.id.editText10);
        txt3 = (EditText) findViewById(R.id.editText11);
        txt4 = (EditText) findViewById(R.id.editText12);
        txt5 = (EditText) findViewById(R.id.editText13);
        btnWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = txt1.getText().toString();
                String finalUrl = url1+url+url2;
                obj = new HandleXml(finalUrl);
                obj.fetchXML();

                while(obj.parsingComplete);
                txt2.setText(obj.getCountry());
                txt3.setText(obj.getTemperature());
                txt4.setText(obj.getHumidity());
                txt5.setText(obj.getPressure());
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
