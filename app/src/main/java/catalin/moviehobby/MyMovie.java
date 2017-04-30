package catalin.moviehobby;

/**
 * Created by Catalin on 30/04/2017.
 */

public class MyMovie {
    String title;
    String poster;

    public MyMovie(String title, String poster) {
        this.title = title;
        this.poster = poster;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }
}
