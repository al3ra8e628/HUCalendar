package com.example.al3ra8e.hucalendar.eventPackage;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.al3ra8e.hucalendar.R;
import com.example.al3ra8e.hucalendar.connection.AccessLinks;
import com.example.al3ra8e.hucalendar.connection.AppController;
import com.example.al3ra8e.hucalendar.connection.ImageParser;
import com.example.al3ra8e.hucalendar.other.DateFormat;
import com.example.al3ra8e.hucalendar.other.Keys;
import com.example.al3ra8e.hucalendar.other.UrlBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class RecycleEventAdapter {

    ArrayList<CustomEvent> events;
    JSONObject response ;
    Context context ;
    RecyclerView eventList ;
    int personId , logInState ;

    public RecycleEventAdapter() {
    }

    public RecycleEventAdapter(int personId , int logInState , JSONObject response, Context context, RecyclerView eventList ,ArrayList<CustomEvent> events) {
        this.response = response;
        this.context = context;
        this.eventList = eventList;
        this.events = events ;
    }

    public RecycleEventAdapter setPersonId(int personId) {
        this.personId = personId;
        return this ;
    }

    public RecycleEventAdapter setPersonId(String personId) {
        this.personId = Integer.parseInt(personId);
        return this ;
    }


    public RecycleEventAdapter setLogInState(int logInState) {
        this.logInState = logInState;
        return this ;
    }

    public RecycleEventAdapter setEvents(ArrayList<CustomEvent> events) {
        this.events = events;
        return this ;
    }

    public JSONObject getResponse() {
        return response;
    }

    public RecycleEventAdapter setResponse(JSONObject response) {
        this.response = response;
        return this ;
    }

    public RecycleEventAdapter setContext(Context context) {
        this.context = context;
        return this ;
    }

    public RecycleEventAdapter setEventList(RecyclerView eventList) {
        this.eventList = eventList;
    return this ;
    }

    public void fillEventsIntoTheList(){
        try {
            JSONArray arr = response.getJSONArray("Events") ;
            for(int i = arr.length()-1 ; i>=0 ; i--){
                JSONObject jsonObject = arr.getJSONObject(i) ;
                String date = jsonObject.getString("event_date") ;
                CustomEvent temp = new CustomEvent(DateFormat.makeDate("yyyy-MM-dd", date) ,
                        jsonObject.getString("event_title") , 0) ;
                temp.setCoverPicture(jsonObject.getString("event_image"));
                temp.setId(jsonObject.getInt("event_id"));
                events.add(temp)  ;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        eventList.setLayoutManager(mLayoutManager);
        eventList.setItemAnimator(new DefaultItemAnimator());
        eventList.setAdapter(new RecycleEventAdapter.EventsAdapter(events));

    }


    public class EventsAdapter extends RecyclerView.Adapter<RecycleEventAdapter.EventsAdapter.MyViewHolder> {
        private List<CustomEvent> events;
        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView eventTitle, eventDate;
            public ImageView eventPicture ;
            ConstraintLayout smallEvent ;
            public MyViewHolder(View view) {
                super(view);
                eventTitle = (TextView) view.findViewById(R.id.smallEventTitle);
                eventDate = (TextView) view.findViewById(R.id.smallEventDate);
                eventPicture = (ImageView) view.findViewById(R.id.smallEventPicture);
                smallEvent = (ConstraintLayout) view.findViewById(R.id.smallEvent);
            }
        }
        public EventsAdapter(ArrayList<CustomEvent> events) {
            this.events = events;
        }
        @Override
        public RecycleEventAdapter.EventsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.small_event, parent, false);
            return new RecycleEventAdapter.EventsAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final RecycleEventAdapter.EventsAdapter.MyViewHolder holder, final int position) {
            CustomEvent event = events.get(position);
            holder.eventTitle.setText(event.getDesc());
            holder.eventDate.setText(DateFormat.getDate(event.getTimeInMillis()));

            String url = new UrlBuilder(AccessLinks.PHOTOS_DIRECTORY).setUrlPath(event.getCoverPicture()).getUrl() ;

             new ImageParser(AppController.getInstance())
                     .setUrl(url).setImage(holder.eventPicture);

            holder.smallEvent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {//code to open event activity on click
                    Intent i = new Intent(context , EventActivity.class) ;
                    i.putExtra("person_id" ,personId+"");
                    i.putExtra(Keys.LOG_IN_STATE_KEY,logInState+"");
                    i.putExtra("eventId" , events.get(position).getId()+"") ;
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                }
            });
        }
        @Override
        public int getItemCount() {
            return events.size();
        }
    }


}
