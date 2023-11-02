package io.github.andrew6rant.tir.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.json.Transformation;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Transformation.class)
public class TransformationMixin {

    @Shadow @Final public static Transformation IDENTITY;

    @Shadow @Final public Vector3f rotation;

    @Shadow @Final public Vector3f scale;

    @Inject(method = "apply(ZLnet/minecraft/client/util/math/MatrixStack;)V", at = @At("HEAD"), cancellable = true)
    public void apply(boolean leftHanded, MatrixStack matrices, CallbackInfo ci) {
        if ((Object)this == IDENTITY) {
            return;
        }
        MinecraftClient client = MinecraftClient.getInstance();
        if (!client.mouse.isCursorLocked()) {
            int i = (int)(((client.mouse.getX() - (client.getWindow().getWidth() / 2)) / (double)client.getWindow().getScaledWidth()) * 5.0d);
            int j = (int)(((client.mouse.getY() - (client.getWindow().getHeight() / 2)) / (double)client.getWindow().getScaledHeight()) * 5.0d);

            //float f = this.rotation.x();
            //float g = this.rotation.y();
            float h = this.rotation.z();

            matrices.multiply(new Quaternionf().rotationXYZ(j * ((float)Math.PI / 180), i * ((float)Math.PI / 180), h * ((float)Math.PI / 180)));
            matrices.scale(this.scale.x(), this.scale.y(), this.scale.z());
            ci.cancel();
        }
    }
}
