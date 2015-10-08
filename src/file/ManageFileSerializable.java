package file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ManageFileSerializable<T>
{
	private String path = "DbText/";
	List<T> objects = new ArrayList<T>();
	private Class<T> clazz;

	public ManageFileSerializable(Class<T> c)
	{
		this.clazz = c;
	}

	private void createFile()
	{
		File dir = new File(path);
		if (!dir.exists())
		{
			dir.mkdir();
		}

		File f = new File(path + getClazz().getSimpleName() + ".dat");
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

	private void encodeList(List<T> list)
	{
		FileOutputStream file = null;
		ObjectOutputStream out = null;
		try
		{
			file = new FileOutputStream(path + getClazz().getSimpleName()
					+ ".dat");
			out = new ObjectOutputStream(file);
			out.writeObject(list);
		} catch (IOException ex)
		{
			ex.printStackTrace();
		} finally
		{
			try
			{
				file.close();
				out.close();
			} catch (IOException ex)
			{
				ex.printStackTrace();
			}
		}
	}

	@SuppressWarnings("unchecked")
	private List<T> decodeList()
	{
		FileInputStream fileRead = null;
		ObjectInputStream oIS = null;
		ArrayList<T> list = null;
		try
		{
			fileRead = new FileInputStream(path + getClass().getSimpleName()
					+ ".dat");
			oIS = new ObjectInputStream(fileRead);
			list = (ArrayList<T>) oIS.readObject();
		} catch (ClassNotFoundException ex)
		{
			ex.printStackTrace();
		} catch (IOException ex)
		{
			ex.printStackTrace();
		} finally
		{
			try
			{
				fileRead.close();
				oIS.close();
			} catch (IOException ex)
			{
				ex.printStackTrace();
			}
		}

		return list;
	}

	public void save(T o)
	{
		createFile();
		objects.add(o);
		encodeList(objects);
	}

	public void remove(T o)
	{
		List<T> t = decodeList();
		t.remove(o);
		encodeList(t);
	}

	public List<T> list(Class<?> c)
	{
		return decodeList();
	}

	private Class<T> getClazz()
	{
		return clazz;
	}
}
