package com.example.t2_f_a18gabrielsm;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParser;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ActivitySalary extends AppCompatActivity {

    class DownloadTask extends AsyncTask<Void, Integer, Boolean>{

        String downloadURL;

        public DownloadTask(String downloadURL) {
            this.downloadURL = downloadURL;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            URL url = null;

            try {
                url = new URL(downloadURL);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            xmlName = Uri.parse(downloadURL).getLastPathSegment();
                xmlFile = new File(getExternalFilesDir(null) + File.separator + xmlName);
                try {

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000);
                    conn.setConnectTimeout(15000);
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);

                    conn.connect();
                    int response = conn.getResponseCode();

                    if (response == HttpURLConnection.HTTP_OK) {
                        OutputStream os = new FileOutputStream(xmlFile);
                        InputStream in = conn.getInputStream();
                        byte data[] = new byte[1024];
                        int count;
                        while ((count = in.read(data)) != -1) {
                            os.write(data, 0, count);
                        }
                        os.flush();
                        os.close();
                        in.close();
                        conn.disconnect();
                    }

                } catch (FileNotFoundException e){
                    Toast.makeText(getApplicationContext(), "Non se atopou ningun arquivo na url:\n" + downloadURL, Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                    return false;
                } catch (Exception e){
                    e.printStackTrace();
                    return false;
                }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean) {
                try {
                    InputStream is = new FileInputStream(xmlFile);
                    XmlPullParser parser = Xml.newPullParser();
                    parser.setInput(is, "UTF-8");

                    ArrayList<Salary> salariesList = new ArrayList<>();

                    int evento = parser.nextTag();
                    while (evento != XmlPullParser.END_DOCUMENT){
                        Salary salary = null;
                        float total_salary;

                        if (evento == XmlPullParser.START_TAG) {
                            if (parser.getName().equals("salary")){
                                salary = new Salary();
                                parser.nextTag();
                                salary.setMonth(parser.nextText());
                                parser.nextTag();
                            }
                            if (parser.getName().equals("amount")){
                                total_salary = Float.parseFloat(parser.nextText());
                                parser.nextTag();
                                while (!parser.getName().equals("salary")){
                                    total_salary += Float.parseFloat(parser.nextText());
                                    parser.nextTag();
                                }
                                salary.setSalary_total(total_salary);
                                salariesList.add(salary);
                                Log.i("Salary", salary.toString());
                            }
                        }
                        evento = parser.next();
                    }

                    for (Salary salary : salariesList){
                        if (salariesDB.insertSalary(salary) == -1){
                            Log.i("ERROR", "Error al añadir " + salary.toString() + " a la base de datos");
                        } else {
                            Log.i("INSERT", "Se ha insertado " + salary.toString() + " en la base de datos");
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            super.onPostExecute(aBoolean);
        }
    }

    SalariesDB salariesDB;

    DownloadTask downloadTask;
    SharedPreferences prefs;
    String xmlName;
    File xmlFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.salary);

        loadDatabase();
        loadClicks();

        prefs = getSharedPreferences("PREFS", MODE_PRIVATE);

    }

    private void loadDatabase() {
        salariesDB = new SalariesDB(getApplicationContext());
        salariesDB.sqLiteDatabase = salariesDB.getWritableDatabase();
    }

    private void downloadXML(){
        EditText edtURL = findViewById(R.id.edtURL);
        String downloadURL = String.valueOf(edtURL.getText());
        if (!edtURL.getText().toString().equals("")) {
            downloadTask = new DownloadTask(downloadURL);
            downloadTask.execute();
        } else {
            Toast.makeText(getApplicationContext(), "O campo non pode estar baleiro", Toast.LENGTH_LONG).show();
        }
    }

    private void showSalaries() {
        TextView tvSalaries = findViewById(R.id.tvSalaries);
        ArrayList<String> salariesList = salariesDB.selectSalaries();
        tvSalaries.setText("");
        for (String salaryLine : salariesList) {
            tvSalaries.append(salaryLine + "\n");
        }
    }

    private void saveToFile() {
        ArrayList<String> salariesList = salariesDB.selectSalaries();
        String fileName = prefs.getString("NAME", "");
        String filePath = getExternalFilesDir(null) + File.separator + fileName;

        try {
            OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(filePath, false));

            for (String line : salariesList) {
                osw.write(line);
            }
            osw.close();

            Log.i("PATH", filePath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadClicks() {
        Button btnDownload = findViewById(R.id.btnDownload);
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadXML();
            }
        });

        Button btnShowSalaries = findViewById(R.id.btnShowSalaries);
        btnShowSalaries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSalaries();
            }
        });

        Button btnSalariesToFile = findViewById(R.id.btnSalariesToFile);
        btnSalariesToFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToFile();
            }
        });

    }

}
