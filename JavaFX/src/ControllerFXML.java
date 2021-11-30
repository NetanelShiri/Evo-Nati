import com.sun.javafx.css.Stylesheet;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.text.TextFlow;

import java.net.URL;
import java.util.regex.Pattern;

public class ControllerFXML {

    @FXML protected Label title;
    @FXML protected ScrollPane mainLayout;

    //panes - center
    @FXML protected TabPane tabPane;
    @FXML protected BorderPane settingsPane;
    @FXML protected AnchorPane runningPane;
    @FXML protected AnchorPane solutionPane;
    @FXML protected ScrollPane rawViewPane;
    @FXML protected ScrollPane graphViewPane;

    //inner-panes
    @FXML protected AnchorPane settingsSlides;
    @FXML protected HBox nextPrev;

    //text flows
    @FXML protected TextFlow subjectsText;
    @FXML protected TextFlow teachersText;
    @FXML protected TextFlow classesText;
    @FXML protected TextFlow rulesText;
    @FXML protected TextFlow engineText;

    @FXML protected Image saveBtn;


    //indicators
    @FXML protected ProgressBar progressBar;
    @FXML protected ProgressIndicator genIndicator;
    @FXML protected ProgressIndicator fitIndicator;
    @FXML protected ProgressIndicator timeIndicator;
    @FXML protected ImageView checkMark;

    //run-tool
    @FXML protected ImageView playBtn;
    @FXML protected ImageView pauseBtn;
    @FXML protected ImageView stopBtn;
    @FXML protected ImageView restartBtn;



    //side labels -- Left
    @FXML protected Label rules;
    @FXML protected Label openFile;
    @FXML protected Label showData;
    @FXML protected Label bestSol;
    @FXML protected Label settings;

    //settings sliders
    @FXML protected Slider genSlide;
    @FXML protected Slider fitSlide;
    @FXML protected Slider timeSlide;
    @FXML protected TextField textTime;
    @FXML protected TextField textFit;
    @FXML protected TextField textGen;

    //Solutions-types
    @FXML protected ToggleButton graphViewBtn;
    @FXML protected ToggleButton rawViewBtn;
    @FXML protected ComboBox teacherViewBtn;
    @FXML protected ComboBox classViewBtn;
    @FXML protected TextFlow rawFlow;
    @FXML protected GridPane teacherGrid;
    @FXML protected GridPane clsGrid;
    @FXML protected Button previous;
    @FXML protected Button next;


    //Chart
    @FXML protected LineChart chart;
    @FXML protected NumberAxis xaxis;
    @FXML protected NumberAxis yaxis;

    //Selection buttons
    @FXML protected ToggleButton trunctionBtn;
    @FXML protected TextField trunctionText;
    @FXML protected ToggleButton rouletteBtn;
    @FXML protected ToggleButton tournamentBtn;
    @FXML protected TextField tournamentText;
    @FXML protected Slider elitism;

    //Mutation buttons
    //Flipping
    @FXML protected ToggleButton flipping;
    @FXML protected ComboBox flippingMenu;
    @FXML protected Slider flippingTup;
    @FXML protected Slider flippingProb;
    @FXML protected ToggleButton flippingActivate;
    //Sizer
    @FXML protected ButtonBar sizerBar;
    @FXML protected ToggleButton sizer;
    @FXML protected ComboBox sizerMenu;
    @FXML protected Slider sizerTup;
    @FXML protected Slider sizerProb;
    @FXML protected ToggleButton sizerActivate;

    //CrossOver buttons
    //Aspect oriented
    @FXML protected ToggleButton aspectOriented;
    @FXML protected ComboBox aspectMenu;
    @FXML protected Slider aspectCut;
    @FXML protected ToggleButton aspectActivate;
    //daytime oriented
    @FXML protected ToggleButton dto;
    @FXML protected Slider dtoCut;
    @FXML protected ToggleButton dtoActivate;

    //animations
    //anim1
    @FXML protected Circle c1;
    @FXML protected Circle c2;
    @FXML protected Circle c3;
    //anim2
    @FXML protected Circle c1e2;
    @FXML protected Circle c2e2;

    protected Pattern pattern;
    protected TextFormatter formatterTrunction;
    protected TextFormatter formatterTournament;
    protected TextFormatter formatterFit;
    protected TextFormatter formatterTime;
    protected TextFormatter formatterGen;
    protected SimpleBooleanProperty isFileSelected;
    protected SimpleBooleanProperty isAlgoRun;
    protected SimpleBooleanProperty lockedLabels;
    protected SimpleBooleanProperty isAspect;
    protected SimpleBooleanProperty isDTO;
    protected SimpleBooleanProperty isPaused;
    protected SimpleBooleanProperty isStarted;
    protected SimpleBooleanProperty isStopped;
    protected SimpleLongProperty durationProp;
    protected boolean alreadyStarted;
    protected final Object lock = new Object();
    protected boolean pause = false;
    protected boolean terminate = false;
}
