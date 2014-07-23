package org.wuqispank;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

import javax.xml.transform.TransformerException;

import org.wuqispank.model.IRequestWrapper;
import org.wuqispank.web.IConfig;

public interface IRequestExporter {
	static final String SQL_DETAIL_RS_ROOT_TAG_NAME = "WsSqlDetailRs";
	static final String EXPORT_ROOT_TAG_NAME = "WuqispankExport";
	static final String EXPORT_SINGLE_REQUEST_TAG_NAME = "Rq";
	static final String EXPORT_SQL_TAG_NAME = "Sql";
	static final String EXPORT_SQL_STMT_TAG_NAME = "StmtText";
	static final String EXPORT_STACK_TRACE_TAG_NAME = "StackTrace";
	static final String EXPORT_ATTRIBUTE_ID_TAG_NAME = "id";
	static final String EXPORT_ATTRIBUTE_THREAD_ID_TAG_NAME = "threadId";
	static final String ATTRIBUTE_NAME_SEQUENCE = "seq";
	static final String ATTRIBUTE_NAME_ENTRY_TIME = "entryTimeMs";
	static final String ATTRIBUTE_NAME_EXIT_TIME = "exitTimeMs";
	static final String ATTRIBUTE_NAME_CLIENT_DATE_TIME = "lousyDateTimeMs";

	static final String FILE_NAME_EXTENSION = ".ws";
	
	void export(IRequestWrapper val) throws TransformerException;
	OutputStream getOutputStream();
	void setOutputStream(OutputStream val);
	void export(IRequestWrapper rw, int sqlSequence) throws TransformerException;

}
