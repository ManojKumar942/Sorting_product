package tasks;

import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class Login {

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

		if(Values.equals("lohi")) {

			WebElement DropDown = driver.findElement(By.className("product_sort_container"));
			Select select = new Select(DropDown);
			select.selectByValue(Values);
			Thread.sleep(2000);

			List <WebElement> products = driver.findElements(By.className("inventory_item_name"));
			List <WebElement> prices = driver.findElements(By.className("inventory_item_price"));

			String expectedproduct = products.get(0).getText();
			String expectedprice = prices.get(0).getText();

			List <WebElement> addbutton = driver.findElements(By.xpath("//button[contains(text(),'Add to cart')]"));
			addbutton.get(0).click();
			driver.findElement(By.className("shopping_cart_link")).click();
			
			String actualproduct = driver.findElement(By.className("inventory_item_name")).getText();
			String actualprice = driver.findElement(By.className("inventory_item_price")).getText();
			String quantity = driver.findElement(By.className("cart_quantity")).getText();
			
			Assert.assertEquals(actualproduct, expectedproduct, "Product name mismatch");
			Assert.assertEquals(actualprice, expectedprice, "Product price mismatch");
			Assert.assertEquals(quantity, "1", "Quantity mismatch");
			
			System.out.println(actualproduct);
			System.out.println(actualprice);
			System.out.println(quantity);
			
			Thread.sleep(2000);
			
			driver.findElement(By.id("continue-shopping")).click();
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

