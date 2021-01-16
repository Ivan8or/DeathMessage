package online.umbcraft.plugins;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;
import net.luckperms.api.query.QueryOptions;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class DeathListener implements Listener {

    DeathMessage plugin;

    public DeathListener(DeathMessage plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onDeathListener(PlayerDeathEvent e) {

        if(!(e.getEntity().getKiller() instanceof Player))
            return;

        Player killer = e.getEntity().getKiller();
        Player victim = e.getEntity();

        String killer_name = getPrefix(killer.getUniqueId())+" "+killer.getName()+" "+getSuffix(killer.getUniqueId());
        String victim_name = getPrefix(victim.getUniqueId())+" "+victim.getName()+" "+getSuffix(victim.getUniqueId());

        String death_str = plugin.getDeathString();

        death_str = death_str.replaceAll("\\(\\(victim\\)\\)",victim_name);
        death_str = death_str.replaceAll("\\(\\(killer\\)\\)",killer_name);
        death_str = ChatColor.translateAlternateColorCodes('&', death_str);

        e.setDeathMessage(death_str);
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
        return suffix;
    }

}
