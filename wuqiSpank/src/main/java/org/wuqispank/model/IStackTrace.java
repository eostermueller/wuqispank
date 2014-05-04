package org.wuqispank.model;

public interface IStackTrace {

	StackTraceElement[] getStackTraceElements();
	String getStackTrace();

	void setStackTraceElements(StackTraceElement[] ste);
}
