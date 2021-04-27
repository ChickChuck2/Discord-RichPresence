import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("Main.fxml")));
        primaryStage.setTitle("Disorder RichPresence");
        primaryStage.getIcons().add(new Image("images/AppIcon.png"));
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();

        primaryStage.setOnCloseRequest(t -> {
            primaryStage.close();
            Platform.exit();
            System.exit(0);
        });

    }

    public static void main(String[] args) {

        launch(args);

    }

}
