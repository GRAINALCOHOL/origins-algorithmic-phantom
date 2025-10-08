package grainalcohol.oap.mixin;

import grainalcohol.oap.api.PityDataHolder;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(BlockEntity.class)
public class BlockEntityMixin implements PityDataHolder {
    @Unique private Map<String, Integer> pityData = new HashMap<>();

    @Inject(method = "writeNbt", at = @At("HEAD"))
    private void writePityDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        if (!pityData.isEmpty()) {
            NbtCompound pityCompound = new NbtCompound();
            pityData.forEach(pityCompound::putInt);
            nbt.put("PityData", pityCompound);
        }
    }

    @Inject(method = "readNbt", at = @At("HEAD"))
    private void readPityDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("PityData")) {
            NbtCompound pityCompound = nbt.getCompound("PityData");
            pityCompound.getKeys().forEach(key -> pityData.put(key, pityCompound.getInt(key)));
        }
    }

    @Override
    public int oap$getPityCount(String poolId) {
        return pityData.getOrDefault(poolId, 0);
    }

    @Override
    public void oap$incrementPity(String poolId) {
        pityData.put(poolId, oap$getPityCount(poolId) + 1);
    }

    @Override
    public void oap$resetPity(String poolId) {
        pityData.put(poolId, 0);
    }
}
