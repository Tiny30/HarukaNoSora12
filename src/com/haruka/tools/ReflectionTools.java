package com.haruka.tools;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 与反射有关的工具类
 * 
 * @author Haru@Lich
 */
public class ReflectionTools {

	private static Map<Class, Class> mapper = new HashMap();

	/**
	 * 获得父类的泛型
	 * 
	 * @param clazz
	 *            传入类型
	 * @param index
	 *            传入查找的泛型索引
	 * @return 返回泛型的类型
	 */
	public static Class getSuperClassGenericType(Class clazz, int index) {

		// 返回父类的泛型
		Type genType = clazz.getGenericSuperclass();
		try {
			Type[] types = ((ParameterizedType) genType).getActualTypeArguments();
			return (Class) types[index];
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Object.class;
		}
	}

	/**	初始化映射 传入包名
	 * @param packageNames
	 */
	public static void initMapper(String... packageNames) {
		for (String packageName : packageNames) {
			Enumeration<URL> enu;
			try {
				enu = Thread.currentThread().getContextClassLoader().getResources(packageName.replace(".", "/"));

				while (enu.hasMoreElements()) {
					String path = enu.nextElement().getPath();
					File file = new File(path);
					for (File f : file.listFiles()) {
						if (f.getName().endsWith(".class")) {
							Class c = Class.forName(packageName + "." + f.getName().replaceAll(".class", "")) ;
							List<Class> list = getAllClassByInterface(c) ;
							if(list.size()>0)
							mapper.put(c, list.get(0)) ;
						}
					}
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	/** 获得一个接口的实例， 该实例必须存在于跟该接口同级目录或者子目录
	 * @param clazz 传入接口的class
	 * @return 返回一个接口的实现类对象
	 */
	public static <T> T getInstance(Class<T> clazz) {
		if (!clazz.isInterface()) {
			return null;
		}
		
		try {
			return (T) mapper.get(clazz).newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null ;
	}

	/**
	 * @Description: 根据一个接口返回该接口的所有类
	 * @param c
	 *            接口
	 * @return List<Class> 实现接口的所有类
	 * @author LiYaoHua
	 * @date 2012-4-5 上午11:22:24
	 */
	@SuppressWarnings("unchecked")
	private static List<Class> getAllClassByInterface(Class c) {
		List returnClassList = new ArrayList<Class>();
		// 判断是不是接口,不是接口不作处理
		if (c.isInterface()) {
			String packageName = c.getPackage().getName(); // 获得当前包名
			try {
				List<Class> allClass = getClasses(packageName);// 获得当前包以及子包下的所有类

				// 判断是否是一个接口
				for (int i = 0; i < allClass.size(); i++) {
					if (c.isAssignableFrom(allClass.get(i))) {
						if (!c.equals(allClass.get(i))) {
							returnClassList.add(allClass.get(i));
						}
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return returnClassList;
	}

	/**
	 * 
	 * @Description: 根据包名获得该包以及子包下的所有类不查找jar包中的
	 * @param pageName
	 *            包名
	 * @return List<Class> 包下所有类
	 * @author LiYaoHua
	 * @date 2012-4-5 上午11:26:48
	 */
	private static List<Class> getClasses(String packageName) throws ClassNotFoundException, IOException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		String path = packageName.replace(".", "/");
		Enumeration<URL> resources = classLoader.getResources(path);
		List<File> dirs = new ArrayList<File>();
		while (resources.hasMoreElements()) {
			URL resource = resources.nextElement();
			dirs.add(new File(resource.getFile()));
		}
		ArrayList<Class> classes = new ArrayList<Class>();
		for (File directory : dirs) {
			classes.addAll(findClass(directory, packageName));
		}
		return classes;
	}

	private static List<Class> findClass(File directory, String packageName) throws ClassNotFoundException {
		List<Class> classes = new ArrayList<Class>();
		if (!directory.exists()) {
			return classes;
		}
		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				assert !file.getName().contains(".");
				classes.addAll(findClass(file, packageName + "." + file.getName()));
			} else if (file.getName().endsWith(".class")) {
				classes.add(
						Class.forName(packageName + "." + file.getName().substring(0, file.getName().length() - 6)));
			}
		}
		return classes;
	}

}
