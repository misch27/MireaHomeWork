
public class Main {
    public static void main(String[] args) {
        try {

            HtmlCodeSelenium sel = new HtmlCodeSelenium();

            for(int i=0; i<3; i++){
                ThreadPars thPars = new ThreadPars(i,sel);
                thPars.start();
            }
            sel.parsPage();


        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
