package tests;

import com.jayway.jsonpath.JsonPath;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApiTest {

    Playwright playwright;
    APIRequestContext apiRequest;
    APIResponse loginapiResponse,eventResponse,retriveEventResponse,deleteResponse;

     @Test()
    public void apiTest() {


        HashMap<Object, Object> payload = new HashMap<Object, Object>();
        payload.put("email", "abc@c.com");
        payload.put("password", "aBC@12345");

        //event creation
         Map<String, Object> createEvent = Map.of(
                 "title", "in karla",
                 "description", "A premier technology conference.",
                 "category", "KANDA",
                 "venue", "Mangalore International Centre",
                 "city", "Kudla",
                 "eventDate", "2029-06-15T09:00:00.000Z",
                 "price", 1500,
                 "totalSeats", 500,
                 "imageUrl", "https://example.com/banner.jpg"
         );



//login
        playwright = Playwright.create();
        apiRequest = playwright.request().newContext();
        loginapiResponse = apiRequest.post("https://api.eventhub.rahulshettyacademy.com/api/auth/login",
                RequestOptions.create().setData(payload));
        Assert.assertTrue(loginapiResponse.ok());
           System.out.println(loginapiResponse.text());

            String token = JsonPath.read(loginapiResponse.text(),"$.token");
           //create event
         eventResponse=   apiRequest.post("https://api.eventhub.rahulshettyacademy.com/api/events",
                 RequestOptions.create().setHeader("authorization","Bearer "+token).setData(createEvent));

            Assert.assertTrue(eventResponse.ok(),"event created");
          int eventid=  JsonPath.read(eventResponse.text(),"$.data.id");
          System.out.println(eventid);

          //retrive event
         retriveEventResponse=      apiRequest.get("https://api.eventhub.rahulshettyacademy.com/api/events",
                 RequestOptions.create().setQueryParam("page",1).setQueryParam("limit",12)
                         .setHeader("authorization","Bearer "+token) );
         Assert.assertTrue(retriveEventResponse.ok());

         System.out.println(retriveEventResponse.text());
             List<Integer> retrivedeventids= JsonPath.read(retriveEventResponse.text(),"$.data[*].id");

         Assert.assertTrue(retrivedeventids.contains(eventid),"id retrived succesfully");

         //delete event
         deleteResponse= apiRequest.delete("https://api.eventhub.rahulshettyacademy.com/api/events/"+eventid,
                 RequestOptions.create().setHeader("authorization","Bearer "+token)) ;

         Assert.assertTrue(deleteResponse.ok(), "event deleted succesfully");


         //verify in get call as that event is deleted or not

         retriveEventResponse=      apiRequest.get("https://api.eventhub.rahulshettyacademy.com/api/events",
                 RequestOptions.create().setQueryParam("page",1).setQueryParam("limit",12)
                         .setHeader("authorization","Bearer "+token) );
         Assert.assertTrue(retriveEventResponse.ok());
         List<Integer> retrivedeventidsAfterDelete= JsonPath.read(retriveEventResponse.text(),"$.data[*].id");

         Assert.assertFalse(retrivedeventidsAfterDelete.contains(eventid),"id must not present in get call");



    }


}
