package com.galileo.cc6.helpmate.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.galileo.cc6.helpmate.R;
import com.galileo.cc6.helpmate.dataModels.Assignment;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by EEVGG on 03/11/2016.
 */

public class ListAssignmentAdapter extends BaseAdapter {
    private List<Assignment> cItems;
    private LayoutInflater cInflater;
    private Context ctx;

    public ListAssignmentAdapter(Context ctx, List<Assignment> listAssignment){
        this.setItems(listAssignment);
        this.cInflater=LayoutInflater.from(ctx);
        this.ctx=ctx;
    }
    @Override
    public int getCount() {
        return (getItems() != null && !getItems().isEmpty()) ? getItems().size() : 0 ;
    }

    @Override
    public Assignment getItem(int position) {
        return (getItems() != null && !getItems().isEmpty()) ? getItems().get(position) : null ;
    }

    @Override
    public long getItemId(int position) {
        return (getItems() != null && !getItems().isEmpty()) ? getItems().get(position).getId() : position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolder holder;
        if(v == null) {
            v = cInflater.inflate(R.layout.assingment_item, parent, false);
            holder = new ViewHolder();
            holder.assingment_title = (TextView) v.findViewById(R.id.txt_assingment_title);
            holder.assingment_date = (TextView) v.findViewById(R.id.txt_assingment_date);
            holder.assingment_desc = (TextView) v.findViewById(R.id.txt_assingment_desc);
            holder.assingment_prio = (TextView) v.findViewById(R.id.txt_assingment_priority);
            holder.assingment_repeat=(TextView) v.findViewById(R.id.txt_repeat);
            v.setTag(holder);
        }
        else {
            holder = (ViewHolder) v.getTag();
        }
        Assignment currentItem = getItem(position);
        if(currentItem != null) {
            String myFormat = "dd/MM/yyyy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
            String asBody=currentItem.getCourse().getName()+" : "+currentItem.getTitle();
            holder.assingment_title.setText(asBody);
            holder.assingment_date.setText(sdf.format(currentItem.getDue_date()));
            holder.assingment_desc.setText(currentItem.getDesc());
            holder.assingment_prio.setText(String.valueOf(currentItem.getPriority()));
            holder.assingment_repeat.setText(currentItem.getRepeat());
        }
        return v;
    }

    public List<Assignment> getItems(){
        return cItems;
    }
    public void setItems(List<Assignment> listCourse){
        this.cItems=listCourse;
    }

    class ViewHolder {
        TextView assingment_title;
        TextView assingment_date;
        TextView assingment_desc;
        TextView assingment_prio;
        TextView assingment_repeat;

    }
}
