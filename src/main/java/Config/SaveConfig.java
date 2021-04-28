package Config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.scene.control.TextInputDialog;

import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SaveConfig {
    public void ConfigSaver(String IDApplication, String RPCDescription, String RPCState,
                            String RPCBigIcon, String RPCBigIcontext, String RPCSmallIcon,
                            String RPCSmallIconText, boolean endTimeStampRadio, boolean startTimeStampRadio,
                            String CurrentPartySize, String MaxPartySize) {


        TextInputDialog dialog = new TextInputDialog("MyRichPresence1");
        dialog.setTitle("Salvar Configurações");
        dialog.setHeaderText("Escolha o nome para sua nova presença");
        dialog.setContentText("Nome:");

        Optional<String> ArquivoNome = dialog.showAndWait();

        try {
            Map<String, Object> Configs = new HashMap<>();

            if (ArquivoNome.isPresent()) {

                Configs.put("ApplicationID", IDApplication);

                Configs.put("Description", RPCDescription);
                Configs.put("State", RPCState);

                Configs.put("BigIcon", RPCBigIcon);
                Configs.put("BigIconText", RPCBigIcontext);

                Configs.put("SmallIcon", RPCSmallIcon);
                Configs.put("SmallIconText", RPCSmallIconText);

                Configs.put("EndTimeLapse", endTimeStampRadio);
                Configs.put("StartTimeLapse", startTimeStampRadio);

                Configs.put("CurrentParty", CurrentPartySize);
                Configs.put("MaxParty", MaxPartySize);

                Writer writer = new FileWriter(ArquivoNome.get()+".json");

                Gson gson = new GsonBuilder().setPrettyPrinting().create();

                gson.toJson(Configs, writer);

                writer.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
