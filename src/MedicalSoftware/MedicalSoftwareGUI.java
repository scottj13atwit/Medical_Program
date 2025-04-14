package MedicalSoftware;

/*
 * 	 TEAM 9
 * ------------
 * Jacob Scott
 * Owen Jones
 * Aliya Vagapova
 * 
 */
// Here are all the classes and packages we needed to import from both java and javafx
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.Modality;
import java.util.*;

public class MedicalSoftwareGUI extends Application {
	
	// We created a list to store all patients created during the program's runtime
	private List<Patient> allPatients = new ArrayList<>();

	// Set to track used IDs to ensure uniqueness
	private Set<String> usedIDs = new HashSet<>();

	// Random number generator for ID creation
	private Random random = new Random();

	// Helper method to generate unique 6-digit IDs
	private String generateUniqueID() {
		String newID;
		do {
			// Generate a random 6 digit number
			newID = String.valueOf(100000 + random.nextInt(900000));
		} while (usedIDs.contains(newID));

		// Add the new ID to our UsedID set to make sure it isnt used again
		usedIDs.add(newID);
		return newID;
	}

	// Method to make more uniform text fields
	private TextField createTextField(String promptText) {
		TextField textField = new TextField();
		textField.setPromptText(promptText);
		textField.setStyle("-fx-font-size: 14px; -fx-padding: 5px;");
		return textField;
	}

	// Method to show error messages in a new window
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

	// Matches injuries with their prescribed medications
	private List<Injury> initializeCommonInjuries() {
		List<Injury> injuries = new ArrayList<>();
		Date now = new Date();
		Calendar cal = Calendar.getInstance();

		// Sprained Ankle injury with medications
		Injury ankle = new Injury("I001", "Sprained Ankle", 3);
		cal.setTime(now);
		cal.add(Calendar.DAY_OF_YEAR, 14);
		ankle.prescribeMedication(new Medication("Ibuprofen", "400mg every 6 hours", now, cal.getTime()));
		ankle.prescribeMedication(new Medication("Acetaminophen", "500mg every 8 hours", now, cal.getTime()));

		// Broken Arm injury with medications
		Injury arm = new Injury("I002", "Broken Arm", 5);
		cal.setTime(now);
		cal.add(Calendar.DAY_OF_YEAR, 21);
		arm.prescribeMedication(new Medication("Acetaminophen", "500mg every 6 hours", now, cal.getTime()));
		arm.prescribeMedication(new Medication("Naproxen", "250mg every 12 hours", now, cal.getTime()));

		// Concussion injury with medications
		Injury concussion = new Injury("I003", "Concussion", 4);
		cal.setTime(now);
		cal.add(Calendar.DAY_OF_YEAR, 7);
		concussion.prescribeMedication(new Medication("Acetaminophen", "500mg every 8 hours", now, cal.getTime()));

		injuries.add(ankle);
		injuries.add(arm);
		injuries.add(concussion);
		return injuries;
	}

