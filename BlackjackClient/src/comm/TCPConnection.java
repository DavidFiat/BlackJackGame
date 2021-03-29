package comm;

import java.io.IOException;
import java.net.Socket;

import comm.Receptor.OnMessageListener;

public class TCPConnection extends Thread{

	private static TCPConnection instance = null;

	private TCPConnection() {
	}

	public static synchronized TCPConnection getInstance() {

		if (instance == null) {
			instance = new TCPConnection();
		}

		return instance;

	}

	private Socket socket;
	private String ip;
	private int puerto;
	private Receptor receptor;
	private Emisor emisor;
	@SuppressWarnings("unused")
	private OnMessageListener listener;
	private OnConnectionListener connectionListener;

	public void setPuerto(int puerto) {
		this.puerto = puerto;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@Override
	public void run() {
		try {

			System.out.println("Conectadome al servidor");
			socket = new Socket(ip, puerto);
			receptor = new Receptor(socket.getInputStream());
			receptor.start();
			connectionListener.onConnection();
			emisor = new Emisor(socket.getOutputStream());
			System.out.println("Conectado");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setListenerOfMessages(OnMessageListener listener) {
		receptor.setListener(listener);
	}

	public Emisor getEmisor() {
		return this.emisor;
	}

	public Receptor getReceptor() {
		return receptor;
	}

	public void setConnectionListener(OnConnectionListener connectionListener) {
		this.connectionListener = connectionListener;
	}

	public interface OnConnectionListener {
		public void onConnection();
	}

}
