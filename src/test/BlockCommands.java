package test;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlockCommands implements Listener {
    private List<BlockInfo> blocks = new ArrayList<>();
    private TestPlugin testPlugin;

    public BlockCommands(TestPlugin testPlugin) {
        this.testPlugin = testPlugin;
        this.loadConfig();
    }

    public void loadConfig() {
        blocks.clear();

        FileConfiguration config = testPlugin.getConfig();
        for (String block : config.getStringList("blocks")) {
            try {
                String[] data = block.split(":");
                World world = Bukkit.getWorld(data[0]);
                if (world == null) {
                    throw new RuntimeException("мир " + data[0] + " не найден в блоке " + block);
                }

                BlockInfo blockInfo = new BlockInfo(
                        world,
                        Integer.parseInt(data[1]),
                        Integer.parseInt(data[2]),
                        Integer.parseInt(data[3]),
                        Material.matchMaterial(data[4]),
                        data[5]
                );
                blocks.add(blockInfo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void saveConfig() {
        List<String> blockString = new ArrayList<>();
        for (BlockInfo block : blocks) {
            blockString.add(block.getWorld().getName() + ":" +
                    block.getX() + ":" +
                    block.getY() + ":" +
                    block.getZ() + ":" +
                    block.getMaterial().name() + ":" +
                    block.getCommand());
        }

        FileConfiguration config = testPlugin.getConfig();
        config.set("blocks", blockString);
        testPlugin.saveConfig();
    }

    @EventHandler
    public void on(PlayerInteractEvent event) {
        if (event.getHand().equals(EquipmentSlot.HAND)) {
            if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                Block block = event.getClickedBlock();

                for (BlockInfo blockInfo : blocks) {
                    if (block.getWorld().equals(blockInfo.getWorld())
                            && block.getX() == blockInfo.getX()
                            && block.getY() == blockInfo.getY()
                            && block.getZ() == blockInfo.getZ()
                            && block.getType() == blockInfo.getMaterial()) {

                        String[] commands = blockInfo.getCommand()
                                .replace("{player}", event.getPlayer().getName())
                                .split(",");

                        Bukkit.getLogger().info("Игрок " + event.getPlayer().getName() +
                                " кликнул по блоку " + blockInfo + ", выполняем команды: " +
                                        Arrays.toString(commands));

                        for (String command : commands) {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                        }
                        break;
                    }
                }
            }
        }
    }

    public List<BlockInfo> getBlocks() {
        return blocks;
    }
}
