package com.homesnap.automation.test;

import org.testng.annotations.Test;

import homesnap.automation.framework.BaseTest;
import homesnap.automation.framework.TestInfo;
import homesnap.automation.framework.TestInfo.APPS;
import homesnap.automation.framework.TestInfo.AUTHORS;

public class HomesnapLaunchTest extends BaseTest {
	
	@Test
	@TestInfo(ID = "PRO", APP = APPS.SMOKE, AUTHOR = AUTHORS.STANLEY)
	public void openHomepage() {
		
		System.out.println(driver.getCurrentUrl());
		
		
		
		
	}

}
