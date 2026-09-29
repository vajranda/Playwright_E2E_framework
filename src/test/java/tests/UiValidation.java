package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.file.Paths;

public class UiValidation {
    Playwright playwright;
    BrowserContext context;
    Browser browser;
    Page page;
    @BeforeMethod(alwaysRun=true)
    public void setup() {

        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        context = browser.newContext();

        context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true)
        );

        page = context.newPage();
        page.navigate("https://rahulshettyacademy.com/loginpagePractise/");



    }

    @AfterMethod
    public void closeBrowser(){

        context.tracing().stop(new Tracing.StopOptions()
                .setPath(Paths.get("trace.zip"))
        );


    }

    @Test
    public void childWindow(){
        Locator blinkingText = page.locator(".blinkingText").first();

        Page newPage= context.waitForPage(()-> blinkingText.click());
         newPage.waitForLoadState();
      String email=  newPage.locator(".red").textContent();
      System.out.println(email);
          String emailAddress =  email.split("at ")[1].split(" ")[0];
          page.getByLabel("username").fill(emailAddress);
          System.out.println(page.getByLabel("username").inputValue());


    }

    @Test
    public void uiControls(){

      Locator userRadio=  page.getByRole(AriaRole.RADIO, new Page.GetByRoleOptions().setName("User"));
        userRadio.click();
        page.locator("#okayBtn").click();
        Assert.assertTrue(userRadio.isChecked());
        page.getByRole(AriaRole.COMBOBOX).selectOption("Teacher");
    Locator termsCheckbox =    page.getByRole(AriaRole.CHECKBOX, new Page.GetByRoleOptions().setName("I Agree to the terms and conditions"));
           termsCheckbox.check();
        Assert.assertTrue(termsCheckbox.isChecked());
        page.waitForTimeout(3000);
    }

}