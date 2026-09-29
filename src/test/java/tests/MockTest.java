package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.nio.file.Paths;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class MockTest {


    Playwright playwright;
    Browser browser;
    Page page;
    BrowserContext context;

    @Test(description = "sandbox holds exact 6 events")
    public void fulfillTest() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        //  Browser browser= playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false));
        //  Browser browser= playwright.webkit().launch(new BrowserType.LaunchOptions().setHeadless(false));


        page = browser.newPage();
        page.navigate("https://eventhub.rahulshettyacademy.com/login");

        page.getByPlaceholder("you@email.com").fill("abc@c.com");
        page.getByLabel("Password").fill("aBC@12345");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In")).click();

        assertThat(page.getByRole(AriaRole.LINK,
                new Page.GetByRoleOptions().setName("Browse Events →"))).isVisible();

        page.route("**api/events**", route -> route.fulfill(
                new Route.FulfillOptions().setPath(Paths.get("src/test/resources/6_events.json"))
        ));
        page.navigate("https://eventhub.rahulshettyacademy.com/events");

        Locator eventcardcount = page.locator("#event-card");
        assertThat(eventcardcount.first()).isVisible();
        Assert.assertEquals(eventcardcount.count(), 6);
        assertThat(page.locator(".mx-1").last()).isVisible();


        page.route("**api/events**", route -> route.fulfill(
                new Route.FulfillOptions().setPath(Paths.get("src/test/resources/4_events.json"))
        ));

        page.reload();

        Locator eventcardcount1 = page.locator("#event-card");
        assertThat(eventcardcount1.first()).isVisible();
        Assert.assertEquals(eventcardcount1.count(), 4);
        assertThat(page.locator(".mx-1").last()).isHidden();
    }

    @Test()
    public void resumeTest() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        //  Browser browser= playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false));
        //  Browser browser= playwright.webkit().launch(new BrowserType.LaunchOptions().setHeadless(false));


        page = browser.newPage();
        page.navigate("https://eventhub.rahulshettyacademy.com/login");

        page.getByPlaceholder("you@email.com").fill("abc@c.com");
        page.getByLabel("Password").fill("aBC@12345");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In")).click();

        assertThat(page.getByRole(AriaRole.LINK,
                new Page.GetByRoleOptions().setName("Browse Events →"))).isVisible();


        page.getByTestId("nav-bookings").click();

        page.route("**api/bookings/**", route->route.resume(
                new Route.ResumeOptions().setUrl( "https://api.eventhub.rahulshettyacademy.com/api/bookings/10841")
        ));
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("View Details")).first().click();

     assertThat(page.getByText("Access Denied")).isVisible();

     page.waitForTimeout(3000);


    }


}
