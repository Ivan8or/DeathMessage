package online.umbcraft.plugins;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;
import net.luckperms.api.query.QueryOptions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameRule;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class DeathListener implements Listener {

    DeathMessage plugin;

    public DeathListener(DeathMessage plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public synchronized void onDeathListener(PlayerDeathEvent e) {

        if (!(e.getEntity().getKiller() instanceof Player))
            return;

        Player killer = e.getEntity().getKiller();
        Player victim = e.getEntity();

        String killer_pref = getPrefix(killer.getUniqueId());
        String killer_suf = getSuffix(killer.getUniqueId());
        String killer_name = killer.getName();

        String victim_pref = getPrefix(victim.getUniqueId());
        String victim_suf = getSuffix(victim.getUniqueId());
        String victim_name = victim.getName();


        String death_str = plugin.getDeathString();

        death_str = death_str.replaceAll("\\(\\(victim\\)\\)", victim_name + ChatColor.WHITE);
        death_str = death_str.replaceAll("\\(\\(killer\\)\\)", killer_name + ChatColor.WHITE);
        death_str = death_str.replaceAll("\\(\\(killer_prefix\\)\\)", killer_pref + ChatColor.WHITE);
        death_str = death_str.replaceAll("\\(\\(killer_suffix\\)\\)", killer_suf + ChatColor.WHITE);
        death_str = death_str.replaceAll("\\(\\(victim_prefix\\)\\)", victim_pref + ChatColor.WHITE);
        death_str = death_str.replaceAll("\\(\\(victim_suffix\\)\\)", victim_suf + ChatColor.WHITE);

        death_str = ChatColor.translateAlternateColorCodes('&', death_str);
        death_str = death_str.replaceAll("\\s+", " ");


        boolean messages = killer.getWorld().getGameRuleValue(GameRule.SHOW_DEATH_MESSAGES);
        if (messages)
            e.setDeathMessage(death_str);
        else
            for (Player p : Bukkit.getOnlinePlayers())
                p.sendMessage(death_str);
    }

    public String getPrefix(UUID p) {
        LuckPerms api = LuckPermsProvider.get();
        try {
            api.getUserManager().loadUser((p)).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        User luck_player = api.getUserManager().getUser(p);
        QueryOptions queryOptions = api
                .getContextManager()
                .getQueryOptions(luck_player)
                .get();
        CachedMetaData metaData = luck_player.getCachedData().getMetaData(queryOptions);
        String prefix = metaData.getPrefix();
        if (prefix == null)
            return "";
        return prefix;
    }

    public String getSuffix(UUID p) {
        LuckPerms api = LuckPermsProvider.get();
        try {
            api.getUserManager().loadUser((p)).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        User luck_player = api.getUserManager().getUser(p);
        QueryOptions queryOptions = api.getContextManager().getQueryOptions(luck_player).get();
        CachedMetaData metaData = luck_player.getCachedData().getMetaData(queryOptions);
        String suffix = metaData.getSuffix();
        if (suffix == null)
            return "";
        return suffix;
    }

}
