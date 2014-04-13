<?xml version="1.0" encoding="utf-8"?>
<WuqispankExport>
  <Rq id="ce23db0a-fb2b-49fa">
    <Sql entryTimeMs="23770916" exitTimeMs="23770918"
    lousyDateTimeMs="1395747370968" seq="8">
      <StmtText>select acbscustom0_.J_MRUI as J1_0_, acbscustom0_.J_MRVT as J2_0_, acbscustom0_.J_MRWT as J3_0_, acbscustom0_.J_RAPI as J4_0_, acbscustom0_.J_MSZI as J5_0_, acbscustom0_.J_MSCI as J6_0_, acbscustom0_.J_CVAC as J7_0_, acbscustom0_.J_MSGL as J8_0_, '10' as formula0_, acbscustom0_.J_MSCI as formula1_ from BSDTADLS.J_CUMS acbscustom0_ where acbscustom0_.J_MRUI in ('00001356') order by acbscustom0_.J_MRVT||acbscustom0_.J_MRWT
      </StmtText>
      <StackTrace>
      [org.hsqldb.jdbc.jdbcConnection.prepareStatement(UnknownSource),
      org.apache.commons.dbcp.DelegatingConnection.prepareStatement(DelegatingConnection.java:281),
      org.apache.commons.dbcp.PoolingDataSource$PoolGuardConnectionWrapper.prepareStatement(PoolingDataSource.java:313),
      sun.reflect.GeneratedMethodAccessor3.invoke(Unknown Source),
      org.eclipse.jetty.util.thread.QueuedThreadPool$3.run(QueuedThreadPool.java:543)]</StackTrace>
    </Sql>
    <Sql entryTimeMs="23770916" exitTimeMs="23770918"
    lousyDateTimeMs="1395747370968" seq="9">
      <StmtText>
      select sum(this_.J_BGA_) as y0_, sum(this_.J_BGB_) as y1_, sum(this_.J_BGC_) as y2_, sum(this_.J_BGD_) as y3_, this_.J_BFBI as y4_, this_.J_BFMC as y5_ from BSDTADLS.J_CALM this_ inner join BSDTADLS.J_CAMS fac1_ on this_.J_BFAI=fac1_.J_BAAI and this_.J_BFCI=fac1_.J_BACI inner join BSDTADLS.J_CAMI limit3_ on fac1_.J_BAAI=limit3_.J_BAAI and fac1_.J_BACI=limit3_.J_BACI inner join BSDTADLS.J_CAPF parms2_ on fac1_.J_BAAI=parms2_.J_IELC where this_.J_BFBI in ('00001356') and limit3_.J_BCAC='A' and fac1_.J_CRAI='          ' and fac1_.J_CRCC=this_.J_CQJC and parms2_.J_REXI=this_.J_BFDI and this_.J_BFEI='00' and this_.J_BFFC='00' and this_.J_BFGI=this_.J_BFBI group by this_.J_BFBI, this_.J_BFMC
      </StmtText>
      <StackTrace>
      [org.hsqldb.jdbc.jdbcConnection.prepareStatement(UnknownSource),
      org.apache.commons.dbcp.DelegatingConnection.prepareStatement(DelegatingConnection.java:281),
      org.apache.commons.dbcp.PoolingDataSource$PoolGuardConnectionWrapper.prepareStatement(PoolingDataSource.java:313),
      sun.reflect.GeneratedMethodAccessor3.invoke(Unknown Source),
      org.eclipse.jetty.util.thread.QueuedThreadPool$3.run(QueuedThreadPool.java:543)]</StackTrace>
    </Sql>
    <Sql entryTimeMs="23770916" exitTimeMs="23770918"
    lousyDateTimeMs="1395747370968" seq="10">
      <StmtText>select acbsloan0_.J_MBSI as J1_15_, acbsloan0_.J_MBTI as J2_15_, acbsloan0_.J_MBWI as J3_15_, acbsloan0_.J_MBXI as J4_15_, acbsloan0_.J_CMKI as J5_15_, acbsloan0_.J_CMMI as J6_15_, acbsloan0_.J_CMLI as J7_15_, acbsloan0_.J_MBUI as J8_15_, acbsloan0_.J_MCH8 as J9_15_, acbsloan0_.J_MCI8 as J10_15_, acbsloan0_.J_MCJ8 as J11_15_, acbsloan0_.J_MCK8 as J12_15_, acbsloan0_.J_DHD8 as J13_15_, acbsloan0_.J_MCFC as J14_15_, acbsloan0_.J_AWAC as J15_15_, acbsloan0_.J_MEP8 as J16_15_, acbsloan0_.J_GJAI as J17_15_, acbsloan0_.J_CMPI as J18_15_, acbsloan0_.J_MBVL as J19_15_, acbsloan0_.J_MEE_ as J20_15_, acbsloan0_.J_MEF_ as J21_15_, acbsloan0_.J_MEG_ as J22_15_, acbsloan0_.J_CMS_ as J23_15_, acbsloan0_.J_CNFC as J24_15_, acbsloan0_.J_CNGC as J25_15_, acbsloan0_.J_DUFC as J26_15_, acbsloan0_.J_CMOC as J27_15_, acbsloan0_.J_MCQI as J28_15_, acbsloan0_.J_MCPI as J29_15_, acbsloan0_.J_MCRI as J30_15_, acbsloan0_.J_MCOI as J31_15_, acbsloan0_.J_MDLC as J32_15_, acbsloan0_.J_MBSI as formula10_ from BSDTADLS.J_CLMS acbsloan0_ where acbsloan0_.J_MBUI='00001356' and acbsloan0_.J_AWAC='1' and (acbsloan0_.J_MBVL='N' or acbsloan0_.J_MBVL='Y' and acbsloan0_.J_MBWI &lt;&gt;' ' and acbsloan0_.J_MBWI&lt;&gt;acbsloan0_.J_MBUI)
      </StmtText>
      <StackTrace>
      [org.hsqldb.jdbc.jdbcConnection.prepareStatement(UnknownSource),
      org.apache.commons.dbcp.DelegatingConnection.prepareStatement(DelegatingConnection.java:281),
      org.apache.commons.dbcp.PoolingDataSource$PoolGuardConnectionWrapper.prepareStatement(PoolingDataSource.java:313),
      sun.reflect.GeneratedMethodAccessor3.invoke(Unknown Source),
      org.eclipse.jetty.util.thread.QueuedThreadPool$3.run(QueuedThreadPool.java:543)]</StackTrace>
    </Sql>
    <Sql entryTimeMs="23770916" exitTimeMs="23770918"
    lousyDateTimeMs="1395747370968" seq="11">
      <StmtText>select acbsdateca0_.J_RDQI as J1_34_0_, acbsdateca0_.J_DWKC as J2_34_0_, acbsdateca0_.J_MWY8 as J3_34_0_, acbsdateca0_.J_MXB8 as J4_34_0_, acbsdateca0_.J_MXD8 as J5_34_0_ from BSDTADLS.J_SYDC acbsdateca0_ where acbsdateca0_.J_RDQI='ACA' and acbsdateca0_.J_DWKC='RS'
      </StmtText>
      <StackTrace>
      [org.hsqldb.jdbc.jdbcConnection.prepareStatement(UnknownSource),
      org.apache.commons.dbcp.DelegatingConnection.prepareStatement(DelegatingConnection.java:281),
      org.apache.commons.dbcp.PoolingDataSource$PoolGuardConnectionWrapper.prepareStatement(PoolingDataSource.java:313),
      sun.reflect.GeneratedMethodAccessor3.invoke(Unknown Source),
      org.eclipse.jetty.util.thread.QueuedThreadPool$3.run(QueuedThreadPool.java:543)]</StackTrace>
    </Sql>
    <Sql entryTimeMs="23770916" exitTimeMs="23770918"
    lousyDateTimeMs="1395747370968" seq="12">
      <StmtText>select acbsdateca0_.J_RDQI as J1_34_0_, acbsdateca0_.J_DWKC as J2_34_0_, acbsdateca0_.J_MWY8 as J3_34_0_, acbsdateca0_.J_MXB8 as J4_34_0_, acbsdateca0_.J_MXD8 as J5_34_0_ from BSDTADLS.J_SYDC acbsdateca0_ where acbsdateca0_.J_RDQI='ACL' and acbsdateca0_.J_DWKC='RS'
      </StmtText>
      <StackTrace>
      [org.hsqldb.jdbc.jdbcConnection.prepareStatement(UnknownSource),
      org.apache.commons.dbcp.DelegatingConnection.prepareStatement(DelegatingConnection.java:281),
      org.apache.commons.dbcp.PoolingDataSource$PoolGuardConnectionWrapper.prepareStatement(PoolingDataSource.java:313),
      sun.reflect.GeneratedMethodAccessor3.invoke(Unknown Source),
      org.eclipse.jetty.util.thread.QueuedThreadPool$3.run(QueuedThreadPool.java:543)]</StackTrace>
    </Sql>
    <Sql entryTimeMs="23770916" exitTimeMs="23770918"
    lousyDateTimeMs="1395747370968" seq="13">
      <StmtText>select acbsloanmi0_.J_MBSI as J1_16_0_, acbsloanmi0_.J_MBTI as J2_16_0_, acbsloanmi0_.J_DUEC as J3_16_0_, acbsloanmi0_.J_MCCC as J4_16_0_, acbsloanmi0_.J_MCBC as J5_16_0_, acbsloanmi0_.J_EGFI as J6_16_0_ from BSDTADLS.J_CLMI acbsloanmi0_ where acbsloanmi0_.J_MBSI='RS' and acbsloanmi0_.J_MBTI='000089551'
      </StmtText>
      <StackTrace>
      [org.hsqldb.jdbc.jdbcConnection.prepareStatement(UnknownSource),
      org.apache.commons.dbcp.DelegatingConnection.prepareStatement(DelegatingConnection.java:281),
      org.apache.commons.dbcp.PoolingDataSource$PoolGuardConnectionWrapper.prepareStatement(PoolingDataSource.java:313),
      sun.reflect.GeneratedMethodAccessor3.invoke(Unknown Source),
      org.eclipse.jetty.util.thread.QueuedThreadPool$3.run(QueuedThreadPool.java:543)]</StackTrace>
    </Sql>
    <Sql entryTimeMs="23770916" exitTimeMs="23770918"
    lousyDateTimeMs="1395747370968" seq="14">
      <StmtText>select acbsloanac0_.J_MEYI as J1_17_, acbsloanac0_.J_MEZI as J2_17_, acbsloanac0_.J_MFAI as J3_17_, acbsloanac0_.J_MFBI as J4_17_, acbsloanac0_.J_CJMC as J5_17_, acbsloanac0_.J_MFM8 as J6_17_, acbsloanac0_.J_MFEI as J7_17_, acbsloanac0_.J_RLJC as J8_17_, acbsloanac0_.J_MHK_ as J9_17_, acbsloanac0_.J_MHL_ as J10_17_, acbsloanac0_.J_MHM_ as J11_17_, acbsloanac0_.J_MHN_ as J12_17_, acbsloanac0_.DSCVVA as DSCVVA17_, acbsloanac0_.DSCZCA as DSCZCA17_, acbsloanac0_.DSDEIA as DSDEIA17_, acbsloanac0_.DSECKA as DSECKA17_, acbsloanac0_.J_CKU_ as J17_17_, acbsloanac0_.J_MFCP as J18_17_, acbsloanac0_.J_CNH_ as J19_17_, acbsloanac0_.J_CNI_ as J20_17_, acbsloanac0_.J_CKIR as J21_17_, acbsloanac0_.J_AWBC as J22_17_ from BSDTADLS.J_CLLA acbsloanac0_ where acbsloanac0_.J_MEYI='RS' and acbsloanac0_.J_MEZI='000089551' and acbsloanac0_.J_MFAI='00000000' and acbsloanac0_.J_CJMC='600' and acbsloanac0_.J_MFEI='00001356'
      </StmtText>
      <StackTrace>
      [org.hsqldb.jdbc.jdbcConnection.prepareStatement(UnknownSource),
      org.apache.commons.dbcp.DelegatingConnection.prepareStatement(DelegatingConnection.java:281),
      org.apache.commons.dbcp.PoolingDataSource$PoolGuardConnectionWrapper.prepareStatement(PoolingDataSource.java:313),
      sun.reflect.GeneratedMethodAccessor3.invoke(Unknown Source),
      org.eclipse.jetty.util.thread.QueuedThreadPool$3.run(QueuedThreadPool.java:543)]</StackTrace>
    </Sql>
    <Sql entryTimeMs="23770916" exitTimeMs="23770918"
    lousyDateTimeMs="1395747370968" seq="15">
      <StmtText>select acbsgenera0_.J_DQPC as J1_38_ from BSDTADLS.J_GNPF acbsgenera0_
      </StmtText>
      <StackTrace>
      [org.hsqldb.jdbc.jdbcConnection.prepareStatement(UnknownSource),
      org.apache.commons.dbcp.DelegatingConnection.prepareStatement(DelegatingConnection.java:281),
      org.apache.commons.dbcp.PoolingDataSource$PoolGuardConnectionWrapper.prepareStatement(PoolingDataSource.java:313),
      sun.reflect.GeneratedMethodAccessor3.invoke(Unknown Source),
      org.eclipse.jetty.util.thread.QueuedThreadPool$3.run(QueuedThreadPool.java:543)]</StackTrace>
    </Sql>
    <Sql entryTimeMs="23770916" exitTimeMs="23770918"
    lousyDateTimeMs="1395747370968" seq="16">
      <StmtText>
      select acbsloanes0_.DSCUPI as DSCUPI19_0_, acbsloanes0_.DSCUQI as DSCUQI19_0_, acbsloanes0_.DSCVMA as DSCVMA19_0_ from BSDTADLS.DSCLLE acbsloanes0_ where acbsloanes0_.DSCUPI='RS' and acbsloanes0_.DSCUQI='000089551'
      </StmtText>
      <StackTrace>
      [org.hsqldb.jdbc.jdbcConnection.prepareStatement(UnknownSource),
      org.apache.commons.dbcp.DelegatingConnection.prepareStatement(DelegatingConnection.java:281),
      org.apache.commons.dbcp.PoolingDataSource$PoolGuardConnectionWrapper.prepareStatement(PoolingDataSource.java:313),
      sun.reflect.GeneratedMethodAccessor3.invoke(Unknown Source),
      org.eclipse.jetty.util.thread.QueuedThreadPool$3.run(QueuedThreadPool.java:543)]</StackTrace>
    </Sql>
    <Sql entryTimeMs="23770916" exitTimeMs="23770918"
    lousyDateTimeMs="1395747370968" seq="17">
      <StmtText>select acbsadebus0_.DSDWNC as DSDWNC53_, acbsadebus0_.DSDWOC as DSDWOC53_, acbsadebus0_.DSDWPI as DSDWPI53_, acbsadebus0_.DSDWQT as DSDWQT53_, acbsadebus0_.DSDWRC as DSDWRC53_, acbsadebus0_.DSDWST as DSDWST53_ from BSDTADLS.DSGNEB acbsadebus0_ where acbsadebus0_.DSDWQT='LN_CONTRACT_CATEGORY' and acbsadebus0_.DSDWNC='TABLE_COMMON_MASTER' and acbsadebus0_.DSDWPI='T0335110       CL'
      </StmtText>
      <StackTrace>
      [org.hsqldb.jdbc.jdbcConnection.prepareStatement(UnknownSource),
      org.apache.commons.dbcp.DelegatingConnection.prepareStatement(DelegatingConnection.java:281),
      org.apache.commons.dbcp.PoolingDataSource$PoolGuardConnectionWrapper.prepareStatement(PoolingDataSource.java:313),
      sun.reflect.GeneratedMethodAccessor3.invoke(Unknown Source),
      org.eclipse.jetty.util.thread.QueuedThreadPool$3.run(QueuedThreadPool.java:543)]</StackTrace>
    </Sql>
    <Sql entryTimeMs="23770916" exitTimeMs="23770918"
    lousyDateTimeMs="1395747370968" seq="18">
      <StmtText>select acbscustom0_.J_MRUI as J1_0_, acbscustom0_.J_MRVT as J2_0_, acbscustom0_.J_MRWT as J3_0_, acbscustom0_.J_RAPI as J4_0_, acbscustom0_.J_MSZI as J5_0_, acbscustom0_.J_MSCI as J6_0_, acbscustom0_.J_CVAC as J7_0_, acbscustom0_.J_MSGL as J8_0_, '10' as formula0_, acbscustom0_.J_MSCI as formula1_ from BSDTADLS.J_CUMS acbscustom0_ where acbscustom0_.J_MRUI in ('00000104') order by acbscustom0_.J_MRVT||acbscustom0_.J_MRWT
      </StmtText>
      <StackTrace>
      [org.hsqldb.jdbc.jdbcConnection.prepareStatement(UnknownSource),
      org.apache.commons.dbcp.DelegatingConnection.prepareStatement(DelegatingConnection.java:281),
      org.apache.commons.dbcp.PoolingDataSource$PoolGuardConnectionWrapper.prepareStatement(PoolingDataSource.java:313),
      sun.reflect.GeneratedMethodAccessor3.invoke(Unknown Source),
      org.eclipse.jetty.util.thread.QueuedThreadPool$3.run(QueuedThreadPool.java:543)]</StackTrace>
    </Sql>
    <Sql entryTimeMs="23770916" exitTimeMs="23770918"
    lousyDateTimeMs="1395747370968" seq="19">
      <StmtText>select sum(this_.J_BGA_) as y0_, sum(this_.J_BGB_) as y1_, sum(this_.J_BGC_) as y2_, sum(this_.J_BGD_) as y3_, this_.J_BFBI as y4_, this_.J_BFMC as y5_ from BSDTADLS.J_CALM this_ inner join BSDTADLS.J_CAMS fac1_ on this_.J_BFAI=fac1_.J_BAAI and this_.J_BFCI=fac1_.J_BACI inner join BSDTADLS.J_CAMI limit3_ on fac1_.J_BAAI=limit3_.J_BAAI and fac1_.J_BACI=limit3_.J_BACI inner join BSDTADLS.J_CAPF parms2_ on fac1_.J_BAAI=parms2_.J_IELC where this_.J_BFBI in ('00000104') and limit3_.J_BCAC='A' and fac1_.J_CRAI='          ' and fac1_.J_CRCC=this_.J_CQJC and parms2_.J_REXI=this_.J_BFDI and this_.J_BFEI='00' and this_.J_BFFC='00' and this_.J_BFGI=this_.J_BFBI group by this_.J_BFBI, this_.J_BFMC
      </StmtText>
      <StackTrace>
      [org.hsqldb.jdbc.jdbcConnection.prepareStatement(UnknownSource),
      org.apache.commons.dbcp.DelegatingConnection.prepareStatement(DelegatingConnection.java:281),
      org.apache.commons.dbcp.PoolingDataSource$PoolGuardConnectionWrapper.prepareStatement(PoolingDataSource.java:313),
      sun.reflect.GeneratedMethodAccessor3.invoke(Unknown Source),
      org.eclipse.jetty.util.thread.QueuedThreadPool$3.run(QueuedThreadPool.java:543)]</StackTrace>
    </Sql>
    <Sql entryTimeMs="23770916" exitTimeMs="23770918"
    lousyDateTimeMs="1395747370968" seq="20">
      <StmtText>select acbsloan0_.J_MBSI as J1_15_, acbsloan0_.J_MBTI as J2_15_, acbsloan0_.J_MBWI as J3_15_, acbsloan0_.J_MBXI as J4_15_, acbsloan0_.J_CMKI as J5_15_, acbsloan0_.J_CMMI as J6_15_, acbsloan0_.J_CMLI as J7_15_, acbsloan0_.J_MBUI as J8_15_, acbsloan0_.J_MCH8 as J9_15_, acbsloan0_.J_MCI8 as J10_15_, acbsloan0_.J_MCJ8 as J11_15_, acbsloan0_.J_MCK8 as J12_15_, acbsloan0_.J_DHD8 as J13_15_, acbsloan0_.J_MCFC as J14_15_, acbsloan0_.J_AWAC as J15_15_, acbsloan0_.J_MEP8 as J16_15_, acbsloan0_.J_GJAI as J17_15_, acbsloan0_.J_CMPI as J18_15_, acbsloan0_.J_MBVL as J19_15_, acbsloan0_.J_MEE_ as J20_15_, acbsloan0_.J_MEF_ as J21_15_, acbsloan0_.J_MEG_ as J22_15_, acbsloan0_.J_CMS_ as J23_15_, acbsloan0_.J_CNFC as J24_15_, acbsloan0_.J_CNGC as J25_15_, acbsloan0_.J_DUFC as J26_15_, acbsloan0_.J_CMOC as J27_15_, acbsloan0_.J_MCQI as J28_15_, acbsloan0_.J_MCPI as J29_15_, acbsloan0_.J_MCRI as J30_15_, acbsloan0_.J_MCOI as J31_15_, acbsloan0_.J_MDLC as J32_15_, acbsloan0_.J_MBSI as formula10_ from BSDTADLS.J_CLMS acbsloan0_ where acbsloan0_.J_MBUI='00000104' and acbsloan0_.J_AWAC='1' and (acbsloan0_.J_MBVL='N' or acbsloan0_.J_MBVL='Y' and acbsloan0_.J_MBWI &lt;&gt; ' ' and acbsloan0_.J_MBWI &lt;&gt; acbsloan0_.J_MBUI)
      </StmtText>
      <StackTrace>
      [org.hsqldb.jdbc.jdbcConnection.prepareStatement(UnknownSource),
      org.apache.commons.dbcp.DelegatingConnection.prepareStatement(DelegatingConnection.java:281),
      org.apache.commons.dbcp.PoolingDataSource$PoolGuardConnectionWrapper.prepareStatement(PoolingDataSource.java:313),
      sun.reflect.GeneratedMethodAccessor3.invoke(Unknown Source),
      org.eclipse.jetty.util.thread.QueuedThreadPool$3.run(QueuedThreadPool.java:543)]</StackTrace>
    </Sql>
    <Sql entryTimeMs="23770916" exitTimeMs="23770918"
    lousyDateTimeMs="1395747370968" seq="21">
      <StmtText>
      select acbsdateca0_.J_RDQI as J1_34_0_, acbsdateca0_.J_DWKC as J2_34_0_, acbsdateca0_.J_MWY8 as J3_34_0_, acbsdateca0_.J_MXB8 as J4_34_0_, acbsdateca0_.J_MXD8 as J5_34_0_ from BSDTADLS.J_SYDC acbsdateca0_ where acbsdateca0_.J_RDQI='ACA' and acbsdateca0_.J_DWKC='AT'
      </StmtText>
      <StackTrace>[org.hsqldb.jdbc.jdbcConnection.prepareStatement(UnknownSource),
      org.apache.commons.dbcp.DelegatingConnection.prepareStatement(DelegatingConnection.java:281),
      org.apache.commons.dbcp.PoolingDataSource$PoolGuardConnectionWrapper.prepareStatement(PoolingDataSource.java:313),
      sun.reflect.GeneratedMethodAccessor3.invoke(Unknown Source),
      org.eclipse.jetty.util.thread.QueuedThreadPool$3.run(QueuedThreadPool.java:543)]</StackTrace>
    </Sql>
    <Sql entryTimeMs="23770916" exitTimeMs="23770918"
    lousyDateTimeMs="1395747370968" seq="22">
      <StmtText>select acbsdateca0_.J_RDQI as J1_34_0_, acbsdateca0_.J_DWKC as J2_34_0_, acbsdateca0_.J_MWY8 as J3_34_0_, acbsdateca0_.J_MXB8 as J4_34_0_, acbsdateca0_.J_MXD8 as J5_34_0_ from BSDTADLS.J_SYDC acbsdateca0_ where acbsdateca0_.J_RDQI='ACL' and acbsdateca0_.J_DWKC='AT'
      </StmtText>
      <StackTrace>
      [org.hsqldb.jdbc.jdbcConnection.prepareStatement(UnknownSource),
      org.apache.commons.dbcp.DelegatingConnection.prepareStatement(DelegatingConnection.java:281),
      org.apache.commons.dbcp.PoolingDataSource$PoolGuardConnectionWrapper.prepareStatement(PoolingDataSource.java:313),
      sun.reflect.GeneratedMethodAccessor3.invoke(Unknown Source),
      org.eclipse.jetty.util.thread.QueuedThreadPool$3.run(QueuedThreadPool.java:543)]</StackTrace>
    </Sql>
    <Sql entryTimeMs="23770916" exitTimeMs="23770918"
    lousyDateTimeMs="1395747370968" seq="25">
      <StmtText>select acbsloanmi0_.J_MBSI as J1_16_0_, acbsloanmi0_.J_MBTI as J2_16_0_, acbsloanmi0_.J_DUEC as J3_16_0_, acbsloanmi0_.J_MCCC as J4_16_0_, acbsloanmi0_.J_MCBC as J5_16_0_, acbsloanmi0_.J_EGFI as J6_16_0_ from BSDTADLS.J_CLMI acbsloanmi0_ where acbsloanmi0_.J_MBSI='A1' and acbsloanmi0_.J_MBTI='000000177'
      </StmtText>
      <StackTrace>
      [org.hsqldb.jdbc.jdbcConnection.prepareStatement(UnknownSource),
      org.apache.commons.dbcp.DelegatingConnection.prepareStatement(DelegatingConnection.java:281),
      org.apache.commons.dbcp.PoolingDataSource$PoolGuardConnectionWrapper.prepareStatement(PoolingDataSource.java:313),
      sun.reflect.GeneratedMethodAccessor3.invoke(Unknown Source),
      org.eclipse.jetty.util.thread.QueuedThreadPool$3.run(QueuedThreadPool.java:543)]</StackTrace>
    </Sql>
    <Sql entryTimeMs="23770916" exitTimeMs="23770918"
    lousyDateTimeMs="1395747370968" seq="26">
      <StmtText>select acbsloanmi0_.J_MBSI as J1_16_0_, acbsloanmi0_.J_MBTI as J2_16_0_, acbsloanmi0_.J_DUEC as J3_16_0_, acbsloanmi0_.J_MCCC as J4_16_0_, acbsloanmi0_.J_MCBC as J5_16_0_, acbsloanmi0_.J_EGFI as J6_16_0_ from BSDTADLS.J_CLMI acbsloanmi0_ where acbsloanmi0_.J_MBSI='A1' and acbsloanmi0_.J_MBTI='000000186'
      </StmtText>
      <StackTrace>
      [org.hsqldb.jdbc.jdbcConnection.prepareStatement(UnknownSource),
      org.apache.commons.dbcp.DelegatingConnection.prepareStatement(DelegatingConnection.java:281),
      org.apache.commons.dbcp.PoolingDataSource$PoolGuardConnectionWrapper.prepareStatement(PoolingDataSource.java:313),
      sun.reflect.GeneratedMethodAccessor3.invoke(Unknown Source),
      org.eclipse.jetty.util.thread.QueuedThreadPool$3.run(QueuedThreadPool.java:543)]</StackTrace>
    </Sql>
    <Sql entryTimeMs="23770916" exitTimeMs="23770918"
    lousyDateTimeMs="1395747370968" seq="27">
      <StmtText>select acbsloanmi0_.J_MBSI as J1_16_0_, acbsloanmi0_.J_MBTI as J2_16_0_, acbsloanmi0_.J_DUEC as J3_16_0_, acbsloanmi0_.J_MCCC as J4_16_0_, acbsloanmi0_.J_MCBC as J5_16_0_, acbsloanmi0_.J_EGFI as J6_16_0_ from BSDTADLS.J_CLMI acbsloanmi0_ where acbsloanmi0_.J_MBSI='A1' and acbsloanmi0_.J_MBTI='REVSTANDL'
