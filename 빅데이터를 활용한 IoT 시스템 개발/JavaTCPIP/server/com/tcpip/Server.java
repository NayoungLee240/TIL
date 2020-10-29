package com.tcpip;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.msg.Msg;
//Object Serialization이용
public class Server {

	int port;
	ServerSocket serverSocket;
	Socket socket;
	
	public Server() {}
	public Server(int port) {
		this.port = port;
	}
	public void startServer() throws Exception{
		System.out.println("TCP/IP Server Start....");
		try {
			serverSocket = new ServerSocket(port);
			while(true) {
				System.out.println("Ready Server ...");
				socket = serverSocket.accept();
				System.out.println("Connected..");
				new Receiver(socket).start();
			}
		} catch (IOException e) {
			throw e;
		}
	}
	
	class Receiver extends Thread{
		ObjectInputStream dis;
		Socket socket = null;
		
		public Receiver(Socket socket) {
			this.socket = socket;
			try {
				dis = new ObjectInputStream(socket.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			Msg mo = null;
			while(dis!=null) {
				try {
					mo = (Msg)dis.readObject();
					if(mo.getMsg().equals("q")) {
						System.out.println("나갔습니다.");
						break;
					}
					System.out.println("["+mo.getId()+"]:"+mo.getMsg());
				} catch (IOException e) {
					System.out.println("["+mo.getId()+"]님이 나갔습니다.");
					break;
				}catch (ClassNotFoundException e) {
					System.out.println("잘못된 입력이 들어왔습니다.");
					e.printStackTrace();
				}
			}
			if(dis!=null) {
				try {
					dis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(socket!=null) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public static void main(String[] args) {
		Server server = new Server(7777);
		try {
			server.startServer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
