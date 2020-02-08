package walker.core;

import com.sun.tools.javac.util.Assert;

import java.util.ArrayList;
import java.util.List;

public class Room {

    final int room_size; // 房间大小，可以容纳的最大玩家数

    final int match_nums; // 对局数

    int rest_match_nums; // 剩余对局数

    private List<Player> players = new ArrayList<>(); // 玩家
    private Poker poker = new Poker(); // 扑克牌

    private Player banker; // 庄家，从创建房间的那个人开始轮流当庄
    private int banker_index; // 庄家的索引

    boolean game_start = false; // 记录对局是否已经开始

    boolean can_start = false; // 只有所有人（除庄家外）都准备好了，庄家才可以开始

    final int banker_bottom_score; // 庄家底分大小

    final int min_score; // 玩家的下注的基数，可以是x1, x2, x4

    final int switch_banker_limit; // 下庄要求的台面积分

    int current_bottom_score;

    // @param: 房主，房间大小，对局数，台面上初始分数，基础下注分数，收庄分数
    public Room(Player player, int room_size, int match_nums, int banker_bottom_score, int min_score, int switch_banker_limit) {
        banker = player;
        this.room_size = room_size;
        this.match_nums = match_nums;
        this.rest_match_nums = match_nums;
        this.banker_bottom_score = banker_bottom_score;
        this.min_score = min_score;
        this.switch_banker_limit = switch_banker_limit;

        this.current_bottom_score = banker_bottom_score;
        players.add(player);
    }

    public void start() {
        if (!can_start) return;
        if (players.size() <= 1) { // 玩家数量不够
            return; // make a toast: players are no enough
        }
        game_start = true;
        banker.setScore(-banker_bottom_score);
    }

    public void exchange_seat(int pos1, int pos2) {
        if (game_start) return;
        if (pos1 < 0 || pos1 >= players.size() || pos2 < 0 || pos2 >= players.size()) {
            return;
        }
        players.get(pos1).setSeat(pos2);
        players.get(pos2).setSeat(pos1);

        Player p = players.get(pos1);
        players.set(pos1, players.get(pos2));
        players.set(pos2, p);

    }

    // 简单版中不提供换座位的功能
    public void enter(Player p) {
        if (p == null) return;
        players.add(p);
        p.setSeat(players.size()-1);
    }

    public void exit(Player p) {
        if (p == null) return;
        players.remove(p);
    }

    public void ready(Player p) {
        if (p == banker) return;
        p.setReady(true);
        if (checkReady()) {
            can_start = true;
        }
    }

    public void unready(Player p) {
        if (p == banker) return;
        p.setReady(false);
        can_start = false;
    }

    private boolean checkReady() {
        for (Player p : players) {
            if (!p.isReady()) {
                return false;
            }
        }
        return true;
    }
    // 游戏结束
    private void end() {
        game_start = false;
    }

    // 单一对局
    public void singleMatch() {
        if (!game_start) return; // 游戏没开始，直接退出

        // 发牌
        // 先发庄家
        banker.setHandCards(poker.get5Cards());
        int i = (banker_index+1) % players.size(); // 从庄家的下一家开始发起
        while(i != banker_index) {
            players.get(i).setHandCards(poker.get5Cards());
            i = (i+1) % players.size();
        }
        // 计算庄家的牛
        banker.evalCard();
        // 与庄家比较大小
        i = (banker_index+1) % players.size();
        while (i != banker_index) {
            Player p = players.get(i);
            p.evalCard();
            int res = banker.compareTo(p);
            if (res >= 0) {
                p.setRevenue(-p.getBet_times() * min_score * banker.getPcard().type.getTimes());
            } else {
                // 点数更大的先收钱
                p.setRevenue(p.getBet_times() * min_score * p.getPcard().type.getTimes());
            }
            i = (i + 1) % players.size();
        }
        // 台子上钱的支出计算
        if (handle_revenues()) {
            switch_banker(); // 下庄
        }
        rest_match_nums--; // 游戏局数减一
        if (rest_match_nums == 0) {
            end(); // 结束游戏
        }
    }

    public int getScore(int pos) {
        if (pos < 0 || pos >= players.size())
            return 0;
        return players.get(pos).getScore();
    }



    public List<Integer> getScores() {
        List<Integer> list = new ArrayList<>();
        for (Player p : players) {
            list.add(p.getScore());
        }
        return list;
    }

    public void printRoomInfo() {
        System.out.println("剩余对局数：" + rest_match_nums);
        System.out.println("台上剩余的钱：" + current_bottom_score);
        System.out.println("下庄需要的钱：" + switch_banker_limit);
        System.out.println("底分：" + min_score);
    }
    public void printPlayersInfo() {
        for (int i = 0; i < players.size(); i++) {
            Player p = players.get(i);
            System.out.println(p);

        }

    }

    private boolean handle_revenues() {
        int pre_current_bottom_score = current_bottom_score;
        Player[] ps = new Player[room_size];
        for (int i = 0; i < players.size(); i++) {
            ps[i] = players.get(i);
        }
        for (int i = 0; i < players.size(); i++) {
            for (int j = i+1; j < players.size(); j++) {
                if (ps[i].compareTo(ps[j]) < 0) {
                    Player tp = ps[i];
                    ps[i] = ps[j];
                    ps[j] = tp;
                }
            }
        }

//        for (int i = 0; i < players.size(); i++) {
//            System.out.print(ps[i].getName() +" " +ps[i].getPcard() + ", ");
//        }
//        System.out.println();

        for (int i = 0; i < players.size(); i++) {
            if (ps[i] == banker) continue; // 如果是庄家，跳过
            if (current_bottom_score > ps[i].getRevenue()) {
                current_bottom_score -= ps[i].getRevenue();
                ps[i].setScore(ps[i].getRevenue());
            } else {
                ps[i].setScore(+current_bottom_score);
                current_bottom_score = 0;
                return true;
            }
        }

        Assert.check(current_bottom_score >= 0, "Current_bottom_score " + current_bottom_score);
        banker.setRevenue(current_bottom_score - pre_current_bottom_score);
        // 达到下庄的要求
        if (current_bottom_score >= switch_banker_limit) {
            return true;
        } else
            return false;
    }


    // 逆时针方向下一个玩家为庄家，因此players数组中的排列是逆时针排列
    private void switch_banker() {
        System.out.println("Switch banker");
        if (current_bottom_score > 0) {
            banker.setScore(current_bottom_score);
        }
        banker_index = (banker_index+1) % players.size();
        banker = players.get(banker_index);
        banker.setScore(-banker_bottom_score);
        current_bottom_score = banker_bottom_score;
    }
}
