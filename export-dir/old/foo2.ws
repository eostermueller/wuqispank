<?xml version="1.0" encoding="utf-8"?>
<WuqispankExport>
  <Rq id="ce23db0a-fb2b-49fa-a2b3-62ff831eedb0">
    <Sql entryTimeMs="23770916" exitTimeMs="23770918"
    lousyDateTimeMs="1395747370968" seq="0">
      <StmtText>INSERT INTO Location (name, address) VALUES(?,?)</StmtText>
      <StackTrace>
      [org.hsqldb.jdbc.jdbcConnection.prepareStatement(UnknownSource),
      org.apache.commons.dbcp.DelegatingConnection.prepareStatement(DelegatingConnection.java:281),
      org.apache.commons.dbcp.PoolingDataSource$PoolGuardConnectionWrapper.prepareStatement(PoolingDataSource.java:313),
      sun.reflect.GeneratedMethodAccessor3.invoke(Unknown Source),
      org.eclipse.jetty.util.thread.QueuedThreadPool$3.run(QueuedThreadPool.java:543)]</StackTrace>
    </Sql>
    <Sql entryTimeMs="23770922" exitTimeMs="23770925"
    lousyDateTimeMs="1395747371009" seq="1">
      <StmtText>INSERT INTO Event (name, description, date,
      location) VALUES(?, ?, ?, ?)</StmtText>
      <StackTrace>
      [org.hsqldb.jdbc.jdbcConnection.prepareStatement(UnknownSource),
      org.apache.commons.dbcp.DelegatingConnection.prepareStatement(DelegatingConnection.java:281),
      org.apache.commons.dbcp.PoolingDataSource$PoolGuardConnectionWrapper.prepareStatement(PoolingDataSource.java:313),
      sun.reflect.GeneratedMethodAccessor3.invoke(Unknown Source),
      sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43),
      java.lang.reflect.Method.invoke(Method.java:606),
      org.eclipse.jetty.util.thread.QueuedThreadPool$3.run(QueuedThreadPool.java:543)]</StackTrace>
    </Sql>
  </Rq>
</WuqispankExport>
