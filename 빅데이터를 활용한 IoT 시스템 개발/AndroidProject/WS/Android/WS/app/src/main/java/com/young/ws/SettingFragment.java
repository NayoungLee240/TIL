package com.young.ws;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import static android.content.Context.MODE_PRIVATE;

public class SettingFragment extends Fragment {

    SharedPreferences sp;
    LinearLayout linearLayout;

    Switch notiSwitch;
    Switch vibeSwitch;
    SeekBar vibeSeek;
    TextView vibeValue;
    TextView appInfo;

    public SettingFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_setting, container, false);
        linearLayout = (LinearLayout) viewGroup.findViewById(R.id.setlayout);
        linearLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        notiSwitch = viewGroup.findViewById(R.id.notiSwitch);
        vibeSwitch = viewGroup.findViewById(R.id.vibeSwitch);
        vibeSeek = viewGroup.findViewById(R.id.seekBar);
        vibeValue = viewGroup.findViewById(R.id.vibeText);

        sp = getActivity().getSharedPreferences("setting", MODE_PRIVATE);
        boolean noti = sp.getBoolean("notice",true);
        notiSwitch.setChecked(noti);
        if(!noti){
            vibeSwitch.setClickable(false);
            vibeSwitch.setChecked(false);
            vibeSeek.setEnabled(false);
        }else{
            vibeSwitch.setChecked(sp.getBoolean("vibe",true));
        }
        notiSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences.Editor edit = sp.edit();
                edit.putBoolean("notice", b);
                edit.commit();
                if(b){
                    boolean vi = sp.getBoolean("vibe",true);
                    vibeSwitch.setChecked(vi);
                    if(vi)
                        vibeSeek.setEnabled(true);
                }else{
                    vibeSwitch.setChecked(false);
                    vibeSeek.setEnabled(false);
                }
            }
        });
        vibeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences.Editor edit = sp.edit();
                edit.putBoolean("vibe", b);
                edit.commit();
                if(b){
                    vibeSeek.setEnabled(true);
                }else{
                    vibeSeek.setEnabled(false);
                }
            }
        });
        int v = sp.getInt("vibeValue", 5);
        vibeValue.setText(v+"");
        vibeSeek.setProgress(v);
        vibeSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                vibeValue.setText(i+"");
                SharedPreferences.Editor edit = sp.edit();
                edit.putInt("vibeValue", i);
                edit.commit();

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        return viewGroup;
    }
}