package main;

import java.util.List;

import file.ManageFile;

public class Main
{
	public static void main(String[] args)
	{
		ManageFile<Usuario> manageUsuario = new ManageFile<Usuario>(
				Usuario.class);
		ManageFile<Veiculo> manageVeiculo = new ManageFile<Veiculo>(
				Veiculo.class);
		ManageFile<Endereco> manageEndereco = new ManageFile<Endereco>(
				Endereco.class);
		// List<Usuario> as = manageUsuario.list();
		/*
		 * for (Usuario u : as) { System.out.println(u.getId());
		 * System.out.println(u.getIdade()); System.out.println(u.getNome());
		 * System.out.println(u.getAnimal().getNome()); System.out.println(); }
		 */

		// manageVeiculo.save(o);
		// System.out.println(veiculo.toString());

		// List<Veiculo> veiculosEncontrados = manageVeiculo.findByField("nome",
		// "Camaro");
		// for (Veiculo veiculo2 : veiculosEncontrados)
		// {
		// System.out.println(veiculo2.toString());
		// }
		// DecimalFormat df = new DecimalFormat("#,###.00");
		/*
		 * List<Veiculo> veiculos = manageVeiculo.list(); for (Veiculo v :
		 * veiculos) { System.out.println(v.getId());
		 * System.out.println(v.getNome()); System.out.println(v.getAno());
		 * System.out.println(df.format(v.getValor())); System.out.println(); }
		 */
		Endereco endereco = new Endereco(3, "rua augusta", 15, "3545-4568");
		Veiculo o = new Veiculo(1, "Celta", 2014, 25000);
		Time t = new Time(1, "internacional", 100);
		Usuario usuario = new Usuario();
		usuario.setId(2);
		usuario.setNome("joao");
		usuario.setIdade(30);
		usuario.setEndereco(endereco);
		usuario.setVeiculo(o);
		usuario.setTime(t);

		// manageVeiculo.removeAll();
		// manageUsuario.removeAll();
		// manageEndereco.removeAll();
		//System.out.println(manageUsuario.save(usuario));
		
		System.out.println(manageEndereco.remove(endereco));
		
		// System.out.println(manageVeiculo.countRegisters());

	/*	List<Usuario> a = manageUsuario.list();
		// System.out.println("vazia?"+a.isEmpty());
		// System.out.println(a.get(0).getId());
		for (Usuario usuario2 : a)
		{
			System.out.println(usuario2.toString());
			System.out.println(usuario2.getEndereco().toString());
			System.out.println(usuario2.getVeiculo().toString());
			System.out.println(usuario2.getTime().getNome());
			System.out.println();
		}*/
		/*
		 * Field[] campos = Usuario.class.getDeclaredFields(); for (Field f :
		 * campos) { System.out.println("a"); f.setAccessible(true); try {
		 * 
		 * System.out.println(f.get(usuario) + "  " +
		 * f.getType().getCanonicalName() + "     " + f.getName()); Field[] a =
		 * f.get(usuario).getClass().getDeclaredFields(); for (Field field : a)
		 * { field.setAccessible(true); System.out.println(field.get(usuario));
		 * }
		 * 
		 * } catch (IllegalArgumentException | IllegalAccessException |
		 * SecurityException e) { e.printStackTrace(); } }
		 */

	}
}
