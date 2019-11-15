package galileo.abapp.movietablist.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import galileo.abapp.movietablist.R;
import galileo.abapp.movietablist.data.MovieDetail;
import galileo.abapp.movietablist.fragments.MovieBestFragment;
import galileo.abapp.movietablist.fragments.MovieFavoritesFragment;
import galileo.abapp.movietablist.fragments.MovieMainFragment;

public class MainActivity extends AppCompatActivity implements MovieMainFragment.addMovieToFavorites,
                                                                MovieFavoritesFragment.removeFromFavorites,
                                                                MovieBestFragment.addBestMovieToFavorites{


    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;

    public ArrayList<MovieDetail> mainMovies;
    public ArrayList<MovieDetail> bestMovies;
    public ArrayList<MovieDetail> favMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
		
        setSupportActionBar(toolbar);
        mainMovies = new ArrayList<>();
        bestMovies = new ArrayList<>();
        favMovies = new ArrayList<>();
        initLists();

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }

	//implementation of MoviesMainFragment interface 
    @Override
    public void addFav(int n) {
        MovieDetail movie = mainMovies.get(n);
        if(!favMovies.contains(movie)){
            Toast.makeText(getApplicationContext(), "Movie " + movie.getTitle() + " added to Favorites", Toast.LENGTH_SHORT).show();
            favMovies.add(movie);
        }else
            Toast.makeText(getApplicationContext(), "Movie " + movie.getTitle() + " already in Favorites", Toast.LENGTH_SHORT).show();
    }
    //implementation of MoviesBestFragment interface
    @Override
    public void addFavBest(int n) {
        MovieDetail movie = bestMovies.get(n);
        Log.d("TAG",movie.getTitle());
        if(!favMovies.contains(movie)){
            Toast.makeText(getApplicationContext(), "Movie " + movie.getTitle() + " added to Favorites", Toast.LENGTH_SHORT).show();
            favMovies.add(movie);
        }else
            Toast.makeText(getApplicationContext(), "Movie " + movie.getTitle() + " already in Favorites", Toast.LENGTH_SHORT).show();
    }
	//implementation of MoviesFavoritesFragment interface 
    @Override
    public void removeFav(int n) {
        MovieDetail m = favMovies.remove(n);
        Toast.makeText(getApplicationContext(),"Movie "+m.getTitle()+" removed from Favorites",Toast.LENGTH_SHORT).show();
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
			//for each position adds the list to the bundle, so it can be used in the fragment
            Fragment list;
            Bundle b = new Bundle();
            switch (position) {
                case 0:
                    b.putSerializable("movies",mainMovies);
                    list = new MovieMainFragment();
                    list.setArguments(b);
                    return list;
                case 1:
                    b.putSerializable("favs",favMovies);
                    list = new MovieFavoritesFragment();
                    list.setArguments(b);
                    return list;
                case 2:
                    b.putSerializable("best",bestMovies);
                    list = new MovieBestFragment();
                    list.setArguments(b);
                    return list;
                default:
                    return new Fragment();
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

		//changes the title of the tabs
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "MAIN";
                case 1:
                    return "FAVS";
                case 2:
                    return "BEST";
            }
            return null;
        }
    }

	//creates the main movie list
    public void initLists(){
        mainMovies.add(new MovieDetail("Pirates of the Caribbean: Dead Men Tell No Tales","Captain Jack Sparrow is pursued by an old rival, Captain Salazar, who along with his crew of ghost pirates has escaped from the Devil's Triangle, and is determined to kill every pirate at sea. Jack seeks the Trident of Poseidon, a powerful artifact that grants its possessor total control over the seas, in order to defeat Salazar.",65));
        mainMovies.add(new MovieDetail("Beauty and the Beast","A live-action adaptation of Disney's version of the classic 'Beauty and the Beast' tale of a cursed prince and a beautiful young woman who helps him break the spell.",68));
        mainMovies.add(new MovieDetail("Pirates of the Caribbean: The Curse of the Black Pearl","Jack Sparrow, a freewheeling 17th-century pirate who roams the Caribbean Sea, butts heads with a rival pirate bent on pillaging the village of Port Royal. When the governor's daughter is kidnapped, Sparrow decides to help the girl's love save her. But their seafaring mission is hardly simple.",74));
        mainMovies.add(new MovieDetail("Logan","In the near future, a weary Logan cares for an ailing Professor X in a hideout on the Mexican border. But Logan's attempts to hide from the world and his legacy are upended when a young mutant arrives, pursued by dark forces.",78));
        mainMovies.add(new MovieDetail("Wonder Woman","An Amazon princess comes to the world of Man to become the greatest of the female superheroes.",70));
        mainMovies.add(new MovieDetail("Guardians of the Galaxy","Light years from Earth, 26 years after being abducted, Peter Quill finds himself the prime target of a manhunt after discovering an orb wanted by Ronan the Accuser.",79));
        mainMovies.add(new MovieDetail("Interstellar","Interstellar chronicles the adventures of a group of explorers who make use of a newly discovered wormhole to surpass the limitations on human space travel and conquer the vast distances involved in an interstellar voyage.",80));
        mainMovies.add(new MovieDetail("Tomorrow Everything Starts","A man without attachments or responsibilities suddenly finds himself with an abandoned baby and leaves for London to try and find the mother. Eight years later after he and his daughter become inseparable Gloria's mother reappears.",75));
        mainMovies.add(new MovieDetail("Star Wars: The Force Awakens","Thirty years after defeating the Galactic Empire, Han Solo and his allies face a new threat from the evil Kylo Ren and his army of Stormtroopers.",75));
        mainMovies.add(new MovieDetail("Zootopia","Determined to prove herself, Officer Judy Hopps, the first bunny on Zootopia's police force, jumps at the chance to crack her first case - even if it means partnering with scam-artist fox Nick Wilde to solve the mystery.",76));
        mainMovies.add(new MovieDetail("Avatar","In the 22nd century, a paraplegic Marine is dispatched to the moon Pandora on a unique mission, but becomes torn between following orders and protecting an alien civilization.",72));
        setBestMovies();
    }
	
	//add the best rated movies to the bestMovies list
    public void setBestMovies(){
        for(MovieDetail m: mainMovies)
            if(m.getRating()>=75)
                bestMovies.add(m);
    }
}
