package name.KillerGame.Tasks;

import cn.nukkit.Player;
import cn.nukkit.scheduler.PluginTask;
import name.KillerGame.KillerGame;
import name.KillerGame.Room.Room;

public class GameTipsTask extends PluginTask<KillerGame> {

    private Room room;

    public GameTipsTask(KillerGame owner, Room room) {
        super(owner);
        this.room = room;
    }

    @Override
    public void onRun(int i) {
        if (this.room.getMode() == 2) {
            int j = 0;
            for (Integer integer : this.room.getPlayers().values()) {
                if (integer != 0) {
                    j++;
                }
            }
            this.sendActionBar("§a距游戏结束还有"+ this.room.gameTime + "秒\n当前还有： §e" + j + " §a人存活");
        }else {
            this.cancel();
        }
    }

    private void sendActionBar(String string) {
        String mode;
        for (Player player : this.room.getPlayers().keySet()) {
            if (this.room.getPlayerMode(player) == 1) {
                mode = "平民";
            }else if (this.room.getPlayerMode(player) == 2) {
                mode = "侦探";
            }else if (this.room.getPlayerMode(player) == 3) {
                mode = "杀手";
            }else {
                mode = "死亡";
            }
            player.sendActionBar("§a身份：" + mode + "\n" + string);
        }
    }

}