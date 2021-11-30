package Manager;


import generated.ETTSelection;

import java.util.*;

public class Selection {

    private String type;
    private String configuration;
    private Integer elitism;
    private double arg = 0;

    Random rand = new Random();

    public Selection(){};
    public Selection(ETTSelection selection){
        this.type = selection.getType();
        this.configuration = selection.getConfiguration();
        this.elitism = selection.getETTElitism();
        if(this.elitism == null) { this.elitism = 0;}
        if(configuration!= null) {
            this.arg = Double.parseDouble(configuration.replaceAll("[^0-9]", ""));
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String value) {
        this.type = value;
    }

    public String getConfiguration() {
        return configuration;
    }

    public void setConfiguration(String value) {
        this.configuration = value;
    }

    public void setArg(double arg){this.arg = arg;}

    public double getArg() { return this.arg; }

    public Integer getElitism() {
        return this.elitism;
    }

    public void setElitism(Integer value) {
        this.elitism = value;
    }

    public List<Solution> chooseSelection(List<Solution> solutions){
        if(type.equals("Truncation")){
            return truncation(solutions);
        }else if(type.equals("RouletteWheel")){
            return rouletteWheel(solutions);
        }else{
            return tournament(solutions);
        }
    }

    public List<Solution> rouletteWheel(List<Solution> solutions)
    {
        double[] arr = new double[solutions.size()];
        double maxVal = 0;
        double randomValue;
        double currVal = 0;
        int itr = -1;
        List<Solution> chosenParents = new ArrayList<>(solutions.size());

        //array of fitness , each one will represent real estate in "wheel"
        for(int i = 0; i<solutions.size(); i++){
            arr[i] = solutions.get(i).getFitness();
            maxVal += arr[i];
        }

        for(int i = 0 ; i< solutions.size();i++) {
            randomValue = (maxVal) * rand.nextDouble();
            do{
                currVal += arr[++itr];
            }while(randomValue >= currVal);
            chosenParents.add(solutions.get(itr));
            currVal = 0.0;
            itr = -1;
        }

        return chosenParents;
    }


    public List<Solution> tournament(List<Solution> solutions)
    {

        double randomValue;
        Solution s1;
        Solution s2;
        boolean bigger;

        List<Solution> chosenParents = new ArrayList<>(solutions.size());

        while(chosenParents.size() <= solutions.size())
        {
            s1 = solutions.get(rand.nextInt(solutions.size()-1));
            s2 = solutions.get(rand.nextInt(solutions.size()-1));
            randomValue =  rand.nextDouble();

            bigger = randomValue > arg;

            if(bigger)
                chosenParents.add(s1.getFitness() > s2.getFitness() ? s1 : s2);
            else
                chosenParents.add(s1.getFitness() > s2.getFitness() ? s2 : s1);
        }

        return chosenParents;
    }

    public List<Solution> truncation(List<Solution> solutions){

        int solutionsAmount = solutions.size();
        double floatedPercentage = arg / 100;
        int howMany = (int) Math.floor(floatedPercentage * solutionsAmount);

        //sort it
         Collections.sort(solutions,Comparator.comparing(Solution::getFitness));
         solutions.subList(solutionsAmount-howMany , solutionsAmount);

        //best 10%
        List<Solution> subbed = new ArrayList<>();
        subbed.addAll(solutions.subList(solutionsAmount-howMany,solutionsAmount));
        solutions.clear();
        return subbed;
    }
}
