package br.com.meuspedidos.fumasa.teste;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FetchJsonTask fjt = new FetchJsonTask(this);
        Uri path = Uri.parse("https://gist.githubusercontent.com/ronanrodrigo/b95b75cfddc6b1cb601d7f806859e1dc/raw/dc973df65664f6997eeba30158d838c4b716204c/products.json");

        fjt.execute(path);
    }

    private class FetchJsonTask extends AsyncTask<Uri, Void, ArrayList> {
        private final static String LOG_TAG = "FetchJsonTask";
        private ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        private Context context;

        public FetchJsonTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            progressBar.setIndeterminate(true);
        }

        @Override
        protected void onPostExecute(ArrayList produtos) {
            progressBar.setIndeterminate(false);

            final ListView lv1 = (ListView) findViewById(R.id.lvProdutos);
            lv1.setAdapter(new CustomListAdapter(this.context, produtos));
            lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                    Object o = lv1.getItemAtPosition(position);
                    Produto produto = (Produto) o;
                    Toast.makeText(MainActivity.this, "Selected :" + " " + produto.getName(), Toast.LENGTH_LONG).show();
                }
            });
        }

        @Override
        protected ArrayList<Produto> doInBackground(Uri... urls) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            ArrayList<Produto> results = new ArrayList<>();

            String jsonStr = null;

            try {
                URL url = new URL(urls[0].toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                jsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                JSONArray jsonArray = new JSONArray(jsonStr);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObj = jsonArray.getJSONObject(i);
                    results.add(Produto.fromJson(jsonObj));
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }

            return results;
        }
    }
}
