package UI;

import Manager.*;


import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;


public class UIControl implements UI {


    EvoManager evoManager;

    public UIControl() {
    }

    public UIControl(EvoManager evoManager) {
        this.evoManager = evoManager;
    }

    public String printage(){
        final String[] flow = {""};

        flow[0] += " -- Classes -- \n ----------------------------------------------\n";
        evoManager.getUd().getClasses().sort(Comparator.comparing(Cls::getId));

        evoManager.getUd().getClasses().forEach(cls -> {
            flow[0] += " [id - " + cls.getId() + " |  subjects: {";
            cls.getRequirements().forEach((subId, hours) -> {

                for (int j = 0; j < evoManager.getUd().getSubjects().size(); j++) {
                    if (subId == evoManager.getUd().getSubjects().get(j).getId()) {
                       flow[0] += "(" + evoManager.getUd().getSubjects().get(j).getName() + " - ";
                    }
                }
                flow[0] += hours + " Hours)";
            });
            flow[0] += "}]\n";
        });
        flow[0] += "\n -- Teachers -- \n ----------------------------------------------\n";
        evoManager.getUd().getTeachers().sort(Comparator.comparing(Teacher::getId));

        evoManager.getUd().getTeachers().forEach(teacher -> {

           flow[0] += " [id - " + teacher.getId() + " |  subjects: ";

            Object[] arr = teacher.getSubjects().toArray();

            for (Object o : arr) {
                flow[0] += "(" + o + " - ";

                for (int j = 0; j < evoManager.getUd().getSubjects().size(); j++) {
                    if ((int) o == evoManager.getUd().getSubjects().get(j).getId()) {
                        flow[0] += evoManager.getUd().getSubjects().get(j).getName() + ")";

                    }
                }
            }
            flow[0] += "}]\n";
        });

        flow[0] += "\n -- Subjects -- \n ----------------------------------------------\n";
        evoManager.getUd().getSubjects().sort(Comparator.comparing(Subject::getId));

        evoManager.getUd().getSubjects().forEach(subject -> {
            flow[0] += " " + subject.getId() + " " + subject.getName() + "\n";
        });

        flow[0] += "\n -- Rules -- \n ----------------------------------------------\n";
        for (Rule rule : evoManager.getUd().getRules()) {
            flow[0] += "[" + rule.getRuleId() + " - " + rule.getType() + "]\n";
        }

        flow[0] += "\n -- Engine Data -- \n ----------------------------------------------\n";
        flow[0] += " Initial population - " + evoManager.getEvo().getInitialPopulation() + "\n";
        flow[0] += " Selection - [" + evoManager.getEvo().getSelection().getType()
                + " - Configuration:" + evoManager.getEvo().getSelection().getArg() + " - Elitism:" +
                evoManager.getEvo().getSelection().getElitism() + "]\n";
        flow[0] += " Crossover - [" + evoManager.getEvo().getCrossover().getName()
                + " - Cutting points : " + evoManager.getEvo().getCrossover().getCuttingPoints() + "]\n";
        flow[0] += " Mutations - ";
        evoManager.getEvo().getMutations().forEach(mutation -> {
            flow[0] += "[" + mutation.getName() + " - probability: " + mutation.getProbability() + "]";
        });

        return flow[0];
    }

    @Override
    public TextFlow subjectData() {
        TextFlow flow = new TextFlow();
        flow.setLineSpacing(5.0);
        evoManager.getUd().getSubjects().sort(Comparator.comparing(Subject::getId));

        evoManager.getUd().getSubjects().forEach(subject -> {
            flow.getChildren().add(new Text(" " + subject.getId() + " " + subject.getName() + "\n\n"));
        });
        return flow;
    }

    @Override
    public TextFlow teacherData() {
        TextFlow flow = new TextFlow();
        flow.setLineSpacing(2.0);
        evoManager.getUd().getTeachers().sort(Comparator.comparing(Teacher::getId));

        evoManager.getUd().getTeachers().forEach(teacher -> {

            flow.getChildren().add(new Text(" [id - " + teacher.getId() + " |  subjects: "));

            Object[] arr = teacher.getSubjects().toArray();

            for (Object o : arr) {
                flow.getChildren().add(new Text("(" + o + " - "));

                for (int j = 0; j < evoManager.getUd().getSubjects().size(); j++) {
                    if ((int) o == evoManager.getUd().getSubjects().get(j).getId()) {
                        flow.getChildren().add(new Text(evoManager.getUd().getSubjects().get(j).getName() + ")"));

                    }
                }
            }
            flow.getChildren().add(new Text("}]\n\n"));
        });
        return flow;
    }

