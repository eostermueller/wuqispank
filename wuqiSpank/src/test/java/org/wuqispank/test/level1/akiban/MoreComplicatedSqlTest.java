package org.wuqispank.test.level1.akiban;

import static org.junit.Assert.*;

import org.junit.Test;
import org.wuqispank.DefaultFactory;
import org.wuqispank.db.ISqlParser;
import org.wuqispank.db.SqlParseException;
import org.wuqispank.db.akiban.AkibanSqlParser;
import org.wuqispank.model.ISqlModel;
import org.wuqispank.model.ISqlWrapper;
import org.wuqispank.model.ITable;

public class MoreComplicatedSqlTest {

	@Test
	public void canParseSqlEndingInNewLineChar() throws SqlParseException {
		String sql = "select acbsloanmi0_.J_MBSI as J1_16_0_, acbsloanmi0_.J_MBTI as J2_16_0_, acbsloanmi0_.J_DUEC as J3_16_0_, acbsloanmi0_.J_MCCC as J4_16_0_, acbsloanmi0_.J_MCBC as J5_16_0_, acbsloanmi0_.J_EGFI as J6_16_0_ from BSDTADLS.J_CLMI acbsloanmi0_ where acbsloanmi0_.J_MBSI='A1' and acbsloanmi0_.J_MBTI='000000177'\n";
		ISqlParser parser = new AkibanSqlParser();
		ISqlModel model = org.wuqispank.DefaultFactory.getFactory().getSqlModel();
		parser.setSqlModel(model);
		parser.parse(sql);
		assertEquals("Count of SQL tables incorrect when parsing the simplest SELECT SQL statement ever", 1, model.getTableCount() );

		//BSDTADLS.J_CLMI acbsloanmi0_
		ITable shouldBe_J_CLMI = model.getTable(0);
		assertEquals("SQL table name incorrectly parsed for simplest SELECT SQL statement ever", "J_CLMI".toLowerCase(), shouldBe_J_CLMI.getName() );
	}
	@Test
	public void canParseUnkProb() throws SqlParseException  {
		String sql = "select acbsloan0_.J_MBSI as J1_15_, acbsloan0_.J_MBTI as J2_15_, acbsloan0_.J_MBWI as J3_15_, acbsloan0_.J_MBXI as J4_15_, acbsloan0_.J_CMKI as J5_15_, acbsloan0_.J_CMMI as J6_15_, acbsloan0_.J_CMLI as J7_15_, acbsloan0_.J_MBUI as J8_15_, acbsloan0_.J_MCH8 as J9_15_, acbsloan0_.J_MCI8 as J10_15_, acbsloan0_.J_MCJ8 as J11_15_, acbsloan0_.J_MCK8 as J12_15_, acbsloan0_.J_DHD8 as J13_15_, acbsloan0_.J_MCFC as J14_15_, acbsloan0_.J_AWAC as J15_15_, acbsloan0_.J_MEP8 as J16_15_, acbsloan0_.J_GJAI as J17_15_, acbsloan0_.J_CMPI as J18_15_, acbsloan0_.J_MBVL as J19_15_, acbsloan0_.J_MEE_ as J20_15_, acbsloan0_.J_MEF_ as J21_15_, acbsloan0_.J_MEG_ as J22_15_, acbsloan0_.J_CMS_ as J23_15_, acbsloan0_.J_CNFC as J24_15_, acbsloan0_.J_CNGC as J25_15_, acbsloan0_.J_DUFC as J26_15_, acbsloan0_.J_CMOC as J27_15_, acbsloan0_.J_MCQI as J28_15_, acbsloan0_.J_MCPI as J29_15_, acbsloan0_.J_MCRI as J30_15_, acbsloan0_.J_MCOI as J31_15_, acbsloan0_.J_MDLC as J32_15_, acbsloan0_.J_MBSI as formula10_ from BSDTADLS.J_CLMS acbsloan0_ where acbsloan0_.J_MBUI='00000104' and acbsloan0_.J_AWAC='1' and (acbsloan0_.J_MBVL='N' or acbsloan0_.J_MBVL='Y' and acbsloan0_.J_MBWI<>' ' and acbsloan0_.J_MBWI<>acbsloan0_.J_MBUI)";
		ISqlParser parser = new AkibanSqlParser();
		ISqlModel model = org.wuqispank.DefaultFactory.getFactory().getSqlModel();
		parser.setSqlModel(model);
		parser.parse(sql);
		assertEquals("Count of SQL tables incorrect when parsing the simplest SELECT SQL statement ever", 1, model.getTableCount() );

		ITable shouldBe_J_CLMS = model.getTable(0);
		assertEquals("SQL table name incorrectly parsed for simplest SELECT SQL statement ever", "J_CLMS".toLowerCase(), shouldBe_J_CLMS.getName() );
	}
//	@Test
//	public void canSkipUnparsableSelectList() throws SqlParseException  {
//		String sql = "select UNKNOWN_VENDOR_FN(acbsloan0_.J_MBSI as J1_15_), acbsloan0_.J_MBTI as J2_15_, acbsloan0_.J_MBWI as J3_15_, acbsloan0_.J_MBXI as J4_15_, acbsloan0_.J_CMKI as J5_15_, acbsloan0_.J_CMMI as J6_15_, acbsloan0_.J_CMLI as J7_15_, acbsloan0_.J_MBUI as J8_15_, acbsloan0_.J_MCH8 as J9_15_, acbsloan0_.J_MCI8 as J10_15_, acbsloan0_.J_MCJ8 as J11_15_, acbsloan0_.J_MCK8 as J12_15_, acbsloan0_.J_DHD8 as J13_15_, acbsloan0_.J_MCFC as J14_15_, acbsloan0_.J_AWAC as J15_15_, acbsloan0_.J_MEP8 as J16_15_, acbsloan0_.J_GJAI as J17_15_, acbsloan0_.J_CMPI as J18_15_, acbsloan0_.J_MBVL as J19_15_, acbsloan0_.J_MEE_ as J20_15_, acbsloan0_.J_MEF_ as J21_15_, acbsloan0_.J_MEG_ as J22_15_, acbsloan0_.J_CMS_ as J23_15_, acbsloan0_.J_CNFC as J24_15_, acbsloan0_.J_CNGC as J25_15_, acbsloan0_.J_DUFC as J26_15_, acbsloan0_.J_CMOC as J27_15_, acbsloan0_.J_MCQI as J28_15_, acbsloan0_.J_MCPI as J29_15_, acbsloan0_.J_MCRI as J30_15_, acbsloan0_.J_MCOI as J31_15_, acbsloan0_.J_MDLC as J32_15_, acbsloan0_.J_MBSI as formula10_ from BSDTADLS.J_CLMS acbsloan0_ where acbsloan0_.J_MBUI='00000104' and acbsloan0_.J_AWAC='1' and (acbsloan0_.J_MBVL='N' or acbsloan0_.J_MBVL='Y' and acbsloan0_.J_MBWI<>' ' and acbsloan0_.J_MBWI<>acbsloan0_.J_MBUI)";
//		ISqlWrapper sqlWrapper = DefaultFactory.getFactory().getSqlWrapper();
//		sqlWrapper.setRetryParseWithoutSelectList(true);
//		ISqlModel model = org.wuqispank.DefaultFactory.getFactory().getSqlModel();
//		parser.setSqlModel(model);
//		parser.parse(sql);
//		assertEquals("Count of SQL tables incorrect when parsing the simplest SELECT SQL statement ever", 1, model.getTableCount() );
//
//		ITable shouldBe_J_CLMS = model.getTable(0);
//		assertEquals("SQL table name incorrectly parsed for simplest SELECT SQL statement ever", "J_CLMS".toLowerCase(), shouldBe_J_CLMS.getName() );
//
//		/**
//		 * 
//		 */
//		parser.setRetryParseWithoutSelectList(false);
//		model = org.wuqispank.DefaultFactory.getFactory().getSqlModel();
//		parser.setSqlModel(model);
//		try {
//			parser.parse(sql);
//			fail("Should have received an exception because of the vendor specific function.");
//		} catch (SqlParseException p) {
//			//expected this
//		}
//		
//	}

}
