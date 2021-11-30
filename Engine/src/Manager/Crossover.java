package Manager;



import generated.ETTCrossover;

import java.util.*;
import java.util.stream.Collectors;

public class Crossover {

    private String configuration;
    private String name;
    private int cuttingPoints;
    private final Random rand = new Random();

    public Crossover(){}
    public Crossover(ETTCrossover crossover){
        this.configuration = crossover.getConfiguration();
        this.name = crossover.getName();
        this.cuttingPoints = crossover.getCuttingPoints();
    }

    public String getConfiguration() {
        return configuration;
    }
    public void setConfiguration(String value) {
        this.configuration = value;
    }

    public String getName() {
        return name;
    }
    public void setName(String value) {
        this.name = value;
    }

    public int getCuttingPoints() {
        return cuttingPoints;
    }
    public void setCuttingPoints(int value) {
        this.cuttingPoints = value;
    }



    public void crossOver(List<Solution> solutions,Solution s1 , Solution s2,UserData ud){
        Solution p1 = s1;
        Solution p2 = s2;
        List<Container> c1,c2;

        c1 = new ArrayList<>();
        c2 = new ArrayList<>();
        int oppose = -1;
        int curCutPoint = 0;
        int size;

        int index = 0;
        Map<Integer, Container> child1;
        Map<Integer, Container> child2;

        if(name.equals("DayTimeOriented")) {
            child1 = sortByDTO(s1.getSolution(), ud);
            child2 = sortByDTO(s2.getSolution(), ud);
        }else if(name.equals("AspectOriented")){
            child1 = aspectOriented(s1.getSolution(), ud);
            child2 = aspectOriented(s2.getSolution(), ud);
        }
        else{
            //default
            child1 = sortByDTO(s1.getSolution(), ud);
            child2 = sortByDTO(s2.getSolution(), ud);
        }

        size = ud.getDays() * ud.getHours() * ud.getTeachers().size()
                * ud.getClasses().size() * ud.getSubjects().size();
        int cuttingSize = Math.min(child1.size(), child2.size());

        int[] cuttingPts = new int[cuttingPoints];

       for(int i = 0 ; i<cuttingPoints;i++){
            cuttingPts[i] = rand.nextInt(cuttingSize+1);
        }
        Arrays.sort(cuttingPts);


        for(int i = 1; i<size;i++){
            if(curCutPoint != cuttingPoints && index == cuttingPts[curCutPoint]) {
                oppose *= -1;
                curCutPoint++;
            }

            //collect fives
            if(child1.containsKey(i) || child2.containsKey(i)) {
                if (oppose == -1) {
                    if(child1.containsKey(i)) {
                        c1.add(child1.get(i));
                    }
                    if(child2.containsKey(i)) {
                        c2.add(child2.get(i));
                    }
                } else {
                    if(child2.containsKey(i)) {
                        c1.add(child2.get(i));
                    }
                    if(child1.containsKey(i)) {
                        c2.add(child1.get(i));
                    }

                }
            }
            index++;
        }

        p1.setSolution(c1);
        p2.setSolution(c2);
        solutions.add(p1);
        solutions.add(p2);
    }


    public Map<Integer,Container> sortByDTO(List<Container> fives, UserData ud)
    {
        Map<Integer,Container> childMap = new HashMap<>();
        List<Container> children = fives.stream().filter(Objects::nonNull).sorted(Comparator.comparing(Container::getDay).thenComparing(Container::getHour).
                thenComparing(Container::getTeacherId).thenComparing(Container::getSubjectId).
                thenComparing(Container::getClsId)).collect(Collectors.toList());

        int jump=0,index=1;
        for(int d = 1; d <= ud.getDays() ; d++)
        {
            for(int h = 1; h <= ud.getHours() ; h++)
            {
                for(int t = 1; t <= ud.getTeachers().size() ; t++)
                {
                    for(int s = 1; s <= ud.getSubjects().size() ; s++)
                    {
                        for(int c = 1; c <= ud.getClasses().size() ; c++)
                        {
                            if(jump < children.size()) {
                                for (int i = jump; i < children.size(); i++) {
                                    if (children.get(i).getDay() == d && children.get(i).getHour() == h
                                            && children.get(i).getTeacherId() == t && children.get(i).getSubjectId() == s
                                            && children.get(i).getClsId() == c) {
                                        childMap.put(index, children.get(i));
                                        jump = i + 1;
                                        break;
                                    } else if (children.get(i).getDay() > d || children.get(i).getHour() > h
                                            || children.get(i).getTeacherId() > t
                                            || children.get(i).getSubjectId() > s || children.get(i).getClsId() > c) {
                                        break;
                                    }
                                }
                            }
                            index++;
                        }
                    }
                }
            }
        }
        return childMap;
    }


    public Map<Integer,Container> aspectOriented(List<Container> fives, UserData ud){
        Map<Integer,Container> childMap = new HashMap<>();
        List<Container> child;
        int size;
        if(configuration.equals("Orientation=TEACHER")) {
            child = fives.stream().filter(Objects::nonNull).sorted(Comparator.comparing(Container::getTeacherId)
                    .thenComparing(Container::getDay).
                            thenComparing(Container::getHour)).collect(Collectors.toList());
            size = ud.getTeachers().size();
        }else{
            child = fives.stream().filter(Objects::nonNull).sorted(Comparator.comparing(Container::getClsId)
                    .thenComparing(Container::getDay).
                            thenComparing(Container::getHour)).collect(Collectors.toList());
            size = ud.getClasses().size();
        }
        int index=1;
        int id = 0;

        for(int obj = 1; obj <= size ; obj++)
        {
            for(int d = 1; d <= ud.getDays() ; d++)
            {
                for(int h = 1; h <= ud.getHours() ; h++)
                {

                    for (Container container : child) {
                        if(configuration.equals("Orientation=TEACHER")){
                            id = container.getTeacherId();
                        }else { id = container.getClsId();}
                        if (container.getDay() == d && container.getHour() == h
                               && id == obj){
                            childMap.put(index, container);
                            h++;
                            break;
                        } else if (container.getDay() > d || container.getHour() > h
                                || id > obj) {
                            break;
                        }
                    }
                }
                index++;
            }
        }

        return childMap;
    }

}
