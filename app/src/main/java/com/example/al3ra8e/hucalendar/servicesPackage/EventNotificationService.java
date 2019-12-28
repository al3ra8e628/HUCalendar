package com.example.al3ra8e.hucalendar.servicesPackage;

import android.app.IntentService;
import android.content.Intent;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.al3ra8e.hucalendar.connection.AccessLinks;
import com.example.al3ra8e.hucalendar.connection.RequestBuilder;
import com.example.al3ra8e.hucalendar.eventPackage.Event;
import com.example.al3ra8e.hucalendar.other.DateFormat;
import com.example.al3ra8e.hucalendar.other.Keys;
import com.example.al3ra8e.hucalendar.other.UrlBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class EventNotificationService extends IntentService {

    public static boolean ServiceIsRun = false;
    public static long lastAcceptedEvent = 0 ;
    public static long personID = 0 ;
    public static long studentID = 0 ;

    ArrayList<Event> events ;

    public EventNotificationService() {
        super("MyWebRequestService");
        events = new ArrayList<>() ;

    }

    protected void onHandleIntent(Intent workIntent) {

        while (ServiceIsRun) {
        events = new ArrayList<>() ;
            HashMap<String , String> params = new HashMap<>();
            params.put("last_accepted",lastAcceptedEvent+"") ;
            params.put("person_id",personID+"") ;

            String url = new UrlBuilder(AccessLinks.GET_ALL_NEW_EVENTS).setUrlParameter(params).getUrl() ;
                new RequestBuilder()
                        .setUrl(url)
                        .setMethod(Request.Method.GET)
                        .onResponse(new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    if(response.has("Events")) {
                                        JSONArray json = response.getJSONArray("Events");
                                        for (int i = 0; i < json.length(); i++) {
                                            JSONObject newDataItem = json.getJSONObject(i);
                                            Event event = new Event()
                                                    .setEventId(newDataItem.getInt("event_id"))
                                                    .setEventDate(newDataItem.getString("event_date"))
                                                    .setEventImage(newDataItem.getString("event_image"))
                                                    .setEventTitle(newDataItem.getString("event_title"))
                                                    .setCategory(newDataItem.getInt("event_category_id"))
                                                    .setFaculty(newDataItem.getInt("event_faculty_id"))
                                                    .build();
                                            events.add(event);
                                            lastAcceptedEvent = newDataItem.getLong("last_accepted");
                                        }
                                        sendBroadcastData(events);
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

    public void sendBroadcastData(ArrayList<Event> events) {
        for(int i = 0 ; i < events.size() ; i++){
            Event event = events.get(i) ;
            Intent intent = new Intent();
            intent.setAction("com.example.Broadcast");
            intent.putExtra("event_id", event.getEventId());
            intent.putExtra("event_title", event.getEventTitle());
            intent.putExtra("event_date", new DateFormat(event.getEventDate()).toString());
            intent.putExtra("event_category", event.getCategory());
            intent.putExtra("event_faculty", event.getFaculty());
            intent.putExtra("event_image", event.getEventImage());
            intent.putExtra("person_id", personID);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Keys.SERVICES_STATE_KEY, Keys.NEW_EVENT_SERVICE);
            sendBroadcast(intent);
        }
    }

}
