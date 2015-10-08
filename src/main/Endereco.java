package main;

import dbText.DbTextInterface;
import annotation.DbText;

public class Endereco implements DbTextInterface
{
	@DbText(primaryKey = true)
	private int id;
	private String rua;
	private int numero;
	private String telefone;

	public Endereco()
	{
	}

	public Endereco(int id, String rua, int numero, String telefone)
	{
		this.id = id;
		this.rua = rua;
		this.numero = numero;
		this.telefone = telefone;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getRua()
	{
		return rua;
	}

	public void setRua(String rua)
	{
		this.rua = rua;
	}

	public int getNumero()
	{
		return numero;
	}

	public void setNumero(int numero)
	{
		this.numero = numero;
	}

	public String getTelefone()
	{
		return telefone;
	}

	public void setTelefone(String telefone)
	{
		this.telefone = telefone;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + numero;
		result = prime * result + ((rua == null) ? 0 : rua.hashCode());
		result = prime * result
				+ ((telefone == null) ? 0 : telefone.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Endereco))
			return false;
		Endereco other = (Endereco) obj;
		if (id != other.id)
			return false;
		if (numero != other.numero)
			return false;
		if (rua == null)
		{
			if (other.rua != null)
				return false;
		} else if (!rua.equals(other.rua))
			return false;
		if (telefone == null)
		{
			if (other.telefone != null)
				return false;
		} else if (!telefone.equals(other.telefone))
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "Endereco [id=" + id + ", rua=" + rua + ", numero=" + numero
				+ ", telefone=" + telefone + "]";
	}

	@Override
	public Object createObject(Object... objects)
	{
		return new Endereco(Integer.parseInt((String) objects[0]),
				(String) objects[0], Integer.parseInt((String) objects[0]),
				(String) objects[0]);
	}
}
