
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/*      Перенесли большую часть кода по обработке полученных html-страниц в
    класс CrawlerTask чтобы реализовать интерфейс Runnable и создать несколько
    экземпляров класса на каждый поток
 */

public class CrawlerTask implements Runnable{
    URLpool urlPool;
    public static final String URL_PREFIX = "http://";

    public CrawlerTask(URLpool urlPool){
        this.urlPool = urlPool;
    }

    // выполнение http GET запроса с нужной ссылкой
    public static void request(PrintWriter out, URLdepthPair url) throws IOException {
        out.println("GET " + url.getPath() + " HTTP/1.1");
        out.println("Host: " + url.getHost());
        out.println("Connection: close");
        out.println();
        out.flush();
    }

    @Override
    public void run() {
        while(true){
            URLdepthPair currentPair = urlPool.getPair();
            try {
                Socket socket = new Socket(currentPair.getHost(), 80);
                socket.setSoTimeout(1000);
                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    request(out, currentPair);
                    String line = in.readLine();
                    while (line != null) {
                        if (line.contains(URL_PREFIX)){
                            String url;

                            Pattern pattern = Pattern.compile("http://[\\w_-]+(\\.[\\w_-]+)([\\w.,@?^=%&:/~+#-]*[\\w@?^=%&/~+#-])?");
                            Matcher matcher = pattern.matcher(line);

                            while(matcher.find()){
                                url = line.substring(matcher.start(), matcher.end());
                                URLdepthPair newPair = new URLdepthPair(url, currentPair.depth+1);
                                urlPool.addPair(newPair);
                            }
                        }
                        line = in.readLine();
                    }
                    socket.close();
                } catch (Exception e) {
                    socket.close();
                }

            } catch (IOException e) {

            }
        }
    }
}
