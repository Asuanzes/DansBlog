package es.tetexe.dansblog;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

/**
 * Created by Alejandro on 02/09/2015.
 */
public class ParseApplications {

    private String data;

    private ArrayList<Application> applications;

    public ParseApplications(String xmlData) {

        data = xmlData;
        applications = new ArrayList<Application>();
    }

    public ArrayList<Application> getApplications() {
        return applications;
    }


    public boolean process() {

        boolean operationStatus = true;
        Application currentRecord = null;
        boolean inItem = false;
        String textValue = "";

        try {

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();

            xpp.setInput(new StringReader(this.data));
            int eventType = xpp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {

                String tagName = xpp.getName();

              if (eventType == XmlPullParser.START_TAG) {


                    if (tagName.equalsIgnoreCase("item")) {

                        inItem = true;
                        currentRecord = new Application();
                    }

                } else if (eventType == XmlPullParser.TEXT) {

                    textValue = xpp.getText();

                } else if (eventType == XmlPullParser.END_TAG) {
                    if (inItem) {
                        if (tagName.equalsIgnoreCase("item")) {
                            applications.add(currentRecord);
                            inItem = false;
                        }
                        if (tagName.equalsIgnoreCase("title")) {
                            currentRecord.setTitle(textValue);
                        } else if (tagName.equals("description")) {
                            currentRecord.setDescription(textValue);

                        } else if (tagName.equalsIgnoreCase("content")) {
                            currentRecord.setContent(textValue);
                        }
                    }

                }

                eventType = xpp.next();

            }


        } catch (Exception e) {
            e.printStackTrace();
            operationStatus = false;
        }

        for (Application app : applications){
            Log.d("LOG", "**************");
            Log.d("LOG", app.getTitle());
            Log.d("LOG", app.getDescription());
            Log.d("LOG", app.getContent());
        }

        return operationStatus;
    }
}
