package com.tcpip;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import com.msg.Msg;

public class Client {

	int port;
	String address;
	Socket socket;
	Sender sender;
	
	public Client() {}
	public Client(String address, int port) {
		this.address = address;
		this.port = port;
	}
	public void connect() throws Exception{
		while(true){
			try {
				socket = new Socket(address, port);
				System.out.println("Connected ...");
				break;
			} catch (Exception e) {
				System.out.println("Re-Try ...");
				Thread.sleep(2000);
			}
		}
		sender = new Sender();
			
	}
	class Sender implements Runnable{
		ObjectOutputStream dos;
		Msg mo;
		public Sender() {
			try {
				dos = new ObjectOutputStream(socket.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		public void setMo(Msg mo) {
			this.mo = mo;
		}
		@Override
		public void run() {
			if(dos!=null) {
				try {
					dos.writeObject(mo);
					System.out.println("Send message");
				} catch (IOException e) {
					System.out.println("Not Avaliable");
					System.exit(0);
				}
			}
		}
	}
	public void request() throws Exception{
		Scanner sc = new Scanner(System.in);
		try {
			while(true) {
				System.out.println("[Input Msg]");
				String msg = sc.nextLine();
				Msg mo = new Msg(address,"jean", msg.trim());
				sender.setMo(mo);
				new Thread(sender).start();
				Thread.sleep(1000);
				if(msg.equals("q")) {
					System.out.println("Exit Client ...");
					break;
				}
			}
		}catch (Exception e) {
			throw e;
		}finally {
			sc.close();
			if(socket != null) {
				socket.close();
			}
		}
	}
	public static void main(String[] args) {
		Client client = new Client("192.168.55.114", 5555);
		try {
			client.connect();
			client.request();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
