package MedicalSoftware;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MedicalSoftwareGUI extends Application {
	@Override

	public void start(Stage primaryStage) {
		Patient p1 = new Patient("Male", "927371", "Owen", "Jones", 25, "4'3", "500");
		Label label = new Label(p1.toString());
		StackPane root = new StackPane(label);
		Scene scene = new Scene(root, 300, 200);

		primaryStage.setTitle("Medical Program!");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}