package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.file.Paths;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class Uicontinuation {
    Playwright playwright;
    Browser browser;
    Page page;


    @BeforeMethod(alwaysRun=true)
    public void setUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        //  Browser browser= playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false));
        //  Browser browser= playwright.webkit().launch(new BrowserType.LaunchOptions().setHeadless(false));
        page = browser.newPage();
        page.navigate("https://rahulshettyacademy.com/AutomationPractice/");

    }

    @Test
    public void advancedUiChecks() {

        assertThat(page.getByPlaceholder("Hide/Show Example")).isVisible();
        page.locator("#hide-textbox").click();
        assertThat(page.getByPlaceholder("Hide/Show Example")).isHidden();
        page.onDialog(dialog -> dialog.accept());
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Alert")).click();
        // page.locator("#alertbtn").click();

        page.locator("#mousehover").hover();
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Reload")).click();

        //   FrameLocator framePage =     page.frameLocator("#courses-iframe");
        //  String errorMsg=     framePage.locator(".sub-frame-error-details strong").textContent();
        //  System.out.println(errorMsg);
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("page.png")));
        Locator inputBox =page.getByPlaceholder("Hide/Show Example");

        inputBox.screenshot(new Locator.ScreenshotOptions().setPath(Paths.get("locators.png")));
        page.locator("#hide-textbox").click();
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("afterpage.png")));


    }


}
