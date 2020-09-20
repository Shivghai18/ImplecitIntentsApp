package com.example.implicitintentapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    Button dateSelect,startTimeSelect,endTimeSelect;
    TextView dateDisplay,startTimeDisplay,endTimeDisplay;
    EditText title,emails,desc;
    Calendar c;
    DatePickerDialog dpd;
    CheckBox cb;
    public static final int Capture=1;
    ImageView pic;
    Button btn;
    Switch sw;
    int k;
    int day,month,year,startHour,startMin,endHour,endMin;
    EditText recepeint,subject,message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dateSelect=(Button)findViewById(R.id.dateBtn);
        startTimeSelect=(Button)findViewById(R.id.startTime);
        endTimeSelect=(Button)findViewById(R.id.endTime);
        dateDisplay=(TextView)findViewById(R.id.selDate);
        endTimeDisplay=(TextView)findViewById(R.id.selEndTime);
        startTimeDisplay=(TextView)findViewById(R.id.selStartTime);
        title=(EditText)findViewById(R.id.title);
        emails=(EditText)findViewById(R.id.emailvalue);
        desc=(EditText)findViewById(R.id.evdesc);
        cb=(CheckBox)findViewById(R.id.ck);
        sw=(Switch)findViewById(R.id.sw);
        btn=(Button)findViewById(R.id.btn);
        pic=(ImageView)findViewById(R.id.img);
        recepeint=(EditText)findViewById(R.id.rec);
        subject=(EditText)findViewById(R.id.sub);

        message=(EditText)findViewById(R.id.msg);

        this.setTitle("Click a Picture");
        if(!hascamera())
        {
            btn.setEnabled(false);
        }
        this.setTitle("Menu");
    }
    private boolean hascamera() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);

    }

    public void openEmail(View view) {
        Intent i=new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        String recText=recepeint.getText().toString();
        String []array=recText.split(",");

        i.putExtra(Intent.EXTRA_EMAIL,array);
        i.putExtra(Intent.EXTRA_SUBJECT,subject.getText().toString());
        i.putExtra(Intent.EXTRA_TEXT,message.getText().toString());

        if(i.resolveActivity(getPackageManager())!=null)
        {
            startActivity(i);
        }
    }

    public void openCamera(View view) {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i, Capture);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Capture && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap photo = (Bitmap) extras.get("data");
            pic.setImageBitmap(photo);
        }

    }
    public void openDatePicker(View view) {
        c=Calendar.getInstance();
        int lday=c.get(Calendar.DAY_OF_MONTH);
        int lmonth=c.get(Calendar.MONTH);
        int lyear=c.get(Calendar.YEAR);

        dpd=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker picker, int mYear, int mMonth, int mDay) {

                dateDisplay.setText(mDay+"/"+mMonth+"/"+mYear);
                day=mDay;
                month=mMonth;
                year=mYear;

            }
        },lday,lmonth,lyear);

        dpd.show();
    }

    public void selectStartTime(View view) {
        Calendar c=Calendar.getInstance();
        int currHour=c.get(Calendar.HOUR_OF_DAY);
        int currMin=c.get(Calendar.MINUTE);
        TimePickerDialog tpd=new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                startTimeDisplay.setText(hourOfDay+":"+minute+":00");
                startHour=hourOfDay;
                startMin=minute;
            }
        },currHour,currMin,android.text.format.DateFormat.is24HourFormat(this));
        tpd.show();
    }

    public void selectEndTime(View view) {
        Calendar c=Calendar.getInstance();
        int currHour=c.get(Calendar.HOUR_OF_DAY);
        int currMin=c.get(Calendar.MINUTE);
        TimePickerDialog tpd=new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                endTimeDisplay.setText(hourOfDay+":"+minute+":00");
                endHour=hourOfDay;
                endMin=minute;
            }
        },currHour,currMin,android.text.format.DateFormat.is24HourFormat(this));
        tpd.show();
    }

    public void insEvent(View view) {
        Calendar startCal=Calendar.getInstance();
        Calendar endCal=Calendar.getInstance();
        startCal.set(year,month,day,startHour,startMin,0);
        endCal.set(year,month,day,endHour,endMin,0);
        long startTime=startCal.getTimeInMillis();
        long endTime=endCal.getTimeInMillis();
        Intent i = new Intent(Intent.ACTION_INSERT);
        i.setData(CalendarContract.Events.CONTENT_URI);
        i.putExtra(CalendarContract.Events.TITLE, title.getText().toString());
        if(cb.isChecked())
        {
            i.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);
        }
        else
        {
            i.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, false);
        }
        i.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,startTime);
        i.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,endTime);
        i.putExtra(CalendarContract.Events.DESCRIPTION, desc.getText().toString());
        i.putExtra(Intent.EXTRA_EMAIL,emails.getText().toString());

        if(sw.isChecked()) {
            i.putExtra(CalendarContract.Events.ACCESS_LEVEL, CalendarContract.Events.ACCESS_PRIVATE);
        }
        else
        {
            i.putExtra(CalendarContract.Events.ACCESS_LEVEL, CalendarContract.Events.ACCESS_PUBLIC);
        }
        startActivity(i);
    }
}
