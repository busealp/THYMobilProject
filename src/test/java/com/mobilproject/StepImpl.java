package com.mobilproject;

import com.mobilproject.helper.GoogleExcel;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.touch.WaitOptions;
import org.assertj.core.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.*;
import java.util.List;

import com.mobilproject.model.SelectorInfo;
import com.thoughtworks.gauge.Step;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import org.openqa.selenium.support.ui.WebDriverWait;

import javax.annotation.Nullable;

import static io.appium.java_client.touch.offset.PointOption.point;
import static java.time.Duration.ofMillis;

public class StepImpl extends HookImpl {

    private Logger logger = LoggerFactory.getLogger(getClass());
    GoogleExcel excel;

    private MobileElement findElement(By by) {

        WebDriverWait webDriverWait = new WebDriverWait(appiumDriver, 20);
        MobileElement mobileElement = (MobileElement) webDriverWait.until(ExpectedConditions.presenceOfElementLocated(by));
        return mobileElement;
    }

    public boolean doesElementExistByKey(String key, int time) {
        SelectorInfo selectorInfo = selector.getSelectorInfo(key);
        try {
            WebDriverWait elementExist = new WebDriverWait(appiumDriver, time);
            elementExist.until(ExpectedConditions.visibilityOfElementLocated(selectorInfo.getBy()));
            return true;
        } catch (Exception e) {
            logger.info(key + " aranan elementi bulamadı");
            return false;
        }

    }

    public MobileElement findElementByKey(String key) {
        SelectorInfo selectorInfo = selector.getSelectorInfo(key);

        MobileElement mobileElement = null;
        try {
            mobileElement = selectorInfo.getIndex() > 0 ? findElements(selectorInfo.getBy())
                    .get(selectorInfo.getIndex()) : findElement(selectorInfo.getBy());
        } catch (Exception e) {
            Assertions.fail("key = %s by = %s Element not found ", key, selectorInfo.getBy().toString());
            e.printStackTrace();

        }
        return mobileElement;
    }

    private void sendKeysToElement(By by, String text) {

        findElement(by).sendKeys(text);
    }

    public List<MobileElement> findElements(By by) throws Exception {
        List<MobileElement> webElementList = null;
        try {
            webElementList = appiumFluentWait.until(new ExpectedCondition<List<MobileElement>>() {
                @Nullable
                @Override
                public List<MobileElement> apply(@Nullable WebDriver driver) {
                    List<MobileElement> elements = driver.findElements(by);
                    return elements.size() > 0 ? elements : null;
                }
            });
            if (webElementList == null) {
                throw new NullPointerException(String.format("by = %s Web element list not found", by.toString()));
            }
        } catch (Exception e) {
            throw e;
        }
        return webElementList;

    }

    public List<MobileElement> findElemenstByKey(String key) {
        SelectorInfo selectorInfo = selector.getSelectorInfo(key);
        List<MobileElement> mobileElements = null;
        try {
            mobileElements = findElements(selectorInfo.getBy());
        } catch (Exception e) {
            Assertions.fail("key = %s by = %s Elements not found ", key, selectorInfo.getBy().toString());
            e.printStackTrace();
        }
        return mobileElements;
    }

    public void swipeDownAccordingToPhoneSize() {
        if (appiumDriver instanceof AndroidDriver) {
            Dimension d = appiumDriver.manage().window().getSize();
            int height = d.height;
            int width = d.width;

            int swipeStartWidth = width / 2, swipeEndWidth = width / 2;
            int swipeStartHeight = (height * 90) / 100;
            int swipeEndHeight = (height * 50) / 100;
            //appiumDriver.swipe(swipeStartWidth, swipeStartHeight, swipeEndWidth, swipeEndHeight, 1000);
            new TouchAction(appiumDriver)
                    .press(PointOption.point(swipeStartWidth, swipeStartHeight))
                    .waitAction(WaitOptions.waitOptions(ofMillis(1000)))
                    .moveTo(PointOption.point(swipeEndWidth, swipeEndHeight))
                    .release()
                    .perform();
        } else {
            Dimension d = appiumDriver.manage().window().getSize();
            int height = d.height;
            int width = d.width;

            int swipeStartWidth = width / 2, swipeEndWidth = width / 2;
            int swipeStartHeight = (height * 90) / 100;
            int swipeEndHeight = (height * 40) / 100;
            // appiumDriver.swipe(swipeStartWidth, swipeStartHeight, swipeEndWidth, swipeEndHeight, 1000);
            new TouchAction(appiumDriver)
                    .press(PointOption.point(swipeStartWidth, swipeStartHeight))
                    .waitAction(WaitOptions.waitOptions(ofMillis(1000)))
                    .moveTo(PointOption.point(swipeEndWidth, swipeEndHeight))
                    .release()
                    .perform();
        }
    }

