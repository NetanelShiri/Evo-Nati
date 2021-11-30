import Manager.Cls;
import Manager.Container;
import Manager.Solution;
import Manager.Teacher;
import UI.UIAdapter;


import javafx.animation.KeyFrame;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;

import java.io.File;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.lang.System.currentTimeMillis;

/**
 * FXML Controller class
 *
 * @author iblecher
 */
@SuppressWarnings({"unchecked" , "rawtypes"})
public class ETTController extends ControllerFXML {

    private UIAdapter adapterApp;
    private Stage primaryStage;

    //essentials from engine
    private double trunctionVal;
    private double tourneyVal;
    private int days , hours;
    private long timePauseRemoval = 0;
    int currentSolution;
    Map<Integer, Solution> bestSolutions;

    public ETTController(){
        //ESSENTIALS
        pattern = Pattern.compile("\\d*|\\d+\\,\\d*");
        formatterFit = new TextFormatter((UnaryOperator<TextFormatter.Change>) change -> {
            return pattern.matcher(change.getControlNewText()).matches() ? change : null;
        });
        formatterTime = new TextFormatter(formatterFit.getFilter());
        formatterGen = new TextFormatter(formatterFit.getFilter());
        formatterTrunction = new TextFormatter(formatterFit.getFilter());
        formatterTournament = new TextFormatter(formatterFit.getFilter());
        durationProp = new SimpleLongProperty(0);
        isFileSelected = new SimpleBooleanProperty(false);
        isAlgoRun = new SimpleBooleanProperty(false);
        lockedLabels = new SimpleBooleanProperty(true);
        isAspect = new SimpleBooleanProperty(false);
        isDTO = new SimpleBooleanProperty(false);
        isStarted = new SimpleBooleanProperty(false);
        isPaused = new SimpleBooleanProperty(false);
        isStopped = new SimpleBooleanProperty(true);

    }

    public void setMainApp(UIAdapter adapterApp) {
        this.adapterApp = adapterApp;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * Initializes the controller class.
     */

    public void initialize() {


        flippingMenu.getItems().addAll("Days","Hours","Subjects","Classes","Teachers");
        flippingMenu.getSelectionModel().selectFirst();
        sizerMenu.getItems().addAll("Days","Hours","Subjects","Classes","Teachers");
        sizerMenu.getSelectionModel().selectFirst();
        aspectMenu.getItems().addAll("Day","Hour","Subject","Class","Teacher");
        aspectMenu.getSelectionModel().selectFirst();


        StringConverter<Number> converter = new NumberStringConverter();
        textTime.textProperty().bindBidirectional(timeSlide.valueProperty(), converter);
        textGen.textProperty().bindBidirectional(genSlide.valueProperty(), converter);
        textFit.textProperty().bindBidirectional(fitSlide.valueProperty(), converter);

        //run-tool
        playBtn.disableProperty().bind(Bindings.and(isFileSelected.not(),lockedLabels));
        pauseBtn.disableProperty().bind(Bindings.and(isFileSelected.not(),lockedLabels));
        stopBtn.disableProperty().bind(Bindings.and(isFileSelected.not(),lockedLabels));
        restartBtn.disableProperty().bind(Bindings.and(isFileSelected.not(),lockedLabels));

        //settings
        settingsSlides.disableProperty().bind(isStopped.not());
        //running
        runningPane.disableProperty().bind(isStarted);
        nextPrev.disableProperty().bind(isStopped.not());


        //left-slide
        rules.disableProperty().bind(Bindings.and(isFileSelected.not(),lockedLabels));
        showData.disableProperty().bind(Bindings.and(isFileSelected.not(),lockedLabels));
        bestSol.disableProperty().bind(Bindings.or(isAlgoRun.not(),lockedLabels));
        formatListener();

        //mutation
        flippingMenu.disableProperty().bind(flipping.selectedProperty().not());
        flippingTup.disableProperty().bind(flipping.selectedProperty().not());
        flippingProb.disableProperty().bind(flipping.selectedProperty().not());
        flippingActivate.disableProperty().bind(flipping.selectedProperty().not());
        sizerMenu.disableProperty().bind(sizer.selectedProperty().not());
        sizerTup.disableProperty().bind(sizer.selectedProperty().not());
        sizerProb.disableProperty().bind(sizer.selectedProperty().not());
        sizerActivate.disableProperty().bind(sizer.selectedProperty().not());

        //cross-over
        aspectMenu.disableProperty().bind(aspectOriented.selectedProperty().not());
        aspectCut.disableProperty().bind(aspectOriented.selectedProperty().not());
        aspectActivate.disableProperty().bind(aspectOriented.selectedProperty().not());
        dtoCut.disableProperty().bind(dto.selectedProperty().not());
        dtoActivate.disableProperty().bind(dto.selectedProperty().not());
    }

    public void formatListener(){
        textGen.setTextFormatter(formatterGen);
        textFit.setTextFormatter(formatterFit);
        textTime.setTextFormatter(formatterTime);
        trunctionText.setTextFormatter(formatterTrunction);
        tournamentText.setTextFormatter(formatterTournament);
     }

    public void resetProgressView(){
        progressBar.setVisible(false);
        checkMark.setVisible(false);
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();

        executor.execute(()-> fitIndicator.setProgress(0));
        executor.execute(()->timeIndicator.setProgress(0));
        executor.execute(()-> genIndicator.setProgress(0));
    }


    public void pausedView(){
        pauseBtn.setVisible(false);
        playBtn.setVisible(true);
        progressBar.setVisible(false);
        checkMark.setVisible(false);
    }
    public void startedView(){
        pauseBtn.setVisible(true);
        playBtn.setVisible(false);
    }


    @FXML
    public void openFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        resetProgressView();

            if (selectedFile != null && adapterApp.importData(selectedFile)) {
                fileLoadedSuccessfully();
            } else {
                errorView();
            }


    }

