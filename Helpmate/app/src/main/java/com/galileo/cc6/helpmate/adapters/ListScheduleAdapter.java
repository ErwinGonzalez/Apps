package com.galileo.cc6.helpmate.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.galileo.cc6.helpmate.R;
import com.galileo.cc6.helpmate.dataModels.Schedule;

import java.util.List;

/**
 * Created by EEVGG on 21/10/2016.
 */

public class ListScheduleAdapter extends BaseAdapter{
    public static final String TAG = "ListScheduleAdapter";
    private List<Schedule> cItems;
    private LayoutInflater cInflater;

    public ListScheduleAdapter(Context ctx, List<Schedule> listSchedule){
        this.setItems(listSchedule);
        this.cInflater=LayoutInflater.from(ctx);
    }

    @Override
    public int getCount() {
        return (getItems() != null && !getItems().isEmpty()) ? getItems().size() : 0 ;
    }

    @Override
    public Schedule getItem(int position) {
        return (getItems() != null && !getItems().isEmpty()) ? getItems().get(position) : null ;
    }

    @Override
    public long getItemId(int position) {
        return (getItems() != null && !getItems().isEmpty()) ? getItems().get(position).getId() : position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ListScheduleAdapter.ViewHolder holder;
        if(v == null) {
            v = cInflater.inflate(R.layout.list_schedule_item, parent, false);
            holder = new ListScheduleAdapter.ViewHolder();
            holder.txtCourseName = (TextView) v.findViewById(R.id.txt_course_name);
            holder.txtStartTime = (TextView) v.findViewById(R.id.schedule_time);
            holder.txtEndTime = (TextView) v.findViewById(R.id.schedule_end_time);
            v.setTag(holder);
        }
        else {
            holder = (ListScheduleAdapter.ViewHolder) v.getTag();
        }

        // fill row data
        Schedule currentItem = getItem(position);
        if(currentItem != null) {
            holder.txtCourseName.setText(currentItem.getCourse().getName());
            holder.txtStartTime.setText(currentItem.getStartTime());
            holder.txtEndTime.setText(currentItem.getEndTime());
        }

        return v;
    }
    public List<Schedule> getItems(){
        return cItems;
    }
    public void setItems(List<Schedule> listSchedule){
        this.cItems=listSchedule;
    }

    private class ViewHolder {
        TextView txtCourseName;
        TextView txtStartTime;
        TextView txtEndTime;
    }
}
