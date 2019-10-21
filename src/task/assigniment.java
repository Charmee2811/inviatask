package task;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalTime;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;


public class assigniment {

	public static WebDriver driver = null;
	public static boolean isBrowserOpened = false;
    public static boolean isURLOpened = false;
    
    
    
 // Code to load configuration properity file
 	public static String config_getproperty(String propertyname) {
 		Properties prop = new Properties();
 		InputStream input = null;
 		try {
 			input = new FileInputStream(System.getProperty("user.dir") + "//src//configuration//config.properties");
 			prop.load(input);
 		}
 		catch (IOException ex) {
 			ex.printStackTrace();
 		}
 		finally {
 			if (input != null) {
 				try {
 					input.close();
 				}
 				catch (IOException e) {
 					e.printStackTrace();
 				}
 	
 			}
 		} 	
 		// Return the property value
 		return prop.getProperty(propertyname);
 	}
    
 	public static String or_getproperty(String propertyname) {
		Properties prop = new Properties();
		InputStream input = null;
	
		try {
			input = new FileInputStream(System.getProperty("user.dir") + "//src//configuration//OR.properties");
			// load a properties file
			prop.load(input);
		}
	
		catch (IOException ex) {
			ex.printStackTrace();
		}
	
		finally {
			if (input != null) {
				try {
					input.close();
				}
	
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	
		// Return the property value
		return prop.getProperty(propertyname);
	}
	
 	public static String testData_getproperty(String propertyname) {
		Properties prop = new Properties();
		InputStream input = null;
	
		try {
			input = new FileInputStream(System.getProperty("user.dir") + "//src//configuration//testData.properties");
			// load a properties file
			prop.load(input);
		}
	
		catch (IOException ex) {
			ex.printStackTrace();
		}
	
		finally {
			if (input != null) {
				try {
					input.close();
				}
	
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	
		// Return the property value
		return prop.getProperty(propertyname);
	}
    
	public void openBrowser() {
		if (!isBrowserOpened) {
			if (config_getproperty("browserType").equals("MOZILLA")){
				DesiredCapabilities caps = new FirefoxOptions().setProfile(new FirefoxProfile()).addTo(DesiredCapabilities.firefox());
				System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir") + "\\geckodriver.exe");	
				driver = new FirefoxDriver(caps);
				System.out.println("MOZZILA Browser opened");
				}
			else if (config_getproperty("browserType").equals("IE")) {
				DesiredCapabilities cap = new DesiredCapabilities();
				cap.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);
				System.setProperty("webdriver.ie.driver", System.getProperty("user.dir") + "\\IEDriverServer.exe");
				driver = new InternetExplorerDriver();
				System.out.println("IE Browser opened");
			} else if (config_getproperty("browserType").equals("CHROME")) {
				System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\chromedriver.exe");
				ChromeOptions options = new ChromeOptions();
				options.addArguments(config_getproperty("LanguageType"));
				driver = new ChromeDriver();
				System.out.println("CHROME Browser opened");
			}
			isBrowserOpened = true;
			driver.manage().window().maximize();
			System.out.println("window Maximised");
		  }
        }
	
	public boolean openURL() {
		driver.get(config_getproperty("URL")); //can mention in config properity
		try {
			System.out.println("URL Opened");
		}
		catch (Throwable t) {
			 System.out.println("URL NOt Opening");
			return false;
		}
		isURLOpened = true;
		return isURLOpened;
	}
	
	public static WebElement getObject(String xpathKey) {
		try {
			new FluentWait<WebDriver>(driver).withTimeout(25000, TimeUnit.SECONDS).pollingEvery(1, TimeUnit.SECONDS)
					.ignoring(NoSuchElementException.class)
					.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(or_getproperty(xpathKey))));
			WebElement x = driver.findElement(By.xpath(or_getproperty(xpathKey)));
			return x;
		}
	
		catch (Throwable t) {
			return null;
		}
	}
	
	public void switchWin() {	
		// String winHandle = driver.getWindowHandles().toString();
		for (String winHandle1 : driver.getWindowHandles())
			driver.switchTo().window(winHandle1);
	}
	
	//compare time
	public boolean compareTime (String actualTime ,String timeRange1 , String timeRange2)
	{
		String[] startTime = actualTime.split("-");
		LocalTime start = LocalTime.parse( timeRange1 + ":00" );
		LocalTime stop = LocalTime.parse(timeRange2 + ":00");
		LocalTime actualFlightStartTime = LocalTime.parse(startTime[0].trim() + ":00") ; //aadding 00 as seconds for time formate
		Boolean isActualFlightStartTime = ( actualFlightStartTime.isAfter( start ) && actualFlightStartTime.isBefore( stop ) ) ;
		
		if (isActualFlightStartTime)
			return true;
		else
			 return true;
	}
	
	//Move Slider till expected range
	public void moveSlider(String sliderXpathKey,String expectedSlideRange,String rangeTextValue )
	{
		int x=10;
		   WebElement slider = driver.findElement(By.xpath(or_getproperty(sliderXpathKey)));
		   int width = slider.getSize().getWidth();
		   Actions act= new Actions(driver);
		  
		   String currSlideRange = driver.findElement(By.xpath(or_getproperty(rangeTextValue))).getText().trim();
		   if(expectedSlideRange != currSlideRange )
		   {
			   act.moveToElement(slider, ((width*x)/100), 0).click();
			   act.build().perform();
			   System.out.println("moved slider to 10%");
		   }
		   else
			   System.out.println("slider already at expected range position");
	}
	
	//select dates from date picker
	public void dateSelector(String dtPickerXpathKey,String dateToSelect)
	{
		WebElement dateWidget = driver.findElement(By.xpath(or_getproperty(dtPickerXpathKey)));
		
		String[] dateArr = dateToSelect.split(".");
		String dd = dateArr[0].trim();
		String mm = dateArr[1].trim();
		String yyyy = dateArr[2].trim();
		
		String dtPickerMonthValue = driver.findElement(By.className("months-wrapper")).getAttribute("data-month");
	  
		for( int i =0; i<Integer.parseInt(mm);i++) //selecting month from date time picker
	    {
		  if(dtPickerMonthValue!= mm)
		  {
			 driver.findElement(By.className("month-button month-button-next icon-arrow-right-bold")).click();//class name of arrow is common for both datetime picker
	        break;
		  }
	    }
		
			List<WebElement> columns=dateWidget.findElements(By.tagName("td")); //selecting date from all cells
			for (WebElement cell: columns)
			{
			   if (cell.getText().equals(dateToSelect))
			   {
			      cell.findElement(By.linkText(dateToSelect)).click();
			      break;
			 }
		  }
		
	}
	
	// Code to close Browser
		public void closeBrowser() {
			driver.quit();
			isBrowserOpened = false;
		}
		
}
	