    public void  tournamentActivated(){ adapterApp.manipulateSelection("Tournament",tourneyVal); }

    public void trunctionActivated(){ adapterApp.manipulateSelection("Truncation",trunctionVal); }

    public void rouletteActivated(){ adapterApp.manipulateSelection("RouletteWheel",0); }

    public void dtoActivated(){
        int cuttingpts = (int)dtoCut.getValue();
        adapterApp.manipulateCrossover("DayTimeOriented","NaN",cuttingpts); }

    public void aspectOrientedActivated(){
        String addon = "Orientation=";
        String orient = aspectMenu.getSelectionModel().getSelectedItem().toString();
        orient = orient.toUpperCase(Locale.ROOT);
        addon = addon.concat(orient);
        int cuttingpts = (int)aspectCut.getValue();
        adapterApp.manipulateCrossover("AspectOriented",addon,cuttingpts);
    }

    public void flippingActivated(){
        //gather picks
        String component = flippingMenu.getSelectionModel().getSelectedItem().toString();
        component = component.substring(component.length()-1);
        int tupples = (int)flippingTup.getValue();
        double probability = flippingProb.getValue();
        adapterApp.manipulateMutation("Flipping",component,tupples,probability);
    }

    public void sizerActivated(){
        //gather picks
        String component = sizerMenu.getSelectionModel().getSelectedItem().toString();
        component = component.substring(component.length()-1);
        int tupples = (int)sizerTup.getValue();
        double probability = sizerProb.getValue();
        adapterApp.manipulateMutation("Sizer",component,tupples,probability);
    }

    public void elitismActivated(){
        adapterApp.manipulateElitism((int)elitism.getValue());
    }

