package Manager;

import generated.*;


import java.util.*;

public class UserData {

    private int days;
    private int hours;
    private List<Teacher> teachers;
    private List<Subject> subjects;
    private List<Cls> cls;
    private List<Rule> rules;
    protected int hardRulesWeight;

    public UserData()
    {
        teachers = new ArrayList<>();
        subjects = new ArrayList<>();
        cls = new ArrayList<>();
        rules = new ArrayList<>();
    }

    //teachers
    public List<Teacher> getTeachers() { return this.teachers; }
    //subjects
    public List<Subject> getSubjects() { return this.subjects; }
    //classes
    public List<Cls> getClasses() { return this.cls; }
    //rules
    public List<Rule> getRules() { return this.rules; }


    //hard rules weight
    public int getHardRulesWeight() {
        return hardRulesWeight;
    }
    public void setHardRulesWeight(int value) {
        this.hardRulesWeight = value;
    }

    //hours
    public int getHours() { return hours; }
    public void setHours(int value) { this.hours = value; }

    //days
    public int getDays() { return days; }
    public void setDays(int value) { this.days = value; }

    public boolean initUserData(ETTDescriptor generatedEtt){

        this.setDays(generatedEtt.getETTTimeTable().getDays());
        this.setHours(generatedEtt.getETTTimeTable().getHours());
        this.setHardRulesWeight(generatedEtt.getETTTimeTable().getETTRules().getHardRulesWeight());
        ETTTeachers tempTeachers = generatedEtt.getETTTimeTable().getETTTeachers();
        ETTSubjects tempSubjects = generatedEtt.getETTTimeTable().getETTSubjects();
        ETTClasses tempClasses = generatedEtt.getETTTimeTable().getETTClasses();
        ETTRules tempRules = generatedEtt.getETTTimeTable().getETTRules();

        final Set<String> setToReturn = new HashSet<>();

        List<Integer> list = new ArrayList<>();
        //checkers of xml file

        for(int i = 0; i < tempClasses.getETTClass().size(); i++){
            list.add(tempClasses.getETTClass().get(i).getId());
        }
        Collections.sort(list);
        if(!validateList(list)){return false;}
        list.clear();

        for(int i = 0; i < tempTeachers.getETTTeacher().size(); i++){
             list.add(tempTeachers.getETTTeacher().get(i).getId());
        }
        Collections.sort(list);
        if(!validateList(list)){return false;}
        list.clear();

        for(int i = 0; i < tempSubjects.getETTSubject().size(); i++){
             list.add(tempSubjects.getETTSubject().get(i).getId());
        }
        Collections.sort(list);
        if(!validateList(list)){return false;}
        list.clear();

        for(ETTRule temp: tempRules.getETTRule()) {
           if(!setToReturn.add(temp.getETTRuleId())){
                return false;
           }
        }

        for(ETTTeacher temp:tempTeachers.getETTTeacher())
        {
           for(int i = 0 ; i<temp.getETTTeaching().getETTTeaches().size(); i++){
               for(int j = 0 ; j < tempSubjects.getETTSubject().size();j++){
                   if(temp.getETTTeaching().getETTTeaches().get(i).getSubjectId()
                           == tempSubjects.getETTSubject().get(j).getId()){
                       break;
                   }else{
                       if( j == tempSubjects.getETTSubject().size()-1){
                           return false;
                       }
                   }
               }
           }
        }

        for(ETTClass tempCls :tempClasses.getETTClass())
        {
            for(int i = 0 ; i<tempCls.getETTRequirements().getETTStudy().size(); i++){
                for(int j = 0 ; j < tempSubjects.getETTSubject().size();j++){
                    if(tempCls.getETTRequirements().getETTStudy().get(i).getSubjectId()
                            == tempSubjects.getETTSubject().get(j).getId()){
                        break;
                    }else{
                        if( j == tempSubjects.getETTSubject().size()-1){
                            return false;
                        }
                    }
                }
            }
        }

        //subjects copy
        for(ETTSubject temp:tempSubjects.getETTSubject())
        {
            this.subjects.add(new Subject(temp));
        }

        //teachers copy
        for(ETTTeacher temp:tempTeachers.getETTTeacher())
        {
            this.teachers.add(new Teacher(temp));
        }

        //classes copy
        for(ETTClass temp: tempClasses.getETTClass())
        {
            this.cls.add(new Cls(temp));
        }

        //rules copy
        for(ETTRule temp: tempRules.getETTRule()){
            this.rules.add(new Rule(temp));
        }
        return true;
    }

    public boolean validateList(List<Integer> list){
        for (int i = 0 ;i<list.size()-1;i++){
            if((list.get(i)+1) != list.get(i+1)){
                return false;
            }
        }
        return true;
    }

}
