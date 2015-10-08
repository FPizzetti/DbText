package file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import annotation.DbText;
import annotation.Header;

@Header(creator = "Felipe F. P.", creationDate = "21/05/2014", project = "DbText", review = 1)
public class ManageFile<T>
{
	private String path = System.getProperty("user.dir") + "/DbText";
	private Class<T> clazz;

	public ManageFile(Class<T> c)
	{
		this.clazz = c;
		createFile();
	}

	/**
	 * This method create a file based in your class name if it not exists
	 * 
	 * @author Felipe Pizzetti
	 * @since 1.0
	 * 
	 * */
	private void createFile()
	{
		File dir = new File(path);
		if (!dir.exists())
		{
			dir.mkdir();
		}

		File f = new File(path + "/" + getClazz().getSimpleName() + ".dat");
		if (!f.exists())
		{
			try
			{
				f.createNewFile();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	private int getFieldIndex(String fieldName)
	{
		Field[] fields = this.getClazz().getDeclaredFields();
		for (int i = 0; i < fields.length; i++)
		{
			if (fields[i].getName().equals(fieldName))
			{
				return i;
			}
		}
		return -1;
	}

	@SuppressWarnings("unchecked")
	private T createObject(Object... objects)
	{
		try
		{
			Method m = getClazz().getMethod("createObject",
					java.lang.Object[].class);
			return (T) (m.invoke(getClazz().newInstance(), (Object) objects));
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | SecurityException
				| InstantiationException | NoSuchMethodException e1)
		{

			e1.printStackTrace();
		}
		return null;
	}

	/**
	 * This method removes a object from a file
	 * 
	 * @author Felipe Pizzetti
	 * @param Object
	 * @since 1.0
	 * @return boolean
	 * 
	 * */
	public boolean remove(T o)
	{
		String file = path + "/" + getClazz().getSimpleName() + ".dat";
		try
		{
			File inFile = new File(file);

			if (!inFile.isFile())
			{
				System.err.println("Parameter is not an existing file");
				return false;
			}
			File tempFile = new File(path + getClazz().getSimpleName() + ".tmp");

			BufferedReader br = new BufferedReader(new FileReader(file));
			PrintWriter pw = new PrintWriter(new FileWriter(tempFile));

			String line = null;

			String lineToRemove = objectToString(o).trim();
			while ((line = br.readLine()) != null)
			{
				if (!line.trim().equals(lineToRemove))
				{
					pw.println(line);
					pw.flush();
				}
			}
			pw.close();
			br.close();

			if (!inFile.delete())
			{
				System.err.println("Could not delete file");
				return false;
			}

			if (!tempFile.renameTo(inFile))
			{
				System.err.println("Could not rename file");
				return false;
			}
			return true;

		} catch (FileNotFoundException ex)
		{
			ex.printStackTrace();
		} catch (IOException ex)
		{
			ex.printStackTrace();
		}
		return false;
	}

	/**
	 * This method inserts a object into a file
	 * 
	 * @author Felipe Pizzetti
	 * @param Object
	 * @return boolean
	 * @since 1.0
	 * 
	 * */
	public boolean save(T o)
	{
		boolean isSalvable = true;
		try
		{
			for (Field f : getClazz().getDeclaredFields())
			{

				f.setAccessible(true);
				if (isComposition(f))
				{

					String uri2 = path + "/" + f.getType().getSimpleName()
							+ ".dat";
					File file = new File(uri2);
					if (!file.exists())
					{
						try
						{
							file.createNewFile();
						} catch (IOException e)
						{
							e.printStackTrace();
						}
					}

					try
					{
						// cria objeto q esta dentro
						Field fa = getClazz().getField(f.getName());
						Object ob = fa.get(o);
						Files.write(Paths.get(uri2), objectToString(ob)
								.getBytes(), StandardOpenOption.APPEND);

					} catch (NoSuchFieldException | SecurityException
							| IllegalArgumentException | IllegalAccessException e)
					{
						e.printStackTrace();
					}
				}

				String c = objectToString(o).trim();
				String[] campos = c.split(";");
				int index = getFieldIndex(f.getName());
				if (isPrimaryKey(f) || isUnique(f) && !isComposition(f))
				{
					if (findByField(f.getName(), campos[index]).size() != 0)
					{
						if (isUnique(f))
						{
							System.err.println("Error - Not Unique. "
									+ getClazz().getSimpleName() + "."
									+ f.getName());
						}

						if (isPrimaryKey(f))
						{
							System.err
									.println("Error - Primary key duplicated. "
											+ getClazz().getSimpleName() + "."
											+ f.getName());
						}
						isSalvable = false;
						break;
					}
				}
			}

			if (isSalvable)
			{
				String uri = path + "/" + o.getClass().getSimpleName() + ".dat";
				Path path = Paths.get(uri);
				byte[] bytes = objectToString(o).getBytes();
				Files.write(path, bytes, StandardOpenOption.APPEND);
			}

		} catch (Exception e)
		{
			isSalvable = false;
			e.printStackTrace();
		}
		return isSalvable;
	}

	private boolean isPrimaryKey(Field f)
	{
		if (f.isAnnotationPresent(DbText.class))
		{
			DbText annot = f.getAnnotation(DbText.class);
			return annot.primaryKey();
		}
		return false;
	}

	private boolean isComposition(Field f)
	{
		if (f.isAnnotationPresent(DbText.class))
		{
			DbText annot = f.getAnnotation(DbText.class);
			return annot.composition();
		}
		return false;
	}

	private boolean isUnique(Field f)
	{
		if (f.isAnnotationPresent(DbText.class))
		{
			DbText annot = f.getAnnotation(DbText.class);
			return annot.unique();
		}
		return false;
	}

	public List<T> findByField(String field, String value)
	{
		int index = getFieldIndex(field);
		String uri = path + "/" + getClazz().getSimpleName() + ".dat";
		List<T> objetos = new ArrayList<>();
		try
		{
			BufferedReader leitor = new BufferedReader(new FileReader(new File(
					uri)));
			String line;
			while ((line = leitor.readLine()) != null)
			{
				try
				{
					String[] campos = line.trim().split(";");
					if (campos[index].contains(value))
					{
						try
						{
							objetos.add(createObject(((Object[]) campos)));
						} catch (SecurityException | IllegalArgumentException e)
						{
							e.printStackTrace();
						}
					}
				} catch (Exception e)
				{
					System.err.println("Error - Field not found");
					e.printStackTrace();
				}
			}
		} catch (SecurityException | IllegalArgumentException | IOException e)
		{
			e.printStackTrace();
		}

		return objetos;
	}

	public Object find(Object o)
	{
		String fileName = path + "/" + o.getClass().getSimpleName() + ".dat";
		File file = new File(fileName);

		String line = "";
		String lineToFind = objectToString(o).trim();
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(file));
			while ((line = br.readLine()) != null)
			{
				if (line.trim().equals(lineToFind))
				{
					String[] campos = line.split(";");
					return (T) createObject((Object[]) campos);
				}
			}
			br.close();
		} catch (IOException e1)
		{
			e1.printStackTrace();
		}
		return null;
	}

	private String objectToString(Object o)
	{
		Field[] f = o.getClass().getDeclaredFields();
		StringBuilder line = new StringBuilder();
		for (Field field : f)
		{
			field.setAccessible(true);
			if (isComposition(field))
			{
				try
				{
					Object comp = field.get(o);
					// add fk
					for (Field fieldComp : comp.getClass().getDeclaredFields())
					{
						fieldComp.setAccessible(true);
						if (isPrimaryKey(fieldComp))
						{
							line.append(fieldComp.get(comp)).append(";");
						}
					}
				} catch (IllegalArgumentException | IllegalAccessException e)
				{
					e.printStackTrace();
				}
			}

			else
			{
				try
				{
					line.append(field.get(o)).append(";");
				} catch (IllegalArgumentException | IllegalAccessException e)
				{
					e.printStackTrace();
				}
			}
		}
		line.append(System.getProperty("line.separator"));
		return line.toString();
	}

	private String fullObjectToString(Object o)
	{
		List<Field> fieldsComposition = new ArrayList<>();

		for (Field f : getClazz().getDeclaredFields())
		{
			if (isComposition(f))
			{

				fieldsComposition.add(f);
			}
		}

		List<String> contemTudo = new ArrayList<>();
		Set<Integer> indexes = new HashSet<Integer>();

		Collection<String> c1 = null;

		String[] campos = null;

		List<String> camposLista = new ArrayList<String>(Arrays.asList(campos));

		c1 = new ArrayList<String>(camposLista);
		contemTudo.addAll(c1);

		for (Field ff : getClazz().getFields())
		{
			if (isComposition(ff))
			{
				int index = getFieldIndex(ff.getName());
				indexes.add(index);
			}
		}
		for (Field fComposition : fieldsComposition)
		{
			int a = getFieldIndex(fComposition.getName());
			int primarykeyindex = 0;
			for (Field f : fComposition.getType().getDeclaredFields())
			{
				if (isPrimaryKey(f))
				{
					List<String> linhas;
					try
					{
						linhas = Files.readAllLines(
								Paths.get(path
										+ "/"
										+ fComposition.getType()
												.getSimpleName() + ".dat"),
								Charset.defaultCharset());
						for (String linha : linhas)
						{
							String[] camposComposicao = linha.split(";");
							if (camposComposicao[primarykeyindex]
									.equals(campos[a]))
							{

								Collection<String> cComp = new ArrayList<String>(
										Arrays.asList(camposComposicao));

								contemTudo.addAll(cComp);
							}
						}
					} catch (IOException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else
				{
					primarykeyindex++;
				}
			}
		}
		int qtd = 0;
		for (Integer i : indexes)
		{
			contemTudo.remove(i.intValue() - qtd);
			qtd++;
		}
		return "";
	}

	/**
	 * This method returns every object of a file, in a list
	 * 
	 * @author Felipe Pizzetti
	 * @since 1.0
	 * @return List<T>
	 * 
	 * */
	public List<T> list()
	{
		String uri = path + "/" + getClazz().getSimpleName() + ".dat";
		// Path path = Paths.get(uri);
		List<T> objetos = new ArrayList<>();

		try
		{
			boolean isPossui = false;
			List<Field> fieldsComposition = new ArrayList<>();
			Scanner lerArquivo = new Scanner(new File(uri));
			for (Field f : getClazz().getDeclaredFields())
			{
				if (isComposition(f))
				{
					isPossui = true;
					fieldsComposition.add(f);
				}
			}

			while (lerArquivo.hasNextLine())
			{
				List<String> contemTudo = new ArrayList<>();
				Set<Integer> indexes = new HashSet<Integer>();
				String line = lerArquivo.nextLine();
				Collection<String> c1 = null;

				String[] campos = line.split(";");

				List<String> camposLista = new ArrayList<String>(
						Arrays.asList(campos));

				c1 = new ArrayList<String>(camposLista);
				contemTudo.addAll(c1);

				for (Field ff : getClazz().getFields())
				{
					if (isComposition(ff))
					{
						int index = getFieldIndex(ff.getName());
						indexes.add(index);
					}
				}

				try
				{
					if (!isPossui)
					{
						objetos.add(createObject((Object[]) campos));
					} else
					{
						for (Field fComposition : fieldsComposition)
						{
							int a = getFieldIndex(fComposition.getName());
							int primarykeyindex = 0;
							for (Field f : fComposition.getType()
									.getDeclaredFields())
							{
								if (isPrimaryKey(f))
								{
									List<String> linhas = Files.readAllLines(
											Paths.get(path
													+ "/"
													+ fComposition.getType()
															.getSimpleName()
													+ ".dat"), Charset
													.defaultCharset());

									for (String linha : linhas)
									{
										String[] camposComposicao = linha
												.split(";");
										if (camposComposicao[primarykeyindex]
												.equals(campos[a]))
										{

											Collection<String> cComp = new ArrayList<String>(
													Arrays.asList(camposComposicao));

											contemTudo.addAll(cComp);
										}
									}
								} else
								{
									primarykeyindex++;
								}
							}
						}
						int qtd = 0;
						for (Integer i : indexes)
						{
							contemTudo.remove(i.intValue() - qtd);
							qtd++;
						}
						objetos.add(createObject(contemTudo.toArray()));
					}

				} catch (SecurityException | IllegalArgumentException e)
				{
					e.printStackTrace();
				}
			}
		} catch (IOException | SecurityException | IllegalArgumentException e)
		{
			e.printStackTrace();
		}
		return (List<T>) objetos;
	}

	public int countRegisters()
	{
		LineNumberReader lnr;
		try
		{
			lnr = new LineNumberReader(new FileReader(new File(path + "/"
					+ getClazz().getSimpleName() + ".dat")));
			lnr.skip(Long.MAX_VALUE);
			return lnr.getLineNumber();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return 0;

	}

	public boolean removeAll()
	{
		File f = new File(path + "/" + getClazz().getSimpleName() + ".dat");
		if (f.exists())
		{
			f.delete();
			try
			{
				f.createNewFile();
			} catch (IOException e)
			{
				e.printStackTrace();
				return false;
			}
			return true;
		}
		return false;

	}

	private Class<T> getClazz()
	{
		return clazz;
	}
}
