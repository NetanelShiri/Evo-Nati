package Manager;


import generated.ETTRule;

import java.util.*;

public class Rule {

    enum RuleType {
        TeacherIsHuman,Singularity,Satisfactory,Knowledgeable,DayOffTeacher,
        Sequentiality,DayOffClass,WorkingHoursPreference
    }

    private String ruleId;
    private String type;
    private String configuration;
    private int conf;
    private final RuleType ruleType;

    public Rule(ETTRule rule){
        this.ruleId = rule.getETTRuleId();
        this.type = rule.getType();
        this.configuration = rule.getETTConfiguration();
        if(configuration!= null) {
            this.conf = Integer.parseInt(configuration.replaceAll("[^0-9]", ""));
        }
        this.ruleType = RuleType.valueOf(ruleId);
    }
    public String getConfiguration() {
        return configuration;
    }

    public void setConfiguration(String value) {
        this.configuration = value;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String value) {
        this.ruleId = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String value) {
        this.type = value;
    }

    public String toString(){
        return "[Rule Type: "+ type + " | Rule Id: "+ruleId +"]";
    }


    public Map<String,Double> calculateRule(Solution solution){
        Map<String,Double> results = new HashMap<>();
        switch(ruleType){
            case TeacherIsHuman:
                results.put(ruleId,teacherIsHuman(solution));
                break;
            case Singularity:
                results.put(ruleId,singularity(solution));
                break;
            case Satisfactory:
                results.put(ruleId,satisfactory(solution));
                break;
            case DayOffTeacher:
                results.put(ruleId,dayOffTeacher(solution));
                break;
            case Knowledgeable:
                results.put(ruleId,knowledgeable(solution));
                break;
            case Sequentiality:
                results.put(ruleId,sequentiality(solution));
                break;
            case DayOffClass:
                break;
            case WorkingHoursPreference:
                break;
            default:
                results.put(ruleId,0.0);
        }
       return results;
    }

    public double calculateAverage(String type, List<Rule> rules , Map<String,Double> scores){
        double amount = 0;
        double  score = 0;

        for(int i =0;i<rules.size();i++){
            if(rules.get(i).getType().equals(type)){
                score += scores.get(rules.get(i).ruleId);
                amount++;
            }
        }

        score = score / amount;
        return score;
    }

    public double teacherIsHuman(Solution solution){

        int duplicates = 0;
        double percentage;
        List<Container> tempSol = solution.getSolution();
        Container temp1,temp2;
        for(int i = 0;i<tempSol.size()-1;i++){
           temp1 = tempSol.get(i);
           temp2 = tempSol.get(i+1);
           //comparing day+hour+teacherID
           if((temp1.getDay() == temp2.getDay())
                   && (temp1.getHour() == temp2.getHour())
                   && (temp1.getTeacherId() == temp2.getTeacherId() )){
                    duplicates++;
           }
        }
        percentage = 100 - ((duplicates/(double)solution.getSolution().size()) * 100.0);
        return percentage;
    }

    public double sequentiality(Solution solution){
        List<Container> fives = solution.getSolution();

        double sumFaults = 0;
        double percentage;
        int currDay;
        int currHour;
        int currLen = 0;
        int maxLen = 0;
        int[][] dhSeq = new int[solution.getUd().getDays()+1][solution.getUd().getHours()+1];
        int subsFaulty = 0;
        for (int[] row: dhSeq)
            Arrays.fill(row, 0);

        for(Subject subject : solution.getUd().getSubjects()) {
            //scanning all subjects occurrences
            for (int i = 0; i < solution.getSolution().size(); i++) {
                if(solution.getSolution().get(i).getSubjectId() == subject.getId()){
                    currDay = solution.getSolution().get(i).getDay();
                    currHour = solution.getSolution().get(i).getHour();
                    dhSeq[currDay][currHour]++;
                }
            }
            for (int d = 1;d <= solution.getUd().getDays(); d++)
            {
                for(int h = 1;h <=solution.getUd().getHours(); h++){

                    //getting the max sequence length
                    if (dhSeq[d][h] > 0) {
                        currLen++;
                    }
                    else {
                        currLen = 0;
                    }
                    if (currLen > maxLen)
                    {
                        maxLen = currLen;
                    }
                }
                if(maxLen > conf){
                    subsFaulty++;
                }
               maxLen=0;
                currLen = 0;
            }
            //now for each subject we get the amount of faulty days.
            //we come up with a formula that calculate : foreach sub -> += (faultydays/maxdays) /size of subs
            sumFaults += ((double)subsFaulty/(double)solution.getUd().getDays());
            subsFaulty = 0;
            for (int[] row: dhSeq)
                Arrays.fill(row, 0);
        }

        percentage =  100 - ((sumFaults)*100)/((double)solution.getUd().getSubjects().size());
        return percentage;
    }

