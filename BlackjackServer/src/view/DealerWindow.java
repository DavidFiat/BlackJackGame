package view;

import control.DealerController;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DealerWindow extends Stage {

	// UI Elements
	private Scene scene;
	private TextArea messagesArea;
	private DealerController contol;

	public DealerWindow() {
		messagesArea = new TextArea();

		VBox vBox = new VBox();
		vBox.getChildren().add(messagesArea);

		scene = new Scene(vBox, 400, 400);
		this.setScene(scene);
		setContol(new DealerController(this));
	}

	public DealerController getContol() {
		return contol;
	}

	public void setContol(DealerController contol) {
		this.contol = contol;
	}

}
