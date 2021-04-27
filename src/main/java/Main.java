import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Objects;

public class Main extends Application {

    private static final String iconImageLoc =
            "images/AppIconTray.png";

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

        final TrayIcon trayIcon;

        if (SystemTray.isSupported()) {
            SystemTray tray = SystemTray.getSystemTray();

            java.awt.Image image = Toolkit.getDefaultToolkit().getImage("images/AppIconTray.png");

            ActionListener exitListener = e -> System.exit(0);

            ActionListener showApp = new ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                primaryStage.show();
                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }
                        }
                    });
                }
            };

            ActionListener hideApp = e -> Platform.runLater(primaryStage::hide);



            PopupMenu popup = new PopupMenu("menu de opções");

            MenuItem HideApp = new MenuItem("Esconder programa");
            HideApp.addActionListener(hideApp);
            popup.add(HideApp);

            MenuItem ShowApp = new MenuItem("Mostrar programa");
            ShowApp.addActionListener(showApp);
            popup.add(ShowApp);

            MenuItem exitApp = new MenuItem("Sair do App");
            exitApp.addActionListener(exitListener);
            popup.add(exitApp);

            URL imageLoc = new URL(
                    iconImageLoc);

            trayIcon = new TrayIcon(ImageIO.read(imageLoc),"Disorder-RichPresence", popup);

            trayIcon.setImageAutoSize(true);

            try {
                tray.add(trayIcon);

            } catch (AWTException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, "pani no sistema alguem me disconfigurou");
        }
    }

    public static void main(String[] args) {

        launch(args);

    }

}
