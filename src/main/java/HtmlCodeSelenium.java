import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.List;


class HtmlCodeSelenium {
    private static final Object waitObject = new Object();
    private final String fileLocation = System.getProperty("user.dir");
    private final String page = "http://yobit.net/";
    private ArrayList<String> htmlCode = new ArrayList<>();
    private int maxRow = 0;

    public int synMaxRow() throws InterruptedException {
        synchronized (waitObject) {
            waitObject.notifyAll();
            while (maxRow == 0) {
                waitObject.wait();
            }
        }
        return maxRow;
    }

    public void parsPage() throws InterruptedException {
        int pix = 0;
        String service = fileLocation + "\\chromedriver.exe";
        System.setProperty("webdriver.chrome.driver", service);
        WebDriver driver = new ChromeDriver();
        try {
            driver.get(page);
            Thread.sleep(10000);
            boolean good = false;
            String currency = driver.findElement(By.className("c_1")).getText();
            int num = 0;
            while (!good) {
                WebElement table = driver.findElement(By.xpath("//*[@id='trade_market']"));
                List<WebElement> rows = table.findElement(By.xpath("tbody")).findElements(By.xpath("tr"));
                for (; num <= rows.size(); num++) {
                    try {
                        rows.get(num).click();
                    } catch (Exception noElement) {
                        pix = pix - 10;
                        scrolling(driver,pix);
                        break;
                    }

                    while (true) {
                        Thread.sleep(500);
                        try {
                            String newCurrency = driver.findElement(By.className("c_1")).getText();
                            if (!newCurrency.equals(currency)) {
                                break;
                            }
                        }catch (org.openqa.selenium.StaleElementReferenceException | org.openqa.selenium.NoSuchElementException  excep){ }
                    }


                    currency = driver.findElement(By.className("c_1")).getText();
                    htmlCode.add(driver.getPageSource());
                    maxRow = rows.size();
                    synMaxRow();
                    if (rows.size() - 1 == num) {
                        good = true;
                    }

                }
            }
        }finally {
            driver.close();
        }
    }

    private void scrolling(WebDriver driver, int pix) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("document.getElementById('market_base_list').style.top = '" +Integer.toString(pix)+"px';");

    }

    public synchronized ArrayList<String> getHtmlCode() {
        return htmlCode;
    }
}
