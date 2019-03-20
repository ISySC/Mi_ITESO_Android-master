package mx.iteso.miiteso.miiteso.classes;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC on 22/08/2018.
 */

public class ScheduleDay {
    private String nombreDia;
    private ArrayList<Subject> subjects = new ArrayList<>();
    protected final int MAX_SIZE = 16, HORA_INICIO = 7, HORA_FIN = 22;

    public ArrayList<Subject> getclases() {
        return subjects;
    }

    public ScheduleDay(String dayName) {
        this.nombreDia = dayName;
        if (subjects.size() < MAX_SIZE)
            for (int i = 7; i < MAX_SIZE + 7; i++) {
                Subject subject = new Subject(i, i + 1, 0, "", "", "", "", "");
                subjects.add(subject);
            }
    }

    public String getNombreDia() {
        return nombreDia;
    }

    public void addSubject(Subject subject) {

        try {
            if (subjects.size() <= MAX_SIZE) {
                if (subject.getHoraFin() - subject.getHoraInicio() > 1) {

                    int incio = subject.getHoraInicio(), fin = subject.getHoraFin();
                    for (int i = 0; i < fin - incio; i++) {
                        Subject cls = new Subject(subject.getHoraInicio() + i, subject.getHoraInicio() + 1 + i,
                                subject.getClaseID(), subject.getGrupo(), subject.getSalon(), subject.getAsignatura()
                                , subject.getNombreDia(), subject.getHoraInicioFin());
                        //cls.setHoraInicio(cls.getHoraInicio()+ i);
                        //cls.setHoraFin(cls.getHoraFin()+i);
                        //this.subjects.set(this.subjects.indexOf(getSubject(cls.getHoraInicio())),cls);
                        //getSubject(cls.getHoraInicio());
                        for (int o = 0; o < this.subjects.size(); o++) {
                            if (this.subjects.get(o).getHoraInicio() == cls.getHoraInicio()) {
                                this.subjects.set(o, cls);

                            }

                        }
                    }
                } else {

                    if (subject.getHoraInicio() >= 9 && subject.getHoraInicio() < 23)
                        this.subjects.set(this.subjects.indexOf(getSubject(subject.getHoraInicio())), subject);

                /*for (int o = 0; 0 < this.subjects.size();o++)
                {
                    if (this.subjects.get(o).getHoraInicio() == subject.getHoraInicio() && this.subjects.get(o).getHoraFin() == subject.getHoraFin())
                        this.subjects.set(o,subject);

                }*/
                }
            }
        } catch (Exception ex) {
        }
    }

    public void setNombreDia(String nombreDia) {
        this.nombreDia = nombreDia;
    }

    public Subject getSubject(int HoraInicio) {
        Subject subject = new Subject();
        for (int o = 0; o < this.subjects.size(); o++) {
            if (this.subjects.get(o).getHoraInicio() == HoraInicio)
                subject = this.subjects.get(o);
        }
        return subject;
    }

}
