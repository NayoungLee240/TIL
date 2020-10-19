package com.nyoungee.p524;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class SecondActivity extends AppCompatActivity {
    String url = "http://www.kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.json?key=430156241533f1d058c603178cc3ca0e&targetDt=20201018";
    ListView listView;
    LinearLayout container;
    ArrayList<Movie> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        // 필요한 변수 선언
        listView = findViewById(R.id.listView);
        container = findViewById(R.id.container);
        list = new ArrayList<>();
        getData();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            ProgressDialog progressDialog;
            
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                progressDialog = new ProgressDialog(SecondActivity.this);


            }
        });
    }
    private void getData(){
        ItemAsync itemAsync = new ItemAsync();
        itemAsync.execute(url);
    }

    class ItemAsync extends AsyncTask<String,Void,String> {  // 마지막 String :서버에서 네트워크를 받아오는 것을 String으로 리턴하겠다.
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(SecondActivity.this);
            progressDialog.setTitle("Get Data...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String url = strings[0].toString();
            String result = HttpConnect.getString(url);

            Log.d("Test", url);
            return result; // return 하면 onPostExcute 로 간다.
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
//            JSONArray ja = null;
            try {
                JSONObject j = new JSONObject(s);
                Log.d("Test","Here");
//                JSONArray j= new JSONArray(s);
                JSONObject j1 = j.getJSONObject("boxOfficeResult");

                JSONArray ja = j1.getJSONArray("dailyBoxOfficeList");
                for(int i=0;i<ja.length();i++){
                    JSONObject jo = ja.getJSONObject(i);
                    String movieNm = jo.getString("movieNm");
                    String movieCd = jo.getString("movieCd");
                    String rank = jo.getString("rank");
                    String openDt = jo.getString("openDt");
                    String rankInten = jo.getString("rankInten");
                    String audiAcc = jo.getString("audiAcc");
                    Movie item = new Movie(rank,rankInten,movieCd,movieNm,openDt,audiAcc);
                    list.add(item);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ItemAdapter itemAdapter = new ItemAdapter();
            listView.setAdapter(itemAdapter);

        }
    } // end AsyncTask

    class ItemAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = null;
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView = inflater.inflate(R.layout.item,container,true);
            TextView name = itemView.findViewById(R.id.mName);
            TextView open = itemView.findViewById(R.id.openDate);
            TextView rank = itemView.findViewById(R.id.rank);
            TextView rankInten = itemView.findViewById(R.id.rankInten);
            TextView audiAcc = itemView.findViewById(R.id.audiAcc);

            name.setText(list.get(position).getMovieNm());
            open.setText(list.get(position).getOpenDt());
            rank.setText(list.get(position).getRank());
            audiAcc.setText(list.get(position).getAudiAcc()+"명");


            String ri = list.get(position).getRankInten();
            int riNum = Integer.valueOf(ri);
            if(riNum<0){
                Drawable img = getResources().getDrawable( R.drawable.ic_down );
                img.setBounds( 0, 0, 60, 60 );
                rankInten.setCompoundDrawables( img, null, null, null );
                ri = ri.substring(1);
            } else if (riNum > 0) {
                Drawable img = getResources().getDrawable( R.drawable.ic_up );
                img.setBounds( 0, 0, 60, 60 );
                rankInten.setCompoundDrawables( img, null, null, null );
            } else{

                Drawable img = getResources().getDrawable( R.drawable.ic_zero );
                img.setBounds( 0, 0, 60, 60 );
                rankInten.setCompoundDrawables( img, null, null, null );
            }
            rankInten.setText(ri);

            ImageView img = itemView.findViewById(R.id.imageView);
            img.setImageResource(R.drawable.ic_launcher_background);

            return itemView;
        }
    } // end Adapter

}