package Controller;

import Config.SaveConfig;
import Presence.Presence;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import jdk.nashorn.internal.runtime.ParserException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;

public class Controller {

    public ComboBox<String> IDApplication;

    public ComboBox<String> RPCBigIcon;
    public TextField RPCBigIcontext;

    public ComboBox<String> RPCSmallIcon;
    public TextField RPCSmallIconText;

    public TextField RPCState;
    public TextField RPCDescription;

    public Button startRPC;

    public RadioButton startTimeStampRadio;
    public RadioButton endTimeStampRadio;
    public TextField MaxPartySize;
    public TextField CurrentPartySize;

    public TabPane tabela;

    public Tab defaultTab;
    public Tab sequenciaTab;

    public Button ButtonTabSequence;
    public Button ButtonTabDefault;

    public Label readylabel;

    ToggleGroup timelapse = new ToggleGroup();

    public void initRPC() {
        endTimeStampRadio.setToggleGroup(timelapse);
        startTimeStampRadio.setToggleGroup(timelapse);

        createNewPresence();
    }

    public void ChangeSequencia() {
        tabela.getSelectionModel().select(sequenciaTab);
    }
    public void changeToDefault() {
        tabela.getSelectionModel().select(defaultTab);
    }

    public void SaveConfig() {
        SaveConfig saveconfig = new SaveConfig();

        saveconfig.ConfigSaver(IDApplication.getValue(), RPCDescription.getText(), RPCState.getText(),
                RPCBigIcon.getValue(), RPCBigIcontext.getText(), RPCSmallIcon.getValue(),
                RPCSmallIconText.getText(), endTimeStampRadio.isSelected(), startTimeStampRadio.isSelected(),
                CurrentPartySize.getText(), MaxPartySize.getText());
    }

    public void ConfigLoad() throws FileNotFoundException {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json");

        fileChooser.getExtensionFilters().add(extFilter);

        File selectedFile = fileChooser.showOpenDialog(null);
        String Config = selectedFile.getAbsolutePath();

        System.out.println(Config);

        Gson gson = new Gson();

        JsonReader reader = new JsonReader(new FileReader(Config));
        Map<String, Object> jsonObject = gson.fromJson(reader, Map.class);

        try{

            String ApplicationIDLoad = (String) jsonObject.get("ApplicationID");

            String DescriptionLoad = (String) jsonObject.get("Description");
            String StateLoad = (String) jsonObject.get("State");

            String BigIconLoad = (String) jsonObject.get("BigIcon");
            String BigIconTextLoad = (String) jsonObject.get("BigIconText");

            String SmallIconLoad = (String) jsonObject.get("SmallIcon");
            String SmallIconTextLoad = (String) jsonObject.get("SmallIconText");

            boolean StartTimeLapseLoad = (boolean) jsonObject.get("StartTimeLapse");
            boolean EndTimeLapseLoad = (boolean) jsonObject.get("EndTimeLapse");

            String CurrentPartysizeLoad = (String) jsonObject.get("CurrentParty");
            String MaxPartysizeLoad = (String) jsonObject.get("MaxParty");

            IDApplication.getItems().add(ApplicationIDLoad);
            IDApplication.getSelectionModel().selectFirst();

            RPCDescription.setText(DescriptionLoad);
            RPCState.setText(StateLoad);

            RPCBigIcon.getItems().add(BigIconLoad);
            RPCBigIcon.getSelectionModel().selectFirst();
            RPCBigIcontext.setText(BigIconTextLoad);

            RPCSmallIcon.getItems().add(SmallIconLoad);
            RPCSmallIcon.getSelectionModel().selectFirst();
            RPCSmallIconText.setText(SmallIconTextLoad);

            endTimeStampRadio.setSelected(EndTimeLapseLoad);
            startTimeStampRadio.setSelected(StartTimeLapseLoad);

            CurrentPartySize.setText(CurrentPartysizeLoad);
            MaxPartySize.setText(MaxPartysizeLoad);

        } catch (ParserException e) {
            e.printStackTrace();
        }
    }

    public void createNewPresence() {
        Presence presence = new Presence();
        presence.StartPresence(IDApplication.getValue(), RPCDescription.getText(),RPCState.getText(), RPCBigIcon.getValue(),
                RPCBigIcontext.getText(), RPCSmallIcon.getValue(), RPCSmallIconText.getText(),endTimeStampRadio.isSelected(),
                startTimeStampRadio.isSelected(), CurrentPartySize.getText(),MaxPartySize.getText(),startRPC,readylabel);
    }
    public void shutdownRPC() {
        Presence presence = new Presence();
        presence.StopPresence(startRPC);
    }
}

