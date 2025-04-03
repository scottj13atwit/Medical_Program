package MedicalSoftware;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.geometry.*;

public class MedicalSoftwareGUI extends Application {
	private TextField createTextField() {
		TextField textField = new TextField();
		textField.setPromptText("Enter text here...");
		textField.setStyle("-fx-font-size: 14px; -fx-padding: 5px;");
		return textField;
	}

	@Override

	public void start(Stage primaryStage) {
		//Patient p1 = new Patient("Male", "927371", "Owen", "Jones", 25, "4'3", "500");
//		StackPane stackpane = new StackPane();
		TextField fName = createTextField();
		fName.setPromptText("First Name: ");
		TextField lName = createTextField();
		lName.setPromptText("Last Name: ");
		TextField ID = createTextField();
		ID.setPromptText("Patient ID: ");
		TextField age = createTextField();
		age.setPromptText("Age: ");
		TextField sex = createTextField();
		sex.setPromptText("Sex: ");
		TextField Height = createTextField();
		Height.setPromptText("Height: ");
		TextField Weight = createTextField();
		Weight.setPromptText("Weight: ");
		Button submitButton = new Button("Submit");
		
		VBox root = new VBox(10, fName, lName, ID, age, sex, Height, Weight, submitButton);
		
		Scene scene = new Scene(root, 300, 200);

		primaryStage.setTitle("Medical Program!");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}