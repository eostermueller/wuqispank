package org.wuqispank.web;

import java.util.regex.Pattern;


import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

@Ignore
public class HardCodedTable_T_e_s_t {
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
  public void testHardCodedTable() throws Exception {
    driver.get(baseUrl + "/wuqispank/test/org.wuqispank.web.test.tap.HardCodedTable");
    assertEquals("CUST_CUST_REL", driver.findElement(By.xpath("//*[@id=\"label-74\"]")).getText());
    assertEquals("CUST_CUST_REL", driver.findElement(By.cssSelector("#label-74 > g > text")).getText());
    assertEquals("153", driver.findElement(By.cssSelector("#shape-70 > rect:nth-child(1)")).getAttribute("x"));
    assertEquals("149", driver.findElement(By.cssSelector("#shape-70 > rect:nth-child(1)")).getAttribute("y"));
    assertEquals("5", driver.findElement(By.cssSelector("#shape-70 > rect:nth-child(1)")).getAttribute("width"));
    assertEquals("624", driver.findElement(By.cssSelector("#shape-70 > rect:nth-child(1)")).getAttribute("height"));
    assertEquals("153", driver.findElement(By.cssSelector("#shape-70 > rect:nth-child(2)")).getAttribute("x"));
    assertEquals("149", driver.findElement(By.cssSelector("#shape-70 > rect:nth-child(2)")).getAttribute("y"));
    assertEquals("5", driver.findElement(By.cssSelector("#shape-70 > rect:nth-child(2)")).getAttribute("width"));
    assertEquals("624", driver.findElement(By.cssSelector("#shape-70 > rect:nth-child(2)")).getAttribute("height"));
    assertEquals("1", driver.findElement(By.cssSelector("text")).getText());
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
