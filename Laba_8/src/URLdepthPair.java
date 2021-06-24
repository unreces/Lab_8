import java.net.*;
import java.util.Objects;

/*      Ничего не изменилось по сравнению с 7ой лабой
 */

public class URLdepthPair {
    public static final String URL_PREFIX = "http://";

    String url;
    int depth;

    public URLdepthPair(String url, int depth){
        this.url = url;
        this.depth = depth;
    }


    public String getHost() throws MalformedURLException{
        return new URL(url).getHost();
    }
    public String getPath() throws MalformedURLException{
        return new URL(url).getPath();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        URLdepthPair that = (URLdepthPair) o;
        return Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return url.hashCode();
    }
}
