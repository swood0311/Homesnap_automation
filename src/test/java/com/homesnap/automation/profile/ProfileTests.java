package com.homesnap.automation.profile;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

import homesnap.automation.framework.BaseTest;
import homesnap.automation.framework.WebDriverWrapper;
import homesnap.page.objects.LoginPage;
import homesnap.page.objects.UserHomePage;

public class ProfileTests extends BaseTest{

	
	@Test
	public void viewAgentProfilePageTest() {
		
		// 1. Create login page 
		LoginPage loginPage = new LoginPage(driver);
		
		
		//2. Login Profile Tab	
		UserHomePage userHomePage = loginPage.login("swoodley@homesnap.com", "Trombone1!");
		
		         
		// 3. Click profile Tab
		userHomePage.clickProfileButton();
		
		// 4. Assert my agent profile says Me
		Assert.assertEquals(userHomePage.getUserInfo(), "Me");
	
		// 5. click on my public profile 
		userHomePage.clickMyPublicProfile();
		
			
	}
	@Test
	public void viewMyAgentOnlyProfilePageTest() {
		
		
		// 1. Create login page 
		LoginPage loginPage = new LoginPage(driver);
		
		
		// 2. Login Profile Tab	
		UserHomePage userHomePage = new UserHomePage(driver);
		
		// 3. Click profile Tab
		userHomePage.clickProfileButton();
		
		// 4. Click on My Agent-Only Profile
		userHomePage.clickMyAgentOnlyProfile();
		
		// 5. Click My Account
		userHomePage.clickMyAccount();
		
		// 6. Assert my agent profile says Me
		Assert.assertEquals(userHomePage.getUserInfo(), "Me");
		
		
		
		
	}
	
	public void changeUserProfileSettingsTest() {
		
		
	}

}
