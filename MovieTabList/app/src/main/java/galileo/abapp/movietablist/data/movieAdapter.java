package galileo.abapp.movietablist.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import galileo.abapp.movietablist.R;

/**
 * Created by EEVGG on 02/06/2017.
 */

public class movieAdapter extends RecyclerView.Adapter<movieAdapter.ViewHolder>{

    private ArrayList movies;
    private Context context;
    private handleFavsCallback callback;

    public movieAdapter(Context context, ArrayList movies){
        this.movies = movies;
        this.context = context;

    }

    private Context getContext(){
        return context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        MovieDetail movie = (MovieDetail)movies.get(position);

        holder.tvTitle.setText(movie.getTitle());
        holder.tvRating.setText(getContext().getString(R.string.rating,movie.getRating()));
        holder.tvDesc.setText(movie.getDesc());

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvTitle;
        TextView tvRating;
        TextView tvDesc;

        ViewHolder(View view) {
            super(view);
            tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            tvRating = (TextView) view.findViewById(R.id.tvRating);
            tvDesc = (TextView) view.findViewById(R.id.tvDesc);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
			//executes the fragment handlePressed implementation
            if(callback!=null)
                callback.handlePressed(getAdapterPosition());
        }
    }
	//gets the handleFavsCallback from the fragment
    public void setCallback(handleFavsCallback c){
        callback = c;
    }
	//interface so that fragments can implement the onclick method
    public interface handleFavsCallback{
        void handlePressed(int n);
    }
}

