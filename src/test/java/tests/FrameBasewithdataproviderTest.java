package tests;

import Utils.Dataprovider;
import com.microsoft.playwright.Tracing;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.AdminEventsPage;
import pages.BookingformPage;
import pages.DashboardPage;
import pages.Login;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;

public class FrameBasewithdataproviderTest extends Utility{
    DashboardPage dashboardPage;

    @AfterMethod
    public void teardown(){

        context.tracing().stop(new Tracing.StopOptions()
                .setPath(Paths.get("trace.zip"))
        );


    }

  @DataProvider(name="eventdata")
  public Object[][] eventdataprovider() throws IOException {

      return Dataprovider.dataProvider("/src/test/resources/eventTestdata.json");

  }






    @Test(dataProvider = "eventdata",description = "event booked",groups="smoke")
    public void
    demoTest(HashMap<String,String> data) {
        String eventTitle ="barclays bank";
        Login login = new Login(page,baseUrl);
      dashboardPage=  login.loginToApplication();
        dashboardPage.loadEvents();



        // step1 logged in and try yo click on admin page

        AdminEventsPage adminEventsPage =new AdminEventsPage(page);
        adminEventsPage.createEvent(
                data.get("title"),
                data.get("description"),
                data.get("category"),
                data.get("city"),
                data.get("venue"),
                data.get("dateTime"),
                data.get("price"),
                data.get("seat")
        );

        //step3 created verify the event in event page
       adminEventsPage.goTo();


        //after card is fetched
        adminEventsPage.calculateSeatAvailablity( data.get("title"));
      BookingformPage bookingformPage= adminEventsPage.OpenbookEventform();

        //clicked on book ticket and filling booking form
        bookingformPage.bookEvent(
                data.get("fullname"),
                data.get("email"),
        data.get("phone")

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
