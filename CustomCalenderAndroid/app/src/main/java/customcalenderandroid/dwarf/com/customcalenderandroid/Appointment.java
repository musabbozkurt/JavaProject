package customcalenderandroid.dwarf.com.customcalenderandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class Appointment extends AppCompatActivity implements View.OnClickListener {

    private TextView selectedDate;
    private TextView titleText;
    private TextView alarmDate;
    private TextView alarmTime;
    private TextView description;
    private TextView where;
    private CheckBox showMap;
    private TextView startTime;
    private TextView endTime;
    private Button freetimeButton;
    private Button saveButton;
    private Button weatherBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);
        selectedDate = (TextView) this.findViewById(R.id.editText8);
        titleText = (TextView) this.findViewById(R.id.editText);
        alarmDate = (TextView) this.findViewById(R.id.editText2);
        alarmTime = (TextView) this.findViewById(R.id.editText3);
        description = (TextView) this.findViewById(R.id.editText4);
        where = (TextView) this.findViewById(R.id.editText5);
        startTime = (TextView) this.findViewById(R.id.editText6);
        endTime = (TextView) this.findViewById(R.id.editText7);
        showMap = (CheckBox) this.findViewById(R.id.checkBox);
        freetimeButton = (Button) this.findViewById(R.id.button);
        freetimeButton.setOnClickListener(this);
        weatherBtn = (Button) this.findViewById(R.id.weatherbtn);
        weatherBtn.setOnClickListener(this);
        saveButton = (Button) this.findViewById(R.id.button2);
        saveButton.setOnClickListener(this);

        Bundle b = getIntent().getExtras();
        String obj = b.getString("Appointment");

        selectedDate.setText(obj);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.button:
                // it was the first button
                Intent i;
                i = new Intent(this,FreeTimeActivity.class);
                startActivity(i);
                break;
            case R.id.button2:
                // it was the second button
                break;
            case R.id.weatherbtn:
                Intent i2 = new Intent(this,WeatherActivity.class);
                startActivity(i2);
                break;
        }
    }
}
