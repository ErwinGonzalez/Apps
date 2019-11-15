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
import galileo.abapp.movietablist.data.movieAdapter;

/**
 * Created by EEVGG on 01/06/2017.
 */

public class MovieMainFragment extends Fragment implements movieAdapter.handleFavsCallback {
	
    RecyclerView rv;
    RecyclerView.LayoutManager manager;
    movieAdapter adapter;
    ArrayList movies;
    addMovieToFavorites mCallback;

    public  MovieMainFragment(){}
	
	//called when a fragment is attached to an activity
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (addMovieToFavorites) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_main,container,false);
        rv = (RecyclerView) parentView.findViewById(R.id.main_recycler_view);
        movies = (ArrayList)getArguments().getSerializable("movies");//gets the movies from the bundle
        LinearLayoutManager llm = new LinearLayoutManager(this.getContext());
        rv.setLayoutManager(llm);
        adapter = new movieAdapter(getActivity(),movies);
        adapter.setCallback(this);//sets the onclick interface 
        rv.setAdapter(adapter);//sets adapter on the recycler view
        manager = new LinearLayoutManager(getActivity());
        return parentView;
    }
	//adapter callback
    @Override
    public void handlePressed(int n) {
        mCallback.addFav(n);
    }
	//interface to be inplemented by the Main Activity
    public interface addMovieToFavorites{
        void addFav(int n);
    }
}