package com.example.mybucketlist.login;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class PHPRequest {
    private URL url;
    public String result_string;

    public PHPRequest(String url) throws MalformedURLException {
        this.url = new URL(url);
    }

    private String readStream(InputStream in) throws IOException {
        StringBuilder jsonHtml = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        String line;

        while ((line = reader.readLine()) != null)
            jsonHtml.append(line);

        reader.close();
        return jsonHtml.toString();
    }

    public String PHPCONN(final String postData){
        String result = null;
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(5000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(postData.getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close();
            result = readStream(conn.getInputStream());
            conn.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public String PhPsave(final String id, final String input_title, final String input_local, final String input_date, final String input_detail, final String input_complete, final String ident_num){
        String postData = "ID=" + id + "&" + "TITEL=" + input_title + "&" + "LOCAL=" + input_local + "&" + "COMPLT=" + input_complete + "&" + "TDATE=" + input_date + "&" + "BODY=" + input_detail + "&" + "IDNT_NUM=" + ident_num;
        result_string = PHPCONN(postData);
        return result_string;
    }

    public String PhPid_check(final String id){
        String postData = "ID=" + id;
        result_string = PHPCONN(postData);
        return result_string;
    }

    public String PhPlogin(final String id, final String pw){
        String postData = "ID=" + id + "&" + "PW=" + pw;
        result_string = PHPCONN(postData);
        return result_string;
    }

    public String PhPsign_up(final String id, final String pw, final String name) {
        String postData = "ID=" + id + "&" + "PW=" + pw + "&" + "NAME=" + name;
        result_string = PHPCONN(postData);
        return result_string;
    }
}