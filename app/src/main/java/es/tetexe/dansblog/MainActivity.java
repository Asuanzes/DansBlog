package es.tetexe.dansblog;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {


    Button btnParse;
    ListView listView;
    String xmlData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnParse = (Button) findViewById(R.id.btnParse);
        listView = (ListView) findViewById(R.id.listView);

        btnParse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseApplications parse = new ParseApplications(xmlData);
                 parse.process();
            }
        });

        new DownloadData().execute("http://www.enriquedans.com/feed");
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class DownloadData extends AsyncTask<String, Void, String> {

        String myXmlData;


        @Override
        protected String doInBackground(String... urls) {
            try {

                myXmlData = downloadXML(urls[0]);

            } catch (IOException e) {
                return "Unable to download XML file.";
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("OnpostExecute", myXmlData);
            xmlData = myXmlData;

        }

        private String downloadXML(String theUrl) throws IOException {

            int BUFFER_SIZE = 2000;

            InputStream is = null;

            String xmlContents = "";

            try {
                URL url = new URL(theUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(20000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                int response = conn.getResponseCode();
                Log.d("DownloadXMl", "The response returned is:" + response);
                is = conn.getInputStream();

                InputStreamReader isr = new InputStreamReader(is);

                int charRead;

                char[] inputBuffer = new char[BUFFER_SIZE];
                try {
                    while ((charRead = isr.read(inputBuffer)) > 0) {
                        String readString = String.copyValueOf(inputBuffer, 0, charRead);
                        xmlContents += readString;
                        inputBuffer = new char[BUFFER_SIZE];
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }

            } finally {
                if (is != null)
                    is.close();
            }
            //Colocado en un lugar diferente al vídeo
            return xmlContents;

        }

    }
}
