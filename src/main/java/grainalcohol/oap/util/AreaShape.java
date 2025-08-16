package grainalcohol.oap.util;

public enum AreaShape {
    CUBE(1, "cube"),      // 立方体
    SPHERE(2, "sphere"),  // 球体
    PRISM(3, "prism"),    // 方柱体(四棱柱)
    CYLINDER(4, "cylinder");    // 圆柱体(双向)

    private final int id;
    private final String name;

    AreaShape(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static AreaShape fromName(String name) {
        if (name == null) return CUBE;
        for (AreaShape shape : values()) {
            if (shape.name.equals(name)) {
                return shape;
            }
        }
        return CUBE;
    }
}
