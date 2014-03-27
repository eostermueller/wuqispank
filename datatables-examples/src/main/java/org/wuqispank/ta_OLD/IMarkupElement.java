package org.wuqispank.ta_OLD;

import java.io.IOException;
import java.io.Writer;

public interface IMarkupElement {
	void render(Writer w) throws IOException;

}
