package tr.donsuzturk.commandcooldown;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class Main extends JavaPlugin implements Listener
{
    FileConfiguration config;

    public void onEnable() {
        this.config = this.getConfig();
        this.saveDefaultConfig();
        this.getServer().getPluginManager().registerEvents(this, this);
    }

    public void onDisable() {
        this.saveConfig();
    }

    @EventHandler
    public void onCommandProcess(final PlayerCommandPreprocessEvent e) {
        final String command = e.getMessage().split(" ", 2)[0].substring(1).toLowerCase();
        final Player p = e.getPlayer();
        if (!this.getConfig().isInt("komutlar." + command)) {
            return;
        }
        System.out.println(this.getConfig().getLong("oyuncu." + p.getUniqueId().toString() + "." + command) + " / " + this.getConfig().getInt("komutlar." + command) + " / " + System.currentTimeMillis());
        if (this.config.getLong("oyuncu." + p.getUniqueId().toString() + "." + command) + this.getConfig().getInt("komutlar." + command) * 1000L > System.currentTimeMillis()) {
            e.setCancelled(true);
            final String time = (this.config.getLong("oyuncu." + p.getUniqueId().toString() + "." + command) + this.getConfig().getInt("komutlar." + command) * 1000L - System.currentTimeMillis()) / 1000L + "";
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(this.getConfig().getString("mesaj")).replace("<zaman>", time)));
            return;
        }
        this.config.set("oyuncu." + p.getUniqueId().toString() + "." + command, System.currentTimeMillis());
    }
}
