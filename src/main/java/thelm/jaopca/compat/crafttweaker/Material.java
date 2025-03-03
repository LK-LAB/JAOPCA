package thelm.jaopca.compat.crafttweaker;

import java.util.TreeMap;

import org.openzen.zencode.java.ZenCodeType;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.fluid.IFluidStack;
import com.blamejared.crafttweaker.api.fluid.MCFluidStack;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.item.MCItemStack;
import com.blamejared.crafttweaker.api.tag.CraftTweakerTagRegistry;
import com.blamejared.crafttweaker.api.tag.MCTag;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import thelm.jaopca.api.materials.IMaterial;
import thelm.jaopca.utils.MiscHelper;

@ZenRegister
@ZenCodeType.Name("mods.jaopca.Material")
public class Material {

	private static final TreeMap<IMaterial, Material> MATERIAL_WRAPPERS = new TreeMap<>();
	private final IMaterial material;

	public static Material getMaterialWrapper(IMaterial material) {
		return MATERIAL_WRAPPERS.computeIfAbsent(material, Material::new);
	}

	private Material(IMaterial material) {
		this.material = material;
	}

	public IMaterial getInternal() {
		return material;
	}

	@ZenCodeType.Getter("name")
	public String getName() {
		return material.getName();
	}

	@ZenCodeType.Getter("type")
	public String getType() {
		return material.getType().getName();
	}

	@ZenCodeType.Getter("alternativeNames")
	public String[] getAlternativeNames() {
		return material.getAlternativeNames().toArray(new String[0]);
	}

	@ZenCodeType.Method
	public Material getExtra(int index) {
		return new Material(material.getExtra(index));
	}

	@ZenCodeType.Method
	public boolean hasExtra(int index) {
		return material.hasExtra(index);
	}

	@ZenCodeType.Method
	public MCTag getItemTag(String prefix) {
		return getTag(Registry.ITEM_REGISTRY.getRegistryName(), prefix, "/");
	}

	@ZenCodeType.Method
	public MCTag getItemTag(String prefix, String tagSeperator) {
		return getTag(Registry.ITEM_REGISTRY.getRegistryName(), prefix, tagSeperator);
	}

	@ZenCodeType.Method
	public MCTag getFluidTag(String prefix) {
		return getTag(Registry.FLUID_REGISTRY.getRegistryName(), prefix, "/");
	}

	@ZenCodeType.Method
	public MCTag getFluidTag(String prefix, String tagSeperator) {
		return getTag(Registry.FLUID_REGISTRY.getRegistryName(), prefix, tagSeperator);
	}

	@ZenCodeType.Method
	public MCTag getTag(ResourceLocation registry, String prefix) {
		return getTag(registry, prefix, "/");
	}

	@ZenCodeType.Method
	public MCTag getTag(ResourceLocation registry, String prefix, String tagSeperator) {
		return CraftTweakerTagRegistry.INSTANCE.tagManager(registry).
				tag(MiscHelper.INSTANCE.getTagLocation(prefix, material.getName(), tagSeperator));
	}

	@ZenCodeType.Method
	public IItemStack getItemStack(String prefix, int count) {
		MiscHelper helper = MiscHelper.INSTANCE;
		ItemStack stack = helper.getItemStack(helper.getTagLocation(prefix, material.getName()), count);
		return new MCItemStack(stack);
	}

	@ZenCodeType.Method
	public IItemStack getItemStack(String prefix) {
		return getItemStack(prefix, 1);
	}

	@ZenCodeType.Method
	public IFluidStack getFluidStack(String prefix, int count) {
		MiscHelper helper = MiscHelper.INSTANCE;
		FluidStack stack = helper.getFluidStack(helper.getTagLocation(prefix, material.getName()), count);
		return new MCFluidStack(stack);
	}

	@ZenCodeType.Method
	public MaterialForm getMaterialForm(Form form) {
		if(!form.containsMaterial(this)) {
			return null;
		}
		return MaterialForm.getMaterialFormWrapper(form.getInternal(), material);
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Material other)) {
			return false;
		}
		return material == other.material;
	}

	@Override
	public int hashCode() {
		return material.hashCode()+7;
	}
}
