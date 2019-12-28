package com.example.al3ra8e.hucalendar.studentPackage;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.al3ra8e.hucalendar.R;
import com.example.al3ra8e.hucalendar.connection.AccessLinks;
import com.example.al3ra8e.hucalendar.connection.AppController;

import org.json.JSONException;
import org.json.JSONObject;

////https://hucalendar.000webhostapp.com/signUp.php?name=ahmad&username=sdff&password=fdfff
public class SignUp extends AppCompatActivity {
       EditText name ;
       EditText username ;
       EditText password ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        name = (EditText) findViewById(R.id.signUpName);
        username = (EditText) findViewById(R.id.signUpUsername);
        password = (EditText) findViewById(R.id.signUpPassword);
    }


      public String prepairUrl(String name ,  String username , String password){
          return AccessLinks.SIGNIN4STUDENT+"?"+"name="+name+"&username="+username+"&password="+password  ;
      }


    public void signUp(View view) {
         String url = prepairUrl(name.getText().toString() , username.getText().toString() , password.getText().toString()) ;
        final ProgressDialog pDialog  = new ProgressDialog(SignUp.this) ;
        pDialog.setMessage("loading ......");
        pDialog.setCancelable(false);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        showResultOfSignUp(response);
                        pDialog.hide();
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext() , "pad connection" , Toast.LENGTH_LONG).show();
                pDialog.hide();
            }
        }

        ) ;
     AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }
    public void showResultOfSignUp(JSONObject responose){
        int res = 0 ;
        if(responose.has("result")){
            try {
                res = Integer.parseInt(responose.getString("result")) ;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (res == 0){
            Toast.makeText(getApplicationContext() , "sign up fail" , Toast.LENGTH_LONG).show();
        }else if (res ==1 ){
            Toast.makeText(getApplicationContext() , "sign up done" , Toast.LENGTH_LONG).show();
        }

    }
}
