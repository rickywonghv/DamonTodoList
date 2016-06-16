package com.damonw.nodejs.todolist;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Damon on 29/5/16.
 */
public class MyAdapter extends BaseAdapter {
    private LayoutInflater myInflater;
    private List<todoitems> msgs;

    public MyAdapter(Context context, List<todoitems> msg){
        myInflater = LayoutInflater.from(context);
        this.msgs = msg;
    }

    @Override
    public int getCount() {
        return msgs.size();
    }

    @Override
    public Object getItem(int arg0) {
        return msgs.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        return msgs.indexOf(getItem(position));
    }

    private class ViewHolder {
        TextView txttodoTitle;
        TextView txttodoId;
        TextView txttodoCat;

        public ViewHolder(TextView txttodoTitle,TextView txttodoId, TextView txttodoCat ){
            this.txttodoTitle = txttodoTitle;
            this.txttodoId = txttodoId;
            this.txttodoCat = txttodoCat;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView==null){
            convertView = myInflater.inflate(R.layout.list_item, null);
            holder = new ViewHolder(
                    (TextView) convertView.findViewById(R.id.todotitle),
                    (TextView) convertView.findViewById(R.id.todoid),
                    (TextView) convertView.findViewById(R.id.todocat)
            );

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        todoitems msg = (todoitems)getItem(position);
        holder.txttodoTitle.setText(msg.getName());
        holder.txttodoId.setText(msg.getId());
        holder.txttodoCat.setText(msg.getCat());

        return convertView;
    }
}