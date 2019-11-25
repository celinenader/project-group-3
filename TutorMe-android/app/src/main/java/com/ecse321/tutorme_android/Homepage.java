package com.ecse321.tutorme_android;

import android.app.DownloadManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import cz.msebera.android.httpclient.Header;

import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

public class Homepage extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        TextView textView = findViewById(R.id.homepage_text);
        textView.setText("Welcome to TutorME!");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            textView.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        }

        setCalendarEvents();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setCalendarEvents(){
        final CompactCalendarView compactCalendarView  = findViewById(R.id.compactcalendar_view);
        //fetch from backend and set.
        HttpUtils.get("/api/lesson/getall", new RequestParams(), new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                if(statusCode!=200) return;

                for(int i = 0; i<response.length(); i++){
                    try {
                        final JSONObject lessonObj = response.getJSONObject(i);
                        final int lessonId = lessonObj.getInt("lessonId");

                        RequestParams requestParams = new RequestParams();
                        requestParams.put("lessonId",lessonId);
                        HttpUtils.get("/api/lesson/getCourseRoom", requestParams,
                        new JsonHttpResponseHandler(){
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                createEvent(compactCalendarView, lessonObj, response);
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                            }
                            });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

            }
        });
        //color, time, and data.

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createEvent(final CompactCalendarView compactCalendarView, JSONObject lessonObj, JSONObject courseRoomResp){
        System.out.println("created as well");
        try {
            System.out.println(lessonObj.getInt("lessonId"));

            String courseName = null;
            Integer roomId = null;

            if(!courseRoomResp.isNull("course") && !courseRoomResp.getJSONObject("course").isNull("courseName")){
                courseName = courseRoomResp.getJSONObject("course").getString("courseName");
            }

            if(!courseRoomResp.isNull("room") && !courseRoomResp.getJSONObject("room").isNull("room_id")){
                roomId = courseRoomResp.getJSONObject("room").getInt("room_id");
            }

            StringBuilder sb = new StringBuilder();
            if(courseName!=null) sb.append(courseName + " ");
            if(roomId!=null) sb.append("at: " + roomId.intValue());

            if(sb.length()==0) sb.append("Lesson #"+lessonObj.getInt("lessonId"));

            String date = lessonObj.getString("startTime");
            long dateLong = LocalDateTime.parse(date).toEpochSecond(ZoneOffset.UTC);
            System.out.println(date);
            System.out.println(dateLong);
            Event event = new Event(Color.GREEN, dateLong, sb.toString().trim());
            compactCalendarView.addEvent(event);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
