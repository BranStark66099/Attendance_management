package main;

import javafx.beans.property.*;

public class AttendanceRecord {
    private final IntegerProperty studentID;
    private final StringProperty subjectName;
    private final StringProperty date;
    private final StringProperty status;

    public AttendanceRecord(int studentID, String subjectName, String date, String status) {
        this.studentID = new SimpleIntegerProperty(studentID);
        this.subjectName = new SimpleStringProperty(subjectName);
        this.date = new SimpleStringProperty(date);
        this.status = new SimpleStringProperty(status);
    }

    public IntegerProperty studentIDProperty() { return studentID; }
    public StringProperty subjectNameProperty() { return subjectName; }
    public StringProperty dateProperty() { return date; }
    public StringProperty statusProperty() { return status; }
}