    public void errorView(){
        openFile.requestFocus();
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.4), evt -> title.requestFocus()),
                new KeyFrame(Duration.seconds(0.8), evt -> openFile.requestFocus()));
        timeline.setCycleCount(3);
        timeline.play();
    }

    public void bestSolution(){

        hideAllLayouts();
        hideInternalCenterLayouts();
        solutionPane.setVisible(true);
        rawViewPane.setVisible(true);
        bestSolutions = adapterApp.getBestOfGeneration();
        currentSolution = adapterApp.getBestIndex();
        days = adapterApp.fetchDays();
        hours = adapterApp.fetchHours();
        calculateSolutionData();
        rawView();
        chartMaker();
        dynamicTeacherClass();
    }

    @FXML
    public void previousSolution(){
        if(currentSolution - 1 == 1){
            previous.setDisable(true);
        }
        if(next.isDisabled()) { next.setDisable(false);}
        currentSolution--;
        calculateSolutionData();
    }


    @FXML
    public void nextSolution(){
        if(currentSolution + 1 == bestSolutions.size()){
            next.setDisable(true);
        }
        if(previous.isDisabled()) { previous.setDisable(false);}
        currentSolution++;
        calculateSolutionData();
    }

    public void chartMaker(){

        XYChart.Series series = new XYChart.Series();

        xaxis.setAutoRanging(true);
        xaxis.setLowerBound(1);
        xaxis.setTickUnit(20);
        xaxis.setLabel("Generations");

        yaxis.setAutoRanging(false);
        yaxis.setLowerBound(0);
        yaxis.setUpperBound(100);
        yaxis.setTickUnit(20);
        yaxis.setLabel("Fitness");

        for(int i = 1 ; i<bestSolutions.size();i++){
            series.getData().add(new XYChart.Data(i, bestSolutions.get(i).getFitness()));
        }
        chart.getData().clear();
        chart.getData().add(series);
    }

    public void hideInternalCenterLayouts(){
        rawViewPane.setVisible(false);
        graphViewPane.setVisible(false);
        teacherGrid.setVisible(false);
        clsGrid.setVisible(false);
    }

    public void fileLoadedSuccessfully(){
        if(dataFlow()) {
            enableRunTool();
            isFileSelected.set(true);
            lockedLabels.set(false);
            title.requestFocus();
        }
    }

    public void enableRunTool(){
        playBtn.setOpacity(100);
        restartBtn.setOpacity(100);
        stopBtn.setOpacity(100);
        pauseBtn.setOpacity(100);
    }

    public void hideAllLayouts(){
        tabPane.setVisible(false);
        settingsPane.setVisible(false);
        runningPane.setVisible(false);
        solutionPane.setVisible(false);
    }

    public void settings(){
        hideAllLayouts();
        settingsPane.setVisible(true);

    }

    public void rules(){
        hideAllLayouts();
        runningPane.setVisible(true);
    }

    public boolean dataFlow(){
        subjectsText.getChildren().clear();
        teachersText.getChildren().clear();
        classesText.getChildren().clear();
        rulesText.getChildren().clear();
        engineText.getChildren().clear();
        try {
            subjectsText.getChildren().addAll(adapterApp.fetchSubjectData());
            teachersText.getChildren().addAll(adapterApp.fetchTeacherData());
            classesText.getChildren().addAll(adapterApp.fetchClassData());
            rulesText.getChildren().addAll(adapterApp.fetchRulesData());
            engineText.getChildren().addAll(adapterApp.fetchEngineData());
        }catch(Exception e){
            errorView();
            return false;
        }
        return true;
    }


    public void showData(){
        dataFlow();
        hideAllLayouts();
        tabPane.setVisible(true);

    }

    public void progress(){
        timePauseRemoval = 0;
        FetchProgress fr = new FetchProgress();
        Thread th =  new Thread(fr);
        th.setDaemon(true);
        th.start();

    }

    public class FetchProgress implements Runnable{
        Map<String,Double> progress;
        int delay = 500;

        @Override
        public void run() {
            hideAllLayouts();
            runningPane.setVisible(true);
            Platform.runLater(() -> progressBar.setProgress(ProgressBar.INDETERMINATE_PROGRESS));
            progressBar.setVisible(true);

            final long startTime = currentTimeMillis();
            long duration;



            while (true) {
                progress = adapterApp.fetchProgress();
                try { Thread.sleep(delay);
                } catch (InterruptedException ignored) {  }
                if(progress.get("generations") != null && genSlide.getValue() != 0){
                    genIndicator.setProgress(progress.get("generations"));
                    if(progress.get("generations")>=1) break;
                }
                try { Thread.sleep(delay);
                } catch (InterruptedException ignored) { }
                if (progress.get("fitness") != null && fitSlide.getValue() != 0) {
                    fitIndicator.setProgress(progress.get("fitness"));
                    if(progress.get("fitness") >= 1) break;
                }

                duration = (currentTimeMillis() - startTime - timePauseRemoval )/1000;
                try { Thread.sleep(delay);
                } catch (InterruptedException ignored) { }

                if(timeSlide.getValue() != 0) {
                    if(duration >= timeSlide.getValue()) break;
                    timeIndicator.setProgress((double) duration / timeSlide.getValue());
                }

                if(pause){
                    long timePauseChecker = currentTimeMillis();
                    //pause + taking lock time
                    try {
                        synchronized (lock) { lock.wait(); }
                    }catch(InterruptedException ignored){ }
                    timePauseRemoval += currentTimeMillis() - timePauseChecker;
                }
                if(terminate){break;}
            }

            isStopped.set(true);
            isPaused.set(false);
            isStarted.set(false);
            if(!terminate) {
                Platform.runLater(() -> progressBar.setProgress(100));
                checkMark.setVisible(true);
            }
        }
    }

    public void runAlgo() {


        if(!alreadyStarted) {
            alreadyStarted = true;
            if(!isPaused.get()){adapterApp.resetData();}
            //prepare program state
            startedView();
            isStarted.set(true);
            isPaused.set(false);
            isStopped.set(false);
            terminate = false;
            isAlgoRun.set(true);
            resetProgressView();

            //run it
            adapterApp.runAlgorithm(genSlide.getValue(), fitSlide.getValue(), timeSlide.getValue());

            progress();
        }else{
            continueRun();
        }

    }

    public synchronized void continueRun(){
        isStarted.set(true);
        isPaused.set(false);
        isStopped.set(false);

        pause = false;
        synchronized (lock) {
            lock.notifyAll();
        }
        adapterApp.pauseAlgo();
        startedView();
    }


    public synchronized void pauseRun() {
        isPaused.set(true);
        isStarted.set(false);
        isStopped.set(false);
        pause = true;
        adapterApp.pauseAlgo();
        pausedView();
    }

    public synchronized void stopRun() throws InterruptedException {
        isStopped.set(true);
        isPaused.set(false);
        isStarted.set(false);
        alreadyStarted = false;
        terminate = true;
        adapterApp.terminateAlgo();
        pausedView();
        resetProgressView();
    }

    public void restartRun() throws InterruptedException {
        stopRun();
        Thread.sleep(1000);
        runAlgo();
    }



    @FXML
    public void dynamicTeacherClass() {

        int index = 0;
        ObservableList<String> classItems = FXCollections.observableArrayList();
        ObservableList<String> teacherItems = FXCollections.observableArrayList();

        List<Cls> classes = adapterApp.getClasses();
        List<Teacher> teachers = adapterApp.getTeachers();

        for(Teacher teacher : teachers) {
            teacherItems.add(index++,teacher.getName());
        }

        index = 0;
        for(Cls cls : classes) {
            classItems.add(index++,cls.getName());
        }

        teacherViewBtn.setItems(teacherItems);

        classViewBtn.setItems(classItems);

    }

    public void teacherGrid(){

        rawViewBtn.setSelected(false);
        graphViewBtn.setSelected(false);

        //keeping grid lines
        cleanGrid(this.teacherGrid);


        List<Container> solution = bestSolutions.get(currentSolution).getSolution();
        List<Container> filteredSolution;


        Object temp = teacherViewBtn.getSelectionModel().getSelectedItem();
        if(temp == null){return;}
        String currTeacher = temp.toString();
        filteredSolution = solution.stream().filter(f -> f.getTeacher().getName().equals(currTeacher))
                .collect(Collectors.toList());


        for(int d = 1 ; d <= days;d++){

            for(int h = 1; h <= hours;h++){
                int finalD = d;
                int finalH = h;

                Container five = filteredSolution.stream().filter(f ->
                    f.getDay() == finalD  && f.getHour() == finalH)
                        .findFirst().orElse(null);
                if(five == null){continue;}

               teacherGrid.add(new Label(five.getCls().getId()+" - "+five.getCls().getName()+
                        "\n" + five.getSubjectId()+" - "+five.getSubjectName() ),d,h);

            }

        }


    }

    public void clsGrid(){

        rawViewBtn.setSelected(false);
        graphViewBtn.setSelected(false);

        //keeping grid lines
        cleanGrid(this.clsGrid);

        List<Container> solution = bestSolutions.get(currentSolution).getSolution();
        List<Container> filteredSolution;

        Object temp = classViewBtn.getSelectionModel().getSelectedItem();
        if(temp == null){return;}
        String currCls = temp.toString();
        filteredSolution = solution.stream().filter(f -> f.getCls().getName().equals(currCls))
                .collect(Collectors.toList());


        for(int d = 1 ; d <= days;d++){

            for(int h = 1; h <= hours;h++){
                int finalD = d;
                int finalH = h;

                Container five = filteredSolution.stream().filter(f ->
                        f.getDay() == finalD  && f.getHour() == finalH)
                        .findFirst().orElse(null);
                if(five == null){continue;}

                clsGrid.add(new Label(five.getTeacher().getId()+" - "+five.getTeacher().getName()+
                        "\n" + five.getSubjectId()+" - "+five.getSubjectName() ),d,h);

            }

        }


    }

    public void cleanGrid(GridPane grid){
        Node node = grid.getChildren().get(0);
        grid.getChildren().clear();
        grid.getChildren().add(0,node);
        initializeGrid(grid);
        rawViewBtn.setSelected(false);
        graphViewBtn.setSelected(false);
    }


    @FXML
    public void graphView(){
        hideInternalCenterLayouts();
        graphViewPane.setVisible(true);
        chart.setVisible(true);
    }

    @FXML
    public void teacherView(){
        hideInternalCenterLayouts();
        teacherGrid.setVisible(true);
    }

    @FXML
    public void clsView(){
        hideInternalCenterLayouts();
        clsGrid.setVisible(true);
    }





    public void calculateSolutionData(){

        rawFlow.getChildren().clear();
        rawFlow.getChildren().addAll(adapterApp.printRaw(currentSolution));
        teacherGrid();
        clsGrid();
    }

    @FXML
    public void rawView(){
        hideInternalCenterLayouts();
        rawViewPane.setVisible(true);
        rawFlow.setVisible(true);
    }

    
    public void trunctext(){
        trunctionBtn.setVisible(false);
        trunctionText.setVisible(true);
    }
    @FXML
    public void enterTrunction(ActionEvent ae){
        trunctionBtn.setVisible(true);
        trunctionText.setVisible(false);
        trunctionVal = Double.parseDouble(trunctionText.getText());
        if(trunctionVal>100 || trunctionVal < 0 ) {trunctionVal = 10;} //default
        trunctionActivated();
    }

    public void tournamenttxt(){
        tournamentBtn.setVisible(false);
        tournamentText.setVisible(true);
    }
    @FXML
    public void enterTournament(ActionEvent ae){
        tournamentBtn.setVisible(true);
        tournamentText.setVisible(false);
        tourneyVal = Double.parseDouble(tournamentText.getText());
        if(tourneyVal>1 || tourneyVal < 0 ) {tourneyVal = 0.5;} //default
        tournamentActivated();
    }


    //--------------------------------------ANIMATIONS--------------------------------------
    private void setTransition(Circle circle , double x , double y){
        TranslateTransition trans = new TranslateTransition(Duration.seconds(2),circle);
        trans.setToX(x);
        trans.setToY(y);
        trans.setAutoReverse(true);
        trans.setCycleCount(2);
        trans.play();
    }

    @FXML
    private void easterEgg1(){
        if(!c1.isVisible() && !c2.isVisible() && !c3.isVisible()) {
            c1.setVisible(true);
            c2.setVisible(true);
            c3.setVisible(true);
            setRotate(c1, 360, 2);
            setRotate(c2, 180, 4);
            setRotate(c3, 145, 6);
        }else{
            c1.setVisible(false);
            c2.setVisible(false);
            c3.setVisible(false);
        }
    }

    private void setRotate(Circle circle, int angle, int duration){
        RotateTransition rt = new RotateTransition(Duration.seconds(duration),circle);

        rt.setAutoReverse(true);
        rt.setByAngle(angle);
        rt.setDelay(Duration.seconds(0));
        rt.setRate(3);
        rt.setCycleCount(3);
        rt.play();
    }

    @FXML
    private void easterEgg2(){
        if(!c1e2.isVisible() && !c2e2.isVisible()) {
            c1e2.setVisible(true);
            c2e2.setVisible(true);
            setTransition(c2e2,-150,-220);
            setTransition(c1e2,150,220);
        }else{
            c1e2.setVisible(false);
            c2e2.setVisible(false);
        }
    }



    //--------------------------------------THEMES--------------------------------------
    public void regularTheme(){
        final String regularTheme = Objects.requireNonNull(getClass().getResource("Themes/MainTheme.css")).toExternalForm();
        mainLayout.getStylesheets().clear();
        mainLayout.getStylesheets().add(regularTheme);

    }

    public void darkTheme(){
        final String darkTheme = Objects.requireNonNull(getClass().getResource("Themes/darkMode.css")).toExternalForm();
        mainLayout.getStylesheets().clear();
        mainLayout.getStylesheets().add(darkTheme);
    }

    public void violetTheme(){
        final String violetTheme = Objects.requireNonNull(getClass().getResource("Themes/violetMode.css")).toExternalForm();
        mainLayout.getStylesheets().clear();
        mainLayout.getStylesheets().add(violetTheme);
    }


    enum DaysEnum{ Sunday , Monday, Tuesday , Wednesday , Thursday , Friday};
    public void initializeGrid(GridPane grid){
        Label label;

        label = new Label("H / D");
        label.setStyle("-fx-font-family: 'Agency FB';" + "-fx-font-weight: 700");
        grid.add(label,0,0);

        for(int i = 0 ; i < 6; i++){
            label = new Label(DaysEnum.values()[i].name());
            label.setStyle("-fx-font-family: 'Agency FB';" +  "-fx-font-weight: 700");
            grid.add(label,i+1,0);
        }


        for(int i = 1; i <  teacherGrid.getRowConstraints().size() ; i++) {
            label = new Label(Integer.toString(i));
            label.setStyle("-fx-font-family: 'Agency FB';" + "-fx-font-weight: 700");
            grid.add(label, 0, i);

        }
    }
}
