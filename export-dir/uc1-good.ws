<?xml version="1.0" encoding="utf-8"?>
<WuqispankExport>
  <Rq id="uc1-good">
    <Sql entryTimeMs="23770916" exitTimeMs="23770918"
    lousyDateTimeMs="1395747370968" seq="10">
      <StmtText>
         SELECT * FROM CUSTOMER C, ACCOUNT A WHERE C.CUST_ID = A.CUST_ID AND C.CUST_ID IN (SELECT CUST_REL_ID FROM CUST_CUST_REL 
	 WHERE CUST_ID = 'SPANK-001')
      </StmtText>
      <StackTrace>
      [org.hsqldb.jdbc.jdbcConnection.prepareStatement(UnknownSource),
      org.apache.commons.dbcp.DelegatingConnection.prepareStatement(DelegatingConnection.java:281),
      org.apache.commons.dbcp.PoolingDataSource$PoolGuardConnectionWrapper.prepareStatement(PoolingDataSource.java:313),
      sun.reflect.GeneratedMethodAccessor3.invoke(Unknown Source),
      org.eclipse.jetty.util.thread.QueuedThreadPool$3.run(QueuedThreadPool.java:543)]</StackTrace>
    </Sql>


  </Rq>
</WuqispankExport>









