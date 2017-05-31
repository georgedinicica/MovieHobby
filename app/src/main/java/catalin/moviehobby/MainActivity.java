package catalin.moviehobby;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    private ListView lv;
    ArrayList<Movie> movieList = new ArrayList<>();
    private static String imgBody = "https://image.tmdb.org/t/p/w500";
    ArrayList<MyMovie> myMovieList = new ArrayList<>();

    ArrayList<HashMap<String, String>> contactList;
    private String myURL = "https://api.themoviedb.org/3/discover/movie?api_key=9d78e326bf184dcfaf30197ee99b2d8e&primary_release_year=2017";
    private Button like, dislike;
    ImageView poster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contactList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list);
        poster = (ImageView) findViewById(R.id.myPoster);

        like = (Button) findViewById(R.id.likeButton);
        dislike = (Button) findViewById(R.id.dislikeButton);
        new GetContacts().execute();
    }

    public void likeAction(View view) {

        Toast.makeText(this, "movie Liked", Toast.LENGTH_SHORT).show();
        like.setVisibility(View.INVISIBLE);
        dislike.setVisibility(View.INVISIBLE);
        poster.setVisibility(View.INVISIBLE);
    }

    public void dislikeAction(View view) {
        Toast.makeText(this, "Dislike", Toast.LENGTH_SHORT).show();
        like.setVisibility(View.INVISIBLE);
        dislike.setVisibility(View.INVISIBLE);
        poster.setVisibility(View.INVISIBLE);
    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MainActivity.this, "Json Data is downloading", Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            String myUrl = myURL;
            String jsonStr = sh.makeServiceCall(myUrl);
            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {

                    JSONObject json = new JSONObject(jsonStr);
                    for (int i = 0; i < json.getJSONArray("results").length(); i++) {
                        JSONArray results = json.getJSONArray("results");
                        String poster = results.getJSONObject(i).getString("poster_path");
                        String title = results.getJSONObject(i).getString("original_title");
                        String releaseDate = results.getJSONObject(i).getString("release_date");
                        String plot = results.getJSONObject(i).getString("overview");


                        HashMap<String, String> contact = new HashMap<>();
                        contact.put("original_title", title);
                        contact.put("release_date", releaseDate);
                        contact.put("poster_path", poster);
                        contact.put("overview", "PLOT:\n" + plot);
                        contactList.add(contact);

                        MyMovie movie = new MyMovie(title, poster);
                        myMovieList.add(movie);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            ListAdapter adapter = new SimpleAdapter(MainActivity.this, contactList,
                    R.layout.list_item, new String[]{"original_title", "release_date", "overview", "poster_path"},
                    new int[]{R.id.title, R.id.releaseDate, R.id.plot, R.id.poster});
            ArrayAdapter<Movie> movieAdapter = new ArrayAdapter<>(MainActivity.this,
                    android.R.layout.simple_list_item_1,
                    movieList);

            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                    new Runnable() {
                        @Override
                        public void run() {
                            Picasso.with(MainActivity.this)
                                    .load(imgBody + myMovieList.get(position).getPoster())
                                    .resize(250, 250)
                                    .centerCrop()
                                    .into((ImageView) findViewById(R.id.myPoster));
                        }
                    }.run();

                    findViewById(R.id.myPoster).setVisibility(View.VISIBLE);
                    like.setVisibility(View.VISIBLE);
                    dislike.setVisibility(View.VISIBLE);
                }

            });
        }
    }
}
