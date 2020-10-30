package com.young.tcpip;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.msg.Msg;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    TextView tx_list, tx_msg;
    EditText et_ip, et_msg;

    int port;
    String address;
    String id;
    Socket socket;

    Sender sender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tx_list = findViewById(R.id.tx_list);
        tx_msg = findViewById(R.id.tx_msg);
        et_ip = findViewById(R.id.et_ip);
        et_msg = findViewById(R.id.et_msg);
        port = 5555;
        address = "192.168.55.114";
        id = "[phone]";
        new Thread(con).start();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
            Msg msg = new Msg(null, id, "q");
            sender.setMsg(msg);
            new Thread(sender).start();
            if (socket != null) {
                socket.close();
            }
//            finish();
//            onDestroy();
        } catch (Exception e) {

        }
    }

    Runnable con = new Runnable() {
        @Override
        public void run() {
            try {
                connect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    private void connect() throws IOException {
        try {
            socket = new Socket(address, port);
        } catch (Exception e) {
            while (true) {
                try {
                    Thread.sleep(2000);
                    socket = new Socket(address, port);
                    System.out.println("Conneted ...");
                    break;
                } catch (Exception e1) {
                    System.out.println("Retry ...");
                }
            }
        }

        System.out.println("Connected Server: " + address);
        sender = new Sender(socket);
        new Receiver(socket).start();
        getList();
    }

    private void getList() {
        Msg msg = new Msg(null, id, "1");
        sender.setMsg(msg);
        new Thread(sender).start();
    }

    public void clickBt(View v) {
//        String ip = et_ip.getText().toString();
        String msg = et_msg.getText().toString();
        if(!msg.equals("")&&msg!=null)
            sendMsg();
    }

    public void sendMsg() {
        String s_ip = et_ip.getText().toString();
        String ms = et_msg.getText().toString();
        Msg msg = null;
        //1을 보내면 사용자 리스트
        if (ms.equals("1")) {
            msg = new Msg(id, ms);
        } else if (s_ip == null || s_ip.equals("")) {
            msg = new Msg(null, id, ms);
        } else {
            ArrayList<String> ips = new ArrayList<>();
            ips.add(s_ip);
            msg = new Msg(ips, id, ms);
        }

        sender.setMsg(msg);
        new Thread(sender).start();
        if (ms.equals("q")) {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Bye ...");
            // Sender연결 해제
        }

    }

    class Receiver extends Thread {
        Socket socket;
        ObjectInputStream oi;

        public Receiver() {
        }

        public Receiver(Socket socket) throws IOException {
            this.socket = socket;
            oi = new ObjectInputStream(socket.getInputStream());
        }

        @Override
        public void run() {
            while (oi != null) {
                Msg msg = null;
                try {
                    msg = (Msg) oi.readObject();
                    if (msg.getMaps() != null) {
                        tx_list.setText("");
                        HashMap<String, Msg> hm = msg.getMaps();
                        Set<String> keys = hm.keySet();
                        for (final String k : keys) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    String tx = tx_list.getText().toString();
                                    tx_list.setText(tx + k + "\n");
                                }
                            });
                        }
                        continue;
                    }
                    final Msg finalMsg = msg;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String tx = tx_msg.getText().toString();
                            tx_msg.setText(tx + finalMsg.getId() + ":" + finalMsg.getMsg() + "\n");
                        }
                    });
                } catch (Exception e) {
//					e.printStackTrace();
                    System.out.println("서버 연결이 끊겼습니다.");
                    break;
                }
            }
            try {
                if (oi != null) {
                    oi.close();
                }
                if (socket != null)
                    socket.close();
            } catch (Exception e) {
            }
        }

    }

    class Sender implements Runnable {
        Socket socket;
        ObjectOutputStream oo;
        Msg msg;

        public Sender() {
        }

        public Sender(Socket socket) throws IOException {
            this.socket = socket;
            oo = new ObjectOutputStream(socket.getOutputStream());
        }

        public void setMsg(Msg msg) {
            this.msg = msg;
        }

        @Override
        public void run() {
            if (oo != null) {
                try {
                    oo.writeObject(msg);
                    et_msg.setText("");
                } catch (IOException e) {
//					e.printStackTrace();
                    try {
                        if (socket != null) {
                            socket.close();
                        }
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    try {// 재접속이 필요!
                        if (!msg.getMsg().equals("q")) {
                            System.out.println("[sender]Retry ...");
                            Thread.sleep(2000);
                            connect();
                        }
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
    }
}