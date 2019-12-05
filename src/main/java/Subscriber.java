import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Subscriber {
    private ZooKeeper zkCli;
    private String connectStr = "192.168.6.122:2181,192.168.6.122:2183";
    private int sessionTimeout = 20000;

    /**
     * 创建号连接
     * @throws IOException
     */
    public void getConnect() throws IOException {
        zkCli = new ZooKeeper(connectStr, sessionTimeout, watchedEvent -> {
            System.out.println(watchedEvent.getState());
        });
    }

    /**
     * 订阅服务
     * @throws KeeperException
     * @throws InterruptedException
     */
    public void getChildren() throws KeeperException, InterruptedException {
        List<String> children = zkCli.getChildren("/Services/ServerA", new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                try {
                    //循环监听
                    getChildren();
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        System.out.println(children);

        List<String> hostList = new ArrayList<>(6);
        for (String child : children) {
            byte[] data = zkCli.getData("/Services/ServerA/" + child, false, null);
            hostList.add(new String(data));
        }
        System.out.println(hostList);
    }

    public static void main(String[] args) {
        Subscriber subscriber = new Subscriber();
        try {
            subscriber.getConnect();
            subscriber.getChildren();
            Thread.sleep(30000000000L);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }
}