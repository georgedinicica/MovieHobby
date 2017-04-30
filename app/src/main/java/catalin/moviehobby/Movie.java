package catalin.moviehobby;


import android.widget.ImageView;

public class Movie {
    String name;
    String released;
    int year;
    ImageView poster;

    public Movie(String name, String released, int year, ImageView poster) {
        this.name = name;
        this.released = released;
        this.year = year;
        this.poster = poster;

    }

    public Movie(String name, int year, String released) {

        this.name = name;
        this.year = year;
        this.released = released;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getReleased() {
        return released;
    }

    public void setReleased(String released) {
        this.released = released;
    }

    public ImageView getPoster() {
        return poster;
    }

    public void setPoster(ImageView poster) {
        this.poster = poster;
    }
}
