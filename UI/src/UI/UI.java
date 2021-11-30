package UI;

import javafx.scene.text.TextFlow;

public interface UI {


    TextFlow subjectData();

    TextFlow teacherData();

    TextFlow classesData();

    TextFlow rulesData();

    TextFlow engineData();

    TextFlow printRaw(int index);

    boolean dataCheck();


}
