package net.atomiccloud.skywars.listeners;

import net.atomiccloud.skywars.SkyWarsPlugin;
import net.atomiccloud.skywars.Team;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MoveListener implements Listener
{

    private SkyWarsPlugin plugin;

    public MoveListener(SkyWarsPlugin plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event)
    {
        if ( plugin.getGameManager().getPlayer( event.getPlayer().getName() ).getTeam().equals( Team.PLAYER ) )
        {
            event.setTo( event.getFrom() );
        }
    }
}