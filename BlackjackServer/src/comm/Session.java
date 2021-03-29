package comm;

import java.io.IOException;
import java.net.Socket;

public class Session {

	private String id;
	@SuppressWarnings("unused")
	private Socket socket;
	private Receptor receptor;
	private Emisor emisor;

	public Session(String id, Socket socket) {
		try {
			this.setId(id);
			this.socket = socket;
			receptor = new Receptor(socket.getInputStream());
			receptor.start();
			emisor = new Emisor(socket.getOutputStream());
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public Emisor getEmisor() {
		return this.emisor;
	}

	public Receptor getReceptor() {
		return this.receptor;
	}

	public String getID() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
