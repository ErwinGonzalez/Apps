package galileo.abapp.movietablist.data;

import java.io.Serializable;

/**
 * Created by EEVGG on 01/06/2017.
 */

public class MovieDetail implements Serializable{
    private String title;
    private String desc;
    private int rating;
    public MovieDetail(){}

    public MovieDetail(String t,String d, int r){
        this.title=t;
        this.desc=d;
        this.rating=r;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc=desc;
    }
    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
