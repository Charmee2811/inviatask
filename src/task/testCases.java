package task;

import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


public class testCases extends assigniment {

	@BeforeTest
	public void preAction()
	{
		openBrowser();
		openURL() ;
		Assert.assertTrue(driver.getTitle().contains("Urlaub"));
		System.out.println("page loaded");
	}
	@Test
	public void TC1()
	{
		//Defination/Declarations
		int noOfAdults = Integer.parseInt(testData_getproperty("noOfAdults"));
		int noOfChildren = Integer.parseInt(testData_getproperty("noOfChildren"));
		boolean isSortDecsending = false;
	
 //Enter initial travel criteria	 
		driver.findElement(By.xpath(or_getproperty("location"))).sendKeys(testData_getproperty("location"));
	
		//select initial dates
		dateSelector(or_getproperty("arrivalDates"), testData_getproperty("initalArrivalDate"));
		dateSelector(or_getproperty("returnDates"), testData_getproperty("initalReturnDate"));
		
		//Select no of Travelers
		driver.findElement(By.xpath(or_getproperty("noOfPeople"))).click();
		if (noOfAdults > 2)
		{
			for (int i = noOfAdults; i <= 2; i--)
				driver.findElement(By.xpath(or_getproperty("noOfAdults"))).click();		
		}	
		if (noOfChildren > 0) 
	    {
		   for (int i = noOfChildren; i <= 0; i--)
				driver.findElement(By.xpath(or_getproperty("noOfChildren"))).click();		
		}
		
		driver.findElement(By.xpath(or_getproperty("offersSearch"))).click();
	 
//Edit the initial selected dates at hotel page
		 getObject("hotelSelecPg_DTPickerArival"); //wait till new page is loaded
		 dateSelector(or_getproperty("hotelSelecPg_DTPickerArival"), testData_getproperty("ChangedArrivalDate"));
			dateSelector(or_getproperty("hotelSelecPg_DTPickerReturn"), testData_getproperty("ChangedReturnDate"));
		
		driver.findElement(By.xpath(or_getproperty("offersSearch"))).click();
		
//Apply Filters 4* rated hotel and best customer rating	
		driver.findElement(By.xpath(or_getproperty("4hotelRating"))).click(); 
		driver.findElement(By.xpath(or_getproperty("bestCustRating"))).click();
		String verifyHotelFilterSelected = driver.findElement(By.xpath(or_getproperty("verifyFilters"))).getText();
		if (verifyHotelFilterSelected.contains("from 4 stars") && verifyHotelFilterSelected.contains("Excellent"))
			System.out.println("hotel filters selected correctly");
		else
			System.out.println("hotel selection filters are not applied correctly ");

//sorting according to higest price and verify	
		Select dropdown1 = new Select(driver.findElement(By.xpath(or_getproperty("hotelSorting"))));
		  dropdown1.selectByVisibleText("Höchster Preis");
		
		  List<WebElement> hotelPriceBox = driver.findElements(By.className("priceBox")); 
		  ArrayList<Float> allHotelsPrice=new ArrayList<>();
		   for(int i=0; i<hotelPriceBox.size(); i++)
			 allHotelsPrice.add(Float.parseFloat(hotelPriceBox.get(i).getText()));
		   
		   for (int i = 0; i < allHotelsPrice.size()-1; i++) 
		   {
	           if (allHotelsPrice.get(i) > allHotelsPrice.get(i+1)) 
	               isSortDecsending = true;     
	           else
	        	   isSortDecsending = false;
	        }
	        
			if(!isSortDecsending){
		        Assert.fail("Not is decesding order");
		    }
		
 //select most expensive hotel
		String selectedHotelName = driver.findElement(By.className("js-ibe4 hotel-name")).getText();
		driver.findElement(By.xpath(or_getproperty("mostExpensiveHotel_1") + testData_getproperty("hotelNummber") + or_getproperty("mostExpensiveHotel_2"))).click();	

 //selection for slider time range		
		switchWin();  
		moveSlider(or_getproperty("maxArrivalTimeRangeSlider"), testData_getproperty("arrivalFlightRangeMax"),or_getproperty("maxSelectedArrivalTime"));//moving rightside slider till max range ie 21
		moveSlider(or_getproperty("minArrivalTimeRangeSlider"), testData_getproperty("arrivalFlightRangeMin"), or_getproperty("minSelectedArrivalTime"));
		moveSlider(or_getproperty("maxReturnTimeRangeslider"), testData_getproperty("returnFlightRangeMax"),or_getproperty("maxSelectedReturnTime"));
		moveSlider(or_getproperty("minReturnTimeRangeSlider"), testData_getproperty("returnlFlightRangeMin"),or_getproperty("minSelectedReturnTime"));
		
 //select arrival checkbox “Anreisedatum”
		driver.findElement(By.xpath(or_getproperty("selectArrival"))).click();

 //Select direct flight and its count
			driver.findElement(By.xpath(or_getproperty("directFlight"))).click();
			driver.findElement(By.xpath(or_getproperty("offersSearch"))).click();
			List<WebElement> ttlDirectFlightRecords = driver.findElements(By.xpath(or_getproperty("ttlRecords")));
			int countDirectFlight = ttlDirectFlightRecords.size();
			System.out.println("Total avaiable direct flights are:-  " + countDirectFlight );
		
 //verify flight time of first result falls in desired range for both(arrival and departure) 
		   String arrivalFlightTm =driver.findElement(By.xpath(or_getproperty("arrivalFlightTimeFor1st"))).getText();
		   String returnFlightTm =driver.findElement(By.xpath(or_getproperty("returnFlightTimeFor1st"))).getText();
		   Boolean isArrivalFlighTimeMatch = compareTime(arrivalFlightTm ,testData_getproperty("arrivalFlightRangeMin"),testData_getproperty("arrivalFlightRangeMax"));
		   Boolean isdepartureFlighTimeMatch = compareTime(returnFlightTm ,testData_getproperty("returnFlightRangeMin"),testData_getproperty("returnFlightRangeMax"));
		
		   if (isArrivalFlighTimeMatch && isdepartureFlighTimeMatch )
			  Assert.assertTrue(true,"flight time of first searched record falls in desired time range");
		   else
			 Assert.fail("flight time does not fall in desired time range"); 
		
  //select first offer go to booking
			driver.findElement(By.xpath(or_getproperty("checkAvaiablity"))).click();
			String isOfferAvaiable = driver.findElement(By.xpath(or_getproperty("offerAvaiable"))).getText();
			if (isOfferAvaiable.contains("Offer still available"))
				driver.findElement(By.xpath(or_getproperty("bookingLink"))).click();
			else
				Assert.fail("offer is not avaiable hence can not book");
			switchWin();
		
  //Verification of hotel name
			String bookingHotelName = driver.findElement(By.className("hotel-name")).getText();
			if (bookingHotelName.contains(selectedHotelName))
				Assert.assertTrue(true,"selected hotel is only appearing for booking");
			else
				Assert.fail("selected hotel and booking hotel mismatched");
			
			closeBrowser();
		
  }	
}
	