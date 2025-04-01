package MedicalSoftware;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class Injury {
    private String injuryID;
    private String description;
    private int severity;
    private Date date;
    private List<Medication> prescribedMedications;
    
    public Injury(String injuryID, String description, int severity) {
        this.injuryID = injuryID;
        this.description = description;
        this.severity = severity;
        this.date = new Date();
        this.prescribedMedications = new ArrayList<>();
    }
    
    public void prescribeMedication(Medication med) {
        prescribedMedications.add(med);
    }
    
    public List<Medication> getPrescribedMedications() {
        return prescribedMedications;
    }
    
    public String getInjuryID() {
        return injuryID;
    }
    
    public String getDescription() {
        return description;
    }
}