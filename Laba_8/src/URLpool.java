import java.util.LinkedList;

public class URLpool {

    /*
            Перенесли списки проверенных и непроверенных сайтов в
        отдельный класс чтобы реализовать многопоточные методы доступа
        к ним
     */

    static LinkedList<URLdepthPair> checkedLinks = new LinkedList<>();
    static LinkedList<URLdepthPair> uncheckedLinks = new LinkedList<>();
    int maxDepth;
    // кол-во ожидающих потоков
    int cWaiting;

    public URLpool (int maxDepth){
        this.maxDepth = maxDepth;
        cWaiting = 0;
    }

    // synchronized - синхронизированная многопоточность - вид многопоточности,
    // в которой пока метод исполняется другим потоком, никакой другой поток не может
    // исполнять этот же метод
    public synchronized URLdepthPair getPair(){
        // пока список сайтов на очереди пуст, усыплять поток и увеличивать счетчик
        while (uncheckedLinks.isEmpty()){
            cWaiting++;
            try {
                wait();
            } catch (InterruptedException e){
                System.out.println("Ignoring InterruptedException");
            }
            // уменьшать счетчик, когда поток просыпается
            cWaiting--;
        }
        // возвращаем и удаляем из списка сайт
        return uncheckedLinks.removeFirst();
    }

    public synchronized void addPair(URLdepthPair pair){
        // проверка на наличие ссылки в списках и добавление нового сайта в списки
        if (!checkedLinks.contains(pair)){
            checkedLinks.add(pair);
            if (pair.depth < maxDepth){
                uncheckedLinks.add(pair);
                // уведомление о пополнении списка
                notify();
            }
        }
    }

    public int getWaiting() {
        return cWaiting;
    }
}
