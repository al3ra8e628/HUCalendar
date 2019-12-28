package com.example.al3ra8e.hucalendar.creatorsPackage;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.al3ra8e.hucalendar.R;
import com.example.al3ra8e.hucalendar.connection.EventUploader;
import com.example.al3ra8e.hucalendar.other.DateFormat;
import com.example.al3ra8e.hucalendar.other.Keys;
import com.example.al3ra8e.hucalendar.other.Permissions;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

public class AddEventActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener , TimePickerDialog.OnTimeSetListener{
    Date selectedDate ;
    int PICTURE_REQUEST_CODE = 1 ;
    public int creatorId ;
    public EditText eventTitle ;
    public TextView eventDate , eventTime ;
    public AppCompatSpinner categories , faculties ;
    public Intent pictureIntent  ;
    ConstraintLayout eventCover ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        initial();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void uploadEventAction(View view) {
        new EventUploader()
                .setActivity(this)
                .initial()
                .insertEventData() ;
    }


    public void changePictureAction(View view) {
        changePicture();
    }


    public void changePicture() {
        Permissions permissions = new Permissions(this) ;
        if(permissions.hasPermission()){
            Intent i = new Intent() ;
            i.setType("image/*") ;
            i.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(i , PICTURE_REQUEST_CODE);
        }else{
            permissions.requestPermission();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == RESULT_OK && data!= null){
            if(requestCode == PICTURE_REQUEST_CODE){
                pictureIntent = data ;
                try {
                    Bitmap image = MediaStore.Images.Media.getBitmap(getContentResolver() , data.getData()) ;
                    eventCover.setBackgroundDrawable(new BitmapDrawable(getResources(),image));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    // request for permission start in this line @_@
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean allows = true ;
        switch (requestCode){
            case 1234 :
                for (int res : grantResults){
                    allows = allows && (res == PackageManager.PERMISSION_GRANTED) ;
                }
                break;
            default:
                allows = false ;
                break;
        }
        if(allows){
            changePicture();
        }
    }
    // request for permission end in this line ;



    private void initial(){
        getSupportActionBar().hide();
        selectedDate = new Date() ;
        eventDate = (TextView) findViewById(R.id.eventDate);
        eventTime = (TextView) findViewById(R.id.eventTime);
        faculties = (AppCompatSpinner) findViewById(R.id.faculties);
        eventCover = (ConstraintLayout) findViewById(R.id.eventCover);
        categories = (AppCompatSpinner) findViewById(R.id.categories);
        eventTitle = (EditText) findViewById(R.id.eventTitle);
        faculties.setAdapter(new SpinnerAdapter(getResources().getStringArray(R.array.faculties)));
        categories.setAdapter(new SpinnerAdapter(getResources().getStringArray(R.array.categories)));
        creatorId = Integer.parseInt(getIntent().getStringExtra("creator_id")) ;
    }


    public void showDatePicker(View view) {
        showDialog(Keys.DATE_PICKER_DIALOG_ID);
    }
    public void showTimePicker(View view) {
        showDialog(Keys.TIME_PICKER_DIALOG_ID);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected Dialog onCreateDialog(int id) {
        Calendar cal = Calendar.getInstance() ;
      if(id == Keys.DATE_PICKER_DIALOG_ID){
        return new DatePickerDialog(
                 this
                ,this
                ,cal.get(Calendar.YEAR)
                ,cal.get(Calendar.MONTH)
                ,cal.get(Calendar.DAY_OF_MONTH));
      }
       else if(id == Keys.TIME_PICKER_DIALOG_ID){
        return new TimePickerDialog(
                 this
                ,this
                ,cal.get(Calendar.HOUR)
                ,cal.get(Calendar.MINUTE)
                ,true);
        }
        return null ;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        try {
            selectedDate = DateFormat.makeDate(day , (month+1) , year) ;
            eventDate.setText(new DateFormat(selectedDate).getDefaultFormat());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        eventTime.setText(hour+":"+minute);
    }


    private class SpinnerAdapter extends BaseAdapter {
        String  [] categories ;
        public SpinnerAdapter(String [] categories){
            this.categories = categories ;
        }

        @Override
        public int getCount() {
            return categories.length;
        }

        @Override
        public Object getItem(int position) {
            return categories[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = getLayoutInflater().inflate(R.layout.text_view_add_event , null ) ;
            TextView temp = (TextView) view.findViewById(R.id.categoryText);
            temp.setText(categories[position]);
            return view ;
        }
    }


}
