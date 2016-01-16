package net.atomiccloud.skywars.listeners;

import net.atomiccloud.skywars.SkyWarsPlugin;
import net.atomiccloud.skywars.game.GameState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class ExplosionListener implements Listener
{

    private SkyWarsPlugin plugin;

    public ExplosionListener(SkyWarsPlugin plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event)
    {
        if ( !plugin.getGameManager().getGameState().equals( GameState.IN_GAME ) )
        {
            event.blockList().clear();
        }

        if ( !plugin.getGameManager().getGameState().isPvp() )
        {
            event.setCancelled( true );
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event)
    {
        if ( !plugin.getGameManager().getGameState().equals( GameState.IN_GAME ) )
        {
            event.blockList().clear();
        }

        if ( !plugin.getGameManager().getGameState().isPvp() )
        {
            event.setCancelled( true );
        }
    }
}
