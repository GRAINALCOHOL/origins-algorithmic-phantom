package grainalcohol.oap.mixin;

import net.minecraft.advancement.AdvancementProgress;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AdvancementProgress.class)
public interface AdvancementProgressAccessor {
    @Accessor("requirements")
    String[][] getRequirements();
}
