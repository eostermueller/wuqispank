package org.wuqispank.web.test.tablecount;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class XmlSwimlanes_1_test {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  @Before
  public void setUp() throws Exception {
    driver = new FirefoxDriver();
    baseUrl = "http://localhost:8081";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void testXmlSwimlanes1() throws Exception {
    driver.get(baseUrl + "/wuqispank/test/org.wuqispank.web.test.tap.XmlSwimlanes_1");
    assertEquals("CUST_CUST_REL", driver.findElement(By.cssSelector("#label-9 > g > text")).getText());
    assertEquals("ACCOUNT", driver.findElement(By.cssSelector("#label-12 > g > text")).getText());
    assertEquals("ACCT_ACCT_REL", driver.findElement(By.cssSelector("#label-15 > g > text")).getText());
    assertEquals("CUST_CUST_REL", driver.findElement(By.cssSelector("#label-10 > g > text")).getText());
    assertEquals("ACCOUNT", driver.findElement(By.cssSelector("#label-13 > g > text")).getText());
    assertEquals("ACCT_ACCT_REL", driver.findElement(By.cssSelector("#label-16 > g > text")).getText());
  }

  @After
  public void tearDown() throws Exception {
    driver.quit();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }
  }

  private boolean isElementPresent(By by) {
    try {
      driver.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  private boolean isAlertPresent() {
    try {
      driver.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }

  private String closeAlertAndGetItsText() {
    try {
      Alert alert = driver.switchTo().alert();
      String alertText = alert.getText();
      if (acceptNextAlert) {
        alert.accept();
      } else {
        alert.dismiss();
      }
      return alertText;
    } finally {
      acceptNextAlert = true;
    }
  }
}
