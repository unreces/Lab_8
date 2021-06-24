import java.util.List;

public class Crawlers {
    public static boolean isNumeric(String str) {
        for (int i = 0; i < str.length(); i++) {
                if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static void printResult(List<URLdepthPair> list) {
        for (URLdepthPair pair : list) {
            System.out.println("Depth: " + pair.depth + " URL: " + pair.url);
        }
    }


    public static void main(String[] args) {

        // http://old.code.mu/tasks/advanced/php/parsing/praktika-po-parsingu-sajtov-initial.html
        // http://www.google.ru/
        // http://www.quizful.net/post/Java-RegExp

        args = new String[]{"   http://old.code.mu/tasks/advanced/php/parsing/praktika-po-parsingu-sajtov-initial.html", "3", "10"};

        // Проверка правильности вводимых аргументов
        if (args.length == 3 && isNumeric(args[1]) && isNumeric(args[2])) {
            String url = args[0];
            int threads = Integer.parseInt(args[2]);
            URLpool urlPool = new URLpool(Integer.parseInt(args[1]));
            urlPool.addPair(new URLdepthPair(url, 0));

            // Создаем нужное кол-во потоков и привязываем к ним CrawlerTask-и
            for (int i = 0; i < threads; i++) {
                CrawlerTask task = new CrawlerTask(urlPool);
                Thread thread = new Thread(task);
                thread.start();
            }

            // Не завершаем программу пока все потоки не уйдут в режим ожидания
            while (urlPool.getWaiting() != threads) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    System.out.println("Ignoring  InterruptedException");
                }
            }
            try {
                // Выводим результат
                printResult(urlPool.checkedLinks);
            }
            catch (NullPointerException e){
                System.out.println("Not Link");
            }
            // завершаем работу программы
            System.exit(0);
        }
        else{
            System.out.println("usage: java Crawler <URL> <maximum_depth> <num_threads> or second/third not digit");
        }
    }
}
