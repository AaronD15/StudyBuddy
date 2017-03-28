package edu.usf.devices.mobile.studybuddy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aaron on 3/27/2017.
 */

public class GroupListAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Group> dataSource;

    public GroupListAdapter(Context context, ArrayList<Group> items) {
        this.context = context;
        dataSource = items;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return dataSource.size();
    }

    @Override
    public Object getItem(int position) {
        return dataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
            return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = inflater.inflate(R.layout.grouplist_item, parent, false);
        TextView TitleTextView = (TextView)rowView.findViewById(R.id.group_title_text);
        TextView ClassTextView = (TextView)rowView.findViewById(R.id.group_class_text);
        TextView CreatorTextView = (TextView)rowView.findViewById(R.id.group_creator_text);

        Group group = (Group)getItem(position);

        TitleTextView.setText(group.title);
        ClassTextView.setText(group.course);

        return rowView;
    }
}
