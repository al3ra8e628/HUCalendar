package com.example.al3ra8e.hucalendar.eventPackage;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.al3ra8e.hucalendar.R;
import com.example.al3ra8e.hucalendar.connection.AccessLinks;
import com.example.al3ra8e.hucalendar.connection.AppController;
import com.example.al3ra8e.hucalendar.connection.ImageParser;
import com.example.al3ra8e.hucalendar.connection.RequestBuilder;
import com.example.al3ra8e.hucalendar.connection.UploadMaterial;
import com.example.al3ra8e.hucalendar.other.DateFormat;
import com.example.al3ra8e.hucalendar.other.Keys;
import com.example.al3ra8e.hucalendar.other.Permissions;
import com.example.al3ra8e.hucalendar.other.TextFormat;
import com.example.al3ra8e.hucalendar.other.UrlBuilder;
import com.example.al3ra8e.hucalendar.studentPackage.Student;
import com.nbsp.materialfilepicker.MaterialFilePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;


public class EventActivity extends AppCompatActivity {
    LinearLayout commentLayout ;
    RecyclerView commentsRecycleView;
    ConstraintLayout eventLayout;
    TextView eventTitle  ;
    ArrayList<Comment> comments ;
    SwipeRefreshLayout swipeRefreshLayout ;
    TextView studentName  , attendNumber;
    EditText newComment ;
    Button attendBtn  ;
    CircleImageView studentProfilePicture ;
    Student std ;
    int eventId , eventAcceptState ;
    String personId ,  loginState;
    CommentsRecycleAdapter commentsRecycleAdapter ;
    EventActivity thisActivity ;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_event);
         getSupportActionBar().hide();
         thisActivity = this ;

         final View view = getLayoutInflater().inflate(R.layout.new_comment_body , null) ;
         initial(this , view) ;

         setEventInfo(eventId);
         commentsRecycleAdapter.setComments(eventId);
         setAttendedNumber(eventId);

        setLayoutBasedOnLogInState();

         swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
             @Override
             public void onRefresh() {
                 setEventInfo(eventId);
                 commentsRecycleAdapter.setComments(eventId);
                 setAttendedNumber(eventId);
                 setLayoutBasedOnLogInState();
                 swipeRefreshLayout.setRefreshing(false);
             }
         });
    }

    public void setAttendedNumber(int eventId){
        String url = new UrlBuilder(AccessLinks.ATTEND_NUMBER).setUrlParameter("id" ,eventId+"").getUrl() ;
             new RequestBuilder()
                .setActivity(this)
                .setMethod(Request.Method.GET)
                .setUrl(url)
                .onResponse(new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            attendNumber.setText(response.getString("number")+" person attend");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .onError(new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                })
                .execute();
    }

    private void getStudent(){
        String url = new UrlBuilder( AccessLinks.STUDENT_INFO_LINK ).setUrlParameter("person_id" , personId+"").getUrl();
        new RequestBuilder()
                .setActivity(this)
                .setUrl(url)
                .setMethod(Request.Method.GET)
                .onResponse(new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                                try {
                                    std.setId(response.getInt("std_id"));
                                    std.setFirstName(TextFormat.firstCharUpper(response.getString("first_name")));
                                    std.setLastName(TextFormat.firstCharUpper(response.getString("last_name")));
                                    std.setImage(response.getString("student_image"));
                                    setNewCommentInfo() ;
                                    changeAttendBtnState(eventId , std.getId());
                                }catch (JSONException e){
                                    e.printStackTrace();
                                }
                    }
                })
                .onError(new Response.ErrorListener() {
                   @Override
                    public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                    }
                })
                .execute();

    }


    private void setEventInfo(final int eventId) {

        String url = new UrlBuilder(AccessLinks.GET_EVENT_INFO).setUrlParameter("id", eventId + "").getUrl();

        new RequestBuilder()
                .setUrl(url)
                .setMethod(Request.Method.GET)
                .setActivity(this)
                .onResponse(new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            eventTitle.setText(response.getString("event_title"));
                            String eventPic = response.getString("event_image");
                            String picUrl = new UrlBuilder(AccessLinks.PHOTOS_DIRECTORY).setUrlPath(eventPic).getUrl();
                            new ImageParser(AppController.getInstance())
                                    .setUrl(picUrl)
                                    .setImage(eventLayout);

                            changeAcceptBtnState(eventId);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .onError(new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                })
                .execute();
    }


    private void setNewCommentInfo(){

        studentName.setText(std.getFirstName()+" "+std.getLastName());
        String url = new UrlBuilder(AccessLinks.PHOTOS_DIRECTORY).setUrlPath(std.getImage()).getUrl() ;
        new ImageParser(AppController.getInstance()).setUrl(url).setImage(studentProfilePicture);

    }


    public void attendAction(View view) {
        switch (loginState){
            case Keys.STUDENT_LOG_IN+"" :
                attendToEvent();
                break;
            case Keys.ADMIN_LOG_IN+"" :
                acceptEvent();
                break;
            case Keys.CREATOR_LOG_IN+"" :
                uploadNewMaterial();
                break;
        }
    }


    //creator options
    private void uploadNewMaterial(){
        Permissions permissions = new Permissions(this) ;
        if(permissions.hasPermission()){
            new MaterialFilePicker()
                    .withActivity(this)
                    .withRequestCode(4055)
                    .withCloseMenu(true)
                    .withRootPath("/storage")
                    .withFilterDirectories(true)
                    .withHiddenFiles(true)
                    .start();
        }else{
            permissions.requestPermission();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data){
        if(resultCode == RESULT_OK && data!= null && requestCode == 4055){
            final EditText materialTitle = new EditText(this) ;
            new  AlertDialog.Builder(thisActivity)
                    .setMessage("enter the title of material")
                    .setView(materialTitle)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String title = "" ;
                            if(!materialTitle.getText().toString().equals(""))
                                title = materialTitle.getText().toString() ;
                            else
                                title = "unknown material" ;

                            new UploadMaterial()
                                    .setData(data)
                                    .setActivity(thisActivity)
                                    .setEventId(eventId)
                                    .setMaterialTitle(title)
                                    .uploadFile();
                        }
                    })
                    .setCancelable(false)
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(getApplicationContext() , "material not added" , Toast.LENGTH_LONG).show();
                        }
                    })
                    .show() ;
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
            uploadNewMaterial();
        }
    }
    // request for permission end in this line @_@
    //creator options


    private void acceptEvent(){
        if(eventAcceptState == 1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity);
            builder.setCancelable(false)
                   .setTitle("Remove Event")
                   ;
            builder.setMessage("This option will remove everything related to this event are you sure ?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            acceptOrRemoveAction();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).show();
        }else{
            acceptOrRemoveAction();
        }
    }

    private void acceptOrRemoveAction(){
        HashMap<String , String> params = new HashMap<>() ;
        params.put("event_id" , ""+eventId);
        String url  = new UrlBuilder(AccessLinks.ACCEPT_EVENT).setUrlParameter(params).getUrl();
        new RequestBuilder()
                .setUrl(url)
                .setActivity(this)
                .setMethod(Request.Method.GET)
                .onResponse(new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        changeAcceptBtnState(eventId);
                    }
                })
                .onError(new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                })
                .execute();
        thisActivity.finish();
    }


    public void changeAcceptBtnState(int eventId){
        if(loginState.equals(Keys.ADMIN_LOG_IN+"")){
        HashMap<String , String> params = new HashMap<>() ;
        params.put("event_id" ,eventId+"") ;
        String url = new UrlBuilder(AccessLinks.IS_ACCEPTED).setUrlParameter(params).getUrl() ;

        new RequestBuilder()
                .setUrl(url)
                .setMethod(Request.Method.GET)
                .setActivity(this)
                .onResponse(new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int state = response.getInt("response") ;
                            if(state == 0){
                                attendBtn.setText("accept");
                                eventAcceptState = 0 ;
                            }else if (state == 1){
                                attendBtn.setText("remove event");
                                eventAcceptState = 1 ;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .onError(new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                })
                .execute();
    }
    }

    private void attendToEvent() {
        HashMap<String , String> params = new HashMap<>() ;
        params.put("event_id" , ""+eventId);
        params.put("student_id" , ""+std.getId()) ;

        String url  = new UrlBuilder(AccessLinks.ATTEND_TO_EVENT).setUrlParameter(params).getUrl();

        new RequestBuilder()
                .setUrl(url)
                .setActivity(this)
                .setMethod(Request.Method.GET)
                .onResponse(new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        changeAttendBtnState(eventId , std.getId());
                        setAttendedNumber(eventId);
                    }
                })
                .onError(new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                })
                .execute();
    }



    public void changeAttendBtnState(int eventId , int studentId){
        HashMap<String , String> params = new HashMap<>() ;
        params.put("event_id" ,eventId+"") ;
        params.put("student_id" ,studentId+"") ;
        String url = new UrlBuilder(AccessLinks.IS_ATTENDED).setUrlParameter(params).getUrl() ;

        new RequestBuilder()
                .setUrl(url)
                .setMethod(Request.Method.GET)
                .setActivity(this)
                .onResponse(new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int state = response.getInt("response") ;
                            if(state == 0){
                                attendBtn.setText("attend");
                            }else if (state == 1){
                                attendBtn.setText("cancel attend");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .onError(new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                })
                .execute();
    }



    public void addNewComment(View view) {
        if(!newComment.getText().toString().equals("")){

            HashMap<String,String> params = new HashMap<>();
            params.put("student_id" ,std.getId()+"") ;
            params.put("event_id" , eventId+"") ;
            params.put("comment" , newComment.getText().toString()) ;
            params.put("time" ,new DateFormat(new Date()).getTime()) ;

            String url = new UrlBuilder(AccessLinks.ADD_COMMENT).getUrl() ;
            new RequestBuilder()
                    .setActivity(this)
                    .setMethod(Request.Method.POST)
                    .setUrl(url)
                    .setParams(params)
                    .onStringResponse(new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            commentsRecycleAdapter.setComments(eventId);
                            newComment.setText("");
                        }
                    })
                    .onError(new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    })
                    .executeStringRequest();

        }else{
            Toast.makeText(getApplicationContext() ,"comment Field cannot be empty !" , Toast.LENGTH_SHORT).show();
        }
    }

    public void openMaterialsActivity(View view) {
        Intent intent = new Intent(EventActivity.this , MaterialsActivity.class) ;
        intent.putExtra("event_id" , eventId+"") ;
        intent.putExtra("event_title" , eventTitle.getText().toString()) ;
        intent.putExtra("person_id" , personId) ;
        intent.putExtra(Keys.LOG_IN_STATE_KEY , loginState) ;
        startActivity(intent);
    }



    private void initial(EventActivity context , View view) {
        eventAcceptState = -1 ;
        eventId = Integer.parseInt(getIntent().getStringExtra("eventId")) ; // 2 ;
        personId = getIntent().getStringExtra("person_id") ;
        loginState = getIntent().getStringExtra(Keys.LOG_IN_STATE_KEY) ;
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary) ;// swipe eb9ar sho color yalle belef
        context.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN); /// to make event title visible while keyboard are appeared ;

        eventTitle = (TextView) findViewById(R.id.eventTitle);
        eventLayout = (ConstraintLayout) findViewById(R.id.toolbar_layout);
        attendNumber = (TextView) findViewById(R.id.attendNumber);
        std = new Student() ;
        commentsRecycleView = (RecyclerView) findViewById(R.id.commentListView);
        attendBtn = (Button) findViewById(R.id.attendBtn);
        if(loginState.equals(""+Keys.STUDENT_LOG_IN)){
            initialNewComment(view);
        }else if(loginState.equals(""+Keys.ADMIN_LOG_IN)){

        }

        commentsRecycleAdapter = new CommentsRecycleAdapter(this) ;
    }

    private void initialNewComment(View view){
        commentLayout = (LinearLayout) findViewById(R.id.commentLayout);// new comment layout
        //-----------
        commentLayout.addView(view , 0);
        studentName = (TextView) view.findViewById(R.id.newCommentStudentName);
        newComment = (EditText) view.findViewById(R.id.newCommentText);
        changeAttendBtnState(eventId ,std.getId()) ;
        studentProfilePicture = (CircleImageView) view.findViewById(R.id.newCommentProfilePicture);
        //------------
    }

    private void setLayoutBasedOnLogInState(){
        int key = Integer.parseInt(loginState) ;
        switch (key){
            case Keys.STUDENT_LOG_IN :
                getStudent();
                break;
            case Keys.ADMIN_LOG_IN :

                break;
            case Keys.CREATOR_LOG_IN:
                attendBtn.setText("Add Material");
                break;
        }
    }

}
