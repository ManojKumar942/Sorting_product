package tasks;

import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class HeaderExtract {

	WebDriver driver;

	@BeforeClass
	public void browserLaunch() {
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--incognito");
		driver = new ChromeDriver(options);
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		driver.manage().window().maximize();

		driver.get("https://www.saucedemo.com/");
		driver.findElement(By.id("user-name")).sendKeys("standard_user");
		driver.findElement(By.id("password")).sendKeys("secret_sauce");
		driver.findElement(By.id("login-button")).click();

	}

	@Test(dataProvider = "FilterOptions")
	public void addProduct(String Values) throws InterruptedException {

		if(Values.equals("za")) {

			WebElement DropDown = driver.findElement(By.className("product_sort_container"));
			Select select = new Select(DropDown);
			select.selectByValue(Values);
			Thread.sleep(2000);

			List <WebElement> products = driver.findElements(By.className("inventory_item_name"));
			List <WebElement> prices = driver.findElements(By.className("inventory_item_price"));

			int count = Math.min(3, products.size());

			System.out.println("First " + count + " products after sorting by '" + Values + "':");
			for (int i = 0; i < count; i++) {
				String productName = products.get(i).getText();
				String productPrice = prices.get(i).getText();

				System.out.println((i + 1) + ". Product: " + productName + " | Price: " + productPrice);
			}
		}

		else {
			return;
		}
	}

	@DataProvider(name = "FilterOptions")
	public Object[][] options(){
		return new Object[][]{
			{"hilo"},
			{"lohi"},
			{"az"},
			{"za"}
		};
	}

	@AfterClass
	public void close() {
		if (driver != null) {
			driver.quit();
		}
	}
}