</StmtText>
      <StackTrace>
      [org.hsqldb.jdbc.jdbcConnection.prepareStatement(UnknownSource),
      org.apache.commons.dbcp.DelegatingConnection.prepareStatement(DelegatingConnection.java:281),
      org.apache.commons.dbcp.PoolingDataSource$PoolGuardConnectionWrapper.prepareStatement(PoolingDataSource.java:313),
      sun.reflect.GeneratedMethodAccessor3.invoke(Unknown Source),
      org.eclipse.jetty.util.thread.QueuedThreadPool$3.run(QueuedThreadPool.java:543)]</StackTrace>
    </Sql>
    <Sql entryTimeMs="23770916" exitTimeMs="23770918"
    lousyDateTimeMs="1395747370968" seq="28">
      <StmtText>select acbsloanac0_.J_MEYI as J1_17_, acbsloanac0_.J_MEZI as J2_17_, acbsloanac0_.J_MFAI as J3_17_, acbsloanac0_.J_MFBI as J4_17_, acbsloanac0_.J_CJMC as J5_17_, acbsloanac0_.J_MFM8 as J6_17_, acbsloanac0_.J_MFEI as J7_17_, acbsloanac0_.J_RLJC as J8_17_, acbsloanac0_.J_MHK_ as J9_17_, acbsloanac0_.J_MHL_ as J10_17_, acbsloanac0_.J_MHM_ as J11_17_, acbsloanac0_.J_MHN_ as J12_17_, acbsloanac0_.DSCVVA as DSCVVA17_, acbsloanac0_.DSCZCA as DSCZCA17_, acbsloanac0_.DSDEIA as DSDEIA17_, acbsloanac0_.DSECKA as DSECKA17_, acbsloanac0_.J_CKU_ as J17_17_, acbsloanac0_.J_MFCP as J18_17_, acbsloanac0_.J_CNH_ as J19_17_, acbsloanac0_.J_CNI_ as J20_17_, acbsloanac0_.J_CKIR as J21_17_, acbsloanac0_.J_AWBC as J22_17_ from BSDTADLS.J_CLLA acbsloanac0_ where acbsloanac0_.J_MEYI='A1' and acbsloanac0_.J_MEZI='000000177' and acbsloanac0_.J_MFAI='ATAGENT' and acbsloanac0_.J_CJMC='600' and acbsloanac0_.J_MFEI='00000104'
      </StmtText>
      <StackTrace>
      [org.hsqldb.jdbc.jdbcConnection.prepareStatement(UnknownSource),
      org.apache.commons.dbcp.DelegatingConnection.prepareStatement(DelegatingConnection.java:281),
      org.apache.commons.dbcp.PoolingDataSource$PoolGuardConnectionWrapper.prepareStatement(PoolingDataSource.java:313),
      sun.reflect.GeneratedMethodAccessor3.invoke(Unknown Source),
      org.eclipse.jetty.util.thread.QueuedThreadPool$3.run(QueuedThreadPool.java:543)]</StackTrace>
    </Sql>
    <Sql entryTimeMs="23770916" exitTimeMs="23770918"
    lousyDateTimeMs="1395747370968" seq="29">
      <StmtText>select acbsgenera0_.J_DQPC as J1_38_ from BSDTADLS.J_GNPF acbsgenera0_
      </StmtText>
      <StackTrace>
      [org.hsqldb.jdbc.jdbcConnection.prepareStatement(UnknownSource),
      org.apache.commons.dbcp.DelegatingConnection.prepareStatement(DelegatingConnection.java:281),
      org.apache.commons.dbcp.PoolingDataSource$PoolGuardConnectionWrapper.prepareStatement(PoolingDataSource.java:313),
      sun.reflect.GeneratedMethodAccessor3.invoke(Unknown Source),
      org.eclipse.jetty.util.thread.QueuedThreadPool$3.run(QueuedThreadPool.java:543)]</StackTrace>
    </Sql>
    <Sql entryTimeMs="23770916" exitTimeMs="23770918"
    lousyDateTimeMs="1395747370968" seq="30">
      <StmtText>select acbsloanes0_.DSCUPI as DSCUPI19_0_, acbsloanes0_.DSCUQI as DSCUQI19_0_, acbsloanes0_.DSCVMA as DSCVMA19_0_ from BSDTADLS.DSCLLE acbsloanes0_ where acbsloanes0_.DSCUPI='A1' and acbsloanes0_.DSCUQI='000000177'
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









