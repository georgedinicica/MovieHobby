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

       /* @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
//            String url = "http://api.androidhive.info/contacts/";
//            String jsonStr = sh.makeServiceCall(url);
            String myUrl = "http://www.omdbapi.com/?t=london";
            String jsonStr = sh.makeServiceCall(myUrl);
            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("contacts");

                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);
                        String id = c.getString("id");
                        String name = c.getString("name");
                        String email = c.getString("Title");
                        String address = c.getString("address");
                        String gender = c.getString("gender");

                        // Phone node is JSON Object
                        JSONObject phone = c.getJSONObject("phone");
                        String mobile = phone.getString("mobile");
                        String home = phone.getString("home");
                        String office = phone.getString("office");

                        // tmp hash map for single contact
                        HashMap<String, String> contact = new HashMap<>();

                        // adding each child node to HashMap key => value
                        contact.put("id", id);
                        contact.put("name", name);
                        contact.put("Title", email);
                        contact.put("mobile", mobile);

                        // adding contact to contact list
                        contactList.add(contact);
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
        }*/

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
//            String myUrl = "http://www.omdbapi.com/?t=london";
            String myUrl = myURL;
            String jsonStr = sh.makeServiceCall(myUrl);
            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {


                   /* JSONObject jsonObject = new JSONObject(jsonStr);
                    JSONArray results= jsonObject.getJSONArray("results");
                    String title = jsonObject.getString("Title");
                    int year = jsonObject.getInt("Year");
                    String released = jsonObject.getString("Released");
                    String genre = jsonObject.getString("Genre");
                    String poster = jsonObject.getString("Poster");
                    HashMap<String, String> contact = new HashMap<>();
                    contact.put("poster", poster);
                    contact.put("title", title);
                    contact.put("year", String.valueOf(year));
                    contact.put("released", released);
                    contact.put("genre", genre);

                    MyMovie movie=new MyMovie(title,poster);
                    Movie m = new Movie(title, year, released);
                    movieList.add(m);
                    myMovieList.add(movie);

                    contactList.add(contact);
*/
                    JSONObject json = new JSONObject(jsonStr);
                    for (int i = 0; i < json.getJSONArray("results").length(); i++) {
                        JSONArray results = json.getJSONArray("results");
                        String poster = results.getJSONObject(i).getString("poster_path");
                        String title = results.getJSONObject(i).getString("original_title");

                        HashMap<String, String> contact = new HashMap<>();
                        contact.put("original_title", title);
                        contact.put("poster_path", poster);

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
                    R.layout.list_item, new String[]{"original_title", "poster_path"},
                    new int[]{R.id.title, R.id.poster});
            ArrayAdapter<Movie> movieAdapter = new ArrayAdapter<>(MainActivity.this,
                    android.R.layout.simple_list_item_1,
                    movieList);

            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                    //   Toast.makeText(MainActivity.this, contactList.get(position).toString(), Toast.LENGTH_LONG).show();

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
