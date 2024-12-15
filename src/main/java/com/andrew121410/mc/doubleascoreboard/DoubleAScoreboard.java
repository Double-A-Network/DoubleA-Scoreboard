package com.andrew121410.mc.doubleascoreboard;

import com.andrew121410.mc.world16utils.chat.Translate;
import io.papermc.paper.scoreboard.numbers.NumberFormat;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

import java.util.*;

public final class DoubleAScoreboard extends JavaPlugin {

    private final List<Component> titleFrames = new ArrayList<>();
    private int titleFrameIndex = 0;
    private final Map<UUID, Scoreboard> playerScoreboards = new HashMap<>();

    @Override
    public void onEnable() {
        setupTitleFrames();
        startScoreboardTask();
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
        titleFrames.add(Translate.miniMessage("<gold>Double<blue>-"));
        titleFrames.add(Translate.miniMessage("<gold>Double<blue>-<yellow>A"));
        titleFrames.add(Translate.miniMessage("<gold>Double<blue>-<gold>A"));
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

        Objective objective = board.getObjective(DisplaySlot.SIDEBAR);
        if (objective == null) {
            objective = board.registerNewObjective("custom", Criteria.DUMMY, getNextTitleFrame());
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        } else {
            objective.displayName(getNextTitleFrame());
        }

        // Clear existing scores and update lines
        board.getEntries().forEach(board::resetScores);

        addLine(objective, Translate.miniMessage("<gold><bold>+---------------+"), 10);
        addLine(objective, Translate.miniMessage("<blue><italic>[Network]"), 9);
        addLine(objective, Translate.miniMessage("<light_purple>> <dark_green>Players: <gold>" + Bukkit.getOnlinePlayers().size()), 8);
        addLine(objective, Translate.miniMessage("<light_purple>> <dark_green>Ping: <gold>" + player.getPing()), 7);
        addLine(objective, Translate.miniMessage(" "), 6); // Spacer
        addLine(objective, Translate.miniMessage("<gold><italic>[Player]"), 5);
        addLine(objective, Translate.miniMessage("<light_purple>> <dark_green>Tokens: <gold>" + "1000"), 4);
        addLine(objective, Translate.miniMessage("<gold><bold>+---------------+"), 3);

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

    private void addLine(Objective objective, Component text, int score) {
        String serializedText = LegacyComponentSerializer.legacySection().serialize(text);
        Score scoreLine = objective.getScore(serializedText);
        scoreLine.setScore(score);
        scoreLine.numberFormat(NumberFormat.blank());
    }

    @Override
    public void onDisable() {
        playerScoreboards.clear();
    }
}