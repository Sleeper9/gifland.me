package hu.braso.giflandme;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import hu.braso.giflandme.database.GifDataSource;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    private static final String URL = "http://gifland.us";
    private static final String URL_18 = "http://porn.gifland.us";

    private Deque<String> visitedUrls = new ArrayDeque<>();

    @Bean
    GifDataSource gifDataSource;

    @ViewById
    WebView gifView;
    @ViewById
    Button prev_gif;
    @ViewById
    Button next_gif;

    @AfterViews
    void doAfterView() {
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        next_gif.performClick();
    }

    @Click(R.id.prev_gif)
    void prevGifClick() {
        try {
            GifLoaderAsync loaderAsny = new GifLoaderAsync(true);
            String gif = loaderAsny.execute(visitedUrls.removeLast()).get();
            gifView.loadUrl(gif);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Click({R.id.next_gif, R.id.gifView})
    void nextGifClick() {
        try {
            String url = URL;
            GifLoaderAsync gifLoaderAsync = new GifLoaderAsync(false);
            boolean xxx = PreferenceManager.getDefaultSharedPreferences(this).getBoolean(getString(R.string.pref_xxx_enabled), false);
            if(xxx){
                boolean xxxOnly = PreferenceManager.getDefaultSharedPreferences(this).getBoolean(getString(R.string.pref_xxx_only), false);
                if (xxxOnly) {
                    url = URL_18;
                } else {
                    Random r = new Random();
                    if(r.nextBoolean()){
                        url = URL_18;
                    }
                }
            }
            String gif = gifLoaderAsync.execute(url).get();
            gifView.loadUrl(gif);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private int getScale() {
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = display.getWidth();
        Double val = new Double(width) / new Double(gifView.getWidth());
        val = val * 100d;
        return val.intValue();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class GifLoaderAsync extends AsyncTask<String, Void, String> {

        private final boolean cached;

        public GifLoaderAsync(boolean cached){
            this.cached = cached;
        }

        @Override
        protected String doInBackground(String... params) {
            FileOutputStream output = null;
            String fileName = null;

            if(!cached){
                try {
                    Document document = Jsoup.connect(params[0]).get();

                    int gifId = Integer.parseInt(document.getElementById("gif").getElementsByTag("a").attr("href").substring(1));
                    String gifUrl = document.getElementById("gif").getElementsByTag("img").attr("src");

                    fileName = "gif-" + gifId + ".gif";

                    if (!gifDataSource.exists(gifId)) {
                        InputStream in = new java.net.URL(gifUrl).openStream();
                        output = openFileOutput(fileName, MODE_PRIVATE);
                        byte[] buffer = new byte[4 * 1024]; // or other buffer size
                        int read;

                        while ((read = in.read(buffer)) != -1) {
                            output.write(buffer, 0, read);
                        }
                        output.flush();

                        visitedUrls.add(fileName);

                        gifDataSource.insert(gifId, gifUrl, PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean(getString(R.string.pref_xxx_enabled), false), fileName);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (output != null) {
                        try {
                            output.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                fileName = params[0];
            }

            return "file://" + getFileStreamPath(fileName).getAbsolutePath();
        }
    }
}
