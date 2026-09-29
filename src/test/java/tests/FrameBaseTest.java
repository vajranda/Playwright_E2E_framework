package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import pages.AdminEventsPage;
import pages.BookingformPage;
import pages.DashboardPage;
import pages.Login;

import java.nio.file.Paths;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class FrameBaseTest extends Utility{
    DashboardPage dashboardPage;

    @AfterMethod
    public void teardown(){

        context.tracing().stop(new Tracing.StopOptions()
                .setPath(Paths.get("trace.zip"))
        );


    }

    @Test(description = "event booked",groups="smoke")
    public void
    demoTest() {
        String eventTitle ="barclays bank";
        Login login = new Login(page,baseUrl);
      dashboardPage=  login.loginToApplication();
        dashboardPage.loadEvents();



        // step1 logged in and try yo click on admin page

        AdminEventsPage adminEventsPage =new AdminEventsPage(page);
        adminEventsPage.createEvent(
                eventTitle,
                "Mortgages and payments onboarding details",
                "Sports",
                "SBC ROAD PERTH",
                "Bangalore",
                "2029-09-26T23:56",
                "12333",
                "8"
        );

        //step3 created verify the event in event page
       adminEventsPage.goTo();


        //after card is fetched
        adminEventsPage.calculateSeatAvailablity(eventTitle);
      BookingformPage bookingformPage= adminEventsPage.OpenbookEventform();

        //clicked on book ticket and filling booking form
        bookingformPage.bookEvent(
           "Modiji"  ,
           "modi@avc.in"  ,
           "0912345678"

        );
//        String bookingRef = page.locator(".booking-ref").innerText();
//        System.out.println(bookingRef);
//        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("View My Bookings")).click();
//
//
//        //verify booking histroy
//        Locator bookingCard = page.getByTestId("booking-card");
//        Locator correctBookedCard = bookingCard.filter(new Locator.FilterOptions().setHasText(bookingRef));
//        assertThat(correctBookedCard).isVisible();
//
//       // seat count reduction check
//          page.getByTestId("nav-events").click();
//        page.waitForTimeout(1000);
//         // page.navigate("https://eventhub.rahulshettyacademy.com/events");
//
//        Locator eventCardsAfterBooking = page.getByTestId("event-card");
//        Locator correctEventCardAfterBooking = eventCardsAfterBooking.filter(new Locator.FilterOptions().setHasText(eventTitle));
//       //  assertThat(correctEventCardAfterBooking).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(1000));
//        assertThat(correctEventCardAfterBooking).isVisible();
//
//        //after card is fetched
//        String newSeatStatus = correctEventCardAfterBooking.getByText("seat").innerText();
//        int  seatNumberAfterBooking= Integer.parseInt(newSeatStatus.split(" ")[0]);
//         System.out.println(seatNumberAfterBooking);
       //Assert.assertTrue(seatNumberAfterBooking<seatNumberBeforeBooking);
    }


}
