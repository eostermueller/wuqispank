package org.wuqispank.db;

import org.wuqispank.WuqispankException;

public class SqlParseException extends WuqispankException {
	String m_sqlText = null;
	public void setSql(String sqlText) {
		m_sqlText = sqlText;
	}

	public SqlParseException() {
		// TODO Auto-generated constructor stub
	}

	public SqlParseException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public SqlParseException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public SqlParseException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

//	public SqlParseException(String message, Throwable cause,
//			boolean enableSuppression, boolean writableStackTrace) {
//		super(message, cause, enableSuppression, writableStackTrace);
//		// TODO Auto-generated constructor stub
//	}

}
