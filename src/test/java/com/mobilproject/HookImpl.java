package com.mobilproject;

import com.mobilproject.selector.Selector;
import com.mobilproject.selector.SelectorFactory;
import com.mobilproject.selector.SelectorType;
import com.thoughtworks.gauge.AfterScenario;
import com.thoughtworks.gauge.AfterStep;
import com.thoughtworks.gauge.BeforeScenario;
import com.thoughtworks.gauge.ExecutionContext;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.remote.MobilePlatform;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.FluentWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class HookImpl {
    private Logger logger = LoggerFactory.getLogger(getClass());
    protected static AppiumDriver<MobileElement> appiumDriver;
    protected static FluentWait<AppiumDriver> appiumFluentWait;
    public static boolean localAndroid = false;
    protected static Selector selector;


 @BeforeScenario
    public void beforeScenario(ExecutionContext executionContext) throws MalformedURLException {


        logger.info("*************************************************************************" + "\r\n");
        logger.info("------------------------NEW SCENARIO-------------------------");
        logger.info("SCENARIO NAME: " + executionContext.getCurrentScenario().getName());
        logger.info(executionContext.getAllTags().toString());


        if (StringUtils.isEmpty(System.getenv("key"))) {

            if (localAndroid) {
                logger.info("Local Browser");
                DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
                desiredCapabilities
                        .setCapability(MobileCapabilityType.PLATFORM, MobilePlatform.ANDROID);
                desiredCapabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "android");
                desiredCapabilities.setCapability(MobileCapabilityType.UDID, "5200fc53fe7d94fd");
                desiredCapabilities
                        .setCapability(AndroidMobileCapabilityType.APP_PACKAGE,
                                "com.turkishairlines.mobile");
                desiredCapabilities
                        .setCapability(AndroidMobileCapabilityType.APP_ACTIVITY,
                                "com.turkishairlines.mobile.ui.ACSplash");
                desiredCapabilities.setCapability(MobileCapabilityType.NO_RESET, true);
                desiredCapabilities.setCapability(MobileCapabilityType.FULL_RESET, false);
                desiredCapabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 300);
                desiredCapabilities.setCapability("unicodeKeyboard", false);
                desiredCapabilities.setCapability("resetKeyboard", false);
                URL url = new URL("http://127.0.0.1:4723/wd/hub");
                appiumDriver = new AndroidDriver(url, desiredCapabilities);
            } else {
                DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
                // desiredCapabilities
                //       .setCapability(MobileCapabilityType.PLATFORM, MobilePlatform.IOS);
                desiredCapabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "XCUITest");
                desiredCapabilities
                        .setCapability(MobileCapabilityType.UDID, "0fbe99646739467b8b2700c09c7072e1fd3e455f");
                desiredCapabilities
                        .setCapability(IOSMobileCapabilityType.BUNDLE_ID, "com.turkcell.CSI");
                desiredCapabilities
                        .setCapability(MobileCapabilityType.DEVICE_NAME, "testinium's iPhone");
                desiredCapabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "10.2");
                desiredCapabilities.setCapability(MobileCapabilityType.NO_RESET, true);
                desiredCapabilities.setCapability(MobileCapabilityType.FULL_RESET, false);
                desiredCapabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 300);

                desiredCapabilities.setCapability("sendKeyStrategy", "setValue");

                URL url = new URL("http://127.0.0.1:4723/wd/hub");
                appiumDriver = new IOSDriver<>(url, desiredCapabilities);

            }
        } else {
            String hubURL = "http://hub.testinium.io/wd/hub";
            DesiredCapabilities capabilities = new DesiredCapabilities();
            System.out.println("key:" + System.getenv("key"));
            System.out.println("platform" + System.getenv("platform"));
            System.out.println("version" + System.getenv("version"));
            if (System.getenv("platform").equals("ANDROID")) {
                capabilities.setCapability("key", System.getenv("key"));
                capabilities
                        .setCapability(AndroidMobileCapabilityType.APP_PACKAGE,
                                "com.ttech.android.onlineislem");
                capabilities
                        .setCapability(AndroidMobileCapabilityType.APP_ACTIVITY,
                                "com.ttech.android.onlineislem.ui.splash.SplashActivity");
                // capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME,"uiautomator2");
                capabilities.setCapability(MobileCapabilityType.NO_RESET, true);
                capabilities.setCapability(MobileCapabilityType.FULL_RESET, false);
                capabilities.setCapability("unicodeKeyboard", false);
                capabilities.setCapability("resetKeyboard", false);
                appiumDriver = new AndroidDriver(new URL(hubURL), capabilities);
                localAndroid = true;
            } else {

                capabilities.setCapability(MobileCapabilityType.NO_RESET, true);
                capabilities.setCapability(MobileCapabilityType.FULL_RESET, false);
                capabilities.setCapability("key", System.getenv("key"));
                capabilities.setCapability("waitForAppScript", "$.delay(1000);");
                capabilities.setCapability("bundleId", "com.turkcell.CSI");
                capabilities.setCapability("usePrebuiltWDA", true);
                capabilities.setCapability("useNewWDA", true);

                appiumDriver = new IOSDriver(new URL(hubURL), capabilities);
                localAndroid = false;
            }
        }
        selector = SelectorFactory
                .createElementHelper(localAndroid ? SelectorType.ANDROID : SelectorType.IOS);
        appiumFluentWait = new FluentWait(appiumDriver);
        appiumFluentWait.withTimeout(Duration.ofSeconds(30))
                .pollingEvery(Duration.ofMillis(250))
                .ignoring(NoSuchElementException.class);
    }


    @AfterScenario
    public void afterScenario() {

        if (appiumDriver != null) {
            // appiumDriver.quit();
        }

        logger.info("*************************************************************************" + "\r\n");
    }

    @AfterStep
    public void afterStep(ExecutionContext executionContext) {

        if (executionContext.getCurrentStep().getIsFailing()) {
            logger.info(executionContext.getCurrentStep().getErrorMessage());
            logger.info(executionContext.getCurrentStep().getStackTrace());
        }
    }
}