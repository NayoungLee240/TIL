package com.chat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import com.msg.Msg;

public class Server {
	int port;

	HashMap<String, ObjectOutputStream> maps;

	ServerSocket serverSocket;

	public Server() {
	}

	public Server(int port) {
		this.port = port;
		maps = new HashMap<>();
	}

	/*
	 * Server start Socket 연결 서버를 띄워두고 데이터의 처리를 한다.
	 */
	public void startServer() throws Exception {
		serverSocket = new ServerSocket(port);
		System.out.println("Start Server ...");
		Runnable r = new Runnable() {			
			@Override
			public void run() {
				while (true) {
					try {
						Socket socket = null;
						System.out.println("Ready Server");
						socket = serverSocket.accept();
						System.out.println(socket.getInetAddress() + "에서 접속했습니다.");
						makeOut(socket);
						new Receiver(socket).start();
					}catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		};
		new Thread(r).start();
		
	}

	/*
	 * 각각의 Client가 접속했을 때 각각의 Socket에서 받은 데이터를 HashMap에 데이터를 저장하는역할
	 */
	public void makeOut(Socket socket) throws IOException {
		ObjectOutputStream oo;
		oo = new ObjectOutputStream(socket.getOutputStream());
		maps.put(socket.getInetAddress().toString(), oo);
		System.out.println("현재 접속자 수: " + maps.size());
	}

	/* 데이터를 받는 Thread */
	class Receiver extends Thread {
		Socket socket = null;
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
					if(msg.getMsg().equals("q")) {
						throw new Exception();
					}
					sendMsg(msg);
					System.out.println("["+msg.getId()+"]:"+msg.getMsg());
				} catch (Exception e) {
					/*서버에 클라이언트연결이 끊어진 경우*/
					maps.remove(socket.getInetAddress().toString());
					System.out.println(socket.getInetAddress()+"이 나갔습니다.");
					System.out.println("현재 접속자 수: " + maps.size());
					break;
				}
			}
			try {
				if(oi != null) {
					oi.close();
				}
			}catch (Exception e) {
			}
			try {
				if(socket!=null) {
					socket.close();
				}
			}catch (Exception e) {
			}
		}
	}

	/* 받은 메시지를 클라이언트들에게 전송해준다. */
	public void sendMsg(Msg msg) {
		Sender sender = new Sender();
		sender.setMsg(msg);
		sender.start();
	}

	/* 데이터를 보내는 쓰레드 (주소별로 보내야할까?) */
	class Sender extends Thread {
		Msg msg;
		public Sender() {
		}
		public void setMsg(Msg msg) {
			this.msg = msg;
		}
		@Override
		public void run() {
			Collection<ObjectOutputStream> cols = maps.values();
			Iterator<ObjectOutputStream> it = cols.iterator();
			while(it.hasNext()) {
				try {
					it.next().writeObject(msg);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) {
		Server server = new Server(5555);
		try {
			server.startServer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
