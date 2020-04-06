package com.ikefuku.savemoney;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

public class InputDataActivity extends AppCompatActivity {

    private static final String ACCESS_URL = "http://ikefuku40.vivian.jp/savemoney/index.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_data);

        Spinner spinner = findViewById(R.id.spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.custom_spinner,
                getResources().getStringArray(R.array.itemList)
        );
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        spinner.setAdapter(adapter);

        final Button backBtn = findViewById(R.id.toShowDataActivity);
        backBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(),ShowDataActivity.class);
                startActivity(intent);
                Log.d("debug", "ShowDataActivity→InputDataActivity");
            }
        });

        final Button regBtn = findViewById(R.id.inputdata);
        regBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //目標金額
                EditText editTargetNum = (EditText)findViewById(R.id.targetnum);
                String targetNum = editTargetNum.getText().toString();
                //現在残額
                EditText editRemainum = (EditText)findViewById(R.id.remainnum);
                String remainNum = editRemainum.getText().toString();
                // Spinnerオブジェクトを取得
                Spinner spinner = (Spinner)findViewById(R.id.spinner);
                // 選択されているアイテムのIndexを取得
                int idx = spinner.getSelectedItemPosition();
                // 選択されているアイテムを取得
                String slectItem = (String)spinner.getSelectedItem();
                //入力金額
                EditText editSpendingNum = (EditText)findViewById(R.id.spendingnum);
                String spendingNum = editSpendingNum.getText().toString();


                PostAccess access = new PostAccess();
                access.execute(ACCESS_URL, targetNum, remainNum,slectItem,spendingNum);
            }
        });

    }

    private class PostAccess extends AsyncTask<String, String, String> {


        private boolean _success = false;

        public PostAccess() {

        }

        @Override
        public String doInBackground(String... params) {
            String DEBUG_TAG="testpost";
            String urlStr = params[0];
            String param1 = params[1];
            String param2 = params[2];
            String param3 = params[3];
            String param4 = params[4];

            String postData = "param1= " + param1 + "&param2=" + param2 + "&param3=" + param3 + "&param4=" + param4;
            HttpURLConnection con = null;
            InputStream is = null;
            String result = "";

            try {
                URL url = new URL(urlStr);
                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setConnectTimeout(5000);
                con.setReadTimeout(5000);
                con.setDoOutput(true);
                OutputStream os = con.getOutputStream();
                os.write(postData.getBytes());
                os.flush();
                os.close();
                int status = con.getResponseCode();
                if (status != 200) {
                    throw new IOException("ステータスコード: " + status);
                }
                is = con.getInputStream();

                _success = true;
            }
            catch(SocketTimeoutException | MalformedURLException ex) {
                Log.e(DEBUG_TAG, "タイムアウト", ex);
            } catch(IOException ex) {
                Log.e(DEBUG_TAG, "通信失敗", ex);
            }
            finally {
                if (con != null) {
                    con.disconnect();
                }
                try {
                    if (is != null) {
                        is.close();
                    }
                }
                catch (IOException ex) {
                    Log.e(DEBUG_TAG, "InputStream解析失敗", ex);
                }
            }
            return result;
        }

        @Override
        public void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

        }

        @Override
        public void onPostExecute(String result) {
            if (_success) {
                String name = "";
                String comment = "";

            }
        }


    }
}
