/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wuqispank.web;

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;

import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.request.Request;

public class WebSession extends AuthenticatedWebSession
{
	// Logged in user
	private User user;

	/**
	 * Constructor
	 * 
	 * @param request
	 *            The current request object
	 */
	protected WebSession(Request request)
	{
		super(request);
	}

	/**
	 * Checks the given username and password, returning a User object if if the username and
	 * password identify a valid user.
	 * 
	 * @param username
	 *            The username
	 * @param password
	 *            The password
	 * @return The signed in user
	 */
	@Override
	public final boolean authenticate(final String username, final String password)
	{
		final String WICKET = "wicket";

		// Trivial password "db"
		if (WICKET.equalsIgnoreCase(username) && WICKET.equalsIgnoreCase(password))
		{
			// Create User object
			final User user = new User();

			user.setName(username);


			setUser(user);

			return true;
		}

		return false;
	}

	/**
	 * @return User
	 */
	public User getUser()
	{
		return user;
	}

	/**
	 * @param user
	 *            New user
	 */
	public void setUser(final User user)
	{
		this.user = user;
	}

	/**
	 * @see AuthenticatedWebSession#signOut()
	 */
	@Override
	public void signOut()
	{
		super.signOut();
		user = null;
	}

	/**
	 * @see AuthenticatedWebSession#getRoles()
	 */
	@Override
	public Roles getRoles()
	{
		return null;
	}

}
