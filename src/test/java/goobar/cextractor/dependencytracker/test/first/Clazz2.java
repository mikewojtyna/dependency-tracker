/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package goobar.cextractor.dependencytracker.test.first;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author goobar
 */
@SuppressWarnings("javadoc")
public class Clazz2
{

	// full name reference, without import
	private goobar.cextractor.dependencytracker.test.second.Clazz3 clazz3;

	// the same package reference
	private Clazz1 testClass;

	public Clazz2()
	{
		// generic type reference, outside of project scope (Date)
		List<Date> dates = new ArrayList<Date>();
		dates.get(0);
		testClass.doSomething();
		clazz3.toString();
	}

	// method arguments reference
	public void methodWithStringArg(String msg)
	{
	}
}
