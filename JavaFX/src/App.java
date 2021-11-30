import UI.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;


public class App extends Application {

    private static final String ETT_FXML_PATH = "ETT.fxml";


    @Override
    public void start(Stage primaryStage) throws IOException {



        //load fxml
        FXMLLoader loader = new FXMLLoader();
        URL mainFXML = getClass().getResource(ETT_FXML_PATH);
        loader.setLocation(mainFXML);

        Parent root = getRoot(loader);
        Scene scene = new Scene(root, 800   ,600);


        //wiring the controller
        ETTController ettController = loader.getController();
        UIAdapter adapterApp = new UIAdapter();


        ettController.setPrimaryStage(primaryStage);
        ettController.setMainApp(adapterApp);


        //create the scene

        scene.getStylesheets().add(getClass().getResource("Themes/MainTheme.css").toExternalForm());

        primaryStage.getIcons().add(new Image("Assets/Icons/gary.png"));
        primaryStage.setTitle("Evolutionary Time Table");
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.show();
    }

    private Parent getRoot(FXMLLoader fxmlLoader) throws IOException {
        return fxmlLoader.load(fxmlLoader.getLocation().openStream());
    }

    public static void main(String[] args)
    {
        Application.launch(args);
    }
}
