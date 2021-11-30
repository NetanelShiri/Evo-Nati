package Manager;


import generated.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class EvoEngine {


    private int initialPopulation;
    private Selection selection;
    private Crossover crossover;
    private List<Mutation> mutations;

    public EvoEngine()
    {
        mutations = new ArrayList<>();
        initialPopulation = 0;
        crossover = null;
        selection = null;
    }

    public List<Mutation> getMutations() { return this.mutations; }
    public Crossover getCrossover() { return this.crossover; }
    public Selection getSelection() { return this.selection; }
    public int getInitialPopulation() { return this.initialPopulation;}
    public void setInitialPopulation(int pop){this.initialPopulation = pop;}

    public void setCrossover(Crossover crossover){
        this.crossover = crossover;
    }
    public void setSelection(Selection selection){
        this.selection = selection;
    }

    public void initEvoEngine(ETTDescriptor generatedEtt) {
/*
        int tempPopulation = generatedEtt.getETTEvolutionEngine().getETTInitialPopulation().getSize();

        ETTMutations tempMutations = generatedEtt.getETTEvolutionEngine().getETTMutations();
        ETTSelection tempSelection = generatedEtt.getETTEvolutionEngine().getETTSelection();
        ETTCrossover tempCrossover = generatedEtt.getETTEvolutionEngine().getETTCrossover();;

        //mutations copy
        for(ETTMutation temp:tempMutations.getETTMutation())
        {
            this.mutations.add(new Mutation(temp));
        }

        //selection copy
        this.setSelection(new Selection(tempSelection));

        //crossover copy
        this.setCrossover(new Crossover(tempCrossover));

        //population
        this.setInitialPopulation(tempPopulation);
*/
        this.setInitialPopulation(0);
        this.setCrossover(new Crossover());
        this.setSelection(new Selection());
      //  this.mutations.add(new Mutation());
    }


    public void manipulateMutation(String name ,String component, int tupples , double probability){
        AtomicBoolean found = new AtomicBoolean(false);
        this.mutations.stream().filter(o -> o.getName().equals(name)).forEach(
                o -> {
                    found.set(true);
                    o.setComponent(component);
                    o.setMaxTupples(tupples);
                    o.setProbability(probability);
                }
        );
        if(!found.get()){

            Mutation mut = new Mutation();
            mut.setName(name);
            mut.setComponent(component);
            mut.setMaxTupples(tupples);
            mut.setProbability(probability);
            mutations.add(mut);
        }
    }

    public void manipulateCrossover(String name,String configuration,int cuttingpts){
        crossover.setName(name);
        crossover.setConfiguration(configuration);
        crossover.setCuttingPoints(cuttingpts);
    }
}
