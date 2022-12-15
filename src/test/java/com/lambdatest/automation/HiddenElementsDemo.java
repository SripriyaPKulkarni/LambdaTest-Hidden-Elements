package com.lambdatest.automation;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class HiddenElementsDemo {

	private RemoteWebDriver driver;

	@BeforeMethod
	public void setup(Method m, ITestContext ctx) throws MalformedURLException {
		String username = System.getenv("LT_USERNAME") == null ? "Your LT Username" : System.getenv("LT_USERNAME");
		String authkey = System.getenv("LT_ACCESS_KEY") == null ? "Your LT AccessKey" : System.getenv("LT_ACCESS_KEY");
		;
		String hub = "@hub.lambdatest.com/wd/hub";

		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("browserName", "Chrome");
		capabilities.setCapability("browserVersion", "108.0");
		HashMap<String, Object> ltOptions = new HashMap<String, Object>();
		ltOptions.put("username", "lambda Test username");
		ltOptions.put("accessKey", "access-key");
		ltOptions.put("platformName", "Windows 10");
		ltOptions.put("project", "Untitled");
		capabilities.setCapability("LT:Options", ltOptions);

		driver = new RemoteWebDriver(new URL("https://" + username + ":" + authkey + hub), capabilities);

	}

	@Test
	public void basicTest() throws InterruptedException {
		// navigating to the application under test
		driver.get("https://sripriyapkulkarni.com/");

		// maximize window
		driver.manage().window().maximize();

		// explicit wait - to wait for the  link to be click-able
		WebDriverWait wait = new WebDriverWait(driver, 30);
		wait.until(ExpectedConditions
				.visibilityOfElementLocated(By.xpath("//span[normalize-space()='Automation Practice']"))).click();

		Thread.sleep(1000);

		// Clicking on the Hide button
		driver.findElement(By.xpath("//input[@id='hide-textbox']")).click();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		/*
		 * driver.findElement(By.xpath("//input[@id='displayed-text']")).sendKeys(
		 * "LambdaTest"); driver.findElement(By.id("show-textbox")).click();
		 */

		JavascriptExecutor jse = (JavascriptExecutor) driver;

		// jse.executeScript("document.getElementById('displayed-text').value='LambdaTest';");

		WebElement element = driver.findElement(By.xpath("//input[@id='displayed-text']"));
		((JavascriptExecutor) driver).executeScript("arguments[0].setAttribute('style','visibility:visible;');",
				element);

		jse.executeScript("document.getElementById('displayed-text').value='LambdaTest';");

		driver.findElement(By.id("show-textbox")).click();

	}

	@AfterMethod
	public void tearDown() {
		
		driver.quit();
	}
}
