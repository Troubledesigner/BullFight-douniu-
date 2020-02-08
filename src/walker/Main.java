package walker;

import walker.core.Player;
import walker.core.Room;

public class Main {
    public static void main(String[] argv) {
        Player p1 = new Player(1111, "光机");
        Player p2 = new Player(222, "胖子");
        Player p3 = new Player(33, "鸡婆");
        Player p4 = new Player(22, "李哲");
        Player p5 = new Player(1, "Mike");
        Player p6 = new Player(90, "Nike");
        p2.setBet_times(5);
        p3.setBet_times(10);

        int room_size = 10;
        int match_nums = 10;
        int bottom_score = 50;
        int min_score = 2;
        int limit_switch_banker = 150;
        Room room = new Room(p1, room_size, match_nums, bottom_score, min_score, limit_switch_banker);
        room.enter(p2);
        room.enter(p3);
        room.enter(p4);
        room.enter(p5);
        room.enter(p6);
        room.exit(p2);
        room.start();
        room.printPlayersInfo();
        for (int i = 0; i < 10; i++) {
            room.singleMatch();

            room.printPlayersInfo();
            room.printRoomInfo();
            System.out.println();
            System.out.println();
        }
    }
}