	// Method to open a new window showing patient details when patient is clicked
	private void openPatientDetails(Patient patient) {
		Stage detailStage = new Stage();
		detailStage.setTitle(patient.getName() + " - Patient Details");

		TabPane tabPane = new TabPane();
		tabPane.setStyle("-fx-background-color: #abbbd4;");

		// Creates info tab
		VBox infoBox = new VBox(10);
		infoBox.setPadding(new Insets(10));
		infoBox.setStyle("-fx-background-color: #abbbd4;");

		// Inputs the created strings from Patient Class to the label
		Label nameLabel = createInfoLabel("Name: " + patient.getName());
		Label idLabel = createInfoLabel("ID: " + patient.getId());
		Label sexLabel = createInfoLabel("Sex: " + patient.getSex());
		Label ageLabel = createInfoLabel("Age: " + patient.getAge());
		Label heightLabel = createInfoLabel("Height: " + patient.getHeight());
		Label weightLabel = createInfoLabel("Weight: " + patient.getWeight());

		// Adds labels
		infoBox.getChildren().addAll(nameLabel, idLabel, sexLabel, ageLabel, heightLabel, weightLabel);
		Tab infoTab = new Tab("Info", infoBox);
		infoTab.setClosable(false);

		// Injury Tab
		VBox injuryBox = new VBox(10);
		injuryBox.setPadding(new Insets(10));
		injuryBox.setStyle("-fx-background-color: #abbbd4;");

		// Get list of common injuries with their medications
		List<Injury> commonInjuries = initializeCommonInjuries();

		// Dropdown for injury selection
		ComboBox<Injury> injuryDropdown = new ComboBox<>();
		injuryDropdown.setItems(FXCollections.observableArrayList(commonInjuries));
		injuryDropdown.setStyle("-fx-font-size: 14px; -fx-background-color: #e6e6e6;");
		injuryDropdown.setCellFactory(param -> new ListCell<Injury>() {
			@Override
			protected void updateItem(Injury item, boolean empty) {
				super.updateItem(item, empty);
				setText(item == null ? null : item.getDescription());
			}
		});
		injuryDropdown.setButtonCell(new ListCell<Injury>() {
			@Override
			protected void updateItem(Injury item, boolean empty) {
				super.updateItem(item, empty);
				setText(item == null ? "Select Injury" : item.getDescription());
			}
		});

		// List displaying medications for selected injury
		ListView<Medication> medicationListView = new ListView<>();
		medicationListView.setStyle("-fx-background-color: #e6e6e6; -fx-font-size: 14px;");
		medicationListView.setCellFactory(param -> new ListCell<Medication>() {
			@Override
			protected void updateItem(Medication item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
				} else {
					setText(String.format("%s - %s\nFrom: %tD to: %s", item.getName(), item.getDosage(),
							item.getStartDate(),
							item.getEndDate() != null ? String.format("%tD", item.getEndDate()) : "Ongoing"));
				}
			}
		});

