package Manager;

import java.util.*;

public class Solution {

    private UserData ud;
    private EvoEngine evo;
    private List<Container> solution;
    private Map<String,Double> solutionRules;
    private double fitness = 0; //0-100
    private final Random rand = new Random();

    public Solution(){ }
    public Solution(UserData ud , EvoEngine evo){
         this.ud = ud;
         this.evo = evo;
         this.solution = new ArrayList<>();
         this.solutionRules = new HashMap<>();
         this.fitness = 0;
         createFive(ud.getDays() * ud.getHours() * ud.getTeachers().size() * ud.getClasses().size());
    }

    //deep copy
    public Solution(Solution sol){
        this.solution = new ArrayList<>();
        this.solution.addAll(sol.solution);

        this.solutionRules = new HashMap<>();
        this.solutionRules.putAll(sol.solutionRules);
        this.evo = sol.evo;
        this.ud = sol.ud;
        this.fitness = sol.fitness;
    }

    public List<Container> getSolution(){return this.solution;}
    public double getFitness() { return this.fitness; }
    public void setFitness(double fitness) { this.fitness = fitness;}
    public Map<String,Double> getSolutionRules(){return this.solutionRules;}
    public UserData getUd(){ return this.ud;}

    public void setSolution(List<Container> solution){
        this.solution.clear();
        this.solution = solution;
    }

    public void rulesCheck(){
        for(int i = 0 ;i< ud.getRules().size() ;i++) {

            solutionRules.putAll(ud.getRules().get(i).calculateRule(this));
        }
    }

    public void createFive(int amount){

        Container tempCon;
        int day , hour;
        Cls cls;
        Teacher teacher;
        Subject sub;

        for(int i = 0; i<amount; i++){

            day = dayRandomizer();
            hour = hourRandomizer();
            cls = classRandomizer();
            teacher = teacherRandomizer();
            sub = subjectRandomizer();

            tempCon = new Container(day,hour,cls,teacher,sub);
            solution.add(tempCon);
        }
    }

    public void calcFitness(){
        double maxFit = 100;
        double hardRules = 0;
        double softRules = 0;
        double hardRulesValue;
        double softRulesValue;
        Map<String,String> ruleVal = new HashMap<>();

        //calculating amount of hard/soft rules
        for(int i = 0; i<ud.getRules().size();i++){
            if(ud.getRules().get(i).getType().equals("Hard")){
                ruleVal.put(ud.getRules().get(i).getRuleId(),"Hard");
                hardRules++;
            }else{
                ruleVal.put(ud.getRules().get(i).getRuleId(),"Soft");
                softRules++;
            }
        }

        softRules = softRules + (hardRules * 2.333);

        softRulesValue = (maxFit / softRules);
        hardRulesValue = softRulesValue * 2.333;
        final double hardRulefin = hardRulesValue;
        final double softRulefin = softRulesValue;


        solutionRules.forEach((key,value) ->{
               if(ruleVal.get(key).equals("Hard")){
                   this.fitness += (hardRulefin * value / 100);
               }else{
                   this.fitness += (softRulefin * value / 100);
               }
        });
    }



    public String fiveFormat(int index){
            return solution.get(index).toString();
    }


    public int dayRandomizer(){
        int maxDays = ud.getDays();
        return rand.nextInt(maxDays) + 1;
    }
    public int hourRandomizer(){
        int maxHours = ud.getHours();
        return rand.nextInt(maxHours) + 1;
    }

    public Cls classRandomizer(){
        int allClasses = ud.getClasses().size();
        return ud.getClasses().get(rand.nextInt(allClasses));
    }

    public Teacher teacherRandomizer(){
        int allTeachers = ud.getTeachers().size();
        return ud.getTeachers().get(rand.nextInt(allTeachers));
    }

    public Subject subjectRandomizer(){
        int allSubjects = ud.getSubjects().size();
        return ud.getSubjects().get(rand.nextInt(allSubjects));
    }

}

