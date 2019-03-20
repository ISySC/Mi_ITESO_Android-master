package mx.iteso.miiteso.miiteso.model;

public class ItemSubject {

    String name, teacher,language,comment;
    int credits;
    public ItemSubject(String name,String teacher,String language,String comment,int credits)
    {
        this.name=name;
        this.teacher=teacher;
        this.language=language;
        this.comment=comment;
        this.credits = credits;
    }

    public int getCredits() {
        return credits;
    }

    public String getName() {
        return name;
    }

    public String getComment() {
        return comment;
    }

    public String getLanguage() {
        return language;
    }

    public String getTeacher() {
        return teacher;
    }
}
