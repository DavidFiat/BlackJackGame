package comm;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import com.google.gson.Gson;

import comm.Receptor.OnMessageListener;
import model.Card;
import model.Event;

public class TCPConnection extends Thread {

	// SINGLETON
	private static TCPConnection instance = null;

	private TCPConnection() {
		sessions = new ArrayList<>();
		full = false;
	}

	public static synchronized TCPConnection getInstance() {
		if (instance == null) {
			instance = new TCPConnection();
		}
		return instance;
	}

	public ArrayList<Session> getSessions() {
		return sessions;
	}

	// GLOBAL
	private int puerto;
	private OnConnectionListener connectionListener;
	private OnMessageListener messageListener;
	private ServerSocket server;
	private ArrayList<Session> sessions;
	private boolean full;

	public void setPuerto(int puerto) {
		this.puerto = puerto;
	}

	@Override
	public void run() {
		try {
			server = new ServerSocket(puerto);

			while (true) {
				System.out.println("Esperando en el puerto " + puerto);

				Socket socket = server.accept();
				String id = "";
				if (!full) {
					if (sessions.size() == 0) {
						id = "1";
					} else {
						id = "2";
					}
					System.out.println("Nuevo cliente conectado");
					Session session = new Session(id, socket);
					sessions.add(session);
					if (sessions.size() == 2) {
						full = true;
					}
					connectionListener.onConnection(id);

					Gson gsoon = new Gson();
					String json = gsoon.toJson(new Event("Creation", id));
					session.getEmisor().sendMessage(json);

				} else {
					Session session = new Session(id, socket);
					Gson gsoon = new Gson();
					String json = gsoon.toJson(new Event("Denied"));
					session.getEmisor().sendMessage(json);
				}
				setAllMessageListener(messageListener);

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setAllMessageListener(OnMessageListener listener) {
		for (int i = 0; i < sessions.size(); i++) {
			Session s = sessions.get(i);
			s.getReceptor().setListener(listener);
		}
	}

	public void setConnectionListener(OnConnectionListener connectionListener) {
		this.connectionListener = connectionListener;
	}

	public interface OnConnectionListener {
		public void onConnection(String id);
	}

	public void setMessageListener(OnMessageListener messageListener) {
		this.messageListener = messageListener;
	}

	public void sendBroadcast(String msg) {

		for (int i = 0; i < sessions.size(); i++) {
			Session s = sessions.get(i);
			s.getEmisor().sendMessage(msg);
		}

	}

	public void sendDirectMessage(String id, String msg) {
		for (int i = 0; i < sessions.size(); i++) {
			if (sessions.get(i).getID().equals(id)) {
				sessions.get(i).getEmisor().sendMessage(msg);
				break;
			}
		}
	}

	public boolean getFull() {
		return full;
	}

	public void dispenseCards(Card c, Card c1, Card c2, Card c3) {
		Gson gson = new Gson();
		String json = gson.toJson(c);
		Gson gson1 = new Gson();
		String json1 = gson1.toJson(c1);
		Gson gson2 = new Gson();
		String json2 = gson2.toJson(c2);
		Gson gson3 = new Gson();
		String json3 = gson3.toJson(c3);
		sessions.get(0).getEmisor().sendMessage(json);
		sessions.get(0).getEmisor().sendMessage(json1);
		sessions.get(1).getEmisor().sendMessage(json2);
		sessions.get(1).getEmisor().sendMessage(json3);

		json = gson.toJson(new Event("Active"));
		sessions.get(0).getEmisor().sendMessage(json);
		json = gson.toJson(new Event("Disactive"));
		sessions.get(1).getEmisor().sendMessage(json);

		checkSum();

	}

	private void checkSum() {
		Gson gson4 = new Gson();
		String json4 = gson4.toJson(new Event("Sum", "0"));
		sessions.get(0).getEmisor().sendMessage(json4);
		Gson gson5 = new Gson();
		String json5 = gson5.toJson(new Event("Sum", "1"));
		sessions.get(0).getEmisor().sendMessage(json5);
	}

	public void dispenseCard(Card c, String user) {
		Session a = sessions.get(Integer.parseInt(user) - 1);
		Gson gson = new Gson();
		String json = gson.toJson(c);
		a.getEmisor().sendMessage(json);
	}

	public void getSum(int i) {
		Gson gson5 = new Gson();
		String json5 = gson5.toJson(new Event("Sum", i + ""));
		sessions.get(i - 1).getEmisor().sendMessage(json5);

	}

}
