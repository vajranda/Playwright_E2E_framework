package pages;

import com.microsoft.playwright.Page;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class BookingformPage {
    Page page;
    private static final String name_label="Full Name";
    private static final String   email_locator="#customer-email";
    private static final String phone_locator="#phone";
    private static final String   booking_confirmation_locator ="#confirm-booking";


    public BookingformPage(Page page){
        this.page =page;


    }


    public void  bookEvent(String name, String email, String phonenumber){
        page.getByLabel(name_label).fill("adam");
        page.locator(email_locator).fill("abc@c.com");
        page.locator(phone_locator).fill("098767890932");
        page.locator( booking_confirmation_locator).click();

        //verify booking is confirmed
        assertThat(page.getByText("Booking Confirmed!")).isVisible();

    }
}
