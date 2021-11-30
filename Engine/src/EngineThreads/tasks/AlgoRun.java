package EngineThreads.tasks;

import Manager.EvoManager;
import Manager.Solution;

import java.util.Map;

public class AlgoRun implements Runnable {

    private EvoManager evoManager;
    private Map<String, Double> condition;
    private volatile boolean pause = false;
    private volatile boolean terminate = false;

    public AlgoRun(){ }
    public AlgoRun(EvoManager evoManager,  Map<String, Double> condition){
        this.evoManager = evoManager;
        this.condition=condition;
    }
    public synchronized void switchPause() { pause = !pause;
        synchronized (this) {this.notifyAll();}}

    public synchronized void setTerminate() { terminate = true;}


    @Override
    public void run() {
        startAlgorithm();
    }

    public void startAlgorithm() {
        int itr = 1;
        boolean conditionVal = false;
        while (!conditionVal) {
            if(terminate) { break;}
            evoManager.evaluateFitness();
            evoManager.initiateSelection();
            evoManager.initiateCrossOver();
            evoManager.initiateMutation();

            conditionVal = conditionCheck(evoManager, condition, evoManager.getBestOfGeneration().get(itr));
            itr++;

            if(pause){
                try {
                    synchronized (this) { this.wait(); }
                }catch(InterruptedException ignored){ }
            }
        }
        condition.clear();
        evoManager.setGeneration();
    }

    public boolean conditionCheck(EvoManager evoManager,Map<String, Double> condition, Solution bestSol) {

        double req;
        double calculated;
        boolean conditionMet = false;


        if (condition.containsKey("fitness")) {
            req = condition.get("fitness");
            if(req != 0) {
                if (evoManager.getBestAmountOfFives() <= bestSol.getSolution().size()) {
                    calculated = bestSol.getFitness() / req;
                    evoManager.setProgress("fitness", calculated);
                    if (bestSol.getFitness() > req) {
                        conditionMet = true;
                    }
                }
            }
        }
        if (condition.containsKey("generations")) {
            req = condition.get("generations");
            if(req != 0) {
                if (evoManager.getBestAmountOfFives() <= bestSol.getSolution().size()) {
                    calculated = (double) evoManager.getGeneration() / req;
                    evoManager.setProgress("generations", calculated);
                    if (evoManager.getGeneration() > req) {
                        conditionMet = true;
                    }
                }
            }
        }

        return conditionMet;
    }





}
