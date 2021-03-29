package control;

import comm.Receptor.OnMessageListener;

import com.google.gson.Gson;

import comm.TCPConnection;
import comm.TCPConnection.OnConnectionListener;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import main.Launcher;
import model.Card;
import model.Event;
import view.PlayerWindow;

public class PlayerController implements OnMessageListener, OnConnectionListener {

	private PlayerWindow view;
	private TCPConnection connection;
	private int sum;
	private String usuario;

	public PlayerController(PlayerWindow view) {
		this.view = view;
		init();
		sum = 0;
		usuario = "";
	}

	public void init() {
		connection = TCPConnection.getInstance();
		connection.setConnectionListener(this);
		connection.setIp("127.0.0.1");
		connection.setPuerto(5000);
		connection.start();

	}

	@Override
	public void OnMessage(String msg) {
		Platform.runLater(

				() -> {
					Gson gson = new Gson();
					Event message = gson.fromJson(msg, Event.class);
					if (message.getType().equals("Denied")) {
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Conexión denegada");
						alert.setHeaderText("Ya existen dos jugadores.");
						alert.setContentText("El juego esta lleno");
						alert.showAndWait();
						view.close();
					} else if (message.getType().equals("Creation")) {
						usuario = message.getAux();
					} else if (message.getType().equals("Sum")) {
						Gson gson1 = new Gson();
						String json = gson1.toJson(new Event("Sum", message.getAux() + "-" + sum));
						connection.getEmisor().sendMessage(json);
					} else if (message.getType().equals("won")) {
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Ganador!");
						alert.setHeaderText("Has ganado el juego.");
						alert.setContentText("Has llegado a 21 o eres el más cercano.");
						alert.showAndWait();
						view.close();
						Launcher.main(null);

					} else if (message.getType().equals("lost")) {
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Perdedor!");
						alert.setHeaderText("Has perdido el juego.");
						alert.setContentText("El otro jugador se acercó más o te has pasado de 21.");
						alert.showAndWait();
						view.close();
						Launcher.main(null);

					} else {
						Card message1 = gson.fromJson(msg, Card.class);
						if (message1.getType().equals("Card")) {
							dispense(message1);

						}
					}
				});
	}

	private void dispense(Card c) {
		String la = "";
		switch (c.getNumber()) {
		case "1":
			la = "A";
			sum += 11;
			break;
		case "Joker":
			la = "Joker";
			sum += 11;
			break;
		case "11":
			la = "J";
			sum += 11;
			break;
		case "12":
			la = "Q";
			sum += 11;
			break;
		case "13":
			la = "K";
			sum += 11;
			break;
		default:
			la = "" + Integer.parseInt(c.getNumber());
			sum += Integer.parseInt(c.getNumber());

		}

		switch (c.getSuit()) {
		case "C":
			la += "♥";
			break;
		case "P":
			la += "♠";
			break;
		case "T":
			la += "♣";
			break;
		case "D":
			la += "♦";
			break;
		case "Red":
			la += "🃏";
			break;
		case "Black":
			la += "🃏";
			break;
		}

		view.putCard(la);
		view.setStatus("Jugando" + "\n" + "Puntuación: " + sum);

	}

	@Override
	public void onConnection() {
		connection.setListenerOfMessages(this);
	}

	public void takeCard() {
		Gson gson = new Gson();
		String json = gson.toJson(new Event("Give", usuario));
		connection.getEmisor().sendMessage(json);

	}

	public void stand() {
		Gson gson = new Gson();
		String json = gson.toJson(new Event("Stand", usuario));
		connection.getEmisor().sendMessage(json);
	}

}
