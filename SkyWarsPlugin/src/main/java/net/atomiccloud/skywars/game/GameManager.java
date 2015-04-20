package net.atomiccloud.skywars.game;

import net.atomiccloud.skywars.SkyWarsPlugin;
import net.atomiccloud.skywars.timers.DeathMatchTimer;
import net.atomiccloud.skywars.timers.GameTimer;
import net.atomiccloud.skywars.timers.LobbyTimer;
import net.atomiccloud.skywars.timers.RestartTimer;
import net.atomiccloud.skywars.util.BukkitRunnable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class GameManager
{
    private GameState gameState = GameState.PRE_GAME;

    private Scoreboard votesBoard;

    private Maps winningMap;

    private Maps[] maps = new Maps[ 2 ];

    private Map<String, Integer> votes = new HashMap<>();

    private List<String> playersInGame = new ArrayList<>();
    private Set<String> spectators = new HashSet<>();

    private BukkitTask currentTask;

    private SkyWarsPlugin plugin;

    public GameManager(SkyWarsPlugin plugin)
    {
        this.plugin = plugin;
        maps[ 0 ] = Maps.getRandom();

        if ( maps[ 0 ].ordinal() == Maps.values().length - 1 )
        {
            maps[ 1 ] = Maps.values()[ 0 ];
        } else
        {
            maps[ 1 ] = Maps.values()[ maps[ 0 ].ordinal() + 1 ];
        }

        for ( Maps map : maps )
        {
            votes.put( map.name(), 0 );
        }
        votes.put( "Random", 0 );
        ( (BukkitRunnable) this::makeScoreboard ).runAfter( 2, TimeUnit.SECONDS );
    }

    public GameState getGameState()
    {
        return gameState;
    }

    public void setGameState(GameState gameState)
    {
        if ( currentTask != null )
        {
            currentTask.cancel();
            currentTask = null;
        }
        if ( this.gameState != gameState )
        {
            switch ( gameState )
            {
                case LOBBY_COUNTDOWN:
                    currentTask = new LobbyTimer( plugin ).runTaskTimer( plugin, 0, 20 );
                    break;
                case IN_GAME:
                    currentTask = new GameTimer( plugin ).runTaskTimer( plugin, 0, 20 );
                    break;
                case DEATH_MATCH:
                    currentTask = new DeathMatchTimer( plugin ).runTaskTimer( plugin, 0, 20 );
                    break;
                case POST_GAME:
                    currentTask = new RestartTimer( plugin ).runTaskTimer( plugin, 0, 20 );
                    break;
            }
        }
        this.gameState = gameState;
    }

    public void sendVoteMessage(Player player)
    {
        player.sendMessage( ChatColor.GOLD + "Vote for a map with /vote #" );
        player.sendMessage( ChatColor.translateAlternateColorCodes( '&', "&6&l1. &6"
                + getMaps()[ 0 ].getName() +
                " (&b" + getVotes().get( getMaps()[ 0 ].name() ) + " votes&6)" ) );
        player.sendMessage( ChatColor.translateAlternateColorCodes( '&', "&6&l2. &6" +
                getMaps()[ 1 ].getName() + " (&b" +
                getVotes().get( getMaps()[ 1 ].name() ) + " votes&6)" ) );
        player.sendMessage( ChatColor.translateAlternateColorCodes( '&', "&6&l3. &3" +
                "Random" + " &6(&b" + getVotes().get( "Random" ) + " votes&6)" ) );
    }

    private void makeScoreboard()
    {
        votesBoard = Bukkit.getScoreboardManager().getNewScoreboard();

        Objective objective = votesBoard.registerNewObjective( "votes", "dummy" );
        objective.setDisplayName( ChatColor.GREEN + "Votes" );
        objective.setDisplaySlot( DisplaySlot.SIDEBAR );

        objective.getScore( ChatColor.GOLD + "1. " +
                ChatColor.GRAY + getMaps()[ 0 ].getName() ).setScore( votes.get( getMaps()[ 0 ].name() ) );
        objective.getScore( ChatColor.GOLD + "2. " +
                ChatColor.GRAY + getMaps()[ 1 ].getName() ).setScore( votes.get( getMaps()[ 1 ].name() ) );
        objective.getScore( ChatColor.GOLD + "3. " +
                ChatColor.DARK_AQUA + "Random" ).setScore( votes.get( "Random" ) );
    }


    public void updateScoreboard(int i)
    {
        switch ( i )
        {
            case 1:
                votesBoard.getObjective( "votes" ).getScore(
                        ChatColor.GOLD + "1. " + ChatColor.GRAY + getMaps()[ 0 ].getName() ).setScore(
                        votes.get( getMaps()[ 0 ].name() ) );
                break;
            case 2:
                votesBoard.getObjective( "votes" ).getScore(
                        ChatColor.GOLD + "2. " + ChatColor.GRAY + getMaps()[ 1 ].getName() ).setScore(
                        votes.get( getMaps()[ 1 ].name() ) );
                break;
            case 3:
                votesBoard.getObjective( "votes" ).getScore(
                        ChatColor.GOLD + "3. " + ChatColor.DARK_AQUA + "Random" ).setScore( votes.get( "Random" ) );
                break;
        }
    }

    public Maps[] getMaps()
    {
        return maps;
    }

    public Scoreboard getVotesBoard()
    {
        return votesBoard;
    }

    public Map<String, Integer> getVotes()
    {
        return votes;
    }

    public List<String> getPlayersInGame()
    {
        return playersInGame;
    }

    public Maps getWinningMap()
    {
        return winningMap;
    }

    public void setWinningMap(Maps winningMap)
    {
        this.winningMap = winningMap;
    }

    public Set<String> getSpectators()
    {
        return spectators;
    }
}