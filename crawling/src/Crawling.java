import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Crawling {
	public static void main(String[] args) {
        Reviews reviews = new Reviews("9791165341909");
        reviews.doCrawling();
        reviews.printReviews();
	}
}

class Reviews {
    Review[] reviews;

    //WebDriver
    private WebDriver driver;

    //Properties
    public static final String WEB_DRIVER_ID = "webdriver.chrome.driver";
    public static final String WEB_DRIVER_PATH = "C:/Users/user/Downloads/selenium/chromedriver_win32/chromedriver.exe";

    //크롤링 할 URL
    private String base_url;

    public Reviews(String ISBN) {
        //super();

        //System Property SetUp
        System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);

        //Driver SetUp
        driver = new ChromeDriver();
        base_url = "http://www.kyobobook.co.kr/product/detailViewKor.laf?ejkGb=KOR&mallGb=KOR&barcode=" + ISBN + "&orderClick=LET&Kc=#review";
    }

    public void doCrawling() {

        try {
            //get page (= 브라우저에서 url을 주소창에 넣은 후 request 한 것과 같다)
            driver.get(base_url);

            List<WebElement> elements = driver.findElements(By.className("comment_wrap"));

            reviews = new Review[elements.size()];

            for(int i = 0; i < reviews.length; i++) {
                reviews[i] = new Review(elements.get(i).findElement(By.className("comment")).findElement(By.className("txt")).getText(),
                        elements.get(i).findElement(By.className("date")).getText(),
                        elements.get(i).findElement(By.className("id")).getText());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.close();
        }
    }

    public void printReviews() {
        for(int i = 0; i < reviews.length; i++) {
            System.out.println(reviews[i].id + "\t" + reviews[i].date + "\n" + reviews[i].comment);
            System.out.println("------------------------------------------------------------------");
        }
    }
}
class Review{
	public String comment;
	public String id;
	public String date;
	public Review(String comment, String date, String id) {
		this.comment = comment;
		this.id = id;
		this.date = date;
	}
}
