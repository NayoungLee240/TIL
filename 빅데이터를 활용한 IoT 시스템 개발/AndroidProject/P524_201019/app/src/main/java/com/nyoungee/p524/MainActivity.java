package com.nyoungee.p524;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView tx_id, tx_pwd;
    HttpAsync httpAsync;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tx_id = findViewById(R.id.tx_id);
        tx_pwd = findViewById(R.id.tx_pwd);
    }

    public void ck(View v) {
        String id = tx_id.getText().toString();
        String pwd = tx_pwd.getText().toString();
        Log.d("TEST",id+","+pwd);
        String url = "http://192.168.55.114/android/login.jsp";
        url += "?id="+id+"&pwd="+pwd;
        httpAsync = new HttpAsync();
        httpAsync.execute(url);

    }


    class HttpAsync extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            // 로그인 시 progressDialog 띄움 로그인되면 사라짐.
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle("Login ...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String url = strings[0].toString();
            String result = HttpConnect.getString(url); // 정보가 맞으면 0, 틀리면 1값이 들어온다
            return result;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            Log.d("TEST", s+"");
            String result = s.trim(); // 혹시 모를 불필요한 데이터가 있을걸 대비해서 처리
            if(result.equals("1")){ // 로그인 성공
                Intent intent = new Intent(getApplicationContext(), SecondActivity.class);
                startActivity(intent);
            }else { // 로그인 실패
                AlertDialog.Builder dailog = new AlertDialog.Builder((MainActivity.this));
                dailog.setTitle("LOGIN FAIL");
                dailog.setMessage("Try Again...");
                dailog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                dailog.show();

            }
        }
    }

}