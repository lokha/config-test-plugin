package test;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class TestPlugin extends JavaPlugin {

    private int var1;
    private String text;

    private BlockCommands blockCommands;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.getCommand("testreload").setExecutor(this);

        blockCommands = new BlockCommands(this);
        Bukkit.getPluginManager().registerEvents(blockCommands, this);
        this.getCommand("cmdblock").setExecutor(new CommandCmdBlock(blockCommands));

        this.reloadConfigParams();
    }

    private void reloadConfigParams() {
        FileConfiguration config = this.getConfig();

        var1 = config.getInt("var1");
        text = config.getString("text");

        this.getLogger().info("Достали из конфига новые значения: " +
                "var1 = " + var1 + ", " +
                "text = " + text);

        blockCommands.loadConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("test.reload")) {
            sender.sendMessage("§cНет прав.");
            return false;
        }

        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("settext")) {
                if (args.length > 1) {
                    String text = args[1];
                    this.text = text;
                    this.getConfig().set("text", text);
                    this.saveConfig();
                    sender.sendMessage("§aТекст установлен и сохранен.");
                } else {
                    sender.sendMessage("§cУкажите текст /testreload settext [текст]");
                    return false;
                }
            } else {
                sender.sendMessage("§cДоступно только /testreload settext [текст]");
                return false;
            }
        } else {
            this.reloadConfig();
            this.reloadConfigParams();
            sender.sendMessage("§aConfig reloaded.");
        }

        return true;
    }
}
