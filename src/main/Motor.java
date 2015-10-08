package main;

import annotation.DbText;
import dbText.DbTextInterface;

public class Motor implements DbTextInterface
{
	@DbText(primaryKey = true)
	private int id;
	private int potencia;

	public Motor()
	{
	}

	public Motor(int id, int potencia)
	{
		super();
		this.id = id;
		this.potencia = potencia;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public int getPotencia()
	{
		return potencia;
	}

	public void setPotencia(int potencia)
	{
		this.potencia = potencia;
	}

	@Override
	public Object createObject(Object... objects)
	{
		return new Motor(Integer.parseInt((String) objects[0]),
				Integer.parseInt((String) objects[1]));
	}

}
