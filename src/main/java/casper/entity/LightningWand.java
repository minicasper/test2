package casper.entity;

import casper.plugin.test2;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.bukkit.Statistic.PLAYER_KILLS;

public class LightningWand implements Listener {

    static test2 main;

    static String LIGHTNING_WAND = "lightning wand";

    public LightningWand()
    {

    }

    public LightningWand(test2 main)
    {
        this.main = main;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {

        List<String> lores = new ArrayList<String>();
        lores.add("라이트닝 원드");
        lores.add("선택한 지점에 번개가 떨어집니다.");

        Player player = e.getPlayer();    // 입장한 유저

        ItemStack item = new ItemStack(Material.STICK);

        ItemMeta itemMeta = item.getItemMeta();

        itemMeta.setDisplayName(LIGHTNING_WAND);
        itemMeta.setLore(lores);
        item.setItemMeta(itemMeta);

        itemMeta.addEnchant(Enchantment.DAMAGE_ALL, 1, false);

        item.setItemMeta(itemMeta);

        if (player.getInventory().contains(item))
            return;

        player.getInventory().addItem(item);
    }

    @EventHandler
    public void onPlayerInteractBlock(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (LIGHTNING_WAND.equals(player.getItemInHand().getItemMeta().getDisplayName())) {
            // Creates a bolt of lightning at a given location. In this case, that location is where the player is looking.
            // Can only create lightning up to 200 blocks away.
            player.getWorld().strikeLightning(player.getTargetBlock((Set<Material>) null, 200).getLocation());
        }
    }

    static boolean callLightning = false;

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        main.l = player.getLocation();
        main.lx = main.l.getX();
        main.ly = main.l.getY();
        main.lz = main.l.getZ();

        Block block = player.getWorld().getHighestBlockAt(event.getTo());

        if(block.getType() == Material.WATER || block.getType() == Material.ICE)
        {
            player.setFlying(false);

            double y = block.getLocation().getY();

            player.getLocation().setY(y + 1);

            if (y < main.ly)
            {
                block.setType(Material.ICE);
            }
        }
        else
        {
            player.setFlying(true);
        }

        if((Math.abs(event.getFrom().getX() - event.getTo().getX()) > 0))
        {
            ItemStack item = player.getItemInHand();

            if(item == null || callLightning == true)
                return;

            if (main.buff == true) {

                callLightning = true;

                for (LivingEntity e : player.getWorld().getLivingEntities()
                ) {
                    double x = e.getLocation().getX();
                    double y = e.getLocation().getY();
                    double z = e.getLocation().getZ();
                    if(e.isDead() == false
                            && (x > (main.lx - 25) && x < (main.lx + 25))
                            && (y > (main.ly - 25) && y < (main.ly + 25))
                            && (z > (main.lz - 25) && z < (main.lz+ 25)))
                    {
                        if(e.isGlowing() == false)
                            e.setGlowing(true);

                        if(LIGHTNING_WAND.equals(item.getItemMeta().getDisplayName()) && !(e instanceof Player))
                            player.getWorld().strikeLightning(e.getLocation());
                    }
                }

                Bukkit.getServer().getScheduler().runTaskLater(main, new Runnable(){
                    public void run(){
                        callLightning = false;
                    }
                }, 10);
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event)
    {
        Entity damager = event.getEntity();

        if(damager instanceof Player && EntityDamageEvent.DamageCause.LIGHTNING.name().equals(event.getDamager().getType().name()))
        {
            Player player = ((Player) damager);

            if(main.buff)
            {
                event.setCancelled(true);
            }
            else
            {
                main.buff = true;

                player.setAllowFlight(true);
                player.setFlying(true);

                player.addPotionEffect(PotionEffectType.SPEED.createEffect(6400, 5));
                player.addPotionEffect(PotionEffectType.FIRE_RESISTANCE.createEffect(6400, 50));
                player.addPotionEffect(PotionEffectType.GLOWING.createEffect(6400, 5));
            }
        }
    }
}