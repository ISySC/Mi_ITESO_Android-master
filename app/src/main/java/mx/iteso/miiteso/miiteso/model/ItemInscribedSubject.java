package mx.iteso.miiteso.miiteso.model;

/**
 * Created by PC on 24/10/2018.
 */

public class ItemInscribedSubject {

    String name, teacher, lenguaje, details,classRoom;
    int credits;


    public ItemInscribedSubject(String name, String teacher, String lenguaje, String details, String clasRoom,int credits) {
        this.name=name;
        this.teacher=teacher;
        this.lenguaje = lenguaje;
        this.details = details;
        this.classRoom = clasRoom;
        this.credits= credits;
    }

    public String getName() {
        return name;
    }

    public String getClassRoom() {
        return classRoom;
    }

    public int getCredits() {
        return credits;
    }

    public String getDetails() {
        return details;
    }

    public String getLenguaje() {
        return lenguaje;
    }

    public String getTeacher() {
        return teacher;
    }
}
