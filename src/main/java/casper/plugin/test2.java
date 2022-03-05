package casper.plugin;

import casper.entity.Common;
import casper.entity.LightningWand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public final class test2 extends JavaPlugin implements Listener {

    public ConsoleCommandSender consol = Bukkit.getConsoleSender();

    public Scoreboard board;
    public Objective obj;

    public boolean buff = false;

    public Location l;
    public double lx;
    public double ly;
    public double lz;

    @Override
    public void onEnable() {
        // 시작시, 작동할 코드
        consol.sendMessage("[플러그인 활성화 중 입니다.]");

        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new Common(this), this);
        getServer().getPluginManager().registerEvents(new LightningWand(this), this);
    }

    @Override
    public void onDisable() {
        // 종료시, 작동할 코드
        consol.sendMessage( "[플러그인 비활성화 중 입니다.]");
    }
}
