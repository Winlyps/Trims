package winlyps.trims;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Trims extends JavaPlugin implements CommandExecutor {

    private static final Map<String, Material> MATERIAL_MAP = new HashMap<>();
    private static final Map<String, String> PATTERN_MAP = new HashMap<>();

    static {
        MATERIAL_MAP.put("quartz", Material.QUARTZ);
        MATERIAL_MAP.put("iron", Material.IRON_INGOT);
        MATERIAL_MAP.put("gold", Material.GOLD_INGOT);
        MATERIAL_MAP.put("diamond", Material.DIAMOND);
        MATERIAL_MAP.put("netherite", Material.NETHERITE_INGOT);
        MATERIAL_MAP.put("emerald", Material.EMERALD);
        MATERIAL_MAP.put("redstone", Material.REDSTONE);
        MATERIAL_MAP.put("lapis", Material.LAPIS_LAZULI);
        MATERIAL_MAP.put("amethyst", Material.AMETHYST_SHARD);
        MATERIAL_MAP.put("copper", Material.COPPER_INGOT);

        PATTERN_MAP.put("coast", "minecraft:coast");
        PATTERN_MAP.put("dune", "minecraft:dune");
        PATTERN_MAP.put("eye", "minecraft:eye");
        PATTERN_MAP.put("host", "minecraft:host");
        PATTERN_MAP.put("raiser", "minecraft:raiser");
        PATTERN_MAP.put("rib", "minecraft:rib");
        PATTERN_MAP.put("sentry", "minecraft:sentry");
        PATTERN_MAP.put("shaper", "minecraft:shaper");
        PATTERN_MAP.put("silence", "minecraft:silence");
        PATTERN_MAP.put("snout", "minecraft:snout");
        PATTERN_MAP.put("spire", "minecraft:spire");
        PATTERN_MAP.put("tide", "minecraft:tide");
        PATTERN_MAP.put("vex", "minecraft:vex");
        PATTERN_MAP.put("ward", "minecraft:ward");
        PATTERN_MAP.put("wayfinder", "minecraft:wayfinder");
        PATTERN_MAP.put("wild", "minecraft:wild");
    }

    @Override
    public void onEnable() {
        // Register the command executor and tab completer
        getCommand("trims").setExecutor(this);
        getCommand("trims").setTabCompleter(new TrimsTabCompleter());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }

        Player player = (Player) sender;
        if (args.length != 1) {
            player.sendMessage("Usage: /trims <material>");
            return true;
        }

        String materialName = args[0].toLowerCase();
        Material material = MATERIAL_MAP.get(materialName);
        if (material == null) {
            player.sendMessage("Invalid material.");
            return true;
        }

        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        if (itemInHand.getType().isAir()) {
            player.sendMessage("You must hold an item in your hand.");
            return true;
        }

        String itemType = itemInHand.getType().getKey().toString();
        for (Map.Entry<String, String> entry : PATTERN_MAP.entrySet()) {
            String pattern = entry.getValue();
            String commandString = String.format("give %s %s{Trim:{material:\"minecraft:%s\", pattern:\"%s\"}} 1",
                    player.getName(), itemType, materialName, pattern);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commandString);
        }

        return true;
    }

    public class TrimsTabCompleter implements TabCompleter {
        @Override
        public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
            if (args.length == 1) {
                return new ArrayList<>(MATERIAL_MAP.keySet());
            }
            return null;
        }
    }
}