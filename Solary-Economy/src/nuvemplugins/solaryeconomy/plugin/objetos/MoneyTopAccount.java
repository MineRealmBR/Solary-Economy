package nuvemplugins.solaryeconomy.plugin.objetos;

import java.math.BigDecimal;

public class MoneyTopAccount implements Comparable<MoneyTopAccount>
{
	private String name;
	private BigDecimal balance;

	public MoneyTopAccount(String name, BigDecimal balance)
	{
		this.name = name;
		this.balance = balance;
	}

	public String getName()
	{
		return this.name;
	}

	public BigDecimal getBalance()
	{
		return this.balance;
	}

	public static MoneyTopAccount valueOf(Account account)
	{
		final String name = account.getName();
		final BigDecimal balance = account.getBalance();
		return new MoneyTopAccount(name, balance);
	}

	@Override
	public int compareTo(MoneyTopAccount account)
	{
		if (account.getBalance().doubleValue() > getBalance().doubleValue())
			return 1;

		if (account.getBalance().doubleValue() < getBalance().doubleValue())
			return -1;

		return 0;
	}

}
