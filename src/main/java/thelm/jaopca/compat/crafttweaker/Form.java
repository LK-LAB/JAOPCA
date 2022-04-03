package thelm.jaopca.compat.crafttweaker;

import java.util.TreeMap;

import org.openzen.zencode.java.ZenCodeType;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.fluid.IFluidStack;
import com.blamejared.crafttweaker.api.fluid.MCFluidStack;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.item.MCItemStack;
import com.blamejared.crafttweaker.api.tag.MCTag;
import com.blamejared.crafttweaker.api.tag.manager.ITagManager;
import com.blamejared.crafttweaker.api.tag.manager.TagManagerFluid;
import com.blamejared.crafttweaker.api.tag.manager.TagManagerItem;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import thelm.jaopca.api.forms.IForm;
import thelm.jaopca.api.materials.MaterialType;
import thelm.jaopca.utils.MiscHelper;

@ZenRegister
@ZenCodeType.Name("mods.jaopca.Form")
public class Form {

	private static final TreeMap<IForm, Form> FORM_WRAPPERS = new TreeMap<>();
	private final IForm form;

	public static Form getFormWrapper(IForm form) {
		return FORM_WRAPPERS.computeIfAbsent(form, Form::new);
	}

	private Form(IForm form) {
		this.form = form;
	}

	public IForm getInternal() {
		return form;
	}

	@ZenCodeType.Getter("name")
	public String getName() {
		return form.getName();
	}

	@ZenCodeType.Getter("type")
	public String getType() {
		return form.getType().getName();
	}

	@ZenCodeType.Getter("module")
	public Module getModule() {
		return Module.getModuleWrapper(form.getModule());
	}

	@ZenCodeType.Getter("secondaryName")
	public String getSecondaryName() {
		return form.getSecondaryName();
	}

	@ZenCodeType.Getter("materialTypes")
	public String[] getMaterialTypes() {
		return form.getMaterialTypes().stream().map(MaterialType::getName).toArray(String[]::new);
	}

	@ZenCodeType.Getter("materials")
	public Material[] getMaterials() {
		return form.getMaterials().stream().map(Material::getMaterialWrapper).toArray(Material[]::new);
	}

	@ZenCodeType.Getter("tagSeparator")
	public String getTagSeparator() {
		return form.getTagSeparator();
	}

	@ZenCodeType.Method
	public boolean containsMaterial(Material material) {
		return form.getMaterials().contains(material.getInternal());
	}

	@ZenCodeType.Method
	public MCTag<Item> getItemTag(String suffix) {
		return getTag(TagManagerItem.INSTANCE, suffix);
	}

	@ZenCodeType.Method
	public MCTag<Fluid> getFluidTag(String suffix) {
		return getTag(TagManagerFluid.INSTANCE, suffix);
	}

	@ZenCodeType.Method
	public <T> MCTag<T> getTag(ITagManager<T> manager, String suffix) {
		return new MCTag<>(MiscHelper.INSTANCE.getTagLocation(form.getSecondaryName(), suffix, form.getTagSeparator()), manager);
	}

	@ZenCodeType.Method
	public IItemStack getItemStack(String suffix, int count) {
		MiscHelper helper = MiscHelper.INSTANCE;
		ItemStack stack = helper.getItemStack(helper.getTagLocation(form.getSecondaryName(), suffix), count);
		return new MCItemStack(stack);
	}

	@ZenCodeType.Method
	public IItemStack getItemStack(String suffix) {
		return getItemStack(suffix, 1);
	}

	@ZenCodeType.Method
	public IFluidStack getFluidStack(String suffix, int count) {
		MiscHelper helper = MiscHelper.INSTANCE;
		FluidStack stack = helper.getFluidStack(helper.getTagLocation(form.getSecondaryName(), suffix), count);
		return new MCFluidStack(stack);
	}

	@ZenCodeType.Method
	public MaterialForm getMaterialForm(Material material) {
		if(!containsMaterial(material)) {
			return null;
		}
		return MaterialForm.getMaterialFormWrapper(form, material.getInternal());
	}

	@ZenCodeType.Getter("materialForms")
	public MaterialForm[] getMaterialForms() {
		return form.getMaterials().stream().map(m->MaterialForm.getMaterialFormWrapper(form, m)).toArray(MaterialForm[]::new);
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Form other)) {
			return false;
		}
		return form == other.form;
	}

	@Override
	public int hashCode() {
		return form.hashCode()+5;
	}
}
