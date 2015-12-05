package org.wuqispank.model;

import org.wuqispank.WuqispankException;

public interface IRequestListener {
	void add(IRequestWrapper rl) throws WuqispankException;
}
