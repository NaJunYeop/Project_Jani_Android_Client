package com.example.websocketclient;

import android.net.Network;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class ServerModel {
    private static final String TAG = "RegisterActivity";
    public final static String SERVER_IP = "192.168.43.213";
    public final static String SERVER_PORT = "8080";
    public final static String SERVER_URL = "http://" + SERVER_IP + ":" + SERVER_PORT;
    private HttpURLConnection httpURLConnection;
    private OutputStream outputStream;
    private InputStreamReader inputStreamReader;
    private BufferedReader bufferedReader;
    private URL url;
    private Gson gson;

    private static class LazyHolder {
        public static final ServerModel INSTANCE = new ServerModel();
    }

    private ServerModel() {
        gson = new Gson();
    }

    public static ServerModel getInstance() {
        return LazyHolder.INSTANCE;
    }

    public String requestHttpPost(String requestAdder, String requestJson) {

        try {
            url = new URL(SERVER_URL + requestAdder);
            Log.i("MY TAG", SERVER_URL + requestAdder);
            httpURLConnection = (HttpURLConnection)url.openConnection();

            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Accept", "text/*");
            httpURLConnection.setRequestProperty("Accept-Charset", "UTF-8");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);

            outputStream = httpURLConnection.getOutputStream();
            outputStream.write(requestJson.getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close();

            //getResponseCode와 같은 Method가 Implicit하게 httpURLConnection.connect()를 호출한다.
            if (httpURLConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Log.i("MY TAG", "" + httpURLConnection.getResponseCode());
                return null;
            }

            bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8"));

            String line;
            String ret = "";

            while ((line = bufferedReader.readLine()) != null) {
                ret += line;
            }
            return ret;

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "EXCEPTION";
        } catch (IOException e) {
            e.printStackTrace();
            return "EXCEPTION";
        } finally {
            httpURLConnection.disconnect();
        }
    }

    public String requestHttpGet(String urlAdder) {
        return "";
    }

}
