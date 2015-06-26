package com.untamedears.utils;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Dye;
import org.bukkit.material.MaterialData;
import org.bukkit.material.SpawnEgg;
import com.untamedears.realisticbiomes.GrowthConfig;
import com.untamedears.realisticbiomes.GrowthMap;

public class MaterialAliases {
	// map Material that a user uses to hit the ground to a Material, TreeType,
	// or EntityType
	// that is specified. (ie, hit the ground with some wheat seeds and get a
	// message corresponding
	// to the wheat plant's growth rate
	private static Map<Material, Material> materialAliases = new HashMap<Material, Material>();

	static {
		materialAliases.put(Material.SEEDS, Material.CROPS);
		materialAliases.put(Material.WHEAT, Material.CROPS);
		materialAliases.put(Material.CARROT_ITEM, Material.CARROT);
		materialAliases.put(Material.POTATO_ITEM, Material.POTATO);
		materialAliases.put(Material.POISONOUS_POTATO, Material.POTATO);

		materialAliases.put(Material.MELON_SEEDS, Material.MELON_STEM);
		materialAliases.put(Material.MELON, Material.MELON_BLOCK);
		materialAliases.put(Material.MELON_BLOCK, Material.MELON_BLOCK);
		materialAliases.put(Material.PUMPKIN_SEEDS, Material.PUMPKIN_STEM);
		materialAliases.put(Material.PUMPKIN, Material.PUMPKIN);

		materialAliases.put(Material.INK_SACK, Material.COCOA);

		materialAliases.put(Material.CACTUS, Material.CACTUS);

		materialAliases.put(Material.SUGAR_CANE, Material.SUGAR_CANE_BLOCK);

		materialAliases.put(Material.NETHER_STALK, Material.NETHER_WARTS);

		materialAliases.put(Material.FISHING_ROD, Material.RAW_FISH);
	}

	/**
	 * There is a bug in spigot/bukkit, where the species of a sapling is retrieved the
	 * same way as from a log, which is wrong.
	 * A log can be of two distinct material types, and
	 * carries the species in the first two bits of its data, 3rd and 4th carry the
	 * direction.
	 * A sapling, either as item or block, has only one possible material type and
	 * carries its species in the first three bits. It also has a "ready" flag if it
	 * is a block, so it actually has two invisible growth stages.
	 */
	private static TreeType[] speciesMap = new TreeType[]{
		TreeType.TREE,
		TreeType.REDWOOD,
		TreeType.BIRCH,
		TreeType.JUNGLE,
		TreeType.ACACIA,
		TreeType.DARK_OAK
	};
	
	private static TreeType getTreeType(byte data) {
		int index = data & 7;
		if (speciesMap.length > index) {
			return speciesMap[index];
		} else {
			return null;
		}
	}

	/**
	 * Returns correct TreeType if block material is sapling
	 * @param candidate
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static TreeType getTreeType(Block candidate) {
		return getTreeType(candidate.getData());
	}

	/**
	 * Get config for an item
	 * @param growthConfigs
	 * @param item
	 * @return config or null
	 */
	@SuppressWarnings("deprecation")
	public static GrowthConfig getConfig(GrowthMap growthConfigs,
			ItemStack item) {
		Material material = item.getType();
		if (material == Material.SAPLING) {
			return growthConfigs.get(getTreeType(item.getData().getData()));
		
		} else if (material == Material.EGG) {
			// special case egg maps to chicken (not a spawn egg)
			return growthConfigs.get(EntityType.CHICKEN);
			
		} else if (material == Material.MONSTER_EGG) {
			MaterialData data = item.getData();
			if (data instanceof SpawnEgg) {
				return growthConfigs.get(((SpawnEgg)data).getSpawnedType());
			}

		} else {
			if (isCocoa(material, item.getData())) {
				return growthConfigs.get(Material.COCOA);
			}
			
			if (materialAliases.containsKey(material)) {
				return growthConfigs.get(materialAliases.get(material));
			}
		}
		// if not aliased try to get config directly from itemStack
		return growthConfigs.get(material);
	}

	public static boolean isCocoa(Material material, MaterialData data) {
		return material == Material.INK_SACK && data instanceof Dye && ((Dye)data).getColor() == DyeColor.BROWN;
	}

	@SuppressWarnings("deprecation")
	public static GrowthConfig getConfig(GrowthMap growthConfigs, Block block) {
		Material material = block.getType();
		
		if (material == Material.SAPLING) {
			return growthConfigs.get(getTreeType(block.getData()));
		}

		return growthConfigs.get(material);
	}
}
