package nuvemplugins.solaryeconomy.plugin.objetos;

import java.math.BigDecimal;
import java.sql.ResultSet;

public class Account
{

	public Account(String nome, BigDecimal valor)
	{
		this.name = nome;
		this.balance = valor;
	}

	private String name;
	private BigDecimal balance;
	private boolean toggle;

	public String getName()
	{
		return this.name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public BigDecimal getBalance()
	{
		return this.balance;
	}

	public void setBalance(BigDecimal valor)
	{
		this.balance = valor;
	}

	public boolean isToggle()
	{
		return this.toggle;
	}

	public void setToggle(boolean toggle)
	{
		this.toggle = toggle;
	}

	public static Account valueOf(ResultSet result)
	{
		Account account = null;
		try {
			String name = result.getString("name");
			String valorString = result.getString("valor");
			boolean toggle = result.getInt("toggle") >= 1 ? true : false;
			account = new Account(name, new BigDecimal(valorString));
			account.setToggle(toggle);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return account;
	}

}
