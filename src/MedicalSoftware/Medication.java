package MedicalSoftware;

import java.util.Date;

public class Medication {
    private String name;
    private String dosage;
    private Date startDate;
    private Date endDate;
    
    
    public Medication(String name, String dosage, Date startDate, Date endDate) {
        this.name = name;
        this.dosage = dosage;
        this.startDate = startDate;
        this.endDate = endDate;
    }
    
    public Date getStartDate() {
        return startDate;
    }
    
    public Date getEndDate() {
        return endDate;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDosage() {
        return dosage;
    }
    
    public boolean isActive(Date currentDate) {
        return !currentDate.before(startDate) && 
               (endDate == null || !currentDate.after(endDate));
    }
}