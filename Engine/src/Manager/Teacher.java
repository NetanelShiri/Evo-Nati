package Manager;



import generated.ETTTeacher;
import generated.ETTTeaches;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Teacher{
    // Instance variables
    private String name;
    private int id;
    private int workingHours;
    private Set<Integer> subjectsIds = new HashSet<>();

    //COPY
    public  Teacher(ETTTeacher teacher){
            name = teacher.getETTName();
            id = teacher.getId();
            workingHours = teacher.getETTWorkingHours();
        for(ETTTeaches temp:teacher.getETTTeaching().getETTTeaches()){
            subjectsIds.add(temp.getSubjectId());
        }
    }

    public Teacher(String name, int id , Set<Integer> subjectsId) {
        this.name = name;
        this.id = id;
        this.subjectsIds = subjectsId;
    }

    // Getters
    public String getName() {
        return name;
    }
    public int getId() {
        return id;
    }

    public Set<Integer> getSubjects() {
        return subjectsIds;
    }

    public String toString() {
        return "[Teacher Name: " + name + " | id: "+ id  + " | Subjects : " +
                Arrays.toString(subjectsIds.toArray()) +"]";
    }
}
