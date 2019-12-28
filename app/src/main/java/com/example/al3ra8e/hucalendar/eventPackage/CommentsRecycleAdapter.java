package com.example.al3ra8e.hucalendar.eventPackage;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.al3ra8e.hucalendar.R;
import com.example.al3ra8e.hucalendar.connection.AccessLinks;
import com.example.al3ra8e.hucalendar.connection.AppController;
import com.example.al3ra8e.hucalendar.connection.ImageParser;
import com.example.al3ra8e.hucalendar.connection.RequestBuilder;
import com.example.al3ra8e.hucalendar.other.TextFormat;
import com.example.al3ra8e.hucalendar.other.UrlBuilder;
import com.example.al3ra8e.hucalendar.studentPackage.Student;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentsRecycleAdapter {

   private EventActivity activity ;

    public CommentsRecycleAdapter() {
    }



    public CommentsRecycleAdapter(EventActivity activity) {
        this.activity = activity;
    }

    public CommentsRecycleAdapter setActivity(EventActivity activity) {
        this.activity = activity;
        return this ;
    }

    public void setComments(int eventId) {
        activity.comments = new ArrayList<>()  ;
        String url = new UrlBuilder(AccessLinks.EVENT_COMMENTS).setUrlParameter("id" ,eventId+"").getUrl() ;

        new RequestBuilder()
                .setUrl(url)
                .setMethod(Request.Method.GET)
                .setActivity(activity)
                .onResponse(new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray arr = response.getJSONArray("Comments");
                            for (int i = arr.length() - 1; i >= 0; i--) {
                                try {

                                    JSONObject temp = arr.getJSONObject(i);

                                    Student student = new Student()
                                            .setFirstName(TextFormat.firstCharUpper(temp.getString("first_name")))
                                            .setLastName(TextFormat.firstCharUpper(temp.getString("last_name")))
                                            .setImage(temp.getString("student_image"))
                                            .getStudent() ;

                                    Comment comment = new Comment().setStudent(student)
                                            .setComment(temp.getString("comment"))
                                            .setTime(temp.getString("time")) ;

                                    activity.comments.add(comment) ;

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity.getApplicationContext());
                            activity.commentsRecycleView.setLayoutManager(mLayoutManager);
                            activity.commentsRecycleView.setItemAnimator(new DefaultItemAnimator());
                            activity.commentsRecycleView.setAdapter(new CommentsRecycleAdapter.CommentAdapter(activity.comments));

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

    public class CommentAdapter extends RecyclerView.Adapter<CommentsRecycleAdapter.CommentAdapter.MyViewHolder> {
        private List<Comment> commentList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView stdName, stdComment , commentTime;
            public CircleImageView stdImage ;
            public MyViewHolder(View view) {
                super(view);
                stdName = (TextView) view.findViewById(R.id.listCommentStudentName);
                stdComment = (TextView) view.findViewById(R.id.listCommentText);
                commentTime = (TextView) view.findViewById(R.id.listCommentTime);
                stdImage = (CircleImageView) view.findViewById(R.id.listProfilePicture);
            }
        }

        public CommentAdapter(List<Comment> commentList) {
            this.commentList = commentList;
        }

        @Override
        public CommentsRecycleAdapter.CommentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.comment_body, parent, false);
            return new CommentsRecycleAdapter.CommentAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final CommentsRecycleAdapter.CommentAdapter.MyViewHolder holder, int position) {

            Comment comment = commentList.get(position);

            holder.stdName.setText(comment.getStudent().getFullName());
            holder.stdComment.setText(comment.getComment());
            holder.commentTime.setText(comment.getTime());

            String url =new UrlBuilder(AccessLinks.PHOTOS_DIRECTORY).setUrlPath(comment.getStudent().getImage()).getUrl() ;

            new ImageParser(AppController.getInstance())
                    .setUrl(url)
                    .setImage(holder.stdImage);

        }
        @Override
        public int getItemCount() {
            return commentList.size();
        }
    }

}
