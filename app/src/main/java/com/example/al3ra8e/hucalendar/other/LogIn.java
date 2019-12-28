package com.example.al3ra8e.hucalendar.other;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.al3ra8e.hucalendar.R;
import com.example.al3ra8e.hucalendar.adminPackage.AdminMain;
import com.example.al3ra8e.hucalendar.connection.AccessLinks;
import com.example.al3ra8e.hucalendar.connection.RequestBuilder;
import com.example.al3ra8e.hucalendar.creatorsPackage.AddEventActivity;
import com.example.al3ra8e.hucalendar.creatorsPackage.CreatorMain;
import com.example.al3ra8e.hucalendar.searchPackage.SearchActivity;
import com.example.al3ra8e.hucalendar.studentPackage.StudentActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LogIn extends AppCompatActivity {
    int loginState;
    EditText username;
    EditText password;
    TextView textViewUsername;
    Intent intent ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        initial();
    }

    public void logIn(View view) {
        intent = new Intent(LogIn.this ,SearchActivity.class) ;
        if(username.getText().toString().equals("")){
            Intent i = new Intent(LogIn.this , AddEventActivity.class) ;
            startActivity(i);
            return;
        }

        if(loginState == Keys.STUDENT_LOG_IN)
            intent = new Intent(LogIn.this , StudentActivity.class) ;
        else if(loginState == Keys.ADMIN_LOG_IN)
            intent = new Intent(LogIn.this , AdminMain.class) ;
        else if(loginState == Keys.CREATOR_LOG_IN)
            intent = new Intent(LogIn.this , CreatorMain.class) ;



        intent.putExtra(Keys.LOG_IN_STATE_KEY, ""+loginState);

        HashMap<String , String> params = new HashMap<>() ;
        params.put("username" , username.getText().toString()) ;
        params.put("password" , password.getText().toString()) ;
        params.put(Keys.LOG_IN_STATE_KEY, ""+loginState);

                String url = new UrlBuilder().setUrl(AccessLinks.LOG_IN).setUrlParameter(params).getUrl() ;

                final ProgressDialog pd = new ProgressDialog(LogIn.this);
                pd.setMessage("Loading ... ");
                pd.setCancelable(false);
                pd.show();

                new RequestBuilder()
                        .setUrl(url)
                        .setParams(params)
                        .setActivity(this)
                        .setMethod(Request.Method.GET)
                        .onResponse(new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                pd.dismiss() ;
                                doLogInAction(response , intent);
                            }
                        })
                        .onError(new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                pd.dismiss() ;
                                error.printStackTrace();
                            }
                        })
                        .execute();
    }

    public void doLogInAction(JSONObject response , Intent intent){
        if(response.has("result")){
            Toast.makeText(getApplicationContext() , "username or password incorrect" , Toast.LENGTH_LONG).show();
        }else if(response.has("first_name")){
            try {
                String firstName = response.getString("first_name") ;
                int personId = response.getInt("person_id") ;

                Toast.makeText(getApplicationContext() , "welcome "+firstName, Toast.LENGTH_LONG).show();

                intent.putExtra("person_id" , personId+"") ;
                startActivity(intent);

                SharedPreferences sp = getSharedPreferences("LOGGED_IN" , MODE_PRIVATE) ;
                SharedPreferences.Editor spe = sp.edit() ;

                spe.putString("loggedIn"  , "logged") ;
                spe.putString("person_id" , personId+"") ;
                spe.putString(Keys.LOG_IN_STATE_KEY , loginState+"") ;

                spe.commit() ;

                this.finish();

            } catch (JSONException e) {
                Toast.makeText(getApplicationContext() , "no connection", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }

    private void initial(){
        loginState = Integer.parseInt(getIntent().getStringExtra(Keys.LOG_IN_STATE_KEY)) ;
        username = (EditText) findViewById(R.id.signUpName);
        password = (EditText) findViewById(R.id.signUpUsername);
        textViewUsername = (TextView) findViewById(R.id.usernameTextView) ;

        switch (loginState){
            case Keys.STUDENT_LOG_IN :
                textViewUsername.setText("student username");
                break ;
            case Keys.ADMIN_LOG_IN :
                textViewUsername.setText("admin username");
                break ;
            case Keys.CREATOR_LOG_IN :
                textViewUsername.setText("creator username");
                break ;
        }

    }
}
