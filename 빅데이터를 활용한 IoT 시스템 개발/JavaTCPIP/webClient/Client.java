package com.chat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

import com.msg.Msg;

public class Client {

	int port;
	String address;
	String id;

	Socket socket;

	Sender sender;

	public Client() {
	}

	public Client(String address, int port, String id) {
		this.address = address;
		this.port = port;
		this.id = id;
	}

	/* Server 연결 */
	public void connect() throws Exception {
		while (true) {
			try {
				socket = new Socket(address, port);
				System.out.println("Conneted ...");
				break;
			} catch (Exception e) {
				Thread.sleep(2000);
				System.out.println("Retry ...");
			}
		}
		System.out.println("Connected Server: " + address);
		sender = new Sender(socket);
		new Receiver(socket).start();
	}

	public void sendTarget(String ip, String cmd) {
		ArrayList<String> ips = new ArrayList<String>();
		ips.add(ip);
		Msg msg = new Msg(ips, id, cmd);
		sender.setMsg(msg);
		new Thread(sender).start();
	}

	/* 스캐너에서 입력받아 보낸다. */
	public void sendMsg() {
		Scanner sc = new Scanner(System.in);
		while (true) {
			System.out.println("[Input msg]: ");
			String ms = sc.nextLine();
			Msg msg = null;
			// 1을 보내면 사용자 리스트
			if (ms.equals("1")) {
				msg = new Msg(id, ms);
			} else {
				ArrayList<String> ips = null;
//				ArrayList<String> ips = new ArrayList<>();
//				ips.add("/192.168.55.1");
				msg = new Msg(ips, id, ms);
			}

			sender.setMsg(msg);
			new Thread(sender).start();
			if (ms.equals("q")) {
				break;
			}

		}
		sc.close();
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

	/* 보내는 쓰레드 */
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

	/* 메시지받는 쓰레드 */
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
						HashMap<String, Msg> hm = msg.getMaps();
						Set<String> keys = hm.keySet();
						for (String k : keys) {
							System.out.println(k);
						}
						continue;
					}
					System.out.println(msg.getId() + ":" + msg.getMsg());
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

	public static void main(String[] args) {// 192.168.0.4
		Client client = new Client("192.168.0.4", 5555, "[ny_pc]");
		try {
			client.connect();
			client.sendMsg();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
