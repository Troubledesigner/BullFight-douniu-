package walker.core;


import com.sun.tools.javac.util.Assert;
import walker.core.typebase.CARD_TYPE;
import walker.core.typebase.Suit;

import java.util.Comparator;

public class Card implements Comparable<Card>, Comparator<Card> {
    private Suit suit;
    private int point;

    public static String point2str(int point) {
        if (point >= 2 && point <= 10) {
            return Integer.toString(point);
        } else {
            Assert.check(point <= 13, "Point is out of range");
            switch(point) {
                case 1: return "A";
                case 11: return "J";
                case 12: return "Q";
                case 13: return "K";
            }
        }
        return null;
    }

    @Override
    public int compareTo(Card c) {
        if (c.point == this.point) {
            return this.suit.cmp(c.suit);
        } else if(this.point > c.point) {
            return 1;
        } else {
            return -1;
        }
    }

    @Override
    public int compare(Card c1, Card c2) {
        if (c1.point == c2.point) {
            return c2.suit.cmp(c1.suit);
        } else if(c2.point > c1.point) {
            return 1;
        } else {
            return -1;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card c = (Card) o;
        return c.suit == suit && c.point == point;
    }

    public Card(Suit s, int point) {
        this.suit = s;
        this.point = point;
    }

    public Card(Card c) {
        this.suit = c.suit;
        this.point = c.point;
    }

    public Card() {

    }

    public Suit getSuit() {
        return suit;
    }

    public int getPoint() {
        return point;
    }

    @Override
    public String toString() {
        return suit + ": " + point2str(point);
    }
}

class Pcard {
    int bull = 0;
    CARD_TYPE type = CARD_TYPE.NORMAL;

    @Override
    public String toString() {
        if (type == CARD_TYPE.NORMAL) {
            return "牛"+bull;
        } else {
            return type.toString();
        }
    }
}