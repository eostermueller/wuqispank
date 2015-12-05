package org.wuqispank.model;

import org.wuqispank.WuqispankException;

public interface IRequestManager {

	void registerListener(IRequestListener requestListener1);

	void add(IRequestWrapper rw) throws WuqispankException;

}
