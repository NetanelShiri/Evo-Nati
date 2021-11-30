package EngineThreads;

import EngineThreads.tasks.AlgoRun;
import Manager.EvoManager;

import java.util.Map;

public class EngineThread {

    EvoManager evoManager;

    Map<String, Double> conditions;
    Thread th = null;
    AlgoRun algoRun;

    public EngineThread(){}
    public EngineThread(EvoManager evoManager){
        this.evoManager = evoManager;
    }

    public synchronized void switchState(){ algoRun.switchPause();}
    public synchronized void terminator() throws InterruptedException {
        algoRun.setTerminate();
        th.join();
    }
    public void setCondition(Map<String, Double> conditions){this.conditions = conditions;}

    public void run() {
        algoRun = new AlgoRun(evoManager,conditions);
        th = new Thread(algoRun);
        th.setPriority(10);
        th.start();
    }
}
