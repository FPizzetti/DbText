package main;

import annotation.DbText;
import dbText.DbTextInterface;

public class Time implements DbTextInterface
{
	@DbText(primaryKey = true)
	private int id;

	private String nome;

	private int pontos;

	public Time()
	{
	}

	public Time(int id, String nome, int pontos)
	{
		this.id = id;
		this.nome = nome;
		this.pontos = pontos;
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

	public int getPontos()
	{
		return pontos;
	}

	public void setPontos(int pontos)
	{
		this.pontos = pontos;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + pontos;
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Time))
			return false;
		Time other = (Time) obj;
		if (id != other.id)
			return false;
		if (nome == null)
		{
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (pontos != other.pontos)
			return false;
		return true;
	}

	@Override
	public Object createObject(Object... objects)
	{
		return new Time(Integer.parseInt((String) objects[0]),
				(String) objects[1], Integer.parseInt((String) objects[2]));
	}
}
