package com.andrew121410.mc.doubleascoreboard;

import com.andrew121410.mc.world16utils.chat.Translate;
import io.papermc.paper.scoreboard.numbers.NumberFormat;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class DoubleAScoreboard extends JavaPlugin implements Listener {

    private final List<Component> titleFrames = new ArrayList<>();
    private int titleFrameIndex = 0;
    private final Map<UUID, Scoreboard> playerScoreboards = new HashMap<>();
    private final Map<UUID, Map<Integer, Team>> playerTeams = new HashMap<>();

    @Override
    public void onEnable() {
        setupTitleFrames();
        startScoreboardTask();

        getServer().getPluginManager().registerEvents(this, this);
    }

    private void setupTitleFrames() {
        titleFrames.add(Translate.miniMessage("<gold>D"));
        titleFrames.add(Translate.miniMessage("<gold>D<yellow>o"));
        titleFrames.add(Translate.miniMessage("<gold>Do"));
        titleFrames.add(Translate.miniMessage("<gold>Do<yellow>u"));
        titleFrames.add(Translate.miniMessage("<gold>Dou"));
        titleFrames.add(Translate.miniMessage("<gold>Dou<yellow>b"));
        titleFrames.add(Translate.miniMessage("<gold>Doub"));
        titleFrames.add(Translate.miniMessage("<gold>Doub<yellow>l"));
        titleFrames.add(Translate.miniMessage("<gold>Doubl"));
        titleFrames.add(Translate.miniMessage("<gold>Doubl<yellow>e"));
        titleFrames.add(Translate.miniMessage("<gold>Double"));
        titleFrames.add(Translate.miniMessage("<gold>Double<yellow>-"));
        titleFrames.add(Translate.miniMessage("<gold>Double<blue>-"));
        titleFrames.add(Translate.miniMessage("<gold>Double<blue>- "));
        titleFrames.add(Translate.miniMessage("<gold>Double<blue>-<yellow>A"));
        titleFrames.add(Translate.miniMessage("<gold>Double<blue>-<gold>A<yellow> "));
        titleFrames.add(Translate.miniMessage("<gold>Double<blue>-<gold>A <yellow>N"));
        titleFrames.add(Translate.miniMessage("<gold>Double<blue>-<gold>A N"));
        titleFrames.add(Translate.miniMessage("<gold>Double<blue>-<gold>A N<yellow>e"));
        titleFrames.add(Translate.miniMessage("<gold>Double<blue>-<gold>A Ne"));
        titleFrames.add(Translate.miniMessage("<gold>Double<blue>-<gold>A Ne<yellow>t"));
        titleFrames.add(Translate.miniMessage("<gold>Double<blue>-<gold>A Net"));
        titleFrames.add(Translate.miniMessage("<gold>Double<blue>-<gold>A Net<yellow>w"));
        titleFrames.add(Translate.miniMessage("<gold>Double<blue>-<gold>A Netw"));
        titleFrames.add(Translate.miniMessage("<gold>Double<blue>-<gold>A Netw<yellow>o"));
        titleFrames.add(Translate.miniMessage("<gold>Double<blue>-<gold>A Netwo"));
        titleFrames.add(Translate.miniMessage("<gold>Double<blue>-<gold>A Netwo<yellow>r"));
        titleFrames.add(Translate.miniMessage("<gold>Double<blue>-<gold>A Networ"));
        titleFrames.add(Translate.miniMessage("<gold>Double<blue>-<gold>A Networ<yellow>k"));
        titleFrames.add(Translate.miniMessage("<gold>Double<blue>-<gold>A Network"));
        titleFrames.add(Translate.miniMessage("<dark_green>Double<blue>-<dark_green>A Network"));
        titleFrames.add(Translate.miniMessage("<green>Double<blue>-<green>A Network"));
        titleFrames.add(Translate.miniMessage("<aqua>Double<blue>-<aqua>A Network"));
        titleFrames.add(Translate.miniMessage("<red>Double<blue>-<red>A Network"));
        titleFrames.add(Translate.miniMessage("<light_purple>Double<blue>-<light_purple>A Network"));
        titleFrames.add(Translate.miniMessage("<yellow>Double<blue>-<yellow>A Network"));
    }

    private void startScoreboardTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    updateScoreboard(player);
                }
            }
        }.runTaskTimer(this, 0, 5);
    }

    private void updateScoreboard(Player player) {
        UUID playerUUID = player.getUniqueId();
        Scoreboard board = playerScoreboards.computeIfAbsent(playerUUID, uuid -> createNewScoreboard());
        Map<Integer, Team> teams = playerTeams.computeIfAbsent(playerUUID, uuid -> new HashMap<>());

        Objective objective = board.getObjective(DisplaySlot.SIDEBAR);
        if (objective == null) {
            objective = board.registerNewObjective("custom", Criteria.DUMMY, getNextTitleFrame());
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        } else {
            objective.displayName(getNextTitleFrame());
        }

        String networkPlayers = PlaceholderAPI.setPlaceholders(player, "%bungee_total%");
        // Sometimes the placeholder returns "0" because there's like a delay or something?
        if (networkPlayers.equals("0")) {
            networkPlayers = "1";
        }

        String vaultBalance = PlaceholderAPI.setPlaceholders(player, "%vault_eco_balance%");

        updateLine(board, teams, 10, "<gold><bold>+---------------+");
        updateLine(board, teams, 9, "<blue><italic>[Network]");
        updateLine(board, teams, 8, "<light_purple>> <dark_green>Players: <gold>" + networkPlayers);
        updateLine(board, teams, 7, "<light_purple>> <dark_green>Ping: <gold>" + player.getPing());
        updateLine(board, teams, 6, " "); // Spacer
        updateLine(board, teams, 5, "<gold><italic>[Player]");
        updateLine(board, teams, 4, "<light_purple>> <dark_green>Tokens: <gold>" + vaultBalance);
        updateLine(board, teams, 3, "<gold><bold>+---------------+");

        player.setScoreboard(board);
    }

    private Scoreboard createNewScoreboard() {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        if (manager == null) throw new IllegalStateException("ScoreboardManager is not available");
        return manager.getNewScoreboard();
    }

    private Component getNextTitleFrame() {
        Component frame = titleFrames.get(titleFrameIndex);
        titleFrameIndex = (titleFrameIndex + 1) % titleFrames.size();
        return frame;
    }

    private void updateLine(Scoreboard board, Map<Integer, Team> teams, int score, String content) {
        Team team = teams.computeIfAbsent(score, s -> {
            Team newTeam = board.registerNewTeam("line" + score);
            String entry = createUniqueEntry(score);
            newTeam.addEntry(entry);
            @NotNull Score theScore = board.getObjective(DisplaySlot.SIDEBAR).getScore(entry);
            theScore.setScore(score);
            theScore.numberFormat(NumberFormat.blank());
            return newTeam;
        });

        team.prefix(Translate.miniMessage(content));
    }

    private String createUniqueEntry(int score) {
        return "§" + (char) ('a' + score);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        playerScoreboards.remove(playerUUID);
        playerTeams.remove(playerUUID);
    }

    @Override
    public void onDisable() {
        playerScoreboards.clear();
        playerTeams.clear();
    }
}
