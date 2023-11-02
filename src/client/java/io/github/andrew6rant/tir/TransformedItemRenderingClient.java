package io.github.andrew6rant.tir;

import eu.midnightdust.lib.config.MidnightConfig;
import io.github.andrew6rant.tir.config.Config;
import net.fabricmc.api.ClientModInitializer;

public class TransformedItemRenderingClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		MidnightConfig.init("tir", Config.class);
	}
}