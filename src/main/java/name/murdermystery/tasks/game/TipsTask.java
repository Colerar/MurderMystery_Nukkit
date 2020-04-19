package name.murdermystery.tasks.game;

import cn.nukkit.Player;
import cn.nukkit.scheduler.PluginTask;
import de.theamychan.scoreboard.api.ScoreboardAPI;
import de.theamychan.scoreboard.network.DisplaySlot;
import de.theamychan.scoreboard.network.Scoreboard;
import de.theamychan.scoreboard.network.ScoreboardDisplay;
import name.murdermystery.MurderMystery;
import name.murdermystery.room.Room;


/**
 * 信息显示
 */
public class TipsTask extends PluginTask<MurderMystery> {

    private Room room;

    public TipsTask(MurderMystery owner, Room room) {
        super(owner);
        this.room = room;
    }

    @Override
    public void onRun(int i) {
        if (this.room.getMode() != 2) {
            this.cancel();
        }
        if (this.room.getPlayers().values().size() > 0) {
            int playerNumber = 0;
            for (Integer integer : this.room.getPlayers().values()) {
                if (integer != 0) {
                    playerNumber++;
                }
            }
            String mode;
            for (Player player : this.room.getPlayers().keySet()) {
                switch (this.room.getPlayerMode(player)) {
                    case 1:
                        mode = "平民";
                        break;
                    case 2:
                        mode = "侦探";
                        break;
                    case 3:
                        mode = "杀手";
                        break;
                    default:
                        mode = "死亡";
                        break;
                }
                if (MurderMystery.getInstance().getConfig().getBoolean("计分板显示信息", true)) {
                    Scoreboard scoreboard = ScoreboardAPI.createScoreboard();
                    ScoreboardDisplay scoreboardDisplay = scoreboard.addDisplay(
                            DisplaySlot.SIDEBAR, "MurderMystery", "§eMurderMystery");
                    scoreboardDisplay.addLine("§a当前身份： §l§e" + mode + " ", 0);
                    scoreboardDisplay.addLine("§a剩余时间： §l§e" + this.room.gameTime + " ", 1);
                    scoreboardDisplay.addLine("§a存活人数： §l§e" + playerNumber + " ", 2);
                    if (this.room.getPlayerMode(player) == 3) {
                        scoreboardDisplay.addLine("§a加速冷却： §l§e" + this.room.effectCD + " ", 3);
                    }
                    scoreboard.showFor(player);
                }
                if (MurderMystery.getInstance().getConfig().getBoolean("底部显示信息", true)) {
                    if (this.room.getPlayerMode(player) == 3 && this.room.effectCD > 0) {
                        mode += " 加速冷却剩余：" + room.effectCD + "秒";
                    }
                    player.sendActionBar("§a身份：" + mode + "\n§a距游戏结束还有 "+ this.room.gameTime + " 秒\n当前还有： §e" + playerNumber + " §a人存活");
                }
            }
        }
    }

}
