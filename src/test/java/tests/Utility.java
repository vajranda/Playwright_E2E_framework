package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import org.testng.annotations.BeforeMethod;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Utility {


    Playwright playwright;
    Browser browser;
    Page page;
    BrowserContext context;
    String baseUrl;

    @BeforeMethod(alwaysRun=true)
    public void setUp() throws IOException {
        playwright = Playwright.create();
        Properties prop = new Properties();
        FileInputStream fis = new FileInputStream("src/test/resources/config.properties");
        prop.load(fis);
        String browserName= System.getProperty("browser")!=null? System.getProperty("browser") :prop.getProperty("browser");
        String env= System.getProperty("env")!=null? System.getProperty("env") :prop.getProperty("env");





        if (browserName.equalsIgnoreCase("firefox")) {

            browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false));
        } else if (browserName.equalsIgnoreCase("safari")) {
            browser = playwright.webkit().launch(new BrowserType.LaunchOptions().setHeadless(false));
        } else browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));



        //  Browser browser= playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false));
        //  Browser browser= playwright.webkit().launch(new BrowserType.LaunchOptions().setHeadless(false));


        page = browser.newPage();
        context = page.context();

        context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true)
        );
        PlaywrightAssertions.setDefaultAssertionTimeout(8000);
        baseUrl = prop.getProperty(env+".baseurl");

    }


}
