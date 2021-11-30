package Manager;

public class Container {


    private int day;
    private int hour;
    private Cls cls;
    private Teacher teacher;
    private Subject subject;


    Container(){ }
    Container(int day, int hour, Cls cls, Teacher teacher, Subject subject){
        this.day = day;
        this.hour = hour;
        this.cls = cls;
        this.teacher = teacher;
        this.subject = subject;
    }

    public int getDay() {
        return this.day;
    }
    public int getHour() {
        return this.hour;
    }
    public Cls getCls() { return this.cls;}
    public int getClsId() { return this.cls.getId();}
    public Teacher getTeacher() { return this.teacher;}
    public int getTeacherId() { return this.teacher.getId();}
    public String getSubjectName(){ return this.subject.getName();}
    public int getSubjectId() { return this.subject.getId();}

    //setters
    public void setClsId(int id) { this.cls.setId(id);}
    public void setSubjectName(String name){this.subject.setName(name);}
    public void setDay(int day){this.day = day;}
    public void setHour(int hour){this.hour = hour;}
    public void setCls(Cls cls){this.cls = cls;}
    public void setTeacher(Teacher teacher){this.teacher = teacher;}
    public void setSubject(Subject subject){this.subject = subject;}


    public String toString() {
        return "{" + "Day: " + day + " | Hour: " + hour + " | Class: " + cls.getName() +
                " | Teacher: " + teacher.getName()  + " | Subject: " + subject.getName() +"}";
    }
}
