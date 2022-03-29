package com.example.examplemod.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkAccess.class)
public abstract class MixinChunkAccess {

    @Shadow
    @Final
    protected LevelHeightAccessor levelHeightAccessor;

    @Shadow public abstract ChunkPos getPos();

    @Shadow private long inhabitedTime;

    @Inject(at = @At("HEAD"), method = "incrementInhabitedTime", cancellable = true)
    private void init(long $$0, CallbackInfo ci) {
        if (this.levelHeightAccessor instanceof ServerLevel serverLevel) {
            for (ServerPlayer player : serverLevel.players()) {
                if (ChunkPos.asLong(player.blockPosition()) == this.getPos().toLong()) {
                    return;
                }
            }
            ci.cancel();
        }
    }
}