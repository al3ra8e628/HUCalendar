package com.example.al3ra8e.hucalendar.servicesPackage;
import android.app.IntentService;
import android.content.Intent;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.al3ra8e.hucalendar.connection.AccessLinks;
import com.example.al3ra8e.hucalendar.connection.RequestBuilder;
import com.example.al3ra8e.hucalendar.eventPackage.Comment;
import com.example.al3ra8e.hucalendar.other.Keys;
import com.example.al3ra8e.hucalendar.other.UrlBuilder;
import com.example.al3ra8e.hucalendar.studentPackage.Student;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class CommentNotificationService extends IntentService {

    public static boolean ServiceIsRun = false;
    public static int student_id = 0;
    public static int person_id = 0;
    public static long CommentID = 0;
    ArrayList<Comment> comments ;

    public CommentNotificationService() {
        super("MyWebRequestService");
        comments = new ArrayList<>() ;

    }

    protected void onHandleIntent(Intent workIntent) {

        while (ServiceIsRun) {
        comments = new ArrayList<>() ;
            HashMap<String , String> params = new HashMap<>();
            params.put("id",CommentID+"") ;
            params.put("std_id",student_id+"") ;

            String url = new UrlBuilder(AccessLinks.GET_ALL_NEW_COMMENTS).setUrlParameter(params).getUrl() ;
                new RequestBuilder()
                        .setUrl(url)
                        .setMethod(Request.Method.GET)
                        .onResponse(new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    if(response.has("Comments")) {
                                        JSONArray json = response.getJSONArray("Comments");
                                        for (int i = 0; i < json.length(); i++) {
                                            JSONObject newDataItem = json.getJSONObject(i);
                                            Student student = new Student()
                                                    .setFirstName(newDataItem.getString("first_name"))
                                                    .setLastName(newDataItem.getString("last_name"))
                                                    .setPersonId(newDataItem.getInt("person_id"))
                                                    .setImage(newDataItem.getString("student_image"))
                                                    .getStudent() ;

                                            Comment comment = new Comment()
                                                    .setStudent(student)
                                                    .setComment(newDataItem.getString("comment"))
                                                    .setCommentId(newDataItem.getInt("id"))
                                                    .setEventId(newDataItem.getInt("event_id"))
                                                    .setTime(newDataItem.getString("time")) ;

                                            comments.add(comment);
                                            CommentID = newDataItem.getLong("id");
                                        }
                                        sendBroadcastData(comments);
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

                        try{
                            Thread.sleep(5000);
                        }catch(Exception ex){
                            ex.printStackTrace();
                        }
        }
    }

    public void sendBroadcastData(ArrayList<Comment> comments) {

        for(int i = 0 ; i < comments.size() ; i++){
            Comment comment = comments.get(i) ;
            String msg = " - "+comment.getCommentId()+" - "+comment.getStudent().getFullName()+" made new comment "+comment.getComment()+" at "+comment.getTime() ;
            Intent intent = new Intent();
            intent.setAction("com.example.Broadcast");
            intent.putExtra("msg", msg);
            intent.putExtra("noteNum", comment.getCommentId());
            intent.putExtra("person_id", person_id);
            intent.putExtra("event_id", comment.getEventId());
            intent.putExtra("profile_picture", comment.getStudent().getImage());
            intent.putExtra("first_name", comment.getStudent().getFirstName());
            intent.putExtra("last_name", comment.getStudent().getLastName());
            intent.putExtra(Keys.SERVICES_STATE_KEY, Keys.NEW_COMMENT_SERVICE);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            sendBroadcast(intent);

        }

    }



}