		// Updates medications when injury is selected
		injuryDropdown.valueProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal != null) {
				medicationListView.getItems().setAll(newVal.getPrescribedMedications());
			}
		});

		// Labels for injury tab
		Label selectInjuryLabel = createInfoLabel("Select Injury:");
		Label prescribedMedsLabel = createInfoLabel("Prescribed Medications:");

		injuryBox.getChildren().addAll(selectInjuryLabel, injuryDropdown, prescribedMedsLabel, medicationListView);
		Tab injuryTab = new Tab("Injury", injuryBox);
		injuryTab.setClosable(false);

		// Medication Tab
		VBox medicationBox = new VBox(10);
		medicationBox.setPadding(new Insets(10));
		medicationBox.setStyle("-fx-background-color: #abbbd4;");

		// List displaying all medications from all injuries
		ListView<Medication> allMedsView = new ListView<>();
		allMedsView.setStyle("-fx-background-color: #e6e6e6; -fx-font-size: 14px;");
		allMedsView.setCellFactory(medicationListView.getCellFactory());

		// Collect all medications from all injuries
		List<Medication> allMeds = new ArrayList<>();
		commonInjuries.forEach(injury -> allMeds.addAll(injury.getPrescribedMedications()));
		allMedsView.getItems().setAll(allMeds);

		// Label for medication tab
		Label allMedsLabel = createInfoLabel("All Available Medications:");

		medicationBox.getChildren().addAll(allMedsLabel, allMedsView);
		Tab medicationTab = new Tab("Medication", medicationBox);
		medicationTab.setClosable(false);

		tabPane.getTabs().addAll(infoTab, injuryTab, medicationTab);
		Scene detailScene = new Scene(tabPane, 600, 450);
		detailStage.setScene(detailScene);
		detailStage.show();
	}

	// Method to create info labels
	private Label createInfoLabel(String text) {
		Label label = new Label(text);
		label.setStyle("-fx-font-size: 14px; -fx-text-fill: #333333; -fx-padding: 5px;");
		return label;
	}

	@Override
	public void start(Stage primaryStage) {
		
		// Allows user to input patient data
		TextField fName = createTextField("First Name");
		TextField lName = createTextField("Last Name");
		TextField age = createTextField("Age");
		TextField sex = createTextField("Sex (male/female)");
		TextField height = createTextField("Height");
		TextField weight = createTextField("Weight");
		Button submitButton = new Button("Submit");
		submitButton.setStyle("-fx-font-size: 14px; -fx-padding: 5px;");

		// Left panel for entering patient data
		VBox leftPanel = new VBox(10, fName, lName, age, sex, height, weight, submitButton);
		leftPanel.setAlignment(Pos.TOP_CENTER);
		leftPanel.setStyle("-fx-background-color: #abbbd4; -fx-padding: 10px;");

		// Right panel with search bar and patient list 
		TextField searchBar = createTextField("Search by Name or ID");
		searchBar.setStyle("-fx-background-color: #e6e6e6;");

		// Create container for patient list
		VBox patientListContainer = new VBox(10);
		patientListContainer.setPadding(new Insets(10));
		patientListContainer.setStyle("-fx-background-color: #abbbd4;");

		// Create list for patients
		ListView<Patient> patientListView = new ListView<>();
		patientListView.setStyle("-fx-background-color: #abbbd4;");
		patientListView.setCellFactory(param -> new ListCell<Patient>() {
			@Override
			protected void updateItem(Patient patient, boolean empty) {
				super.updateItem(patient, empty);
				if (empty || patient == null) {
					setText(null);
					setGraphic(null);
				} else {
					// Makes each patient into a new button
					Button patientButton = new Button(patient.getName());
					patientButton.setMaxWidth(Double.MAX_VALUE);
					patientButton.setStyle("-fx-font-size: 14px; -fx-padding: 10px; -fx-background-color: #e6e6e6;");
					patientButton.setOnAction(e -> openPatientDetails(patient));
					setGraphic(patientButton);
				}
			}
		});

		// Make list fill available spaces
		VBox.setVgrow(patientListView, Priority.ALWAYS);

		// Add label for patient list
		Label patientListLabel = createInfoLabel("Patient List:");

		// Add components to patient list container
		patientListContainer.getChildren().addAll(patientListLabel, searchBar, patientListView);

		// Wrap in ScrollPane
		ScrollPane scrollPane = new ScrollPane(patientListContainer);
		scrollPane.setFitToWidth(true);
		scrollPane.setStyle("-fx-background: #abbbd4;");

		// Right panel contains the scrollable patient list
		VBox rightPanel = new VBox();
		rightPanel.getChildren().add(scrollPane);
		rightPanel.setStyle("-fx-background-color: #abbbd4;");
		VBox.setVgrow(scrollPane, Priority.ALWAYS);

		// Submit button logic
		submitButton.setOnAction(e -> {
			try {
				// Get input from all fields
				String first = fName.getText().trim();
				String last = lName.getText().trim();
				String patientSex = sex.getText().trim().toLowerCase();

				// Requires First and Last name
				if (first.isEmpty() || last.isEmpty()) {
					showError("First and last name are required!");
					return;
				}

				// Requires proper sex format
				if (!patientSex.equals("male") && !patientSex.equals("female")) {
					showError("Sex must be either 'male' or 'female'");
					return;
				}
				
				// Creates values for age, height, and weight
				int patientAge = Integer.parseInt(age.getText().trim());
				String patientHeight = height.getText().trim();
				String patientWeight = weight.getText().trim();

				// Generates a unique 6-digit ID
				String patientID = generateUniqueID();

				// Create a new Patient box
				Patient newPatient = new Patient(patientSex, patientID, first, last, patientAge, patientHeight,
						patientWeight);
				allPatients.add(newPatient);

				// Refreshes ListView with updated patients
				patientListView.setItems(FXCollections.observableArrayList(allPatients));

				// Clears the input fields after the submission
				fName.clear();
				lName.clear();
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

		// Search bar logic
		searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
			List<Patient> filteredPatients = new ArrayList<>();
			for (Patient patient : allPatients) {
				if (patient.getName().toLowerCase().contains(newValue.toLowerCase())
						|| patient.getId().contains(newValue)) {
					filteredPatients.add(patient);
				}
			}
			// Update ListView with filtered results
			patientListView.setItems(FXCollections.observableArrayList(filteredPatients));
		});

		// Layout and scene setup
		SplitPane splitPane = new SplitPane();
		splitPane.getItems().addAll(leftPanel, rightPanel);
		splitPane.setDividerPositions(0.30); // Sets the position of the slider to 30%

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

	public static void main(String[] args) {
		launch(args); // Launches the JavaFX application
	}
}