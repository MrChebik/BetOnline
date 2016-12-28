import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by mrchebik on 27.12.16.
 */
public class Bet {
    private static WebDriver driver;

    private static Timer timer = new Timer(true);
    private static Timer timer1hour = new Timer();
    private static Timer timerStake = new Timer();

    public static void main(String[] args) throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");

        makeStake();
    }

    private static void makeStake() throws InterruptedException {
        int idMatch = 6913742;

        driver = new ChromeDriver();
        driver.get("https://www.fonbet.com/live/?locale=ru#" + idMatch);

        Thread.sleep(3000);

        driver.findElement(By.id("editLogin")).sendKeys("3047422");
        driver.findElement(By.id("editPassword")).sendKeys("h5Prh68G");
        driver.findElement(By.id("loginButtonLogin")).click();

        Thread.sleep(1000);

        try {
            driver.findElement(By.id("event" + idMatch + "win1")).click();
        } catch (IllegalArgumentException e) {
            System.out.println("Something wrong, check available bet");
            System.exit(0);
        }

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    driver.findElement(By.id("couponNewSumMinLabel")).click();
                } catch (InvalidElementStateException ignored) {
                }

                List<WebElement> agreeElements = driver.findElements(By.className("stakeContentWarning"));
                for (WebElement element1 : agreeElements) {
                    if (element1.getCssValue("display").equals("block")) {
                        try {
                            element1.findElement(By.className("buttonAgree")).click();
                        } catch (ElementNotVisibleException | NoSuchElementException ignored) {
                        }
                    }
                }

                WebElement makeStake = driver.findElement(By.className("buttonMakeStake"));
                if (makeStake.getCssValue("background-color").equals("rgba(253, 205, 75, 1)")) {
                    makeStake.click();
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
                            }, 3600000);
                        }
                    }, 7500);
                }
            }
        }, 3000);
    }
}
