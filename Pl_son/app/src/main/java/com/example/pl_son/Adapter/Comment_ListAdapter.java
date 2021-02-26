package com.example.pl_son.Adapter;

import android.accessibilityservice.AccessibilityService;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.pl_son.DatabaseAccess;
import com.example.pl_son.R;

import java.util.ArrayList;
import java.util.Map;

public class Comment_ListAdapter extends BaseAdapter {

    ArrayList<Map<String, String>> list = new ArrayList<>();
    LayoutInflater commentInflater;
    Context context;
    DatabaseAccess databaseAccess;
    public Comment_ListAdapter(Activity activity, ArrayList<Map<String, String>> list, Context context, DatabaseAccess databaseAccess) {
        this.list = list;
        commentInflater=(LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context=context;
        this.databaseAccess=databaseAccess;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) { return 0; }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View row_view;
        row_view = commentInflater.inflate(R.layout.list_comment,null);
        TextView user_name =(TextView) row_view.findViewById(R.id.name);
        TextView date =(TextView) row_view.findViewById(R.id.dateText);
        TextView comment =(TextView) row_view.findViewById(R.id.commentText);
        String[] student=databaseAccess.userLogin(Integer.parseInt( list.get(position).get("userId")));
        final String nameSurname=student[3]+" "+student[4];
        user_name.setText(nameSurname);
        date.setText(list.get(position).get("comment_date"));
        comment.setText(list.get(position).get("comment"));
        return row_view;
    }
}
