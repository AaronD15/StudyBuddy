package edu.usf.devices.mobile.studybuddy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Locale;


public class GroupListAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<Group> dataSource;
    private ArrayList<Group> helperList;
    private FirebaseUser user;
    private String userID;

    public GroupListAdapter(Context context, ArrayList<Group> items) {
        dataSource = items;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.helperList = new ArrayList<>();
        this.helperList.addAll(dataSource);
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
    }

    private static class ViewHolder {
        public TextView titletext;
        public TextView classtext;
        public TextView creatortext;
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

        ViewHolder holder;

        if(convertView == null){
            convertView = inflater.inflate(R.layout.grouplist_item, parent, false);
            holder = new ViewHolder();

            holder.titletext = (TextView)convertView.findViewById(R.id.group_title_text);
            holder.classtext = (TextView)convertView.findViewById(R.id.group_class_text);
            holder.creatortext = (TextView)convertView.findViewById(R.id.group_creator_text);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        Group group = (Group)getItem(position);
        holder.titletext.setText(group.title);
        holder.classtext.setText(group.course);
        if (user != null && group.creatorUid.equals(userID)){
            holder.creatortext.setText("You");
        } else {
            holder.creatortext.setText(group.creatorName);
        }
        return convertView;
    }

    // Filter Class
    public void filterByClass(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        dataSource.clear();
        if (charText.length() == 0) {
            dataSource.addAll(helperList);
        } else {
            for (Group group : helperList) {
                if (group.course.toLowerCase(Locale.getDefault()).contains(charText)) {
                    dataSource.add(group);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void filterBySchool(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        dataSource.clear();
        if (charText.length() == 0) {
            dataSource.addAll(helperList);
        } else {
            for (Group group : helperList) {
                if (group.school.toLowerCase(Locale.getDefault()).contains(charText)) {
                    dataSource.add(group);
                }
            }
        }
        notifyDataSetChanged();
    }
}
