package UI;

import EngineThreads.EngineThread;
import Manager.*;


import generated.ETTDescriptor;
import javafx.scene.text.TextFlow;
import jdk.internal.util.xml.impl.Input;


import javax.xml.bind.JAXBContext;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;

import java.util.List;
import java.util.Map;



public class UIAdapter {

    private UserData userData;
    private EvoEngine evoEngine;
    private EvoManager evoManager;
    private UIControl uiControl;
    private EngineThread engineThread;

    public void init() {
        this.userData = new UserData();
        this.evoEngine = new EvoEngine();
        this.uiControl = new UIControl();
    }




    public String printage(){
       return uiControl.printage();
    }

    public TextFlow fetchSubjectData(){
       return uiControl.subjectData();
    }

    public TextFlow fetchTeacherData(){
        return uiControl.teacherData();
    }

    public TextFlow fetchClassData(){
        return uiControl.classesData();
    }

    public TextFlow fetchRulesData(){
        return uiControl.rulesData();
    }

    public TextFlow fetchEngineData(){
        return uiControl.engineData();
    }

    public List<Teacher> getTeachers(){return evoManager.getUd().getTeachers();}

    public List<Cls> getClasses(){return evoManager.getUd().getClasses();}

    public TextFlow printRaw(int index){ return uiControl.printRaw(index);}

    public Map<String,Double> fetchProgress(){
        return evoManager.getProgress();
    }

    public int fetchDays(){ return evoManager.getUd().getDays();}

    public int fetchHours(){ return evoManager.getUd().getHours();}


    public void pauseAlgo(){engineThread.switchState(); }

    public synchronized void terminateAlgo() throws InterruptedException {
        engineThread.terminator();
    }

    public void resetData(){
        evoManager.resetManager();
    }



    public void manipulateSelection(String type,double val){
        evoManager.getEvo().getSelection().setArg(val);
        evoManager.getEvo().getSelection().setType(type);
    }

    public void manipulatePopulation(int population){
        evoManager.getEvo().setInitialPopulation(population);
        evoManager.population(population);
    }

    public void manipulateMutation(String name,String component,int tupples,double probability){
        evoManager.getEvo().manipulateMutation(name,component, tupples, probability);

    }

    public void manipulateCrossover(String name, String configuration,int cuttingpts){
        evoManager.getEvo().manipulateCrossover(name,configuration,cuttingpts);
    }
    public void manipulateElitism(int elitism){
        evoManager.getEvo().getSelection().setElitism(elitism);
    }


    public Map<Integer,Solution> getBestOfGeneration (){
        return evoManager.getBestOfGeneration();
    }

    public double getBestFitness(){return evoManager.getBestOfGeneration().get(0).getFitness();}

    public int getGeneration(){return evoManager.getGeneration();}

    public int getBestIndex(){return evoManager.getBestIndex();}

    public void runAlgorithm(double gen,double fit,double time)
    {
        Map<String, Double> req = new HashMap<>();
        req.put("generations",gen);
        req.put("fitness", fit);
        req.put("time",time);
        engineThread.setCondition(req);
        engineThread.run();
    }

    public boolean importData(File file) {
        ETTDescriptor generatedEtt = fileRead(file);
        System.out.println("works");
        init();

        if (generatedEtt != null) {
            if (!userData.initUserData(generatedEtt)) {
                return false;
            }

            evoEngine.initEvoEngine(generatedEtt);

            evoManager = new EvoManager(userData, evoEngine);

            uiControl = new UIControl(evoManager);

            engineThread = new EngineThread(evoManager);
            System.out.println("works");
           // if(!uiControl.dataCheck()){return false;};

        }
        return true;
    }

    public ETTDescriptor fileRead(File file) {

        ETTDescriptor ett = null;

        try {

            JAXBContext jb = JAXBContext.newInstance(ETTDescriptor.class);

            Unmarshaller unmarshaller = jb.createUnmarshaller();

            try {
                ett = (ETTDescriptor) unmarshaller.unmarshal(file);
            } catch (UnmarshalException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ett;
    }

    public boolean importData2(InputStream stream) {
        ETTDescriptor generatedEtt = fileRead2(stream);

        System.out.println("works");
        init();

        if (generatedEtt != null) {
            if (!userData.initUserData(generatedEtt)) {
                return false;
            }

            evoEngine.initEvoEngine(generatedEtt);

            evoManager = new EvoManager(userData, evoEngine);

            uiControl = new UIControl(evoManager);

            engineThread = new EngineThread(evoManager);
            System.out.println("works");
            // if(!uiControl.dataCheck()){return false;};

        }
        return true;
    }

    public ETTDescriptor fileRead2(InputStream stream) {

        ETTDescriptor ett = null;

        try {

            JAXBContext jb = JAXBContext.newInstance(ETTDescriptor.class);

            Unmarshaller unmarshaller = jb.createUnmarshaller();

            try {
                ett = (ETTDescriptor) unmarshaller.unmarshal(stream);
            } catch (UnmarshalException e) {
                e.printStackTrace();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return ett;
    }


}
