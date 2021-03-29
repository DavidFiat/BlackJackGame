package view;

import java.io.IOException;

import control.PlayerController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class PlayerWindow extends Stage {

	// UI Elements
	private Scene scene;
	private PlayerController control;
	private int SlotEmpty;
	private Label Slot1;
	private Label Slot2;
	private Label Slot3;
	private Label Slot4;
	private Label Slot5;
	private Button takeCard;
	private Button stand;
	private Label status;

	public PlayerWindow() {

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("PlayerWindow.fxml"));
			Parent parent = loader.load();
			scene = new Scene(parent, 477, 422);
			this.setScene(scene);
			SlotEmpty = 1;
			control = new PlayerController(this);
			status = (Label) loader.getNamespace().get("status");
			Slot1 = (Label) loader.getNamespace().get("Slot1");
			Slot2 = (Label) loader.getNamespace().get("Slot2");
			Slot3 = (Label) loader.getNamespace().get("Slot3");
			Slot4 = (Label) loader.getNamespace().get("Slot4");
			Slot5 = (Label) loader.getNamespace().get("Slot5");
			stand = (Button) loader.getNamespace().get("stand");

			takeCard = (Button) loader.getNamespace().get("takeCard");
			takeCard.setOnAction((event) -> {
				control.takeCard();
			});
			stand.setOnAction((event) -> {
				control.stand();
			});

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void putCard(String c) {

		switch (SlotEmpty) {
		case 1:
			Slot1.setText(c);
			SlotEmpty++;
			break;
		case 2:
			Slot2.setText(c);
			SlotEmpty++;
			break;
		case 3:
			Slot3.setText(c);
			SlotEmpty++;
			break;
		case 4:
			Slot4.setText(c);

			SlotEmpty++;
			break;
		case 5:
			Slot5.setText(c);
			SlotEmpty++;
			break;
		}
		System.out.println(c);
	}

	public void setStatus(String status) {
		this.status.setText(status);
	}

}
