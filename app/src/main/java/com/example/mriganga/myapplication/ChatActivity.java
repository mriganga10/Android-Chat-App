package com.example.mriganga.myapplication;

import android.content.Context;
import android.media.Image;
import android.provider.CalendarContract;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.Socket;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import io.socket.client.IO;
import io.socket.emitter.Emitter;

public class ChatActivity extends AppCompatActivity {
    private io.socket.client.Socket socket = null;
    {
        try{

            socket = IO.socket("https://hidden-tor-65305.herokuapp.com");


        }catch(URISyntaxException e){
            Log.v(ChatActivity.class.getSimpleName(),"Could not connect to socket");
            throw new RuntimeException(e);
        }
    }

    private DrawerLayout dl;
    private ActionBarDrawerToggle abdt;

    String name,room;
    ArrayList<Message> mMessages = new ArrayList<Message>();
    MessageAdapter messageAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        dl = (DrawerLayout)findViewById(R.id.drawer_layout);
        abdt = new ActionBarDrawerToggle(this,dl,R.string.Open,R.string.Close);
        abdt.setDrawerIndicatorEnabled(true);
        dl.addDrawerListener(abdt);
        abdt.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView nav_view = (NavigationView)findViewById(R.id.nav_view);

        name = getIntent().getExtras().getString("name");
        room = getIntent().getExtras().getString("room");
        Log.v(ChatActivity.class.getSimpleName(),"Chat Activity Started");
        Log.v(ChatActivity.class.getSimpleName(),name);
        Log.v(ChatActivity.class.getSimpleName(),room);
        socket.connect();
        Log.v(ChatActivity.class.getSimpleName(),"connected to socket");
        Log.v(ChatActivity.class.getSimpleName(),"connected to socket");
        String params = "{\"name\":"+name+",\"room\" : "+room+"}";
        JSONObject par = null;
        try {
          par = new JSONObject(params);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.v(ChatActivity.class.getSimpleName(),"could not make json object");
        }
        Log.v(ChatActivity.class.getSimpleName(),params);
        socket.emit("join",par);
        socket.on("newMessage",handleIncomingMessages);
        ImageButton b = (ImageButton)findViewById(R.id.send_button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });

    }
    protected void updateNavigationUI(String name){
        Log.v("update navigation ui","doing "+name);
        LinearLayout ll = (LinearLayout)findViewById(R.id.nav_lin);
        TextView tv=new TextView(this);
        tv.setText(name);
        tv.setHeight(125);
//        tv.getLayoutParams().width= ViewGroup.LayoutParams.MATCH_PARENT;
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
        ll.addView(tv);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(abdt.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }
    protected void sendMessage(){
        EditText mInputMessageView = (EditText)findViewById(R.id.message_input);
        String message = mInputMessageView.getText().toString().trim();
        mInputMessageView.setText("");
       // addMessage(message);
        JSONObject sendText = new JSONObject();
        try{
            sendText.put("text",message);
            sendText.put("from",name);
            socket.emit("createMessage", sendText);
        }catch(JSONException e){

        }
    }
    protected void updateUI(ArrayList<Message> mMessages){
        if(mMessages!=null){
            messageAdapter = new MessageAdapter(ChatActivity.this, R.layout.layout_message,mMessages);
            ListView lv = (ListView) findViewById(R.id.messages);
            lv.setAdapter(messageAdapter);
            lv.setDivider(null);
            lv.setDividerHeight(0);
            lv.setSelection(messageAdapter.getCount() - 1);

        }
    }
    protected void addMessage(Message m){
        mMessages.add(m);
        if(m.getFrom().equals("Admin") ){
            TextView pb = (TextView) findViewById(R.id.loading);
            pb.setVisibility(View.GONE);
        }
        updateUI(mMessages);

    }
    private Emitter.Listener handleIncomingMessages = new Emitter.Listener(){
        @Override
        public void call(final Object... args){
            ChatActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.v("Listining","listner started");
                    JSONObject data = (JSONObject) args[0];
                    String message;
                    String from;
                    String time;
                    try {
                        message = data.getString("text").toString().trim();
                        from = data.getString("from").toString().trim();
                        time  = data.getString("createdAt").toString().trim();

                        long unixSeconds =Long.parseLong(time);
                        Date date = new Date(unixSeconds);
                        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");

                        String formattedDate = sdf.format(date);
                        time = formattedDate;
                        Log.v("unix seconds",""+formattedDate);

                        Message m = new Message(from,time,message);
                        Log.v("Listining",message);
                        addMessage(m);
                        if(from.equals("Admin")){
                            if( !message.equals("welcome to the chat app")){
                                String n = message.substring(0,message.length()-8);
                                updateNavigationUI(n);
                            }
                            else{
                                updateNavigationUI(name);
                            }

                        }

                    } catch (JSONException e) {
                        Log.v("Listining","Listner Error");
                        // return;
                    }

                }
            });
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        socket.disconnect();
    }
}
