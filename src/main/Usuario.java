package main;

import annotation.DbText;
import dbText.DbTextInterface;

public class Usuario implements DbTextInterface
{
	@DbText(primaryKey = true)
	private int id;

	@DbText(unique = true)
	private String nome;

	private int idade;

	@DbText(composition = true)
	public Endereco endereco;

	@DbText(composition = true)
	public Veiculo veiculo;

	@DbText(composition = true)
	public Time time;

	public Usuario()
	{
	}

	public Usuario(int id, String nome, int idade, Endereco endereco,
			Veiculo veiculo, Time time)
	{
		this.id = id;
		this.nome = nome;
		this.idade = idade;
		this.endereco = endereco;
		this.veiculo = veiculo;
		this.time = time;
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

	public int getIdade()
	{
		return idade;
	}

	public void setIdade(int idade)
	{
		this.idade = idade;
	}

	public Endereco getEndereco()
	{
		return endereco;
	}

	public void setEndereco(Endereco endereco)
	{
		this.endereco = endereco;
	}

	public Veiculo getVeiculo()
	{
		return veiculo;
	}

	public void setVeiculo(Veiculo veiculo)
	{
		this.veiculo = veiculo;
	}

	public Time getTime()
	{
		return time;
	}

	public void setTime(Time time)
	{
		this.time = time;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((endereco == null) ? 0 : endereco.hashCode());
		result = prime * result + id;
		result = prime * result + idade;
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Usuario))
			return false;
		Usuario other = (Usuario) obj;
		if (endereco == null)
		{
			if (other.endereco != null)
				return false;
		} else if (!endereco.equals(other.endereco))
			return false;
		if (id != other.id)
			return false;
		if (idade != other.idade)
			return false;
		if (nome == null)
		{
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "Usuario [id=" + id + ", nome=" + nome + ", idade=" + idade
				+ ", endereco=" + endereco.getRua() + ", veiculo="
				+ veiculo.getNome() + "]";
	}

	public Object createObject(Object... objects)
	{
		return new Usuario(Integer.parseInt((String) objects[0]),
				(String) objects[1], Integer.parseInt((String) objects[2]),
				new Endereco(Integer.parseInt((String) objects[3]),
						(String) objects[4],
						Integer.parseInt((String) objects[5]),
						(String) objects[6]), new Veiculo(
						Integer.parseInt((String) objects[7]),
						(String) objects[8],
						Integer.parseInt((String) objects[9]),
						Double.parseDouble((String) objects[10])), new Time(
						Integer.parseInt((String) objects[11]),
						(String) objects[12],
						Integer.parseInt((String) objects[13])));
	}
}
