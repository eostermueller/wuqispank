package org.wuqispank.web;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.util.io.IClusterable;

public class User implements IClusterable {
	// The user's name
	private String name;

	/**
	 * @return User name
	 */
	public final String getName()
	{
		return name;
	}

	/**
	 * @param string
	 *            User name
	 */
	public final void setName(final String string)
	{
		name = string;
	}

}
