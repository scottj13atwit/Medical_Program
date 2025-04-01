package MedicalSoftware;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class Patient {
    private String sex;
    private String patientID;
    private String fName;
    private String lName;
    private int age;
    private String height;
    private String weight;
    private List<Injury> injuries;
    private List<Medication> medications;
    
    public Patient(String s, String id, String fn, String ln, int birth, String h, String w) {
        // Data validation
        if (s == null || (!s.equalsIgnoreCase("Male") && !s.equalsIgnoreCase("Female"))) {
            throw new IllegalArgumentException("Sex must be Male or Female");
        }
        if (id == null || id.length() != 6 || !id.matches("\\d+")) {
            throw new IllegalArgumentException("Patient ID must be 6 digits");
        }
        
        sex = s;
        patientID = id;
        lName = ln;
        fName = fn;
        age = birth;
        height = h;
        weight = w;
        injuries = new ArrayList<>();
        medications = new ArrayList<>();
    }
    
    public void addInjury(Injury injury) {
        injuries.add(injury);
    }
    
    public void addMedication(Medication med) {
        medications.add(med);
    }
    
    public List<Injury> getInjuries() {
        return injuries;
    }
    
    public List<Medication> getMedications() {
        return medications;
    }
    
    public String getName() {
        return lName + ", " + fName;
    }
    
    public String getPatientID() {
        return patientID;
    }
    
    public List<Medication> getMedicationsForInjury(String injuryId) {
        for (Injury injury : injuries) {
            if (injury.getInjuryID().equals(injuryId)) {
                return injury.getPrescribedMedications();
            }
        }
        return new ArrayList<>();
    }
    
    public Date getFirstMedicationStartDate(String injuryId) {
        List<Medication> meds = getMedicationsForInjury(injuryId);
        if (!meds.isEmpty()) {
            return meds.get(0).getStartDate();
        }
        return null;
    }
    
    public String toString() {
        return String.format("%s, %s%nSex: %s%nPatientID: %s%nAge: %d%n%s, %slbs", 
                lName, fName, sex, patientID, age, height, weight);
    }
}