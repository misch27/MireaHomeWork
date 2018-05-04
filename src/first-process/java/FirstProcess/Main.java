package FirstProcess;

public class Main {
    public static void main(String[] args) {
        try {
            HtmlCodeSelenium sel = new HtmlCodeSelenium();
            ThreadWriteToJSON thJSON = new ThreadWriteToJSON();

            for(int i=0; i<3; i++){
                ThreadPars thPars = new ThreadPars(i,sel, thJSON);
                thPars.start();
            }
            thJSON.start();
            sel.parsPage();

        } catch (InterruptedException | org.openqa.selenium.WebDriverException e) {
            System.exit(-1);
        }
    }
}
