package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class AdminEventsPage {
    Page page;
    private static final String title_label="Title";
    private static final String description_locator="#admin-event-form textarea";
    private static final String category_label="category";
    private static final String city_label="City";
    private static final String venue_label="Venue";
    private static final String date_time_label="Event Date & Time";
    private static final String price_label="Price ($)";
    private static final String seats_label="Total Seats";
    private static final String event_locator="body > nav:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(2) > div:nth-child(5)";
 private static final String event_creation_success_msg ="Event created!";
    private   Locator correctEventCard=null;


    public AdminEventsPage(Page page){

    this.page =page;
}

public void createEvent(String title,String description,String category,String city,String venue,
String date_and_time,String price,String seat){

    page.locator(event_locator).click();

    page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Manage Events")).nth(0).click();
    // user is on admin page and created one event
    page.getByLabel(title_label).fill(title);
    page.locator(description_locator).fill(description);
    page.getByLabel(category_label).selectOption(category);
    page.getByLabel(city_label).fill(city);
    page.getByLabel(venue_label).fill(venue);
    page.getByLabel(date_time_label).fill(date_and_time);
    page.getByLabel(price_label).fill(price);
    page.getByLabel(seats_label).fill(seat);
    page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("+ Add Event")).click();

    assertThat(page.getByText(event_creation_success_msg)).isVisible();
}

public void goTo(){
    page.getByTestId("nav-events").click();
}

public Locator findEventCard(String eventTitle){
    Locator eventCards = page.getByTestId("event-card");
     correctEventCard = eventCards.filter(new Locator.FilterOptions().setHasText(eventTitle));
    // assertThat(correctEventCard).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(1000));
    assertThat(correctEventCard).isVisible();
    return correctEventCard;

}

public void calculateSeatAvailablity(String eventTitle){
      correctEventCard=  findEventCard(eventTitle);
    String oldSeatStaus = correctEventCard.getByText("seat").innerText();
    int seatNumberBeforeBooking = Integer.parseInt(oldSeatStaus.split(" ")[0]);
    System.out.println(seatNumberBeforeBooking);

}

public BookingformPage OpenbookEventform(){

    correctEventCard.getByRole(AriaRole.LINK, new Locator.GetByRoleOptions().setName("Book Now")).click();

    return new BookingformPage(page);

}

}
