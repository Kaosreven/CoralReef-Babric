package com.primetoxinz.coralreef.mixin;

import com.primetoxinz.coralreef.listener.CommonListener;
import net.minecraft.block.Block;
import net.minecraft.entity.living.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerAirMixin {

    @Inject(method = "tick", at = @At("TAIL"))
    private void restoreAirNearCoral(CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity)(Object)this;
        if (player.level == null || CommonListener.CORALS.isEmpty()) return;

        int px = (int) Math.floor(player.x);
        int py = (int) Math.floor(player.y);
        int pz = (int) Math.floor(player.z);

        for (int dx = -2; dx <= 2; dx++) {
            for (int dy = -2; dy <= 2; dy++) {
                for (int dz = -2; dz <= 2; dz++) {
                    int blockId = player.level.getBlockID(px + dx, py + dy, pz + dz);
                    for (Block coral : CommonListener.CORALS) {
                        if (blockId == coral.id) {
                            player.air = 300;
                            return;
                        }
                    }
                }
            }
        }
    }
}
