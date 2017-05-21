package tut.ac.za.openmerchant.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by ProJava on 11/25/2015.
 */public class BackgroundTask extends AsyncTask<String,Void,String> {
    AlertDialog builder;
     Context context;
    String user_name;



    public BackgroundTask(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        builder = new AlertDialog.Builder(context).create();
        builder.setTitle("Sending Message...");
        builder.show();



    }

    @Override
    protected String doInBackground(String... params) {
        String reg_url = "http://flightapp1.000webhostapp.com/messenger.php";


        //name,surname,username,email,sex,province,password


                 String username = params[0];
                String subject = params[1];
            String message = params[2];





        try {
            URL url = new URL(reg_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream os = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            String data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8") + "&" +
                    URLEncoder.encode("message", "UTF-8") + "=" + URLEncoder.encode(message, "UTF-8")+
                    URLEncoder.encode("subject", "UTF-8") + "=" + URLEncoder.encode(subject, "UTF-8");;

            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            os.close();

            InputStream is = httpURLConnection.getInputStream();
            is.close();
            httpURLConnection.disconnect();
            return "Message Sent...";

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }







        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {


        Toast.makeText(context,result,Toast.LENGTH_LONG).show();

        //builder.dismiss();
    }




}
