package Manager;

import generated.ETTMutation;


import java.util.Random;

public class Mutation {

    protected double probability;
    private String name;
    private String configuration;
    private int maxTupples;
    private String component;

    Random rand = new Random();

    public Mutation(){}
    public Mutation(ETTMutation mutation){
        this.name = mutation.getName();
        this.configuration = mutation.getConfiguration();
        this.component = configuration.substring(configuration.length() - 1);
        this.maxTupples = Integer.parseInt(configuration.replaceAll("[^0-9]", ""));
        this.probability = mutation.getProbability();
    }

    public double getProbability() {
        return probability;
    }
    public void setProbability(double value) {
        this.probability = value;
    }
    public void setMaxTupples(int tupples){this.maxTupples = tupples;}
    public void setComponent(String component) {this.component = component;}
    public String getName() {
        return name;
    }
    public void setName(String value) {
        this.name = value;
    }
    public String getConfiguration() {
        return configuration;
    }
    public void setConfiguration(String value) {
        this.configuration = value;
    }

    public void chooseMutation(Solution solution,int req){
        if(name.equals("Flipping")){
            flipping(solution);
        }else if(name.equals("Sizer")){
            sizer(solution , req);
        }
    }
    public void flipping(Solution solution){
        int tuples = rand.nextInt(maxTupples) + 1;
        int randomFive;


        for(int i = 0; i<tuples;i++){
            randomFive = rand.nextInt(solution.getSolution().size());

            if(component.equals("D")){
               solution.getSolution().get(randomFive).setDay(solution.dayRandomizer());
            }else if(component.equals("H")){
                solution.getSolution().get(randomFive).setHour(solution.hourRandomizer());
            }else if(component.equals("C")){
                solution.getSolution().get(randomFive).setCls(solution.classRandomizer());
            }else if(component.equals("T")){
                solution.getSolution().get(randomFive).setTeacher(solution.teacherRandomizer());
            }else{
                solution.getSolution().get(randomFive).setSubject(solution.subjectRandomizer());
            }
        }
    }

    //there is still no explanation on who choose "decider" value neg or pos, so i have randomized it for now.
    public void sizer(Solution solution , int decider){

        int randomFive;
        if(decider >= 0) {
            solution.createFive(maxTupples);
        }else{
            int fivesAmount = rand.nextInt(maxTupples) + 1;
            int minimum = solution.getUd().getDays();
            for(int i = 0; i<fivesAmount; i++){
                randomFive = rand.nextInt(solution.getSolution().size());
                if(minimum < solution.getSolution().size()){
                    solution.getSolution().remove(randomFive);
                }
            }
        }
    }

}
