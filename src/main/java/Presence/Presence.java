package Presence;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import javafx.scene.control.*;

import java.util.Optional;

public class Presence {

    DiscordRPC lib = DiscordRPC.INSTANCE;

    public void StartPresence(String IDApplication, String RPCDescription, String RPCState,
                              String RPCBigIcon, String RPCBigIcontext, String RPCSmallIcon,
                              String RPCSmallIconText, boolean endTimeStampRadio, boolean startTimeStampRadio,
                              String CurrentPartySize, String MaxPartySize, Button startRPC, Label readylabel) {

        startRPC.setText("Update Presence.Presence");

        DiscordRichPresence presence = new DiscordRichPresence();
        DiscordEventHandlers handlers = new DiscordEventHandlers();

        handlers.ready = (user) -> System.out.println("Ready!");
        readylabel.setText("Pronto!!");
        lib.Discord_Initialize(IDApplication, handlers, true, IDApplication);

        presence.details = RPCDescription;
        presence.state = RPCState;

        presence.largeImageKey = RPCBigIcon;
        presence.largeImageText = RPCBigIcontext;

        presence.smallImageKey = RPCSmallIcon;
        presence.smallImageText = RPCSmallIconText;

        if (startTimeStampRadio) {
            //Passado
            presence.startTimestamp = System.currentTimeMillis() / 1000;
        }
        if (endTimeStampRadio) {
            //restante
            TextInputDialog dialog = new TextInputDialog("1000");
            dialog.setTitle("Tempo de termino");
            dialog.setHeaderText("Escolha o tempo que irá terminar o tempo ocorrido");
            dialog.setContentText("Tempo:");

            Optional<String> GetMs = dialog.showAndWait();
            int MS = Integer.parseInt(GetMs.get());

            presence.endTimestamp = presence.startTimestamp + System.currentTimeMillis() / MS;
        }
        try {
            int CurrentPartySizeI = Integer.parseInt(CurrentPartySize);
            int MaxPartySizeI = Integer.parseInt(MaxPartySize);

            presence.partySize = CurrentPartySizeI;
            presence.partyMax = MaxPartySizeI;

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

    public void StopPresence(Button startRPC) {
        lib.Discord_Shutdown();
        startRPC.setText("Start Presence");
    }
}
