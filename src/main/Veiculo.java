package main;

import annotation.DbText;
import dbText.DbTextInterface;

public class Veiculo implements DbTextInterface
{
	@DbText(primaryKey = true)
	private int id;
	private String nome;
	private int ano;
	private double valor;

	public Veiculo()
	{
	}

	public Veiculo(int id, String nome, int ano, double valor)
	{
		this.id = id;
		this.nome = nome;
		this.ano = ano;
		this.valor = valor;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getNome()
	{
		return nome;
	}

	public void setNome(String nome)
	{
		this.nome = nome;
	}

	public int getAno()
	{
		return ano;
	}

	public void setAno(int ano)
	{
		this.ano = ano;
	}

	public double getValor()
	{
		return valor;
	}

	public void setValor(double valor)
	{
		this.valor = valor;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ano;
		result = prime * result + id;
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		long temp;
		temp = Double.doubleToLongBits(valor);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Veiculo))
			return false;
		Veiculo other = (Veiculo) obj;
		if (ano != other.ano)
			return false;
		if (id != other.id)
			return false;
		if (nome == null)
		{
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (Double.doubleToLongBits(valor) != Double
				.doubleToLongBits(other.valor))
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "Veiculo [id=" + id + ", nome=" + nome + ", ano=" + ano
				+ ", valor=" + valor + "]";
	}

	@Override
	public Object createObject(Object... objects)
	{
		return new Veiculo(Integer.parseInt((String) objects[0]),
				(String) objects[1], Integer.parseInt((String) objects[2]),
				Double.parseDouble((String) objects[3]));
	}

}
