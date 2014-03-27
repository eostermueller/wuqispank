package org.wuqispank.model;

import java.util.Arrays;

public class DefaultStackTrace implements IStackTrace, java.io.Serializable {
	StackTraceElement[] m_stackTraceElements = null;
	@Override
	public void setStackTraceElements(StackTraceElement[] ste) {
		m_stackTraceElements = ste;
	}

	@Override
	public StackTraceElement[] getStackTraceElements() {
		return m_stackTraceElements;
	}
	public String toString() {
		return Arrays.toString(getStackTraceElements());
	}

	@Override
	public String getStackTrace() {
		return Arrays.toString(this.getStackTraceElements());
	}

}