    @Step({"<seconds> saniye bekle ", "Wait <second> seconds"})
    public void waitBySecond(int seconds) throws InterruptedException {
        Thread.sleep(seconds * 1000);
    }

    @Step({"<key> li elementi bul ve tıkla", "Click element by <key>"})
    public void clickByKey(String key) {
        doesElementExistByKey(key, 5);

        findElementByKey(key).click();
        logger.info(key + "elemente tıkladı");

    }


    @Step("<text> gün sonraya bilet seçilir")
    public void selectDate(String text) {
        int after = Integer.parseInt(text.toString());
        Calendar date = Calendar.getInstance();
        int currentDay = date.get(Calendar.DATE);
        int month = date.get(Calendar.MONTH);
        int afterDay;

        if (month % 2 == 1) {
            if (currentDay <= 31 - after) {
                afterDay = currentDay + after;

            } else {
                currentDay = 0;
                afterDay = currentDay + after;

            }
        } else {
            if (currentDay <= 30 - after) {
                afterDay = currentDay + after;

            } else {
                currentDay = 0;
                afterDay = currentDay + after;

            }
        }
        findElement(By.xpath("//*[@text='" + afterDay + "']")).click();
    }

    @Step("<key> elementine <deger> yaz")
    public void elementeDegerYaz(String key, String deger) throws IOException, GeneralSecurityException {
        switch (deger) {
            case "ad":
                sendKeysToElement(selector.getElementInfoToBy(key), excel.Excel(0));
                break;
            case "soyad":
                sendKeysToElement(selector.getElementInfoToBy(key), excel.Excel(1));
                break;

            case "cinsiyet":
                clickByKey(key);
                break;

            case "dogumyili":
                clickByKey(key);
                break;

            case "dogumayi":
                clickByKey(key);
                break;
            case "dogumgünü":
                clickByKey(key);
                break;
            case "eposta":
                sendKeysByKeyNotClear(key, excel.Excel(2));
                break;
            case "tc":
                clickByKey(key);
                break;
            case "tcGir":
                sendKeysToElement(selector.getElementInfoToBy(key), excel.Excel(3));
                break;
            case "btn_TekYön":
                clickByKey(key);
                break;
            case "telefon":
                clickByKey(key);
                break;

        }
    }

    @Step({"<key> li elementi bul ve <text> değerini yaz",
            "Find element by <key> and send keys <text>"})
    public void sendKeysByKeyNotClear(String key, String text) {
        doesElementExistByKey(key, 5);
        findElementByKey(key).setValue(text);

    }

    @Step("swipe et")
    public void swipeMethod() {
        if (appiumDriver instanceof IOSDriver) {
            Dimension size = appiumDriver.manage().window().getSize();
            int x = size.getWidth() - 1;
            int starty = (int) (size.getHeight() * 0.10);
            int endy = (int) (size.getHeight() * 0.60);

            new TouchAction(appiumDriver).longPress(PointOption.point(x, starty))
                    .moveTo(PointOption.point(x, endy))
                    .release().perform();
        } else {
            new TouchAction(appiumDriver).longPress(PointOption.point(2, 800))
                    .moveTo(PointOption.point(2, 568))
                    .release().perform();
        }

        System.out.println("swipe yapıldı");
    }

    @Step({"<key> li elementi bulana kadar swipe et ve tıkla",
            "Find element by <key>  swipe and click"})
    public void clickByKeyWithSwipe(String key) throws InterruptedException {
        int maxRetryCount = 10;
        while (maxRetryCount > 0) {
            List<MobileElement> elements = findElemenstByKey(key);
            if (elements.size() > 0) {
                if (elements.get(0).isDisplayed() == false) {
                    maxRetryCount--;
                    swipeDownAccordingToPhoneSize();
                    waitBySecond(1);

                } else {
                    elements.get(0).click();
                    logger.info(key + " elementine tıklandı");
                    break;
                }
            } else {
                maxRetryCount--;
                swipeDownAccordingToPhoneSize();
                waitBySecond(1);
            }

        }
    }

    @Step("Rastgele <rastgeleKoltuk> seç")
    public void randomKoltukk(String key) {
        List<WebElement> boskoltuk = (List<WebElement>) selector.getElementInfoToBy(key);

        int randKoltuk = randomNumber(0, boskoltuk.size());

        boskoltuk.get(randKoltuk).click();
    }

    public static int randomNumber(int start, int end) {
        Random rn = new Random();
        int randomNumber = rn.nextInt(end - 1) + start;
        return randomNumber;
    }

    @Step("klavye kapat")
    public void closeKey() {
        appiumDriver.hideKeyboard();
    }


    @Step("deneme")
    public void implementation1() throws IOException, GeneralSecurityException {
        System.out.println(excel.Excel(2));

    }

}