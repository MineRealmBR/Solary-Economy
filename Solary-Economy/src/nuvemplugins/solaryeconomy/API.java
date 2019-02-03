package nuvemplugins.solaryeconomy;

import java.util.List;

import nuvemplugins.solaryeconomy.plugin.Economia;
import nuvemplugins.solaryeconomy.plugin.objetos.Account;

/**
 * Esta classe é para auxiliar na criação de novos plugins usando a api do
 * Solary-Economy
 * 
 */

public class API
{

	// Pega o magnata atual do servidor.
	public Account getMagnata()
	{
		return Economia.MAGNATA;
	}

	// Retorna true se a conta estiver com o coins desabilitado, ou false caso
	// contr§rio
	public boolean isToggle(String account)
	{
		return Economia.isToggle(account);
	}

	// Retorna o money top atual do servidor.
	public List<Account> getMoneyTop()
	{
		return Economia.MONEY_TOP;
	}

	/*
	 * Você pode usar a classe 'Economia' para integrar qualquer plugin ao
	 * Solary-Economy *
	 */
}
