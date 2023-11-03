package io.github.andrew6rant.tir.mixin.client;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.andrew6rant.tir.interfaces.TransformationInterface;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.render.model.json.Transformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {
    /* This doesn't work, @WrapWithCondition annotation is targeting an instruction with a non-void return type
    I'll fix it later
    @WrapWithCondition(
            method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/model/json/ModelTransformation;getTransformation(Lnet/minecraft/client/render/model/json/ModelTransformationMode;)Lnet/minecraft/client/render/model/json/Transformation;"))
        public boolean tir$skipNonGUIRendering(ModelTransformation instance, ModelTransformationMode renderMode) {
            return renderMode != ModelTransformationMode.GUI;
    }*/

    @WrapOperation(
            method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/model/json/ModelTransformation;getTransformation(Lnet/minecraft/client/render/model/json/ModelTransformationMode;)Lnet/minecraft/client/render/model/json/Transformation;"))
    public Transformation tir$skipNonGUIRendering(ModelTransformation instance, ModelTransformationMode renderMode, Operation<Transformation> original) {
        if (renderMode == ModelTransformationMode.GUI) {
            //System.out.println("gui");
            return Transformation.IDENTITY;
        } else {
            //System.out.println("else");
            return original.call(instance, renderMode);
        }
    }

    /*@ModifyArgs(
            method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V",
            at = @At(value = "HEAD"))
    public void tir$skipNonGUIRendering(Args args) {
        if (args.get(1) == ModelTransformationMode.GUI) {
            args.set()
        }
    }*/



    @Inject(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V",
    at = @At(target = "Lnet/minecraft/client/render/model/BakedModel;getTransformation()Lnet/minecraft/client/render/model/json/ModelTransformation;", value = "INVOKE", shift = At.Shift.BEFORE))
    public void tir$renderGUI(ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model, CallbackInfo ci) {
        if (renderMode == ModelTransformationMode.GUI) {
            ((TransformationInterface)model.getTransformation().getTransformation(renderMode)).tir_apply(matrices);
        }
    }

    /*@Redirect(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V",
    at = @At(target = "Lnet/minecraft/client/render/model/json/Transformation;apply(ZLnet/minecraft/client/util/math/MatrixStack;)V", value = "INVOKE"))
    public void renderItem(Transformation instance, boolean leftHanded, MatrixStack matrices) {
        // do nothing, I already reimplement this in the above method
    }*/
}
