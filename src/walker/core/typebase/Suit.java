package walker.core.typebase;

public enum  Suit implements Comparable<Suit> {
    SPADE("黑桃", 3), HEART("红桃", 2), DIAMOND("方块", 1), CLUB("梅花", 0);
    private String name;
    private int index;

    Suit(String s, int index) {
        this.name = s;
        this.index = index;
    }

    public int cmp(Suit s) {
        if (this.index > s.index) {
            return 1;
        } else if (this.index < s.index) {
            return -1;
        } else {
            return 0;
        }
    }

    public static String getName(int index) {
        for (Suit s : Suit.values()) {
            if (s.getIndex() == index) {
                return s.name;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public  String toString() {
        return this.name;
    }
}
