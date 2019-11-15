package galileo.abapp.movietablist.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import galileo.abapp.movietablist.R;
import galileo.abapp.movietablist.activities.MainActivity;
import galileo.abapp.movietablist.data.movieAdapter;

/**
 * Created by EEVGG on 01/06/2017.
 */

public class MovieFavoritesFragment extends Fragment implements movieAdapter.handleFavsCallback{

        RecyclerView rv;
        RecyclerView.LayoutManager manager;
        movieAdapter adapter;
        TextView tv;
        ArrayList movies;
        removeFromFavorites mCallback;

        public  MovieFavoritesFragment(){}

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);

            // This makes sure that the container activity has implemented
            // the callback interface. If not, it throws an exception
            try {
                mCallback = (removeFromFavorites) context;
            } catch (ClassCastException e) {
                throw new ClassCastException(context.toString()
                        + " must implement OnHeadlineSelectedListener");
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View parentView = inflater.inflate(R.layout.fragment_main,container,false);
            tv =(TextView) parentView.findViewById(R.id.section_label);
            rv = (RecyclerView) parentView.findViewById(R.id.main_recycler_view);
            movies = (ArrayList)getArguments().getSerializable("favs");
            LinearLayoutManager llm = new LinearLayoutManager(this.getContext());
            rv.setLayoutManager(llm);
            adapter = new movieAdapter(getActivity(),movies);
            adapter.setCallback(this);
            rv.setAdapter(adapter);
            manager = new LinearLayoutManager(getActivity());
            return parentView;
        }

	//called when the fragment becomes visible to the user
    @Override
    public void setUserVisibleHint(boolean visibleHint){
		//if it beomes visible, refresh the list so it uses current data
        if(visibleHint)
            adapter.notifyDataSetChanged();
    }
    @Override
    public void handlePressed(int n) {
        mCallback.removeFav(n);
		//refresh the list to remove the item from the view
        adapter.notifyDataSetChanged();
    }
	//interface to be implemented on the MainActivity
    public interface removeFromFavorites{
        void removeFav(int n);
    }
}
