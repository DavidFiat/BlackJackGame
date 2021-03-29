package control;

import comm.Receptor.OnMessageListener;

import java.util.ArrayList;
import java.util.Collections;

import com.google.gson.Gson;

import comm.TCPConnection;
import comm.TCPConnection.OnConnectionListener;
import javafx.application.Platform;
import main.Launcher;
import model.Card;
import model.Event;
import view.DealerWindow;

public class DealerController implements OnMessageListener, OnConnectionListener {

	private DealerWindow view;
	private TCPConnection connection;
	private ArrayList<Card> cards;
	private int Player1Sum;
	private int Player2Sum;
	private boolean Player1Paused;
	private boolean Player2Paused;

	public DealerController(DealerWindow view) {
		this.view = view;
		Player1Sum = 0;
		Player2Sum = 0;
		Player1Paused = false;
		Player2Paused = false;
		init();
	}

	public void init() {
		connection = TCPConnection.getInstance();
		connection.setPuerto(5000);
		connection.start();
		connection.setConnectionListener(this);
		connection.setMessageListener(this);

	}

	@Override
	public void onConnection(String id) {
		Platform.runLater(() -> {
			if (!connection.getFull()) {
				cards = new ArrayList<Card>(52);
				String[] suit = new String[4];
				suit[0] = "C";
				suit[1] = "P";
				suit[2] = "T";
				suit[3] = "D";
				int s = 0;
				while (cards.size() < 52) {
					for (int count = 1; count < 14; count++) {
						Card c = new Card(count + "", suit[s]);
						cards.add(c);
					}
					s++;

				}
				cards.add(new Card("Joker", "Red"));
				cards.add(new Card("Joker", "Black"));
				Collections.shuffle(cards);

			} else {
				Card c = cards.get(0);
				Card c1 = cards.get(1);
				Card c2 = cards.get(2);
				Card c3 = cards.get(3);
				connection.dispenseCards(c, c1, c2, c3);
				cards.remove(c);
				cards.remove(c1);
				cards.remove(c2);
				cards.remove(c3);

			}
		}

		);
	}

	@Override
	public void OnMessage(String msg) {
		Platform.runLater(() -> {
			Gson gson = new Gson();
			Event message = gson.fromJson(msg, Event.class);
			if (message.getType().equals("Give")) {
				Collections.shuffle(cards);
				connection.dispenseCard(cards.get(0), message.getAux());
				cards.remove(0);
				Collections.shuffle(cards);
				connection.getSum(Integer.parseInt(message.getAux()));
			} else if (message.getType().equals("Sum")) {
				int index = Integer.parseInt(message.getAux().split("-")[0]) - 1;
				int sum = Integer.parseInt(message.getAux().split("-")[1]);
				if (index == 0) {
					Player1Sum = sum;
				} else {
					Player2Sum = sum;
				}
				gameStatus();

			} else if (message.getType().equals("Stand")) {
				int i = Integer.parseInt(message.getAux());
				if (i == 0) {
					Player1Paused = true;
				} else {
					Player2Paused = true;
				}
			}
		});

	}

	private void gameStatus() {
		String los = "lost";
		String wo = "won";
		Event lost = new Event("lost");
		Event won = new Event("won");
		Gson gson = new Gson();
		String lostt = gson.toJson(lost);
		String wonn = gson.toJson(won);
		boolean cases = false;
		if (Player1Sum > 21) {
			connection.getSessions().get(0).getEmisor().sendMessage(lostt);
			connection.getSessions().get(1).getEmisor().sendMessage(wonn);
			cases = true;

		} else if (Player1Sum == 21) {
			connection.getSessions().get(0).getEmisor().sendMessage(wonn);
			connection.getSessions().get(1).getEmisor().sendMessage(lostt);
			cases = true;
		}

		if (Player2Sum > 21) {
			connection.getSessions().get(0).getEmisor().sendMessage(wonn);
			connection.getSessions().get(1).getEmisor().sendMessage(lostt);
			cases = true;
		} else if (Player2Sum == 21) {
			connection.getSessions().get(1).getEmisor().sendMessage(wonn);
			connection.getSessions().get(0).getEmisor().sendMessage(lostt);
			cases = true;
		}
		if (cases == true)
			Launcher.main(null);
	}

}
