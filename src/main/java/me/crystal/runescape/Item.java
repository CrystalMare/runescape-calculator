package me.crystal.runescape;

/**
 * @author Sven Olderaan (admin@heaven-craft.net)
 */
public enum Item {
    PERFECT_JUJU_PRAYER_3(32797),
    JUJU_FISHING_4(20019),
    HARMONY_MOSS(32947)
    ;

    private final int id;

    Item(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
