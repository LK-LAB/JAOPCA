package thelm.jaopca.compat.thermalexpansion;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import java.util.TreeSet;

import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import thelm.jaopca.api.JAOPCAApi;
import thelm.jaopca.api.helpers.IMiscHelper;
import thelm.jaopca.api.materials.IMaterial;
import thelm.jaopca.api.materials.MaterialType;
import thelm.jaopca.api.modules.IModule;
import thelm.jaopca.api.modules.IModuleData;
import thelm.jaopca.api.modules.JAOPCAModule;
import thelm.jaopca.utils.ApiImpl;
import thelm.jaopca.utils.MiscHelper;

@JAOPCAModule(modDependencies = "thermal_expansion")
public class ThermalExpansionModule implements IModule {

	private static final Set<String> BLACKLIST = new TreeSet<>(Arrays.asList(
			"copper", "gold", "iron", "lead", "nickel", "silver", "tin"));
	private static final Set<String> PULVERIZER_BLACKLIST = new TreeSet<>();
	private static final Set<String> SMELTER_BLACKLIST = new TreeSet<>(Arrays.asList(
			"netherite", "netherite_scrap"));

	static {
		if(ModList.get().isLoaded("alltheores")) {
			Collections.addAll(PULVERIZER_BLACKLIST, "platinum", "zinc");
		}
		if(ModList.get().isLoaded("thermal_integration")) {
			if(ModList.get().isLoaded("create")) {
				Collections.addAll(BLACKLIST, "zinc");
			}
			if(ModList.get().isLoaded("tconstruct")) {
				Collections.addAll(BLACKLIST, "cobalt");
			}
		}
	}

	@Override
	public String getName() {
		return "thermal_expansion";
	}

	@Override
	public Multimap<Integer, String> getModuleDependencies() {
		ImmutableSetMultimap.Builder builder = ImmutableSetMultimap.builder();
		builder.put(0, "dusts");
		builder.put(1, "dusts");
		builder.put(1, "nuggets");
		return builder.build();
	}

	@Override
	public Set<MaterialType> getMaterialTypes() {
		return EnumSet.of(MaterialType.INGOT);
	}

	@Override
	public Set<String> getDefaultMaterialBlacklist() {
		return BLACKLIST;
	}

	@Override
	public void onCommonSetup(IModuleData moduleData, FMLCommonSetupEvent event) {
		JAOPCAApi api = ApiImpl.INSTANCE;
		ThermalExpansionHelper helper = ThermalExpansionHelper.INSTANCE;
		IMiscHelper miscHelper = MiscHelper.INSTANCE;
		Item richSlag = ForgeRegistries.ITEMS.getValue(new ResourceLocation("thermal:rich_slag"));
		for(IMaterial material : moduleData.getMaterials()) {
			ResourceLocation oreLocation = miscHelper.getTagLocation("ores", material.getName());
			ResourceLocation rawMaterialLocation = miscHelper.getTagLocation("raw_materials", material.getName());
			ResourceLocation dustLocation = miscHelper.getTagLocation("dusts", material.getName());
			ResourceLocation extraDustLocation = miscHelper.getTagLocation("dusts", material.getExtra(1).getName());
			ResourceLocation materialLocation = miscHelper.getTagLocation(material.getType().getFormName(), material.getName());
			ResourceLocation extraMaterialLocation = miscHelper.getTagLocation(material.getExtra(1).getType().getFormName(), material.getExtra(1).getName());
			ResourceLocation extraNuggetLocation = miscHelper.getTagLocation("nuggets", material.getExtra(1).getName());
			if(material.hasExtra(1)) {
				if(!PULVERIZER_BLACKLIST.contains(material.getName())) {
					helper.registerPulverizerRecipe(
							new ResourceLocation("jaopca", "thermal_expansion.ore_to_dust."+material.getName()),
							oreLocation, 1, new Object[] {
									dustLocation, 2F,
									extraDustLocation, 0.1F,
									Blocks.GRAVEL, 0.2F,
							}, 4000, 0.2F);
					helper.registerPulverizerRecipe(
							new ResourceLocation("jaopca", "thermal_expansion.raw_material_to_dust."+material.getName()),
							rawMaterialLocation, 1, new Object[] {
									dustLocation, 1.25F,
									extraDustLocation, 0.05F,
							}, 4000, 0.1F);
				}
				if(!SMELTER_BLACKLIST.contains(material.getName())) {
					helper.registerSmelterRecipe(
							new ResourceLocation("jaopca", "thermal_expansion.ore_to_material_smelter."+material.getName()),
							new Object[] {
									oreLocation,
							}, new Object[] {
									materialLocation, 1F,
									extraMaterialLocation, 0.2F,
									richSlag, 0.2F,
							}, 3200, 0.2F);
					helper.registerSmelterRecipe(
							new ResourceLocation("jaopca", "thermal_expansion.raw_material_to_material."+material.getName()),
							new Object[] {
									rawMaterialLocation,
							}, new Object[] {
									materialLocation, 1F,
									extraNuggetLocation, 1F,
							}, 3200, 0.1F);
				}
			}
			else {
				if(!PULVERIZER_BLACKLIST.contains(material.getName())) {
					helper.registerPulverizerRecipe(
							new ResourceLocation("jaopca", "thermal_expansion.ore_to_dust."+material.getName()),
							oreLocation, 1, new Object[] {
									dustLocation, 2F,
									Blocks.GRAVEL, 0.2F,
							}, 4000, 0.2F);
					helper.registerPulverizerRecipe(
							new ResourceLocation("jaopca", "thermal_expansion.raw_material_to_dust."+material.getName()),
							rawMaterialLocation, 1, new Object[] {
									dustLocation, 1.25F,
							}, 4000, 0.1F);
				}
				if(!SMELTER_BLACKLIST.contains(material.getName())) {
					helper.registerSmelterRecipe(
							new ResourceLocation("jaopca", "thermal_expansion.ore_to_material_smelter."+material.getName()),
							new Object[] {
									oreLocation,
							}, new Object[] {
									materialLocation, 1F,
									richSlag, 0.2F,
							}, 3200, 0.2F);
					helper.registerSmelterRecipe(
							new ResourceLocation("jaopca", "thermal_expansion.raw_material_to_material."+material.getName()),
							new Object[] {
									rawMaterialLocation,
							}, new Object[] {
									materialLocation, 1F,
							}, 3200, 0.1F);
				}
			}
		}
	}
}
