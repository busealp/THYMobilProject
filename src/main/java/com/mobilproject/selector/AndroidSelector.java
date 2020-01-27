package com.mobilproject.selector;

import com.mobilproject.model.ElementInfo;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

public class AndroidSelector implements Selector {

  @Override
  public By getElementInfoToBy(ElementInfo elementInfo) {
    By by = null;
    if (elementInfo.getAndroidType().equals("css")) {
      by = By.cssSelector(elementInfo.getAndroidValue());
    } else if (elementInfo.getAndroidType().equals("id")) {
      by = By.id(elementInfo.getAndroidValue());
    } else if (elementInfo.getAndroidType().equals("xpath")) {
      by = By.xpath(elementInfo.getAndroidValue());
    } else if (elementInfo.getAndroidType().equals("class")) {
      by = By.className(elementInfo.getAndroidValue());
    } else if (elementInfo.getAndroidType().equals("text")){
      by = By.linkText(elementInfo.getAndroidValue());
    }else if (elementInfo.getAndroidType().equals("accessibilityid")){
      by = MobileBy.AccessibilityId(elementInfo.getAndroidValue());
    }
    else if (elementInfo.getAndroidType().equals("uiSelector")){
      by = by = MobileBy.AndroidUIAutomator(elementInfo.getAndroidValue());;
    }
    return by;
  }

  @Override
  public int getElementInfoToIndex(ElementInfo elementInfo) {
    return elementInfo.getAndroidIndex();
  }
}
