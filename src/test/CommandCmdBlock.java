package test;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class CommandCmdBlock implements CommandExecutor {
    private BlockCommands blockCommands;

    public CommandCmdBlock(BlockCommands blockCommands) {
        this.blockCommands = blockCommands;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command alias, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Можно писать только от имени игрока!");
            return false;
        }

        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            sender.sendMessage(
                    "§e========[Команды блоков]==========\n" +
                            "§4/cmdblock set [айди или название материала] [команда] §7- добавить или изменить блок (нужно смотреь на блок)\n" +
                            "§4/cmdblock remove §7- удалить блок (нужно смотреь на блок)\n" +
                            "§4/cmdblock list §7- список блоков"
                    );
            return false;
        }
        Player player = (Player) sender;

        if (args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("add")) {
            Block block = player.getTargetBlock(null, 7);
            if (block == null || block.getType().equals(Material.AIR)) {
                sender.sendMessage("§cНужно смотреть на блок!");
                return false;
            }

            if (args.length < 3) {
                sender.sendMessage("§cУкажите тип блока и команду: /addblock [материал] [команда]");
                return false;
            }

            String typeName = args[1];
            String command = String.join(" ", Arrays.copyOfRange(args, 2, args.length));


            Material material = Material.matchMaterial(typeName);
            if (material == null) {
                sender.sendMessage("§cМатериал " + typeName + " не найден, укажите id или название материала.");
                return false;
            }

            boolean replace = false;
            for (BlockInfo blockInfo : blockCommands.getBlocks()) {
                if (block.getWorld().equals(blockInfo.getWorld())
                        && block.getX() == blockInfo.getX()
                        && block.getY() == blockInfo.getY()
                        && block.getZ() == blockInfo.getZ()) {
                    replace = true;
                    blockCommands.getBlocks().remove(blockInfo);
                    break;
                }
            }

            blockCommands.getBlocks().add(new BlockInfo(
                    block.getWorld(),
                    block.getX(),
                    block.getY(),
                    block.getZ(),
                    material,
                    command
            ));
            blockCommands.saveConfig();
            if (replace) {
                sender.sendMessage("§aБлок обновлен.");
            } else {
                sender.sendMessage("§aБлок добавлен.");
            }
            return true;
        }


        if (args[0].equalsIgnoreCase("remove")) {
            Block block = player.getTargetBlock(null, 7);
            if (block == null || block.getType().equals(Material.AIR)) {
                sender.sendMessage("§cНужно смотреть на блок!");
                return false;
            }

            boolean remove = false;
            for (BlockInfo blockInfo : blockCommands.getBlocks()) {
                if (block.getWorld().equals(blockInfo.getWorld())
                        && block.getX() == blockInfo.getX()
                        && block.getY() == blockInfo.getY()
                        && block.getZ() == blockInfo.getZ()) {
                    remove = true;
                    blockCommands.getBlocks().remove(blockInfo);
                    break;
                }
            }
            blockCommands.saveConfig();

            if (remove) {
                sender.sendMessage("§aБлок успешно удален.");
            } else {
                sender.sendMessage("§cБлок не найден, список блоков - /cmdblock list");
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("list")) {
            sender.sendMessage("§eСписок блоков: ");
            for (BlockInfo blockInfo : blockCommands.getBlocks()) {
                sender.sendMessage("§7- " + blockInfo);
            }
            return true;
        }


        sender.sendMessage("§сАргумент команды не найден, описание команды - /cmdblock help");
        return false;
    }
}
