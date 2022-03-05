package casper.entity;

import casper.plugin.test2;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.RenderType;
import org.bukkit.scoreboard.Score;

import java.util.Set;

public class Common implements Listener {

    static test2 main;

    public Common()
    {

    }

    public Common(test2 main)
    {
        this.main = main;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();    // 입장한 유저

        player.getWorld().setTime(15000);
        player.setPlayerWeather(WeatherType.CLEAR);
        player.setWalkSpeed(0.5f);
        player.setLevel(0);
        player.setHealth(player.getMaxHealth());
        player.setOp(true);

        for (PotionEffect p : player.getActivePotionEffects()
        ) {
            player.removePotionEffect(p.getType());
        }

        for (LivingEntity l : player.getWorld().getLivingEntities()
        ) {
            if(l.isGlowing())
                l.setGlowing(false);
        }

        for (String tag : player.getScoreboardTags()
             ) {
            player.removeScoreboardTag(tag);
        }

        resetScoreboard();

        player.setScoreboard(main.board);

        main.buff = false;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {

        if(event.getEntity() instanceof  LivingEntity)
        {
            LivingEntity entity = event.getEntity();

            double x = entity.getLocation().getX();
            double y = entity.getLocation().getY();
            double z = entity.getLocation().getZ();
            if((x > (main.lx - 25) && x < (main.lx + 25))
                    && (y > (main.ly - 25) && y < (main.ly + 25))
                    && (z > (main.lz - 25) && z < (main.lz+ 25)))
            {
                if(EntityDamageEvent.DamageCause.LIGHTNING.name().equals(entity.getLastDamageCause().getCause().name()))
                {
                    Score score = main.obj.getScore(entity.getType().toString());

                    score.setScore(score.getScore() + 1);
                }
            }
        }
    }

    public void resetScoreboard() {
        main.board = Bukkit.getScoreboardManager().getNewScoreboard();
        main.obj = main.board.registerNewObjective("ServerName", "dummy", "처치한 적");
        main.obj.setDisplaySlot(DisplaySlot.SIDEBAR);
    }
}
