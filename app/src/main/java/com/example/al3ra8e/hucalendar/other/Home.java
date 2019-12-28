package com.example.al3ra8e.hucalendar.other;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.al3ra8e.hucalendar.R;
import com.example.al3ra8e.hucalendar.adminPackage.AdminMain;
import com.example.al3ra8e.hucalendar.creatorsPackage.CreatorMain;
import com.example.al3ra8e.hucalendar.studentPackage.SignUp;
import com.example.al3ra8e.hucalendar.studentPackage.StudentActivity;


public class Home extends AppCompatActivity  implements View.OnClickListener{

    RelativeLayout selectionBarContainer ;
    int user ;
    TextView signUp ;
    Context context;
    Button admin , student, creator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if(isLoggedIn()){
            return;
        }
        initial(this);
    }

    private View createSelectionBar() {

        LayoutInflater inflater = getLayoutInflater() ;
        View view =  inflater.inflate(R.layout.select_type_view, null) ;

        admin   = (Button) view.findViewById(R.id.selectionBarAdmin);
        student = (Button) view.findViewById(R.id.selectionBarStudent);
        creator = (Button) view.findViewById(R.id.selectionBarCreator);

        admin.setOnClickListener(this);
        student.setOnClickListener(this);
        creator.setOnClickListener(this);

        return   view ;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void setSelectedButton(Button selected , Button btn2 , Button btn3 , int userType){
        selected.setBackground(getResources().getDrawable(R.drawable.selected_button_background));
        btn2.setBackground(getResources().getDrawable(R.drawable.select_part_background));
        btn3.setBackground(getResources().getDrawable(R.drawable.select_part_background));

        selected.setTextColor(getResources().getColor(R.color.white));
        btn2.setTextColor(getResources().getColor(R.color.black));
        btn3.setTextColor(getResources().getColor(R.color.black));

        user = userType ;

        if(userType == 0)
            signUp.setVisibility(View.VISIBLE);
        else
            signUp.setVisibility(View.INVISIBLE);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.selectionBarAdmin :
                setSelectedButton(admin , student , creator , 1);
                break;

            case R.id.selectionBarStudent :
                setSelectedButton(student ,  admin  , creator , 0);
                break;

            case R.id.selectionBarCreator :
                setSelectedButton(creator , student , admin , 2);
                break;
        }
    }

    public void logInAction(View view) {
        Intent intent = new Intent(Home.this , LogIn.class);
        switch (user){
            case 0 :
                intent.putExtra(Keys.LOG_IN_STATE_KEY, ""+Keys.STUDENT_LOG_IN) ;
                break;
            case 1 :
                intent.putExtra(Keys.LOG_IN_STATE_KEY, ""+Keys.ADMIN_LOG_IN) ;
                break;
            case 2 :
                intent.putExtra(Keys.LOG_IN_STATE_KEY, ""+Keys.CREATOR_LOG_IN) ;
                break;
        }
        startActivity(intent);
        this.finish();
    }

    private void initial(Context context){
        this.context = context ;
        selectionBarContainer = (RelativeLayout) findViewById(R.id.selectionBarContainer);
        selectionBarContainer.addView(createSelectionBar());

        user  = 1 ;

        signUp = (TextView) findViewById(R.id.signUp) ;

        signUp.setVisibility(View.INVISIBLE);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this , SignUp.class) ;
                startActivity(intent);
            }});
    }



    private boolean isLoggedIn(){

        SharedPreferences prefs = getSharedPreferences("LOGGED_IN" , MODE_PRIVATE);
        String restoredText = prefs.getString("loggedIn" , null);

        if (restoredText != null) {

            String personId = prefs.getString("person_id" , null);
            String logInState = prefs.getString(Keys.LOG_IN_STATE_KEY , null);

            Intent i = null ;

            switch (logInState){
                case Keys.ADMIN_LOG_IN+"" :
                    i = new Intent(Home.this , AdminMain.class);
                    break;
                case Keys.CREATOR_LOG_IN+"" :
                    i = new Intent(Home.this , CreatorMain.class);
                    break;
                case Keys.STUDENT_LOG_IN+"" :
                    i = new Intent(Home.this , StudentActivity.class);
                    break ;
            }

            i.putExtra("person_id" , personId) ;
            i.putExtra(Keys.LOG_IN_STATE_KEY , logInState) ;

            startActivity(i);

            this.finish();
            return true ;
        }
        return false ;
    }



}
