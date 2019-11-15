package com.galileo.cc6.helpmate.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.galileo.cc6.helpmate.R;
import com.galileo.cc6.helpmate.dataModels.Course;
import com.galileo.cc6.helpmate.dataModels.Schedule;
import com.galileo.cc6.helpmate.database.ScheduleDAO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EEVGG on 20/10/2016.
 */

public class ListCourseAdapter extends BaseAdapter {

    public static final String TAG = "ListScheduleAdapter";
    private List<Course> cItems;
    private LayoutInflater cInflater;
    private Context ctx;

    public ListCourseAdapter(Context ctx, List<Course> listCourse){
        this.setItems(listCourse);
        this.cInflater=LayoutInflater.from(ctx);
        this.ctx=ctx;
    }

    @Override
    public int getCount() {
        return (getItems() != null && !getItems().isEmpty()) ? getItems().size() : 0 ;
    }

    @Override
    public Course getItem(int position) {
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
            v = cInflater.inflate(R.layout.list_course_item, parent, false);
            holder = new ViewHolder();
            holder.txtCourseName = (TextView) v.findViewById(R.id.txt_course_name);
            holder.txtCourseColor = (TextView) v.findViewById(R.id.txt_course_color);
            v.setTag(holder);
        }
        else {
            holder = (ViewHolder) v.getTag();
        }

        // fill row data
        Course currentItem = getItem(position);
        if(currentItem != null) {
            holder.txtCourseName.setText(" " + currentItem.getName());
            String col = currentItem.getColor();
            switch (col){
                case "Rojo":
                    holder.txtCourseName.setBackgroundColor(Color.RED);
                    holder.txtCourseName.setTextColor(Color.WHITE);
                    break;
                case "Azul":
                    holder.txtCourseName.setBackgroundColor(Color.BLUE);
                    holder.txtCourseName.setTextColor(Color.WHITE);
                    break;
                case "Amarillo":
                    holder.txtCourseName.setBackgroundColor(Color.YELLOW);
                    holder.txtCourseName.setTextColor(Color.BLACK);
                    break;
                case "Verde":
                    holder.txtCourseName.setBackgroundColor(Color.GREEN);
                    holder.txtCourseName.setTextColor(Color.BLACK);
                    break;
                case "Anaranjado":
                    holder.txtCourseName.setBackgroundColor(Color.rgb(255, 80, 0));
                    holder.txtCourseName.setTextColor(Color.BLACK);
                    break;
                case "Negro":
                    holder.txtCourseName.setBackgroundColor(Color.BLACK);
                    holder.txtCourseName.setTextColor(Color.WHITE);
                    break;
            }
            ScheduleDAO sch = new ScheduleDAO(ctx);
            String str="";
            List<Schedule> scList= sch.getScheduleOfCourse(currentItem.getId());
            for(Schedule e:scList){
                switch (e.getDay()){
                    case 1:
                        str=str+" Lun ";
                        break;
                    case 2:
                        str=str+" Mar ";
                        break;
                    case 3:
                        str=str+" Mie ";
                        break;
                    case 4:
                        str=str+" Jue ";
                        break;
                    case 5:
                        str=str+" Vie ";
                        break;
                    case 6:
                        str=str+" Sab ";
                        break;
                    case 0:
                        str=str+" Dom ";
                        break;
                }
            }
            holder.txtCourseColor.setText(str);
        }

        return v;
    }
    public List<Course> getItems(){
        return cItems;
    }
    public void setItems(List<Course> listCourse){
        this.cItems=listCourse;
    }

    class ViewHolder {
        TextView txtCourseName;
        TextView txtCourseColor;

    }
}
