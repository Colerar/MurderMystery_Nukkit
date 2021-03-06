package cn.lanink.murdermystery.tasks.game;

import cn.lanink.murdermystery.MurderMystery;
import cn.lanink.murdermystery.room.Room;
import cn.lanink.murdermystery.tasks.VictoryTask;
import cn.lanink.murdermystery.tasks.WaitTask;
import cn.lanink.murdermystery.utils.Tools;
import cn.nukkit.Player;
import cn.nukkit.level.Sound;
import cn.nukkit.scheduler.PluginTask;
import me.onebone.economyapi.EconomyAPI;

import java.util.Map;

/**
 * 游戏时间计算
 */
public class TimeTask extends PluginTask<MurderMystery> {

    private final Room room;

    public TimeTask(MurderMystery owner, Room room) {
        super(owner);
        this.room = room;
    }

    public void onRun(int i) {
        if (this.room.getMode() != 2) {
            this.cancel();
        }
        //计时与胜利判断
        if (room.gameTime > 0) {
            room.gameTime--;
            int playerNumber = 0;
            boolean killer = false;
            for (Integer integer : room.getPlayers().values()) {
                if (integer != 0) {
                    playerNumber++;
                }
                if (integer == 3) {
                    killer = true;
                }
            }
            if (killer) {
                if (playerNumber < 2) {
                    victory(3);
                }
            }else {
                victory(1);
            }
        }else {
            victory(1);
        }
        //开局10秒后给物品
        if (room.gameTime >= room.getGameTime()-10) {
            int time = room.gameTime - (room.getGameTime() - 10);
            if (time <= 5 && time >= 1) {
                this.sendMessage("§e杀手将在" + time + "秒后拿到剑！");
                Tools.addSound(room, Sound.RANDOM_CLICK);
            }else if (time < 1) {
                this.sendMessage("§e杀手已拿到剑！");
                for (Map.Entry<Player, Integer> entry : room.getPlayers().entrySet()) {
                    if (entry.getValue() == 2) {
                        Tools.giveItem(entry.getKey(), 1);
                    }else if (entry.getValue() == 3) {
                        Tools.giveItem(entry.getKey(), 2);
                    }
                }
            }
        }
        //杀手CD计算
        if (room.effectCD > 0) {
            room.effectCD--;
        }
        if (room.swordCD > 0) {
            room.swordCD--;
        }
    }

    private void sendMessage(String string) {
        for (Player player : this.room.getPlayers().keySet()) {
            player.sendMessage(string);
        }
    }

    private void victory(int victoryMode) {
        if (this.room.getPlayers().values().size() > 0) {
            this.room.setMode(3);
            for (Map.Entry<Player, Integer> entry : this.room.getPlayers().entrySet()) {
                if (victoryMode == 3) {
                    entry.getKey().sendTitle("§a杀手获得胜利！", "", 10, 30, 10);
                    if (entry.getValue() == 3) {
                        int money = owner.getConfig().getInt("杀手胜利奖励", 0);
                        if (money > 0) {
                            EconomyAPI.getInstance().addMoney(entry.getKey(), money);
                            entry.getKey().sendMessage("§a你获得了胜利奖励: " + money + " 元");
                        }
                    }
                    continue;
                }else if (entry.getValue() == 1 || entry.getValue() == 2) {
                    int money = owner.getConfig().getInt("平民胜利奖励", 0);
                    if (money > 0) {
                        EconomyAPI.getInstance().addMoney(entry.getKey(), money);
                        entry.getKey().sendMessage("§a你获得了胜利奖励: " + money + " 元");
                    }
                }
                entry.getKey().sendTitle("§a平民和侦探获得胜利！", "", 10, 30, 10);
            }
            owner.getServer().getScheduler().scheduleRepeatingTask(
                    MurderMystery.getInstance(), new VictoryTask(MurderMystery.getInstance(), this.room, victoryMode), 20,true);
        }else {
            this.room.setMode(1);
            owner.getServer().getScheduler().scheduleRepeatingTask(
                    MurderMystery.getInstance(), new WaitTask(MurderMystery.getInstance(), this.room), 20,true);
        }
        this.cancel();
    }

}
