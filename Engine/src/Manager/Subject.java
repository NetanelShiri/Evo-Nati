package Manager;


import generated.ETTSubject;

public class Subject {



    private String name;
    private int id;

    //COPY
    public  Subject(ETTSubject subject){
        name = subject.getName();
        id = subject.getId();
    }

    public Subject(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int value) {
        this.id = value;
    }

    public String toString() {
        return "[Subject Name: " + name + " | id: "+ id + "]";
    }
}