    public double satisfactory(Solution solution){


        double finalPercentage = 0;
        double currClassPercentage = 0;
        double tooManyHours = 0;
        double tempClassReq;
        List<Cls> cls = solution.getUd().getClasses();

        int currHours;
        int currSubId;
        int currClsId;
        for(int i = 0 ; i<cls.size(); i++){
            currClsId = cls.get(i).getId();
            for (Map.Entry<Integer, Integer> entry : cls.get(i).getRequirements().entrySet()) {
                currSubId = entry.getKey();
                currHours = entry.getValue();

                for(int j = 0 ; j< solution.getSolution().size(); j++){
                    if(solution.getSolution().get(j).getClsId() == currClsId &&
                       solution.getSolution().get(j).getSubjectId() == currSubId) {

                        --currHours;
                        if(currHours < 0){
                            ++currHours;
                            tooManyHours++;
                        }
                    }
                }
                //we want closest to requested.
                tempClassReq = (double) entry.getValue();
                tempClassReq = (tooManyHours > tempClassReq) ? tempClassReq/tooManyHours : tooManyHours/tempClassReq;
                currClassPercentage += (tempClassReq * 100);
            }
            finalPercentage += (currClassPercentage/cls.get(i).getRequirements().size());
            currClassPercentage = 0;
            tooManyHours = 0;
        }
        finalPercentage = (finalPercentage/(double)cls.size());
        return finalPercentage;
    }

    public double dayOffTeacher(Solution solution){
        int teachersAmount = solution.getUd().getTeachers().size();
        int validTeachers = 0;
        int maxDays = solution.getUd().getDays();
        int currDay;
        double percentage;
        int[] workDays = new int[maxDays];
        Arrays.fill(workDays,0);


        for(Teacher teacher: solution.getUd().getTeachers()) {
            for (int i = 0; i < solution.getSolution().size(); i++) {
                   if(solution.getSolution().get(i).getTeacher().getId() == teacher.getId()){
                       currDay = solution.getSolution().get(i).getDay();
                       workDays[currDay-1]++;
                   }
            }
            for(int wd : workDays){
                if(wd == 0) {
                    validTeachers++;
                    break;
                }
            }
            Arrays.fill(workDays,0);
        }

        percentage = ((double)validTeachers/(double)teachersAmount) * 100.0;
        return percentage;
    }

    public double singularity(Solution solution){
        double percentage;
        double duplicates = 0;
        List<Container> tempSol = solution.getSolution();
        Container temp1,temp2;
        for(int i = 0;i<tempSol.size()-1;i++){
            temp1 = tempSol.get(i);
            temp2 = tempSol.get(i+1);
            //comparing day+hour+classID
            if(temp1.getDay() == temp2.getDay()
                    && temp1.getHour() == temp2.getHour() && temp1.getClsId() == temp2.getClsId()){
                duplicates++;
            }
        }
        percentage = 100 - ((duplicates/(double)solution.getSolution().size()) * 100.0);
        return percentage;
    }

    public double knowledgeable(Solution solution) {
        double percentage;
        double lackOfKnowledge = 0;
        List<Container> tempSol = solution.getSolution();
        int subjectID;
        for(int i = 0;i<tempSol.size()-1;i++) {
            subjectID = tempSol.get(i).getSubjectId();
            if(!tempSol.get(i).getTeacher().getSubjects().contains(subjectID)){
                lackOfKnowledge++;
            }
        }
        percentage = 100 - ((lackOfKnowledge/(double)solution.getSolution().size()) * 100.0);
        return percentage;
    }

}
