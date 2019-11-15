package com.galileo.cc6.helpmate.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.galileo.cc6.helpmate.R;
import com.galileo.cc6.helpmate.dataModels.Course;
import com.galileo.cc6.helpmate.database.ScheduleDAO;

import java.util.List;

/**
 * Created by EEVGG on 11/11/2016.
 */

public class CourseGridAdapter extends BaseAdapter {
    public static final String TAG = "ListScheduleAdapter";
    private List<Course> cItems;
    private LayoutInflater cInflater;
    private Context ctx;

    public CourseGridAdapter(Context ctx, List<Course> listCourse){
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
        //Adapter gets the calling view, and assigns the respective items
        if(v == null) {
            v = cInflater.inflate(R.layout.grid_item, parent, false);
            holder = new ViewHolder();
            holder.gridLabel = (TextView) v.findViewById(R.id.grid_item_label);
            holder.gridImage = (ImageView) v.findViewById(R.id.grid_item_image);
            v.setTag(holder);
        }
        else {
            holder = (ViewHolder) v.getTag();
        }

        // fill row data
        Course currentItem = getItem(position);
        if(currentItem != null) {
            holder.gridLabel.setText(currentItem.getName());
            ScheduleDAO sch = new ScheduleDAO(ctx);
            String col = currentItem.getColor();
            //changes notebook color based on the course data
            switch (col){
                case "Rojo":
                    holder.gridImage.setImageResource(R.drawable.cuaderno_rojo);
                    break;
                case "Amarillo":
                    holder.gridImage.setImageResource(R.drawable.cuaderno_amarillo);
                    break;
                case "Azul":
                    holder.gridImage.setImageResource(R.drawable.cuaderno_azul);
                    break;
                case "Verde":
                    holder.gridImage.setImageResource(R.drawable.cuaderno_verde);
                    break;
                case "Anaranjado":
                    holder.gridImage.setImageResource(R.drawable.cuaderno_anaranjado);
                    break;
                case "Negro":
                    holder.gridImage.setImageResource(R.drawable.cuaderno_negro);
                    break;
            }

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
        TextView gridLabel;
        ImageView gridImage;

    }
}
