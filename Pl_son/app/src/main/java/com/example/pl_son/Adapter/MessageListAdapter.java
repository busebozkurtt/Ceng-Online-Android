package com.example.pl_son.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;


import com.example.pl_son.Classes.Message;
import com.example.pl_son.Classes.MessageInfo;
import com.example.pl_son.Classes.Student;
import com.example.pl_son.DatabaseAccess;
import com.example.pl_son.R;
import com.example.pl_son.ReadMessageActivity;

import java.util.ArrayList;
import java.util.List;

public class MessageListAdapter extends BaseAdapter {

    List<MessageInfo> messageInf= new ArrayList<MessageInfo>();
    LayoutInflater messageInflater;
    Context context;
    DatabaseAccess databaseAccess;
    int user_id;

    public MessageListAdapter(Activity activity, ArrayList<MessageInfo> messageInf, Context context, DatabaseAccess databaseAccess, int user_id) {
        this.messageInf = messageInf;
        messageInflater=(LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context=context;
        this.databaseAccess=databaseAccess;
        this.user_id=user_id;
    }

    @Override
    public int getCount() {
        return messageInf.size();
    }

    @Override
    public Object getItem(int position) {
        return messageInf.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row_view;
        row_view = messageInflater.inflate(R.layout.list,null);
        ImageButton viewButton = row_view.findViewById(R.id.viewButton);
        CheckBox c=(CheckBox)row_view.findViewById(R.id.checkBox);
        Button delete =(Button) row_view.findViewById(R.id.delete);
        delete.setVisibility(View.GONE);

        if(messageInf.get(position).getIs_read()==1) {
            c.setChecked(true);

        }
        else {
            c.setChecked(false);
        }

        TextView name =(TextView) row_view.findViewById(R.id.studentName);
        TextView date =(TextView) row_view.findViewById(R.id.deliveryDate);
        name.setText(messageInf.get(position).getName()+" "+messageInf.get(position).getSurname());
        date.setText(messageInf.get(position).getDate());

        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ReadMessageActivity.class);
                intent.putExtra("messageId",messageInf.get(position).getId());
                intent.putExtra("userId",user_id);
                intent.putExtra("state",messageInf.get(position).getState());
                context.startActivity(intent);
            }
        });
        return row_view;
    }

}

