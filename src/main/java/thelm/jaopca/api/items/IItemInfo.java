package thelm.jaopca.api.items;

import net.minecraft.item.Item;
import net.minecraft.util.IItemProvider;
import thelm.jaopca.api.materialforms.IMaterialForm;
import thelm.jaopca.api.materialforms.IMaterialFormInfo;

public interface IItemInfo extends IMaterialFormInfo, IItemProvider {

	IMaterialFormItem getMaterialFormItem();

	default Item getItem() {
		return getMaterialFormItem().asItem();
	}

	@Override
	default IMaterialForm getMaterialForm() {
		return getMaterialFormItem();
	}

	@Override
	default Item asItem() {
		return getItem();
	}
}
