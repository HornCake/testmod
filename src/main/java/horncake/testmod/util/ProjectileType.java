package horncake.testmod.util;

public enum ProjectileType {
    BASIC(0),
    BOUND(1);

    private final int id;

    private ProjectileType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static ProjectileType getType(int i) {
        for(ProjectileType type : ProjectileType.values()) {
            if(type.getId() == i) return type;
        }
        return BASIC;
    }
}
