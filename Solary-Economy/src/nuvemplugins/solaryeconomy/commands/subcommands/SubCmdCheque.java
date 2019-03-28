package nuvemplugins.solaryeconomy.commands.subcommands;

import java.math.BigDecimal;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import nuvemplugins.solaryeconomy.abstracts.SubCommand;
import nuvemplugins.solaryeconomy.app.SolaryEconomy;
import nuvemplugins.solaryeconomy.plugin.Economia;
import nuvemplugins.solaryeconomy.plugin.objetos.Cheque;

public class SubCmdCheque extends SubCommand
{

	public SubCmdCheque(String command) {
		super("cheque", "§cUse: /" + command + " cheque [valor]", "");
	}

	@Override
	public void execute(CommandSender sender, String[] args)
	{
		Player player = ((sender instanceof Player) ? ((Player) sender) : null);
		if (player != null) {
			if (args.length >= 2) {

				BigDecimal valor = this.numbers.getDecimal(args[1]);
				if (valor.doubleValue() > 0.0D) {
					if (valor.doubleValue() >= 1000.D) {
						if (Economia.getBalance(player).doubleValue() >= valor.doubleValue()) {

							if (player.getInventory().firstEmpty() != -1) {
								
								Economia.substractBalance(player.getName(), valor);
								player.getInventory().addItem(new Cheque(valor).toItemStack());
								player.sendMessage("§aVocê acaba de criar um cheque no valor de "
										+ SolaryEconomy.numberFormat(valor));
								
							} else {
								player.sendMessage("§cVocê não pode criar um cheque com o inventário cheio.");
							}

						} else {
							player.sendMessage("§cVocê não tem esse saldo para criar um cheque.");
						}
					} else {
						player.sendMessage("§cVocê precisa criar um cheque com o valor mínimo de "
								+ SolaryEconomy.numberFormat(new BigDecimal(1000.0)));
					}
				} else {
					sender.sendMessage(SolaryEconomy.mensagens.get("NUMBER_NULL"));
				}

			} else {
				sender.sendMessage(this.getUsage());
			}

		}
	}

}
