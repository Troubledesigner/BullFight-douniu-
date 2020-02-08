package walker.core;

import walker.core.typebase.CARD_TYPE;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static walker.core.Utils.getNiu;

public class Player implements Comparable<Player> {
    private int id; // id
    private String name; // 名字
    private int score = 0; // 积分

    private int bet_times = 1; // 下注倍数

    private boolean isReady; // 是否已经准备好

    private List<Card> handCards; // 手牌，限定有且仅有5张。如果不足5张无法比较

    private boolean isOpen; // 开牌

    private Pcard pcard = new Pcard(); // 手牌大小和类型

    private int seat; // 座位编号

    private int revenue = 0; // 营收

    public int getRevenue() {
        return revenue;
    }

    public void setRevenue(int revenue) {
        this.revenue = revenue;
    }

    public int getSeat() {
        return seat;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }

    public Pcard getPcard() {
        return pcard;
    }

    // 一下函数都保证list中的元素为5个，且每个card都是合法的
    public void evalCard() {
        int sum = 0;
        Map<Integer, Integer> identical = new HashMap<>();
        int gold_num = 0;
        int silver_num = 0;
        for (Card c : handCards) {
            if (c.getPoint() > 10) { // if the card is 'J', 'Q', 'K'
                gold_num++;
                silver_num++;
                sum += 10;
            } else {
                sum += c.getPoint();
                if (c.getPoint() == 10) {
                    silver_num++;
                }
            }
            if (identical.containsKey(c.getPoint())) {
                identical.replace(c.getPoint(), identical.get(c.getPoint())+1);
            } else {
                identical.put(c.getPoint(), 1);
            }
        }
        // 炸弹
        for (Integer i : identical.values()) {
            if (i == 4) {
                pcard.type = CARD_TYPE.BOOM;
            }
        }
        // 五小
        if (sum < 10) {
            pcard.type = CARD_TYPE.SMALL5;
        }
        // 金花
        if (gold_num == 5) {
            pcard.type = CARD_TYPE.GOLDFLOWER;
        }
        // 银花
        if (silver_num == 5) {
            pcard.type = CARD_TYPE.SIVIRFLOWER;
        }
        int niu = getNiu(handCards);
        pcard.bull = niu;
        if (niu == 10) {
            pcard.type = CARD_TYPE.BULLFULL;
        } else if (niu == 9) {
            pcard.type = CARD_TYPE.BULL9;
        }
        else if (niu == 8) {
            pcard.type = CARD_TYPE.BULL8;
        }
        else {
            pcard.type = CARD_TYPE.NORMAL;
        }
    }

    @Override
    public int compareTo(Player p2) {
        if (this.pcard == null || p2.getPcard() == null) return 0;
        if (this.handCards.size() != 5 || p2.handCards.size() != 5) {
            return 0;
        }
        // 斗牛算法比较两者的大小
        if (pcard.type.getTimes() > p2.pcard.type.getTimes()) {
            return 1;
        } else if (pcard.type.getTimes() < p2.pcard.type.getTimes()) {
            return -1;
        } else {
            int n1 = this.pcard.bull;
            int n2 = p2.pcard.bull;
            if (n1 > n2) {
                return 1;
            } else if (n1 < n2) {
                return -1;
            } else {
                for (int i = 0; i < 5; i++) {
                    int cmp = this.handCards.get(i).compareTo(p2.getHandCards().get(i));
                    if (cmp != 0) {
                        return cmp;
                    }
                }
            }
        }
        return 0;
    }



    public List<Card> getHandCards() {
        return handCards;
    }

    public void setHandCards(List<Card> handCards) {
        Collections.sort(handCards, new Card());
        this.handCards = handCards;
    }


    public boolean isReady() {
        return isReady;
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }


    public Player(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getBet_times() {
        return bet_times;
    }

    public void setBet_times(int bet_times) {
        this.bet_times = bet_times;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public int getId() {
        return id;
    }


    public String getName() {
        return name;
    }


    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score += score;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(id + "@" + name + ":\n");
        sb.append("下注倍数: " + bet_times + "\n");
        sb.append("分数: " + score +"\n" + "座位号: " + seat + "\n");
        sb.append(Poker.cards2str(handCards));
        sb.append(pcard + "\n");
        sb.append("收入：" + revenue + "\n");
        return sb.toString();
    }
}
