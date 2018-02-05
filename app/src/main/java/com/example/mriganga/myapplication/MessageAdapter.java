package com.example.mriganga.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by Mriganga on 5/26/2017.
 */
public class MessageAdapter extends ArrayAdapter<Message> {
    public MessageAdapter(Context context, int resource, ArrayList<Message> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View list_item_view = convertView;
        if(list_item_view==null){
            list_item_view = LayoutInflater.from(getContext()).inflate(R.layout.layout_message,parent,false);
        }
        Message message= getItem(position);
        if(message.getText()!=null || message.getText()!="") {
            TextView text = (TextView) list_item_view.findViewById(R.id.text);
            text.setText(message.getText());
        }
        else{
            TextView text = (TextView) list_item_view.findViewById(R.id.text);
            text.setVisibility(View.GONE);
        }
        if(message.getFrom()!=null) {
            TextView name = (TextView) list_item_view.findViewById(R.id.name);
            name.setText(message.getFrom());
        }
        else{
            TextView name = (TextView) list_item_view.findViewById(R.id.name);
            name.setVisibility(View.GONE);
        }
        if(message.getTime()!=null) {
            TextView time = (TextView) list_item_view.findViewById(R.id.time);
            time.setText(message.getTime());
        }
        else{
            TextView time = (TextView) list_item_view.findViewById(R.id.time);
            time.setVisibility(View.GONE);
        }
        return list_item_view;
    }
}
