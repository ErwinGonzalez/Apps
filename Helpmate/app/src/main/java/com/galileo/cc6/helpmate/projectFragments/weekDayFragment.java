package com.galileo.cc6.helpmate.projectFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.galileo.cc6.helpmate.R;
import com.galileo.cc6.helpmate.adapters.ListScheduleAdapter;
import com.galileo.cc6.helpmate.dataModels.Schedule;
import com.galileo.cc6.helpmate.database.ScheduleDAO;

import java.util.Collections;
import java.util.List;

/**
 * Created by EEVGG on 23/10/2016.
 */

public class weekDayFragment extends Fragment {

    private  int weekDay=0;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.day_tab_layout, container,
                false);
        //inflates view and gets day value from bundle passed as argument
        Bundle bundle = this.getArguments();
        if(bundle!=null){weekDay = getArguments().getInt("pos");}
        setAdapter(rootView);
        return rootView;
    }
    private void setAdapter(View view){
        ListView lv = (ListView) view.findViewById(R.id.fragmentList);
        // creates schedule data access object and get elemets by day
        ScheduleDAO scheduleDAO = new ScheduleDAO(getActivity());
        List<Schedule> scheduleList = scheduleDAO.getScheduleByDay(weekDay);
        //sort them to show bu ascending order based on time of day
        Collections.sort(scheduleList,Schedule.schComparator);
        // fill the listView
        if (scheduleList != null && !scheduleList.isEmpty()) {
            ListScheduleAdapter listScheduleAdapter = new ListScheduleAdapter(getActivity(), scheduleList);
            lv.setAdapter(listScheduleAdapter);
        }
    }
}
