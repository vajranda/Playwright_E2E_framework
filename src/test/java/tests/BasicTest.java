package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.file.Paths;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class BasicTest {
    Playwright playwright;
    Browser browser;
    Page page;
    BrowserContext context;

    @BeforeMethod
    public void setUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
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
        page.navigate("https://eventhub.rahulshettyacademy.com/login");
    }

    @AfterMethod
    public void teardown(){

        context.tracing().stop(new Tracing.StopOptions()
                .setPath(Paths.get("trace.zip"))
        );


    }

    @Test(description = "event booked")
    public void
    demoTest() {

        System.out.println(page.title());
        assertThat(page).hasTitle("EventHub — Discover & Book Events");

//locators
        page.getByPlaceholder("you@email.com").fill("abc@c.com");
        page.getByLabel("Password").fill("aBC@12345");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In")).click();

        assertThat(page.getByRole(AriaRole.LINK,
                new Page.GetByRoleOptions().setName("Browse Events →"))).isVisible();

        // step1 logged in and try yo click on admin page

        page.locator("body > nav:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(2) > div:nth-child(5)").click();

        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Manage Events")).nth(0).click();
        // user is on admin page and created one event
        page.getByLabel("Title").fill("wwe event");
        page.locator("#admin-event-form textarea").fill("wrestmanin and royal rumble ");
        page.getByLabel("category").selectOption("Sports");
        page.getByLabel("City").fill("karkala");
        page.getByLabel("Venue").fill("SRI BHUVANEDRA colleage");
        page.getByLabel("Event Date & Time").fill("2027-07-16T12:08");
        page.getByLabel("Price ($)").fill("100");
        page.getByLabel("Total Seats").fill("10");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("+ Add Event")).click();

        assertThat(page.getByText("Event created!")).isVisible();

        //step3 created verify the event in event page
        page.getByTestId("nav-events").click();
        Locator eventCards = page.getByTestId("event-card");
        Locator correctEventCard = eventCards.filter(new Locator.FilterOptions().setHasText("wwe event"));
        // assertThat(correctEventCard).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(1000));
        assertThat(correctEventCard).isVisible();

        //after card is fetched
        String oldSeatStaus = correctEventCard.getByText("seat").innerText();
      int seatNumberBeforeBooking = Integer.parseInt(oldSeatStaus.split(" ")[0]);
        System.out.println(seatNumberBeforeBooking);
        correctEventCard.getByRole(AriaRole.LINK, new Locator.GetByRoleOptions().setName("Book Now")).click();

        //clicked on book ticket and filling booking form
        page.getByLabel("Full Name").fill("adam");
        page.locator("#customer-email").fill("abc@c.com");
        page.locator("#phone").fill("098767890932");
        page.locator("#confirm-booking").click();

        //verify booking is confirmed
        assertThat(page.getByText("Booking Confirmed!")).isVisible();
        String bookingRef = page.locator(".booking-ref").innerText();
        System.out.println(bookingRef);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("View My Bookings")).click();


        //verify booking histroy
        Locator bookingCard = page.getByTestId("booking-card");
        Locator correctBookedCard = bookingCard.filter(new Locator.FilterOptions().setHasText(bookingRef));
        assertThat(correctBookedCard).isVisible();

       // seat count reduction check
          page.getByTestId("nav-events").click();
        page.waitForTimeout(1000);
         // page.navigate("https://eventhub.rahulshettyacademy.com/events");

        Locator eventCardsAfterBooking = page.getByTestId("event-card");
        Locator correctEventCardAfterBooking = eventCardsAfterBooking.filter(new Locator.FilterOptions().setHasText("wwe event"));
       //  assertThat(correctEventCardAfterBooking).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(1000));
        assertThat(correctEventCardAfterBooking).isVisible();

        //after card is fetched
        String newSeatStatus = correctEventCardAfterBooking.getByText("seat").innerText();
        int  seatNumberAfterBooking= Integer.parseInt(newSeatStatus.split(" ")[0]);
         System.out.println(seatNumberAfterBooking);
        Assert.assertTrue(seatNumberAfterBooking<seatNumberBeforeBooking);
    }


}
