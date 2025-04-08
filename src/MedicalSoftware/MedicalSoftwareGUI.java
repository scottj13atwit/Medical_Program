package MedicalSoftware;


import java.util.ArrayList;
import java.util.List;

import javafx.scene.paint.Color;
import javafx.geometry.Insets;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.control.*;
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
		Patient p1 = new Patient("Male", "927371", "Owen", "Jones", 25, "4'3", "500");
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
		
		VBox leftPanel = new VBox(10, fName, lName, ID, age, sex, Height, Weight, submitButton);
		leftPanel.setAlignment(Pos.TOP_CENTER);
		leftPanel.setStyle("-fx-background-color: #abbbd4;");
		
		Button OJ = new Button(p1.getName());
		OJ.setMaxWidth(Double.MAX_VALUE); // ← this makes the button expand horizontally
		OJ.setStyle("-fx-font-size: 14px; -fx-padding: 10px;"); // optional styling

		VBox rightPanel = new VBox();
		rightPanel.setAlignment(Pos.TOP_LEFT);
		rightPanel.setStyle("-fx-background-color: #abbbd4;");
		//VBox.setVgrow(OJ, Priority.NEVER); // don't stretch vertically
//		OJ.setStyle("-fx-alignment: center-left; -fx-padding: 10px; -fx-font-size: 14px;");
		submitButton.setOnAction(e -> {
		    try {
		        // Get input from fields
		        String first = fName.getText().trim();
		        String last = lName.getText().trim();
		        String id = ID.getText().trim();
		        int patientAge = Integer.parseInt(age.getText().trim()); // make sure age is a number
		        String patientSex = sex.getText().trim();
		        String height = Height.getText().trim();
		        String weight = Weight.getText().trim();

		        // Create new patient
		        Patient newPatient = new Patient(patientSex, id, first, last, patientAge, height, weight);

		        // Create button with patient name
		        Button patientButton = new Button(newPatient.getName());
		        patientButton.setMaxWidth(Double.MAX_VALUE);
		        patientButton.setStyle("-fx-font-size: 14px; -fx-padding: 10px;");

		        // Add button to right panel
		     // Add action to open patient details in new window
		        patientButton.setOnAction(ev -> {
		            Stage detailStage = new Stage();
		            detailStage.setTitle(newPatient.getName() + " - Patient Details");

		            // Create Tabs
		            TabPane tabPane = new TabPane();
		            tabPane.setStyle("-fx-background-color: #abbbd4;");

		            // --- Info Tab ---
		            VBox infoBox = new VBox(10);
		            infoBox.setPadding(new Insets(10));
		            infoBox.getChildren().addAll(
		                new Label("Name: " + newPatient.getName()),
		                new Label("ID: " + newPatient.getId()),
		                new Label("Sex: " + newPatient.getSex()),
		                new Label("Age: " + newPatient.getAge()),
		                new Label("Height: " + newPatient.getHeight()),
		                new Label("Weight: " + newPatient.getWeight())
		            );
		            Tab infoTab = new Tab("Info", infoBox);
		            infoTab.setClosable(false);

		            // --- Medication Tab (placeholder) ---
		            VBox medicationBox = new VBox(10);
		            medicationBox.setPadding(new Insets(10));
		            medicationBox.getChildren().add(new Label("Medication records go here..."));
		            Tab medicationTab = new Tab("Medication", medicationBox);
		            medicationTab.setClosable(false);

		            // --- Injury Tab (placeholder) ---
		            VBox injuryBox = new VBox(10);
		            injuryBox.setPadding(new Insets(10));
		            injuryBox.getChildren().add(new Label("Injury records go here..."));
		            Tab injuryTab = new Tab("Injury", injuryBox);
		            injuryTab.setClosable(false);

		            // Add all tabs to the TabPane
		            tabPane.getTabs().addAll(infoTab, medicationTab, injuryTab);

		            // Set scene and show new stage
		            Scene detailScene = new Scene(tabPane, 400, 300);
		            detailStage.setScene(detailScene);
		            detailStage.show();
		        });

		        // Add button to right panel
		        rightPanel.getChildren().add(patientButton);

		        // Optionally: clear the text fields after submission
		        fName.clear();
		        lName.clear();
		        ID.clear();
		        age.clear();
		        sex.clear();
		        Height.clear();
		        Weight.clear();
		    } catch (Exception ex) {
		        // Basic error handling, you can show a popup instead if you want
		        System.out.println("Error: " + ex.getMessage());
		    }
		});

		

		
		HBox root = new HBox(leftPanel, rightPanel);
		root.setSpacing(20);
		root.setStyle("-fx-background-color: #ffffff;");
		HBox.setHgrow(leftPanel, Priority.ALWAYS);
		HBox.setHgrow(rightPanel, Priority.ALWAYS);
		
		Scene scene = new Scene(root, 800, 500);

		primaryStage.setTitle("Medical Program!");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}