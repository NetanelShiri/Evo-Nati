package Manager;

import java.util.*;

public class EvoManager {

    private UserData ud;
    private EvoEngine evo;
    private List<Solution> solutions;
    private Map<Integer,Solution> bestOfGeneration;
    private int generation = 1;
    private int bestAmountOfFives;
    private Map<String,Double> progress;
    private int bestIndex;

    Random rand = new Random();

    public EvoManager() { }
    public EvoManager(UserData ud, EvoEngine evo){
        this.ud = ud;
        this.evo = evo;
        this.solutions = new ArrayList<>(evo.getInitialPopulation());
        this.bestOfGeneration = new HashMap<>();
        for(int i = 0 ; i < evo.getInitialPopulation() ; i++) {
            this.solutions.add(new Solution(ud, evo));
        }
        this.progress = new HashMap<>();
        calculateOptionalBest();
    }

    //getters
    public List<Solution> getSolutions(){ return this.solutions;}
    public int getGeneration(){return this.generation;}
    public int getBestAmountOfFives(){return this.bestAmountOfFives;}
    public int getBestIndex(){return this.getBestFitness();}
    public void setGeneration(){this.generation = 1;}
    public Map<Integer,Solution> getBestOfGeneration(){return this.bestOfGeneration;}
    public UserData getUd(){return this.ud;}
    public EvoEngine getEvo(){return this.evo;}
    public Map<String,Double> getProgress(){return this.progress;}
    public void setProgress(String cond,double val){ this.progress.put(cond,val);}
    public void incGeneration(){this.generation++; }


    //evaluations
    public void evaluateFitness(){
        sortSolutions(this.solutions);
        double max = 0;
        for (int i = 0; i<solutions.size();i++) {
            solutions.get(i).setFitness(0);
            solutions.get(i).rulesCheck();
            solutions.get(i).calcFitness();
        }
        for (int i = 0; i<solutions.size();i++) {
            if(solutions.get(i).getFitness() > max){
                max = solutions.get(i).getFitness();
            }
        }
        System.out.println("Fitness - " + max);
        System.out.println("Generation -" + generation);
    }


    public void population(int pop){
        for(int i = 0 ; i < evo.getInitialPopulation() ; i++) {
            this.solutions.add(new Solution(ud, evo));
        }
    }

    public void initiateSelection() {
        int elitism = evo.getSelection().getElitism();
        int index = solutions.size()-1;
        List<Solution> tempSolutions = new ArrayList<>();

        //remove amount of elitism added (lets remove the weakest).
        Collections.sort(this.solutions, Comparator.comparing(Solution::getFitness).reversed());
        if(elitism > 0 && elitism < evo.getInitialPopulation()) {
            for (int i = 0; i < elitism; i++) {
                solutions.remove(index--);
            }
            index = 0;
            //get strongest amount of elitists.
            for (int i = 0; i < elitism; i++) {
                tempSolutions.add(this.solutions.get(index++));
            }
        }

        this.solutions = evo.getSelection().chooseSelection(solutions);
        this.solutions.addAll(tempSolutions);

        this.bestOfGeneration.put(generation, new Solution(solutions.get(0)));
        for(int j = 0 ; j< solutions.size();j++){
            if(solutions.get(j).getSolution().size() < bestAmountOfFives) {
                    solutions.get(j).createFive(bestAmountOfFives-solutions.get(j).getSolution().size());
            }
       }
    }

    public void initiateCrossOver(){
        int best10 = solutions.size();
        int rand1 , rand2;
        Solution p1 , p2;

        while(solutions.size() < evo.getInitialPopulation()) {
            rand1 = rand.nextInt(best10);
            p1 = solutions.get(rand1);
            do{
                rand2 = rand.nextInt(best10);
            }while(rand2 == rand1);
            p2 = solutions.get(rand2);

            evo.getCrossover().crossOver(solutions,p1, p2,ud);
        }

        if(solutions.size() > evo.getInitialPopulation()){
            solutions.remove(solutions.size()-1);
        }

        incGeneration();
    }


    public void initiateMutation(){
        double randNum;
        double probability;
        int decider;
        for(int j = 0; j<solutions.size();j++) {
            for (int i = 0; i < evo.getMutations().size(); i++) {
                probability = (evo.getMutations().get(i).getProbability());
                randNum = rand.nextDouble();

                if (randNum <= probability) {
                    decider = rand.nextInt(20 ) - 10;
                    evo.getMutations().get(i).chooseMutation(solutions.get(j),decider);
                }
            }
        }
    }
    public void calculateOptionalBest(){
        this.bestAmountOfFives = 0;
        ud.getClasses().forEach(cls -> {
           cls.getRequirements().forEach((id,hours)->{
               bestAmountOfFives += hours;
           });
        });

    }

    public int getBestFitness(){
        bestIndex = 0;
        double bestFitness = -1;
        for(int i = 1;i<bestOfGeneration.size();i++){
            if(bestOfGeneration.get(i).getFitness() > bestFitness
                    && bestOfGeneration.get(i).getSolution().size() >= bestAmountOfFives){
                bestIndex = i;
                bestFitness = bestOfGeneration.get(i).getFitness();
            }
        }
        return bestIndex;
    }

    public void sortSolutions(List<Solution> solutions){

        for(int i = 0; i<solutions.size();i++) {
            List<Container> fives =  solutions.get(i).getSolution();
            Collections.sort(fives, Comparator.comparing(Container::getDay)
                    .thenComparing(Container::getHour).thenComparing(Container::getTeacherId));
        }
    }

    public void resetManager(){
        this.solutions = new ArrayList<>(evo.getInitialPopulation());
        this.bestOfGeneration = new HashMap<>();
        for(int i = 0 ; i < evo.getInitialPopulation() ; i++) {
            this.solutions.add(new Solution(ud, evo));
        }
        this.progress = new HashMap<>();
        calculateOptionalBest();
        generation = 1;
        progress = new HashMap<>();
        bestIndex = 0;
    }

}
