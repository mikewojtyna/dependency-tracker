/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package goobar.cextractor.dependencytracker;

import java.nio.file.Path;
import java.util.Set;

/**
 * Interface used to track all dependencies.
 *
 * @author goobar
 */
public interface DependencyTracker
{

	/**
	 * Find all static dependencies in the class.
	 *
	 * @param clazz
	 *                Fully qualified class name to load. Class must be
	 *                available inside classpath.
	 * @return Set containing all static dependencies.
	 * @throws ClassNotFoundException
	 *                 Thrown when class can't be loaded.
	 */
	public Set<String> findAllStaticDependencies(String clazz)
		throws ClassNotFoundException;

	/**
	 * Find all static dependencies in the class.
	 *
	 * @param clazz
	 *                Fully qualified class name to load. Class must be
	 *                available inside {@code pathToClasses}
	 * @param pathToClasses
	 *                Path to directory where class files are placed.
	 * @return Set containing all static dependencies.
	 * @throws ClassNotFoundException
	 *                 Thrown when class can't be loaded.
	 */
	public Set<String> findAllStaticDependencies(String clazz,
		Path pathToClasses) throws ClassNotFoundException;
}
