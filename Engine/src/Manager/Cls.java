package Manager;


import generated.ETTClass;
import generated.ETTStudy;

import java.util.HashMap;
import java.util.Map;

public class Cls {


    private String name;
    private HashMap<Integer,Integer> requirements = new HashMap<>(); //<sub-id,hours>
    private int id;

    public Cls(ETTClass cls){
        name = cls.getETTName();
        id = cls.getId();

        for(ETTStudy temp : cls.getETTRequirements().getETTStudy()){
            requirements.put(temp.getSubjectId(),temp.getHours());
        }

    }

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public HashMap<Integer,Integer> getRequirements() {
        return requirements;
    }

    public void setETTRequirements(int subject_id ,int hours) { this.requirements.put(subject_id,hours); }

    public String printReqs(){
        String temp = "[";
        for(Map.Entry<Integer,Integer> entry : requirements.entrySet()){
            temp = temp + "(" + entry.getKey() + " , "+entry.getValue() + ")";
        }
        temp += "]";
        return temp;
    }

    //id
    public int getId() { return id; }
    public void setId(int value) {
        this.id = value;
    }

    public String toString() {
        return "[Class Name: " + name + " | id: "+ id  + " | subjects [id,hours] :" + printReqs();
    }

}
