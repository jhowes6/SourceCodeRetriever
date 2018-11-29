package com.jhowes.sourcecoderetriever1;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

//*******************************************************************************************
//PageLoader class
//
//This class extends AsyncTaskLoader to run a background thread which retrieves the source
//code from a given URL
//*******************************************************************************************
public class PageLoader extends AsyncTaskLoader<String> {
    public String queryString;

    public PageLoader(@NonNull Context context, String string) {
        super(context);
        queryString = string;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Nullable
    @Override
    public String loadInBackground() {
        return getSourceCode(queryString);
    }

    //*******************************************************************************************
    //getSourceCode()
    //
    // * creates a HttpURLConnection to the user-entered URL
    // * reads the source code from the URL connection and returns it as a String
    //*******************************************************************************************
    public  String getSourceCode(String url){
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String result;

        try{
            URL requestURL = new URL(url);
            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if(inStream == null) return null;
            reader = new BufferedReader(new InputStreamReader(inStream));
            String line;
            while((line = reader.readLine()) != null){
                buffer.append(line);

            }
            result =  buffer.toString();
        }catch(IOException e){
            e.printStackTrace();
            return null;
        }
        return result;
    }
}
