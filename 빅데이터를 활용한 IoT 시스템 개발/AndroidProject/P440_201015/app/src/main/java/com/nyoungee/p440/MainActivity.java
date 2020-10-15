package com.nyoungee.p440;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    ArrayList<Person> persons;
    ListView listView;
    LinearLayout container;
    EditText name, birth, phone;
    TextView person_num;
    CalendarView calendarView;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        persons = new ArrayList<>();
        listView = findViewById(R.id.listView);
        container = findViewById(R.id.person);
        name = findViewById(R.id.name);
        birth = findViewById(R.id.date);
//        birth.addTextChangedListener(new DateFormat);
        phone = findViewById(R.id.phone);
//        phone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        person_num = findViewById(R.id.person_num);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Person item = persons.get(position);

                AlertDialog.Builder builder= new AlertDialog.Builder(MainActivity.this);

                builder.setTitle("삭제").setMessage("삭제하시겠습니까?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        persons.remove(position);
                        setList(persons);
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        });

        String permissions[] = {
                Manifest.permission.ACCESS_NETWORK_STATE
        };
        ActivityCompat.requestPermissions(this, permissions, 101);

        IntentFilter intentfilter = new IntentFilter();
        intentfilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");

        actionBar = getSupportActionBar();
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String cmd = intent.getAction();
                ConnectivityManager cm = null;
                NetworkInfo wifi = null;
                if(cmd.equals("android.net.conn.CONNECTIVITY_CHANGE")){
                    cm = (ConnectivityManager)context.getSystemService(
                            Context.CONNECTIVITY_SERVICE
                    );
                    wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                    if(wifi!=null && wifi.isConnected()){
                        actionBar.setLogo(R.drawable.ic_baseline_wifi_24);
                    }else{
                        actionBar.setLogo(R.drawable.ic_baseline_wifi_off_24);
                    }
                    actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME|ActionBar.DISPLAY_USE_LOGO);
                }
            }
        };
        registerReceiver(broadcastReceiver, intentfilter);
    }

    public void onbBt(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater layoutInflater = getLayoutInflater();

        View cview = layoutInflater.inflate(R.layout.cal, (ViewGroup) findViewById(R.id.clayout) );

        calendarView = cview.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String date = ""+year;
                int m = month+1;
                if(m<10){
                    date+="0"+m;
                }else{
                    date+=m;
                }
                if(dayOfMonth<10){
                    date+="0"+dayOfMonth;
                }else{
                    date+=dayOfMonth;
                }
                SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMdd");

                try{
                    Date d = transFormat.parse(date);
                    long dd = d.getTime();
                    calendarView.setDate(dd);
                }catch (Exception e){
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(),date,Toast.LENGTH_SHORT ).show();
            }
        });
        builder.setView(cview);
        builder.setTitle("생일을 입력해주세요");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                long d = calendarView.getDate();
//                Toast.makeText(getApplicationContext(),""+d, Toast.LENGTH_SHORT).show();
                SimpleDateFormat timeStampFormat = new SimpleDateFormat("yy-MM-dd",
                        Locale.KOREA);
                String date = timeStampFormat.format(new Timestamp(d));
                birth.setText(date);

            }

        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();

    }
    public void ckBt(View v){
        if(name.getText().toString().equals("")){
            Toast.makeText(this,"이름을 입력해주세요.",Toast.LENGTH_SHORT).show();
        }else if(birth.getText().toString().equals("")){
            Toast.makeText(this,"생일을 입력해주세요.",Toast.LENGTH_SHORT).show();
        }else if(phone.getText().toString().equals("")){
            Toast.makeText(this,"번호를 입력해주세요.",Toast.LENGTH_SHORT).show();
        }else{
            getData();
        }
    }

    public void getData(){
        persons.add(new Person(name.getText().toString(), birth.getText().toString(), phone.getText().toString()));
        setList(persons);
        name.setText("");
        birth.setText("");
        phone.setText("");
    }
    public void setList(ArrayList<Person> persons){
        PersonAdapter personAdapter = new PersonAdapter(persons);
        listView.setAdapter(personAdapter);
        person_num.setText(persons.size()+ " 명");
    }

    class PersonAdapter extends BaseAdapter {

        ArrayList<Person> datas;
        public PersonAdapter( ArrayList<Person> datas){
            this.datas = datas;
        }

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int position) {
            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = null;
            LayoutInflater inflater =
                    (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(
                    R.layout.person,
                    container,
                    true
            );

            TextView tx_num = view.findViewById(R.id.customer_num);
            TextView tx_birth = view.findViewById(R.id.customer_birth);
            TextView tx_phone = view.findViewById(R.id.customer_phone);

            Person p = datas.get(position);
            int num = position+1;
            tx_num.setText("고객# "+num);
            tx_birth.setText(p.getBirth()+"");
            tx_phone.setText(p.getPhone());

            return view;
        }
    }
}