package walker.core.typebase;

public enum CARD_TYPE {
    NORMAL(1), BULL8(2), BULL9(3), BULLFULL(4), SIVIRFLOWER(5), GOLDFLOWER(6), SMALL5(7), BOOM(8);
    int times;

    CARD_TYPE(int times) {
        this.times = times;
    }

    public int getTimes() {
        return times;
    }

    @Override
    public String toString() {
        switch (this) {
            case NORMAL: return "常规牛";
            case BULL8: return "牛八";
            case BULL9: return "牛九";
            case BULLFULL: return "牛牛";
            case SIVIRFLOWER: return "银花";
            case GOLDFLOWER: return "金花";
            case SMALL5: return "五小";
            default: return null;
        }
    }
}
