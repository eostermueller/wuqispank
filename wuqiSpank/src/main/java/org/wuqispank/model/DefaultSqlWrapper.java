package org.wuqispank.model;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wuqispank.DefaultFactory;
import org.wuqispank.db.ISqlParser;
import org.wuqispank.db.SqlParseException;

public class DefaultSqlWrapper implements ISqlWrapper, java.io.Serializable {
	private static final Logger log = LoggerFactory.getLogger(DefaultSqlWrapper.class);
	private String uniqueId = null;

	private static final long UNINIT = -1;
	private static final String HASH_ALGO = "SHA1";
	private static final String HASH_ENCODING = "UTF-8";
	
//	private ITraceEvent m_sqlEvent = null;
//	private ITraceEvent m_entryEvent = null;
//	private ITraceEvent m_exitEvent = null;
	private ISqlModel m_sqlModel = null;
	private int m_sequence =  -1;
	private IStackTrace m_stackTrace = null;
	
	private long m_agentEntryTimeMillis = UNINIT;
	private long m_agentExitTimeMillis = UNINIT;
	private long m_lousyDateTimeMillis = UNINIT;

	private String m_sqlText;
	private boolean retryParseWithoutSelectList;

	public DefaultSqlWrapper() {
		setSqlModel( DefaultFactory.getFactory().getSqlModel() );
		
	}
	@Override
	public int getSequence() {
		return m_sequence;
	}

	@Override
	public void setSequence(int sequence) {
		this.m_sequence = sequence;
	}
	public String getFromClauseOnward() {
		String rcSql = null;
		int indexOfFromClause = this.getSqlText().toUpperCase().indexOf("FROM");
		if (indexOfFromClause > 0) {
			rcSql = this.getSqlText().substring(indexOfFromClause);
			if (rcSql.toUpperCase().indexOf("FROM") >  0)
				//We found a 2nd FROM clause, perhaps a sub-select, so 
				//this sql statement is much more complicated than this simple algrithm...punt.
				rcSql = null;
		}
			
		return rcSql;
	}


//	public void setSqlEvent(ITraceEvent sqlEvent) throws WuqispankException {
//		if (sqlEvent==null) {
//			throw new WuqispankException("Received a null sql event");
//		} 
//		
//		this.m_sqlEvent = sqlEvent;
//		if (sqlEvent.getEventType()!=ITraceEvent.EventType.ARG) {
//			throw new WuqispankException("Current implementation requires the SQL to be in the ARG trace event.  Instead, found [" + sqlEvent.toString() + "]");
//		} else {
//			setSqlText(sqlEvent.getValue());
//		}
//		
//	}
	@Override
	public void setSqlText(String val) {
		m_sqlText = val.replace("\n"," ").replace("\r"," ").trim();

		ISqlParser parser = DefaultFactory.getFactory().getSqlParser();
		try {
			parser.setSqlModel(getSqlModel());
			parser.parse(m_sqlText);
			this.setUniqueId( getSqlModel().getKey()+"."+this.getShortHash() );
		} catch (SqlParseException e) {
			this.setSqlModel(DefaultFactory.getFactory().getSqlModel());//start over from scratch.
			log.error("Exception parsing sql text.\nFrom Parser:[" + parser.getClass().getName() + "] \nOriginal [" + this.m_sqlText + "]");
			log.error(e.getLocalizedMessage());
			if (e.getCause()!=null)
				e.getCause().printStackTrace();
			else
				e.printStackTrace();
			
			String simplifiedSql = "SELECT UNKNOWN_COLUMNS " + this.getFromClauseOnward();
			try {
				parser.parse(simplifiedSql);
				this.setUniqueId( getSqlModel().getKey()+"."+this.getShortHash() );
			} catch(SqlParseException f) {
				log.error("Exception parsing sql text.\nFrom Parser:[" + parser.getClass().getName() + "] sql[" + f.getSql() + "] \nOriginal [" + this.m_sqlText + "]");
				log.error(f.getLocalizedMessage());
				if (f.getCause()!=null)
					f.getCause().printStackTrace();
				else
					e.printStackTrace();
				try {
					this.setSqlModel(DefaultFactory.getFactory().getSqlModel());
					parser = DefaultFactory.getFactory().getSecondarySqlParser();
					parser.setSqlModel(getSqlModel());
					parser.parse(simplifiedSql);
					this.setUniqueId( getSqlModel().getKey()+"."+this.getShortHash() );
				} catch(SqlParseException secondarySqlParseException) {
					log.error("Exception parsing sql text.\nFrom Parser:[" + parser.getClass().getName() + "] sql[" + secondarySqlParseException.getSql() + "] \nOriginal [" + this.m_sqlText + "]");
					log.error(f.getLocalizedMessage());
					if (f.getCause()!=null)
						f.getCause().printStackTrace();
					else
						e.printStackTrace();
				} catch (NoSuchAlgorithmException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} catch (NoSuchAlgorithmException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String getSqlText() {
		return m_sqlText;
	}

	@Override
	public ISqlModel getSqlModel() {
		return m_sqlModel;
	}

	@Override
	public void setSqlModel(ISqlModel val) {
		m_sqlModel = val;
	}
	public IStackTrace getStackTrace() {
		return m_stackTrace;
	}

	@Override
	public long getAgentEntryTimeMillis() {
		return m_agentEntryTimeMillis;
	}

	@Override
	public void setAgentEntryTimeMillis(long val) {
		m_agentEntryTimeMillis = val;
	}

	@Override
	public long getAgentExitTimeMillis() {
		return m_agentExitTimeMillis;
	}

	@Override
	public void setAgentExitTimeMillis(long val) {
		m_agentExitTimeMillis = val;
	}

	@Override
	public void setStackTrace(IStackTrace val) {
		m_stackTrace = val;
	}

	@Override
	public void setLousyDateTimeMillis(long val) {
		m_lousyDateTimeMillis = val;
		
	}

	@Override
	public long getLousyDateTimeMillis() {
		return m_lousyDateTimeMillis;
	}
	@Override
	public void setUniqueId(String val) {
		this.uniqueId = val;
		
	}

	@Override
	public String getUnique() {
		return this.uniqueId;
	}

	@Override
	public void setRetryParseWithoutSelectList(boolean b) {
		this.retryParseWithoutSelectList = b;
	}
	@Override
	public boolean getRetryParseWithoutSelectList() {
		return this.retryParseWithoutSelectList;
	}
	private String hash(String text, String algorithm, String enc)
	        throws NoSuchAlgorithmException, UnsupportedEncodingException {
	    byte[] hash = MessageDigest.getInstance(algorithm).digest(text.getBytes(enc));
	    BigInteger bi = new BigInteger(1, hash);
	    String result = bi.toString(16);
	    if (result.length() % 2 != 0) {
	        return "0" + result;
	    }
	    return result;
	}
	/**
	 * Since this will be used as a key for this SQL statement in the time series database (InfluxDB),
	 * this needs to be a small, efficient size.  How about 4bytes?
	 * @return a four digit hash of the text in the sql statement.
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public String getShortHash() throws NoSuchAlgorithmException, UnsupportedEncodingException {
		String rc = hash(this.getSqlText(),HASH_ALGO,HASH_ENCODING);
		
		return new String(""+rc.charAt(22)+rc.charAt(6)+rc.charAt(15)+rc.charAt(37));
	}
	

}
