package com.github.erdragh.jet_suit_additions;

import com.github.erdragh.jet_suit_additions.networking.C2SPackets;
import net.fabricmc.api.ModInitializer;

public class JetSuitAdditions implements ModInitializer {

    public static final String MODID = "jet_suit_additions";


    @Override
    public void onInitialize() {
        C2SPackets.register();
    }


}
