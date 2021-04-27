import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import jdk.nashorn.internal.runtime.ParserException;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Controller {

    public ComboBox<String> IDApplication;

    public ComboBox<String> RPCBigIcon;
    public TextField RPCBigIcontext;

    public ComboBox<String> RPCSmallIcon;
    public TextField RPCSmallIconText;

    public TextField RPCDescription;

    public TextField RPCState;

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

        //Trenzin que aparece pra digitar o nome do arquivo chato
        TextInputDialog dialog = new TextInputDialog("MyRichPresence1");
        dialog.setTitle("Salvar Configurações");
        dialog.setHeaderText("Escolha o nome para sua nova presença");
        dialog.setContentText("Nome:");

        Optional<String> ArquivoNome = dialog.showAndWait();

        try {
            Map<String, Object> Configs = new HashMap<>();

            if (ArquivoNome.isPresent()) {

                Configs.put("ApplicationID", IDApplication.getValue());

                Configs.put("Description", RPCDescription.getText());
                Configs.put("State", RPCState.getText());

                Configs.put("BigIcon", RPCBigIcon.getValue());
                Configs.put("BigIconText", RPCBigIcontext.getText());

                Configs.put("SmallIcon", RPCSmallIcon.getValue());
                Configs.put("SmallIconText", RPCSmallIconText.getText());

                Configs.put("EndTimeLapse", endTimeStampRadio.isSelected());
                Configs.put("StartTimeLapse", startTimeStampRadio.isSelected());

                Configs.put("CurrentParty", CurrentPartySize.getText());
                Configs.put("MaxParty", MaxPartySize.getText());

                Writer writer = new FileWriter(ArquivoNome.get()+".json");

                Gson gson = new GsonBuilder().setPrettyPrinting().create();

                gson.toJson(Configs, writer);

                writer.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

            RPCDescription.setText(DescriptionLoad);
            RPCState.setText(StateLoad);

            RPCBigIcon.getItems().add(BigIconLoad);
            RPCBigIcontext.setText(BigIconTextLoad);

            RPCSmallIcon.getItems().add(SmallIconLoad);
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
        startRPC.setText("Update Presence");

        DiscordRPC lib = DiscordRPC.INSTANCE;
        DiscordRichPresence presence = new DiscordRichPresence();
        DiscordEventHandlers handlers = new DiscordEventHandlers();


        String applicationId = IDApplication.getValue();
        String steamId = IDApplication.getValue();

        handlers.ready = (user) -> System.out.println("Ready!");
        readylabel.setText("Pronto!!");
        lib.Discord_Initialize(applicationId, handlers, true, steamId);

        presence.details   = RPCDescription.getText();
        presence.state     = RPCState.getText();

        presence.largeImageKey = RPCBigIcon.getValue();
        presence.largeImageText = RPCBigIcontext.getText();

        presence.smallImageKey = RPCSmallIcon.getValue();
        presence.smallImageText = RPCSmallIconText.getText();


        if (startTimeStampRadio.isSelected()) {
            //Passado
            presence.startTimestamp = System.currentTimeMillis() / 1000;
        }
        if (endTimeStampRadio.isSelected()) {
            //restante

            TextInputDialog dialog = new TextInputDialog("1000");
            dialog.setTitle("Tempo de termino");
            dialog.setHeaderText("Escolha o tempo que irá terminar o tempo ocorrido");
            dialog.setContentText("Tempo:");

            Optional<String> GetMs = dialog.showAndWait();
            int MS = Integer.parseInt(GetMs.get());

            presence.endTimestamp   = presence.startTimestamp + System.currentTimeMillis() / MS;
        }

        try {
            int CurrentPartySizeI = Integer.parseInt(CurrentPartySize.getText());
            int MaxPartySizeI = Integer.parseInt(MaxPartySize.getText());

            presence.partySize = CurrentPartySizeI;
            presence.partyMax  = MaxPartySizeI;

        } catch (Exception exception) {
            System.out.println("Parece que não ah Party para fazer");
        }

        lib.Discord_UpdatePresence(presence);

        Thread t = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                lib.Discord_RunCallbacks();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    lib.Discord_Shutdown();
                    break;
                }
            }
        }, "RPC-Callback-Handler");
        t.start();

    }
}
