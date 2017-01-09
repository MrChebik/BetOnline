import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.*;


/**
 * Created by mrchebik on 27.12.16.
 */
public class Bet {
    private static WebDriver driver;
    private static ChromeOptions options;

    private static Timer timer1hour = new Timer();
    private static Timer timerStake = new Timer();

    public static void main(String[] args) throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");

        options = new ChromeOptions();
        options.addArguments("user-data-dir=/home/mrchebik/.config/google-chrome/Default");

        makeStake();
    }

    private static void makeStake() throws InterruptedException {
        int idMatch = 6964157;
        int betAmount = 15;

        driver = new ChromeDriver(options);
        driver.get("https://www.fonbet.com/live/?locale=ru#" + idMatch);

        Thread.sleep(3000);

        try {
            driver.findElement(By.id("event" + idMatch + "win1")).click();
        } catch (java.util.NoSuchElementException | IllegalArgumentException | ElementNotVisibleException e) {
            System.out.println("Something wrong, check available bet");
            System.exit(0);
        }

        WebElement element = driver.findElement(By.id("couponNewSumEdit"));
        try {
            element.clear();
            element.sendKeys(String.valueOf(betAmount));
        } catch (InvalidElementStateException ignored) {
        }

        try {
            driver.findElement(By.className("newCouponFooter")).click();
        } catch (ElementNotVisibleException | NoSuchElementException ignored) {
        }

        try {
            driver.findElement(By.className("buttonMakeStake")).click();
            timerStake.schedule(new TimerTask() {
                @Override
                public void run() {
                    driver.quit();
                    timer1hour.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            try {
                                makeStake();
                            } catch (InterruptedException ignored) {
                            }
                        }
                    }, 12960000);
                }
            }, 7500);
        } catch (NoSuchElementException ignored) {
        }
    }
}
