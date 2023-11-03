package io.github.andrew6rant.tir.mixin.client;

import net.minecraft.client.render.model.json.JsonUnbakedModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(JsonUnbakedModel.GuiLight.class)
public class JsonUnbakedModelGuiLightMixin {
    /**
     * @author Andrew6rant (Andrew Grant)
     * @reason force GuiLight to be front-facing
     */
    @Overwrite
    public boolean isSide() {
        return false;
    }
}
