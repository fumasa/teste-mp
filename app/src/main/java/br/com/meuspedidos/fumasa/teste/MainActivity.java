package br.com.meuspedidos.fumasa.teste;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
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

    public ImageLoader imgLoader;
    public Dialog dlgFilters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ListView lv1 = (ListView) findViewById(R.id.lvProdutos);
        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Object o = lv1.getItemAtPosition(position);
                Produto produto = (Produto) o;
                Toast.makeText(MainActivity.this, "Selected :" + " " + produto.getName(), Toast.LENGTH_LONG).show();
            }
        });

        final ImageButton btnCategoria =(ImageButton)findViewById(R.id.btnCategoria);
        btnCategoria.setOnClickListener(new View.OnClickListener()   {
            public void onClick(View v)  {
                try {
                    Toast.makeText(MainActivity.this, "Categoria", Toast.LENGTH_LONG).show();

                    dlgFilters = new Dialog(MainActivity.this);
                    dlgFilters.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dlgFilters.setContentView(R.layout.filters_layout);

                    FetchCategoriesJsonTask fcjt = new FetchCategoriesJsonTask(MainActivity.this, dlgFilters);
                    Uri path = Uri.parse("https://gist.githubusercontent.com/ronanrodrigo/e84d0d969613fd0ef8f9fd08546f7155/raw/a0611f7e765fa2b745ad9a897296e082a3987f61/categories.json");

                    fcjt.execute(path);

                    dlgFilters.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        imgLoader = new ImageLoader(this);

        FetchProductsJsonTask fpjt = new FetchProductsJsonTask(this);
        Uri path = Uri.parse("https://gist.githubusercontent.com/ronanrodrigo/b95b75cfddc6b1cb601d7f806859e1dc/raw/dc973df65664f6997eeba30158d838c4b716204c/products.json");

        fpjt.execute(path);
    }

    private class FetchCategoriesJsonTask extends AsyncTask<Uri, Void, ArrayList> {
        private final static String LOG_TAG = "FetchJsonTask";
        private ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        private Dialog dialog;
        private Context context;

        public FetchCategoriesJsonTask(Context context, Dialog dialog) {
            this.context = context;
            this.dialog = dialog;
        }

        @Override
        protected void onPreExecute() {
            progressBar.setIndeterminate(true);
        }

        @Override
        protected void onPostExecute(ArrayList categorias) {
            progressBar.setIndeterminate(false);

            final ListView lv1 = (ListView) this.dialog.findViewById(R.id.lvCategorias);
            lv1.setAdapter(new CategoriasListAdapter(this.context, categorias));
        }

        @Override
        protected ArrayList<Categoria> doInBackground(Uri... urls) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            ArrayList<Categoria> results = new ArrayList<>();

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
                    results.add(Categoria.fromJson(jsonObj));
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }

            return results;
        }
    }

    private class FetchProductsJsonTask extends AsyncTask<Uri, Void, ArrayList> {
        private final static String LOG_TAG = "FetchJsonTask";
        private ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        private Context context;

        public FetchProductsJsonTask(Context context) {
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
            lv1.setAdapter(new ProdutosListAdapter(this.context, produtos, imgLoader));
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
