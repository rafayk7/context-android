package com.rafaykalim.context.libraries;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.telephony.SmsMessage;
import android.util.Log;
import com.rafaykalim.context.libraries.models.WebPageModel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.lang.reflect.Array;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Function;

public class JavaUtils {
    public static Activity getActivityFromContext(Context context) {
        if (context == null) return null;
        if (context instanceof Activity) return (Activity) context;
        if (context instanceof ContextWrapper) return getActivityFromContext(((ContextWrapper)context).getBaseContext());
        return null;
    }

    public static String getMessageFromTextIntent(Bundle bundle)
    {
        Object[] pdus = (Object[]) bundle.get("pdus");
        if (pdus.length == 0) {
            return "" ;
        }
        // large message might be broken into many
        SmsMessage[] messages = new SmsMessage[pdus.length];
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < pdus.length; i++) {
            messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
            sb.append(messages[i].getMessageBody());
        }

        String sender = messages[0].getOriginatingAddress();
        String message = sb.toString();

        if(sender.equals(new ApiUrl().getDestNumber()))
        {
            return message;
        }
        return "";
    }

//    public static ArrayList<WebPageModel> getLinksFromGoogle(String query) throws IOException {
//        AsyncTask.execute(new Runnable() {
//            @Override
//            public void run() {
//                ArrayList<WebPageModel> rArray = new ArrayList<>();
//
//                String google = "http://www.google.com/search?q=";
//                String search = query;
//                String charset = "UTF-8";
//                String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36"; // Change this to your company's name and bot homepage!
//
//                ArrayList<String> badTitles = new ArrayList<>();
//
//                badTitles.add("Google");
//                badTitles.add("here");
//                badTitles.add("Videos");
//                badTitles.add("Images");
//                badTitles.add("Shopping");
//                badTitles.add("Verbatim");
//                badTitles.add("Maps");
//                badTitles.add("Books");
//                badTitles.add("Search tools");
//                badTitles.add("Past hour");
//                badTitles.add("Past 24 hours");
//                badTitles.add("Past week");
//                badTitles.add("Past month");
//                badTitles.add("Past year");
//                badTitles.add(">");
//                badTitles.add("Next >");
//                badTitles.add("Sign in");
//                badTitles.add("Settings");
//                badTitles.add("Privacy");
//                badTitles.add("Terms");
//                badTitles.add("View all");
//
//                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
//                        .permitAll().build();
//                StrictMode.setThreadPolicy(policy);
//
//                //li[class=g]
//                Document doc = Jsoup.connect(google + URLEncoder.encode(search, charset)).userAgent("Mozilla").ignoreHttpErrors(true).get();
//
//                Elements links = doc.select("a[href]");
//                for (Element link : links) {
//                    String title = link.text();
//                    String url = link.absUrl("href"); // Google returns URLs in format "http://www.google.com/url?q=<url>&sa=U&ei=<someKey>".
//
//                    if(isIn(title, badTitles))
//                    {
//                        continue;
//                    }
//
//                    if (!url.startsWith("http")) {
//                        continue; // Ads/news/etc.
//                    }
//
//                    WebPageModel wpm = new WebPageModel(title, url, Jsoup.connect(url).get());
//
//                    rArray.add(wpm);
//                }
//                handleReturn(rArray);
//            }
//        });
//    }

    public interface OnLinksInterface {
        void onLinks(ArrayList<WebPageModel> result);
    }

    public static void getHtmlWebPage(String stringUrl, OnLinksInterface callback) {
        new getLinksFromGoogle(callback).execute(stringUrl);
    }

    private static class getLinksFromGoogle extends AsyncTask<String, Void, ArrayList<WebPageModel>> {
        private String query;
        private OnLinksInterface callback;

        ArrayList<WebPageModel> result = new ArrayList<>();

        getLinksFromGoogle(OnLinksInterface callback) {
            this.callback = callback;
        }

        @Override
        protected ArrayList<WebPageModel> doInBackground(String... params) {
            ArrayList<WebPageModel> rArray = new ArrayList<>();

            String google = "http://www.google.com/search?q=";
            String search = params[0];
            String charset = "UTF-8";
            String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36"; // Change this to your company's name and bot homepage!

            ArrayList<String> badTitles = new ArrayList<>();

            badTitles.add("Google");
            badTitles.add("here");
            badTitles.add("Videos");
            badTitles.add("Images");
            badTitles.add("Shopping");
            badTitles.add("Verbatim");
            badTitles.add("Maps");
            badTitles.add("Books");
            badTitles.add("Search tools");
            badTitles.add("Past hour");
            badTitles.add("Past 24 hours");
            badTitles.add("Past week");
            badTitles.add("Past month");
            badTitles.add("Past year");
            badTitles.add(">");
            badTitles.add("Next >");
            badTitles.add("Sign in");
            badTitles.add("Settings");
            badTitles.add("Privacy");
            badTitles.add("Terms");
            badTitles.add("View all");

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            //li[class=g]
            Document doc = null;

            try {
                doc = Jsoup.connect(google + URLEncoder.encode(search, charset)).userAgent("Mozilla").ignoreHttpErrors(true).get();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Elements links = doc.select("a[href]");
            for (Element link : links) {
                String title = link.text();
                String url = link.absUrl("href"); // Google returns URLs in format "http://www.google.com/url?q=<url>&sa=U&ei=<someKey>".

                if(isIn(title, badTitles))
                {
                    continue;
                }

                if (!url.startsWith("http")) {
                    continue; // Ads/news/etc.
                }

                WebPageModel wpm = null;
                try {
                    wpm = new WebPageModel(title, url, Jsoup.connect(url).get());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                rArray.add(wpm);
            }
            return rArray;
        }

        @Override
        protected void onPostExecute(ArrayList<WebPageModel> response) {
            this.callback.onLinks(response);
        }
    }

    public static ArrayList<WebPageModel> handleReturn(ArrayList<WebPageModel> r)
    {
        return r;
    }
    public static void writeFileOnInternalStorage(Context mcoContext,String sFileName, ArrayList<WebPageModel> sBody) throws IOException {
        File file = new File(mcoContext.getFilesDir(),"webpages");

        if(!file.exists()){
            file.mkdir();
        }

        File titlesFile = new File(file, "webpage_titles.txt");
        FileOutputStream fOutput = new FileOutputStream(titlesFile, true);

        OutputStreamWriter titleWriter = new OutputStreamWriter(fOutput);
        titleWriter.append(sFileName + "\n");
        titleWriter.close();

        try{
            File gpxfile = new File(file, sFileName + ".txt");
            FileWriter writer = new FileWriter(gpxfile);
            int i = 0;

            for(WebPageModel wpm : sBody)
            {
                writer.append(i+ "\n");
                writer.append("Title " + wpm.getTitle() + " ");
                writer.append("url " + wpm.getUrl() + " ");
                writer.append("html " + wpm.getHtml());
            }

            writer.flush();
            writer.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static Boolean isIn(String string, ArrayList<String> list)
    {
        for(String element: list)
        {

            if(element.equals(string))
            {
                return true;
            }
        }
        return false;
    }
}
