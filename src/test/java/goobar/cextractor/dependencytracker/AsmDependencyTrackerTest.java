/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package goobar.cextractor.dependencytracker;

import static org.junit.Assert.assertEquals;
import java.nio.file.FileSystems;
import java.util.HashSet;
import java.util.Set;
import org.junit.Test;

/**
 *
 * @author goobar
 */
@SuppressWarnings("javadoc")
public class AsmDependencyTrackerTest
{

	@Test
	public void should_FindAllStaticDependencies_When_ClasspathIsGiven()
		throws Exception
	{

		// given
		String clazz = "goobar.cextractor.dependencytracker.test.first.Clazz2";
		Set<String> expectedDependencies = expectedDependencies();
		AsmDependencyTracker instance = new AsmDependencyTracker();

		// when
		Set<String> result = instance.findAllStaticDependencies(clazz,
			FileSystems.getDefault().getPath("target",
				"test-classes"));

		// then
		assertEquals(expectedDependencies, result);
	}

	@Test
	public void should_FindAllStaticDependencies_When_UsingDefaultClasspath()
		throws Exception
	{
		// given
		String clazz = "goobar.cextractor.dependencytracker.test.first.Clazz2";
		Set<String> expectedDependencies = expectedDependencies();
		AsmDependencyTracker instance = new AsmDependencyTracker();

		// when
		Set<String> result = instance.findAllStaticDependencies(clazz);

		// then
		assertEquals(expectedDependencies, result);
	}

	private Set<String> expectedDependencies()
	{
		Set<String> expectedResult = new HashSet<String>();
		expectedResult.add("java.util.ArrayList");
		expectedResult.add("java.lang.Object");
		expectedResult.add("java.util.List");
		expectedResult.add("java.util.Date");
		expectedResult.add(
			"goobar.cextractor.dependencytracker.test.first.Clazz1");
		expectedResult.add("java.lang.String");
		expectedResult.add(
			"goobar.cextractor.dependencytracker.test.first.Clazz2");
		expectedResult.add(
			"goobar.cextractor.dependencytracker.test.second.Clazz3");
		return expectedResult;
	}
}
