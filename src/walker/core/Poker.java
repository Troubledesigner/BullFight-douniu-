package walker.core;

import walker.core.typebase.Suit;

import java.util.*;


public class Poker {
    private List<Card> cards = new ArrayList<>(); // 用来记录当前剩余的扑克数量

    Random rand = new Random();

    public Poker() {
        // 初始化一幅牌
        refresh();
    }

    // 洗牌, 复杂度为O(n)
    public void shuffle() {
        for (int i = 0; i < cards.size(); i++) {
            swapAt(i, randRange(i, cards.size()));
        }
    }

    private int randRange(int min, int max) {
        return rand.nextInt(max - min) + min;
    }

    private void swapAt(int i, int j) {
        Card temp = cards.get(i);
        cards.set(i, cards.get(j));
        cards.set(j, temp);
    }


    // 刷新牌，重新恢复所有的牌
    public void refresh() {
        for (int i = 0; i < 4; i++) {
            for (int j = 1; j <= 13; j++) {
                cards.add(new Card(Suit.values()[i], j));
            }
        }
        shuffle();
    }

    // 从牌堆中抽取最后一张牌
    public Card getCard() {
        if (cards.size() <= 0) {
            refresh();
        }
        return cards.remove(cards.size()-1);
    }

    public List<Card> get5Cards() {
        List<Card> list = new ArrayList<>();
        int i = 0;
        while (i < 5) {
            Card c = getCard();
            if (!list.contains(c)) {
                list.add(c);
                i++;
            }
        }
        return list;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cards.size(); i++) {
            sb.append(cards.get(i).toString() + "  ");
            if ((i+1)%20 == 0) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    public static String cards2str(List<Card> list) {
        try {
            StringBuilder sb = new StringBuilder("{ ");
            for (Card c : list) {
                sb.append(c + ", ");
            }
            sb.append(" }\n");
            return sb.toString();
        } catch (Exception e) {

        }
        return null;
    }

    // Poker simple test
//    public static void main(String[] args) {
//        Poker poker = new Poker();
//        System.out.println(poker);
////        int time = 100;
////        for (int i = 0; i < time; i++) {
////            System.out.print(poker.getCard() + "  ");
////            if ((i+1) % 20 == 0) {
////                System.out.println();
////            }
////        }
//        int p1_win = 0;
//        int win = 0;
//        double rate = 0.0;
//        int time = 1000;
//        for (int k = 0; k < time; k++) {
//            List<Card> list = poker.get5Cards();
//            List<Card> list2 = poker.get5Cards();
//
//            Cmp_res ans = cardsCmp(list, list2);
//            if (ans.cmp != 0) {
//                win++;
//            }
//            if (ans.cmp == 1) {
//                p1_win++;
//            }
//            System.out.println(ans);
//        }
//        rate =(double) p1_win / win;
//        System.out.println(rate);
//    }
}
