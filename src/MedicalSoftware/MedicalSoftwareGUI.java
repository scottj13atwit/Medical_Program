package MedicalSoftware;

import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.Modality;

public class MedicalSoftwareGUI extends Application {
	// A list to store all patients created during the program's runtime
	private List<Patient> allPatients = new ArrayList<>();

	// Helper method to create styled text fields
	private TextField createTextField(String promptText) {
		TextField textField = new TextField();
		textField.setPromptText(promptText);
		textField.setStyle("-fx-font-size: 14px; -fx-padding: 5px;");
		return textField;
	}

	// Method to show error messages in a popup window
	private void showError(String message) {
		Stage errorStage = new Stage();
		errorStage.initModality(Modality.APPLICATION_MODAL); // Makes the window modal
		errorStage.setTitle("Input Error");

		VBox vbox = new VBox(10);
		vbox.setPadding(new Insets(15));
		vbox.setAlignment(Pos.CENTER);

		Label errorLabel = new Label(message);
		Button okButton = new Button("OK");
		okButton.setOnAction(e -> errorStage.close());

		vbox.getChildren().addAll(errorLabel, okButton);

		Scene scene = new Scene(vbox, 300, 100);
		errorStage.setScene(scene);
		errorStage.showAndWait(); // Shows the window and waits for it to be closed
	}

	@Override
	public void start(Stage primaryStage) {
		// --- Input fields for patient details ---
		TextField fName = createTextField("First Name");
		TextField lName = createTextField("Last Name");
		TextField ID = createTextField("Patient ID (6 digits)");
		TextField age = createTextField("Age");
		TextField sex = createTextField("Sex (male/female)");
		TextField height = createTextField("Height");
		TextField weight = createTextField("Weight");
		Button submitButton = new Button("Submit");

		// --- Left panel for entering patient data ---
		VBox leftPanel = new VBox(10, fName, lName, ID, age, sex, height, weight, submitButton);
		leftPanel.setAlignment(Pos.TOP_CENTER);
		leftPanel.setStyle("-fx-background-color: #abbbd4; -fx-padding: 10px;");

		// --- Right panel setup with search bar and grid for patients ---
		TextField searchBar = createTextField("Search by Name or ID");

		// Grid to hold patient buttons in rows and columns
		GridPane patientGrid = new GridPane();
		patientGrid.setHgap(10);
		patientGrid.setVgap(10);
		patientGrid.setPadding(new Insets(10));

		// Optional: Wrap grid in a scroll pane
		ScrollPane scrollPane = new ScrollPane(patientGrid);
		scrollPane.setFitToWidth(true);
		scrollPane.setStyle("-fx-background: #abbbd4;");

		// Right panel contains the search bar and scrollable grid
		VBox rightPanel = new VBox(10, searchBar, scrollPane);
		rightPanel.setAlignment(Pos.TOP_LEFT);
		rightPanel.setStyle("-fx-background-color: #abbbd4; -fx-padding: 10px;");
		VBox.setVgrow(scrollPane, Priority.ALWAYS);

		// --- Submit button logic ---
		submitButton.setOnAction(e -> {
			try {
				// Get input from fields
				String first = fName.getText().trim();
				String last = lName.getText().trim();
				String id = ID.getText().trim();
				String patientSex = sex.getText().trim().toLowerCase();

				// Validate ID (must be 6 digits)
				if (!id.matches("\\d{6}")) {
					showError("Patient ID must be exactly 6 digits!");
					return;
				}

				// Validate sex (must be male or female)
				if (!patientSex.equals("male") && !patientSex.equals("female")) {
					showError("Sex must be either 'male' or 'female'");
					return;
				}

				int patientAge = Integer.parseInt(age.getText().trim());
				String patientHeight = height.getText().trim();
				String patientWeight = weight.getText().trim();

				// Create a new Patient object
				Patient newPatient = new Patient(patientSex, id, first, last, patientAge, patientHeight, patientWeight);
				allPatients.add(newPatient); // Add patient to the list

				// Create a button for the new patient
				Button patientButton = new Button(newPatient.getName());
				patientButton.setMaxWidth(Double.MAX_VALUE);
				patientButton.setStyle("-fx-font-size: 14px; -fx-padding: 10px;");
				patientButton.setOnAction(ev -> openPatientDetails(newPatient));

				// Add the button to the grid
				int count = patientGrid.getChildren().size();
				int columns = 3;
				int row = count / columns;
				int col = count % columns;
				patientGrid.add(patientButton, col, row);

				// Clear the input fields after submission
				fName.clear();
				lName.clear();
				ID.clear();
				age.clear();
				sex.clear();
				height.clear();
				weight.clear();
			} catch (NumberFormatException ex) {
				showError("Age must be a valid number!");
			} catch (Exception ex) {
				showError("Error: " + ex.getMessage());
			}
		});

		// --- Search bar logic ---
		searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
			patientGrid.getChildren().clear(); // Clear the grid only
			for (Patient patient : allPatients) {
				if (patient.getName().toLowerCase().contains(newValue.toLowerCase())
						|| patient.getId().contains(newValue)) {
					Button patientButton = new Button(patient.getName());
					patientButton.setMaxWidth(Double.MAX_VALUE);
					patientButton.setStyle("-fx-font-size: 14px; -fx-padding: 10px;");
					patientButton.setOnAction(e -> openPatientDetails(patient));
					int count = patientGrid.getChildren().size();
					int columns = 3;
					int row = count / columns;
					int col = count % columns;
					patientGrid.add(patientButton, col, row);
				}
			}
		});

		// --- Layout and scene setup ---
		SplitPane splitPane = new SplitPane();
		splitPane.getItems().addAll(leftPanel, rightPanel);
		splitPane.setDividerPositions(0.30); // Optional: sets initial divider (30% for left)

		splitPane.setStyle("-fx-background-color: #ffffff;");

		HBox.setHgrow(leftPanel, Priority.ALWAYS);
		HBox.setHgrow(rightPanel, Priority.ALWAYS);
		leftPanel.setMaxWidth(Double.MAX_VALUE);
		rightPanel.setMaxWidth(Double.MAX_VALUE);
		leftPanel.setPrefWidth(0);
		rightPanel.setPrefWidth(0);

		Scene scene = new Scene(splitPane, 800, 500);
		primaryStage.setTitle("Medical Program");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	// Method to open a new window showing patient details
	private void openPatientDetails(Patient patient) {
		Stage detailStage = new Stage(); // New window
		detailStage.setTitle(patient.getName() + " - Patient Details");

		TabPane tabPane = new TabPane(); // Create a TabPane for organizing data
		tabPane.setStyle("-fx-background-color: #abbbd4;");

		// --- Info Tab ---
		VBox infoBox = new VBox(10);
		infoBox.setPadding(new Insets(10));
		infoBox.getChildren().addAll(new Label("Name: " + patient.getName()), new Label("ID: " + patient.getId()),
				new Label("Sex: " + patient.getSex()), new Label("Age: " + patient.getAge()),
				new Label("Height: " + patient.getHeight()), new Label("Weight: " + patient.getWeight()));
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

		// Add tabs to the TabPane
		tabPane.getTabs().addAll(infoTab, medicationTab, injuryTab);

		// Set the scene for the new stage
		Scene detailScene = new Scene(tabPane, 400, 300);
		detailStage.setScene(detailScene);
		detailStage.show();
	}

	public static void main(String[] args) {
		launch(args); // Launch the JavaFX application
	}
}