    @Override
    public TextFlow classesData() {

        TextFlow flow = new TextFlow();
        AtomicReference<String> name = null;
        flow.setLineSpacing(2.0);
        evoManager.getUd().getClasses().sort(Comparator.comparing(Cls::getId));

        evoManager.getUd().getClasses().forEach(cls -> {
            System.out.println(cls.getName());
            flow.getChildren().add(new Text(" [id - " + cls.getId() + " | "));
            flow.getChildren().add(new Text(" subjects: {"));
            cls.getRequirements().forEach((subId, hours) -> {

                for (int j = 0; j < evoManager.getUd().getSubjects().size(); j++) {
                    if (subId == evoManager.getUd().getSubjects().get(j).getId()) {
                        flow.getChildren().add(new Text("(" +
                                evoManager.getUd().getSubjects().get(j).getName() + " - "));
                    }
                }
                flow.getChildren().add(new Text(+hours + "Hours)"));
            });
            flow.getChildren().add(new Text("}]\n\n"));
        });
        return flow;
    }

    @Override
    public TextFlow rulesData() {
        TextFlow flow = new TextFlow();
        flow.setLineSpacing(2.0);
        for (Rule rule : evoManager.getUd().getRules()) {
            flow.getChildren().add(new Text("[" + rule.getRuleId() + " - " + rule.getType() + "]\n\n"));
        }
        return flow;
    }

    @Override
    public TextFlow engineData() {
        TextFlow flow = new TextFlow();
        flow.setLineSpacing(2.0);
        flow.getChildren().add(new Text(" Initial population - " + evoManager.getEvo().getInitialPopulation() + "\n\n"));
        flow.getChildren().add(new Text(" Selection - [" + evoManager.getEvo().getSelection().getType()
                + " - Configuration:" + evoManager.getEvo().getSelection().getArg() + " - Elitism:" +
                evoManager.getEvo().getSelection().getElitism() + "]\n\n"));
        flow.getChildren().add(new Text(" Crossover - [" + evoManager.getEvo().getCrossover().getName()
                + " - Cutting points : " + evoManager.getEvo().getCrossover().getCuttingPoints() + "]\n\n"));
        flow.getChildren().add(new Text(" Mutations - "));
        evoManager.getEvo().getMutations().forEach(mutation -> {
            flow.getChildren().add(new Text("[" + mutation.getName() + " - probability: " + mutation.getProbability() + "]"));
        });
        return flow;
    }

    @Override
    public TextFlow printRaw(int index) {
        TextFlow flow = new TextFlow();
        flow.setLineSpacing(2.0);
        List<Container> bestFives = evoManager.getBestOfGeneration().get(index).getSolution();
        bestFives.sort(Comparator.comparing(Container::getDay)
                .thenComparing(Container::getHour).thenComparing(Container::getTeacherId));
        evoManager.getUd().getTeachers().sort(Comparator.comparing(Teacher::getId));


        flow.getChildren().add(new Text("Gen (" + index + ") , " + "Fitness (" + String.format("%.2f", evoManager.getBestOfGeneration().get(index).getFitness()) + "):\n"));
        for (Container bestFive : bestFives) {
            flow.getChildren().add(new Text("{" + "Day: " + bestFive.getDay() + " | Hour: " + bestFive.getHour() + " | Class: "
                    + bestFive.getCls().getName() + " | Teacher: " + bestFive.getTeacher().getName()
                    + " | Subject: " + bestFive.getTeacher().getName() + "}\n"));
        }
        return flow;
    }


    @Override
    public boolean dataCheck(){
        boolean fault = true;
        UserData ud = evoManager.getUd();
        EvoEngine evo = evoManager.getEvo();

        //check user data
        if(ud.getDays() > 6 || ud.getHardRulesWeight() < 0 || ud.getHardRulesWeight() > 100){fault=false;}
        //check engine
        if(evo.getSelection().getElitism() > evo.getInitialPopulation()){fault=false;}
        return fault;
    }

}