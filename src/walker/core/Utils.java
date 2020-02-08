package walker.core;

import java.util.*;

class Utils {
    // 得到cards的全部组合，结果输出在ans中
    // con是临时存放中间结果的数组，end是临时变量，index是递归深度，m是每个组合的元素个数也就是con的大小
    static void getCombination(Card[] cards, List<List<Card>> ans, Card[] con, int end, int index, int m) {
        if (end == m) {
            List<Card> l = new ArrayList<>();
            for (int i = 0; i < m; i++) {
                l.add(new Card(con[i]));
            }
            ans.add(l);
        } else {
            if (index >= cards.length) return;
            con[end++] = cards[index];
            getCombination(cards, ans, con, end, index+1, m);
            end--;
            getCombination(cards, ans, con, end, index+1, m);
        }
    }
    // 返回5个元素的所有的包含3个元素的组合
    private static List<List<Card>> get3of5(List<Card> cards) {
        List<List<Card>> ans = new ArrayList<>();
        boolean[] chosen = new boolean[5];
        Card[] con = new Card[5];
        Card[] array_cards = new Card[5];
        for (int i = 0; i < cards.size(); i++) {
            array_cards[i] = cards.get(i);
        }
        getCombination(array_cards, ans, con, 0,0, 3);
        // for debug
//        for (List<Card> l : ans) {
//            System.out.print("{ ");
//            for (Card c : l) {
//                System.out.print(c + ", ");
//            }
//            System.out.println(" }");
//        }
        return ans;
    }


    static List<Card> getRestCard(List<Card> cards, List<Card> discard) {
        List<Card> ans = new ArrayList<>();
        for (Card c : cards) {
            boolean find = false;
            for (Card d : discard) {
                if (c.compareTo(d) == 0) {
                    find = true;
                    break;
                }
            }
            if (!find) {
                ans.add(c);
            }
        }

        // for debug

//        System.out.print("Total: { ");
//        for (Card c : cards) {
//            System.out.print(c + ", ");
//        }
//        System.out.print(" }\nRest: { ");
//        for (Card c : ans) {
//            System.out.print(c + ", ");
//        }
//        System.out.println(" }");
        return ans;
    }



    // 找到三张牌相加为10的倍数，然后结果为剩余两张牌的余数
    public static int getNiu(List<Card> cards) {
        int res = 0; // indicate no bull
        List<List<Card>> threes = get3of5(cards);

        for (List<Card> l : threes) {
            int sum = 0;
            for (Card c : l) {
                if (c.getPoint() > 10) {
                    sum += 10;
                } else {
                    sum += c.getPoint();
                }
            }
            if (sum % 10 == 0) { // 如果是10的倍数
                List<Card> rest = getRestCard(cards, l); // 得到剩余的牌
                if (rest.get(0).getPoint() > 10) {
                    sum = 10;
                } else {
                    sum = rest.get(0).getPoint();
                }

                if (rest.get(1).getPoint() > 10) {
                    sum += 10;
                } else {
                    sum += rest.get(1).getPoint();
                }
                // 比较，是否当前计算的牛，比之前的要大
                if (sum % 10 == 0) { // 牛牛
                    res = 10;
                    break; // 退出
                } else {
                    sum %= 10;
                    if (sum > res) {
                        res = sum;
                    }
                    break; // 不存在更大的情况
                }
            }
        }
        return res;
    }

}
