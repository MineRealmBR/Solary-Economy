package nuvemplugins.solaryeconomy.plugin.objetos;

import java.io.File;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.minerealm.bukkit.engine.lib.nbt.NBTCompound;
import com.minerealm.bukkit.engine.lib.nbt.NBTItem;
import com.minerealm.bukkit.engine.util.BukkitData;

import nuvemplugins.solaryeconomy.app.SolaryEconomy;

public class Cheque
{
	public static final File DATA_FILE = new File(SolaryEconomy.instance.getDataFolder(), "cheques.json");

	public Cheque(BigDecimal valor) {
		this.valor = valor;
	}

	private BigDecimal valor;

	public BigDecimal getValor()
	{
		return valor;
	}

	public ItemStack toItemStack()
	{
		// CREATE UNIQUE ID
		UUID uuid = UUID.randomUUID();

		// CREATE DATA
		BukkitData bukkitData = new BukkitData(DATA_FILE).load();

		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("valor", this.valor);
		bukkitData.getData().add(uuid.toString(), jsonObject);

		bukkitData.save();

		// CREATE ITEM
		ItemStack itemStack = new ItemStack(Material.PAPER);
		NBTItem nbtItem = new NBTItem(itemStack);
		NBTCompound compound = nbtItem.addCompound("economy_cheque");
		compound.setString("uuid", uuid.toString());

		itemStack = nbtItem.getItem();

		ItemMeta itemMeta = itemStack.getItemMeta();

		itemMeta.setDisplayName("§6Cheque De Valores");
		itemMeta.setLore(Arrays.asList(" ", "§eEste é um cheque de valores, ao clicar com o botão",
				"§eDireito você pode creditar o valor do cheque em sua conta.", " ",
				("§6Valor: §f" + SolaryEconomy.numberFormat(this.valor)), " "));

		itemMeta.addEnchant(Enchantment.DURABILITY, 10, false);
		itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		itemStack.setItemMeta(itemMeta);

		return itemStack;
	}

	public static Cheque valueOf(ItemStack itemStack)
	{
		if (itemStack != null && itemStack.getType() == Material.PAPER) {
			NBTItem nbtItem = new NBTItem(itemStack);
			if (nbtItem.hasKey("economy_cheque")) {
				NBTCompound compound = nbtItem.getCompound("economy_cheque");
				if (compound != null) {

					String uuidString = compound.getString("uuid");

					// LOAD DATA
					BukkitData bukkitData = new BukkitData(DATA_FILE).load();
					JsonElement jsonElement = bukkitData.getData().get(uuidString);
					if (jsonElement != null && jsonElement.isJsonObject()) {
						BigDecimal valor = jsonElement.getAsJsonObject().get("valor").getAsBigDecimal();
						if (valor != null && valor.doubleValue() > 0.0D) {
							bukkitData.getData().remove(uuidString);
							bukkitData.save();
							return new Cheque(valor);
						}
					}
				}
			}
		}
		return null;
	}

	public static boolean isCheque(ItemStack itemStack)
	{
		if (itemStack != null && itemStack.getType() == Material.PAPER) {
			NBTItem nbtItem = new NBTItem(itemStack);
			if (nbtItem.hasKey("economy_cheque")) {
				return (nbtItem.getCompound("economy_cheque") != null);
			}
		}
		return false;
	}

}
