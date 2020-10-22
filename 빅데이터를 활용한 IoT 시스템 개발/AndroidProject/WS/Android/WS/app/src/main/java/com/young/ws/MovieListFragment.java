package com.young.ws;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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

public class MovieListFragment extends Fragment {

    String url = "http://www.kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.json?key=430156241533f1d058c603178cc3ca0e&targetDt=20201021";
    ListView listView;
    LinearLayout container;
    ArrayList<Movie> list;

    public MovieListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup)inflater.inflate(R.layout.fragment_movie_list, container, false);
        listView = viewGroup.findViewById(R.id.listView);
        container = viewGroup.findViewById(R.id.container);
        list = new ArrayList<>();
        getData();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                Intent intent = new Intent(getActivity().getApplicationContext(), MovieActivity.class);
                intent.putExtra("movieName",list.get(position).getMovieNm());
                startActivity(intent);
//                LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
//                View alertView = inflater.inflate(R.layout.content, null);
//
//                final ImageView img = alertView.findViewById(R.id.movieImage);
//                String cd = list.get(position).getMovieCd();
//                final String url2 = "http://192.168.55.114/android/img/"+cd+".jpg";
//
//                Thread t = new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        URL httpurl = null;
//                        InputStream is = null;
//                        try {
//                            httpurl = new URL(url2);
//                            is = httpurl.openStream();
//                            final Bitmap bm = BitmapFactory.decodeStream(is);
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    img.setImageBitmap(bm);
//                                }
//                            });
//                        } catch (Exception e) {
//                            img.setImageResource(R.drawable.ic_launcher_background);
//                            e.printStackTrace();
//                        }
//
//                    }
//                });
//                t.start();
//
//                TextView title = alertView.findViewById(R.id.movieName);
//                title.setText(list.get(position).getMovieNm());
//                TextView open = alertView.findViewById(R.id.movieOpen);
//                open.setText("媛쒕큺??"+list.get(position).getOpenDt());
//
//                AlertDialog.Builder builder= new AlertDialog.Builder(SecondActivity.this);
//
//                builder.setView(alertView);
//                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                    }
//                });
//                builder.show();

            }
        });

        return viewGroup;
    }



    private void getData(){
        ItemAsync itemAsync = new ItemAsync();
        itemAsync.execute(url);
    }

    class ItemAsync extends AsyncTask<String,Void,String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(MovieListFragment.this.getContext());
            progressDialog.setTitle("Get Data...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String url = strings[0].toString();
            String result = HttpConnect.getString(url);

            Log.d("Test", url);
            return result;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            try {
                JSONObject j = new JSONObject(s);

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
                    Log.d("Test","Here:"+movieCd+","+movieNm);
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
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView = inflater.inflate(R.layout.item_movie,container,true);
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
                img.setBounds( 0, 0, 30, 30 );
                rankInten.setCompoundDrawables( img, null, null, null );
                ri = ri.substring(1);
            } else if (riNum > 0) {
                Drawable img = getResources().getDrawable( R.drawable.ic_up );
                img.setBounds( 0, 0, 30, 30 );
                rankInten.setCompoundDrawables( img, null, null, null );
            } else{
                Drawable img = getResources().getDrawable( R.drawable.ic_zero);
                img.setBounds( 0, 0, 30, 30 );
                rankInten.setCompoundDrawables( img, null, null, null );
            }
            rankInten.setText(ri);

            final ImageView img = itemView.findViewById(R.id.imageView);

            String cd = list.get(position).getMovieCd();
            final String url2 = "http://192.168.55.114/android/img/"+cd+".jpg";

            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    URL httpurl = null;
                    InputStream is = null;
                    try {
                        httpurl = new URL(url2);
                        is = httpurl.openStream();
                        final Bitmap bm = BitmapFactory.decodeStream(is);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                img.setImageBitmap(bm);
                            }
                        });
                    } catch (Exception e) {
//                        img.setImageResource(R.drawable.ic_launcher_background);
                        e.printStackTrace();
                    }

                }
            });
            t.start();

            return itemView;
        }
    } // end Adapter

}