package cn.grainalcohol.power;

import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;

/**
 * 类型ID: oap:prevent_movement_axis<br>
 * <br>
 * 阻止能力持有者在特定轴向上的移动<br>
 * 就像被方块阻挡一样<br>
 * <br>
 * <b>注意：</b>
 * <ul>
 *     <li>能力持有者会将此能力同步给坐骑，同步范围包括骑乘塔（Riding Tower）中的所有乘客</li>
 *     <li>即坐骑的移动会受到所有乘客所持有的所有此能力的影响</li>
 * </ul>
 *
 * <p><b>JSON字段说明:</b></p>
 * <ul>
 *   <li><b>prevent_positive_x</b> ({@code boolean}): 是否阻止正X轴方向的移动。默认为false</li>
 *   <li><b>prevent_negative_x</b> ({@code boolean}): 是否阻止负X轴方向的移动。默认为false</li>
 *   <li><b>prevent_positive_y</b> ({@code boolean}): 是否阻止正Y轴方向的移动(向上)。默认为false</li>
 *   <li><b>prevent_negative_y</b> ({@code boolean}): 是否阻止负Y轴方向的移动(向下)。默认为false</li>
 *   <li><b>prevent_positive_z</b> ({@code boolean}): 是否阻止正Z轴方向的移动。默认为false</li>
 *   <li><b>prevent_negative_z</b> ({@code boolean}): 是否阻止负Z轴方向的移动。默认为false</li>
 * </ul>
 *
 * @see cn.grainalcohol.mixin.EntityMixin 实际触发逻辑的Mixin类
 */
public class PreventMovementAxisPower extends Power {
    public static final SerializableData DATA = new SerializableData()
            .add("prevent_positive_x", SerializableDataTypes.BOOLEAN, false)
            .add("prevent_negative_x", SerializableDataTypes.BOOLEAN, false)
            .add("prevent_positive_y", SerializableDataTypes.BOOLEAN, false)
            .add("prevent_negative_y", SerializableDataTypes.BOOLEAN, false)
            .add("prevent_positive_z", SerializableDataTypes.BOOLEAN, false)
            .add("prevent_negative_z", SerializableDataTypes.BOOLEAN, false);

    private final boolean preventPositiveX;
    private final boolean preventNegativeX;
    private final boolean preventPositiveY;
    private final boolean preventNegativeY;
    private final boolean preventPositiveZ;
    private final boolean preventNegativeZ;

    public PreventMovementAxisPower(PowerType<?> type, LivingEntity entity,
                                    boolean preventPositiveX, boolean preventNegativeX,
                                    boolean preventPositiveY, boolean preventNegativeY,
                                    boolean preventPositiveZ, boolean preventNegativeZ) {
        super(type, entity);
        this.preventPositiveX = preventPositiveX;
        this.preventNegativeX = preventNegativeX;
        this.preventPositiveY = preventPositiveY;
        this.preventNegativeY = preventNegativeY;
        this.preventPositiveZ = preventPositiveZ;
        this.preventNegativeZ = preventNegativeZ;
    }

    public Vec3d filterMovement(Vec3d movement) {
        double x = movement.x;
        double y = movement.y;
        double z = movement.z;

        if (preventPositiveX && x > 0) x = 0;
        if (preventNegativeX && x < 0) x = 0;
        if (preventPositiveY && y > 0) y = 0;
        if (preventNegativeY && y < 0) y = 0;
        if (preventPositiveZ && z > 0) z = 0;
        if (preventNegativeZ && z < 0) z = 0;

        return new Vec3d(x, y, z);
    }
}
