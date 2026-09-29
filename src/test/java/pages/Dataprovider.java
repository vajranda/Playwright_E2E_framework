package pages;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

public class Dataprovider {


    @DataProvider(name="personalData")
    public Object[][] testdata(){
        HashMap<String, String> user1= new  HashMap<String, String>();
        user1.put("email","k@abc.com");
        user1.put("password","firstone");

        HashMap<String, String> user2= new  HashMap<String, String>();

        user2.put("email","kabab.com");
        user2.put("password","2ndone");

        return new Object[][]{{user1}, {user2}};



    }

   @Test(dataProvider = "jsondata")
    public void fillInfo(HashMap<String, String> data){

        System.out.println(data.get("email"));
        System.out.println(data.get("password"));


    }

    @DataProvider(name = "jsondata")
    public Object[][] getData() throws Exception {
String jsoncontent= new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir")+"/src/test/resources/TC1_TESTDATA.json")));

        Type type = new TypeToken<List<HashMap<String,String>>>() {}.getType();
           List<HashMap<String,String>> list  = new Gson().fromJson(jsoncontent, type);

        Object[][] table = new Object[list.size()][1];

        for (int i = 0; i < list.size(); i++) {
            table[i][0] = list.get(i);
        }

        return table;
    }













}
