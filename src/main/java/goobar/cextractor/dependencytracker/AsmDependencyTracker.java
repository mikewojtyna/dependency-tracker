/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package goobar.cextractor.dependencytracker;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import org.objectweb.asm.ClassReader;

/**
 * Implementation of {@link DependencyTracker} based on ASM Bytecode Framework.
 *
 * @author goobar
 */
public class AsmDependencyTracker implements DependencyTracker
{

	private static final String EXTENSION = ".class";

	private static final String PACKAGE_SEPARATOR = ".";

	@Override
	public Set<String> findAllStaticDependencies(String clazz)
		throws ClassNotFoundException
	{
		try
		{
			return findAllStaticDependencies(
				new ClassReader(clazz));
		}
		catch (IOException e)
		{
			throw wrapException(e);
		}
	}

	@Override
	public Set<String> findAllStaticDependencies(String clazz,
		Path pathToClasses) throws ClassNotFoundException
	{
		try (InputStream classInputStream = Files.newInputStream(
			preparePathFromFullyQualifiedClassNameWithExtension(
				clazz, pathToClasses)))
		{
			return findAllStaticDependencies(
				new ClassReader(classInputStream));
		}
		catch (IOException e)
		{
			throw wrapException(e);
		}
	}

	private Set<String> findAllStaticDependencies(ClassReader classReader)
	{
		DependencyVisitor visitor = new DependencyVisitor();
		classReader.accept(visitor, 0);
		return visitor.getDependencies();
	}

	private Path preparePathFromFullyQualifiedClassNameWithExtension(
		String fullyQualifiedClassName, Path root)
	{
		Path pathWithoutExtension = preparePathFromPackage(
			fullyQualifiedClassName, root);
		return FileSystems.getDefault()
			.getPath(pathWithoutExtension.toString() + EXTENSION);
	}

	private Path preparePathFromPackage(String pckg, Path root)
	{
		Path path = root;
		String[] packageAsArrayOfStrings = pckg
			.split("\\" + PACKAGE_SEPARATOR);
		for (int i = 0; i < packageAsArrayOfStrings.length; i++)
		{
			String subPackage = packageAsArrayOfStrings[i];
			path = path.resolve(subPackage);
		}
		return path;
	}

	/**
	 * @param ex
	 * @return
	 */
	private ClassNotFoundException wrapException(IOException ex)
	{
		return new ClassNotFoundException(ex.getMessage());
	}
}
