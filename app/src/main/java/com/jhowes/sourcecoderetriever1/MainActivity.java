package com.jhowes.sourcecoderetriever1;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<String>, AdapterView.OnItemSelectedListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    public EditText mEditText;
    public TextView mSourceCodeTextView;
    private String mSpinnerChoice = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        if(spinner != null){
            spinner.setOnItemSelectedListener(this);
        }

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.spinner_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        mEditText = (EditText) findViewById(R.id.url_edit_text);
        mSourceCodeTextView = (TextView) findViewById(R.id.source_code_textview);
    }
    //*******************************************************************************************
    //getCode()
    //
    // * on-click method for button
    // * check's internet connection, then uses the LoaderManager to start background task,
    //   passing it the user-entered URL
    //*******************************************************************************************
    public void getCode(View v){
        String url = mSpinnerChoice;
        url += mEditText.getText().toString();

        mSourceCodeTextView.setText(url);

        //check internet connection
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInf = connMgr.getActiveNetworkInfo();
        if(netInf != null && netInf.isConnected() && url.length() != 0){
            Bundle queryBundle = new Bundle();
            queryBundle.putString("URL", url);
            getSupportLoaderManager().restartLoader(0, queryBundle, this);
            mSourceCodeTextView.setText(R.string.loading_text);
        } else mSourceCodeTextView.setText(R.string.check_connection);


    }
    //*******************************************************************************************
    //onCreateLoader()
    //
    // * uses the PageLoader class to return the source code from the URL
    //*******************************************************************************************
    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        return new PageLoader(this, args.getString("URL"));
    }

    //*******************************************************************************************
    //onLoadFinished()
    //
    // * displays the source code from PageLoader class in mSourceCodeTextView
    //*******************************************************************************************
    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        try{
            mSourceCodeTextView.setText(data);
        }catch(Exception e){
            mSourceCodeTextView.setText(R.string.no_results);
            e.printStackTrace();
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }

    //*******************************************************************************************
    //onItemSelected()
    //
    // * sets mSpinnerChoice to the choice selected by the user (HTTP:// or HTTPS://)
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mSpinnerChoice = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Log.d(TAG, "onNothingSelected: ");

    }
}
