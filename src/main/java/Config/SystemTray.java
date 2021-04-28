package Config;

import javafx.application.Platform;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class SystemTray {

    private final PopupMenu popup = new PopupMenu("menu de opções");

    private final MenuItem openItem   = new MenuItem("Open");
    private final MenuItem HideApp = new MenuItem("Esconder");
    private final MenuItem exitApp = new MenuItem("Sair");

    private static final String ImageTrayURL =
            "https://github.com/ChickChuck2/Discord-RichPresence/blob/master/src/main/resources/images/AppIconTray.png?raw=true";

    public SystemTray(Stage primaryStage) throws IOException {
        final TrayIcon trayIcon;

        if (java.awt.SystemTray.isSupported()) {
            java.awt.SystemTray tray = java.awt.SystemTray.getSystemTray();

            exitApp.addActionListener( e -> {
                System.exit(0);
                System.out.println("ok");
            });

            openItem.addActionListener(e -> {
                Platform.runLater(primaryStage::show);
                System.out.println("ok");
            });

            HideApp.addActionListener(e -> {
                Platform.runLater(primaryStage::hide);
                System.out.println("ok");
            });

            popup.add(HideApp);
            popup.add(openItem);
            popup.add(exitApp);

            URL Image = new URL(
                    ImageTrayURL);

            trayIcon = new TrayIcon(ImageIO.read(Image),"Disorder-RichPresence", popup);

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
}
