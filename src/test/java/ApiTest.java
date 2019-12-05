import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import static org.junit.Assert.*;

public class ApiTest {
    private String connectStr = "192.168.6.122:2181,192.168.6.122:2183";
    private int sessionTimeout = 20000;
    private ZooKeeper zkClient;

    @Before
    public void connect() throws IOException {
        zkClient = new ZooKeeper(connectStr, sessionTimeout, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                System.out.println(watchedEvent.getState());
            }
        });
    }

    @Test
    public void createNode() throws UnsupportedEncodingException, KeeperException, InterruptedException {
        String node = zkClient.create("/Apps", "app".getBytes("utf-8"), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println(node);
    }

    /**
     * 获取某个节点的值
     * @throws KeeperException
     * @throws InterruptedException
     */
    @Test
    public void getData() throws KeeperException, InterruptedException {
        byte[] data = zkClient.getData("/qqqq", null, null);
        System.out.println(new String(data));
    }

    /**
     * 获取某个节点下面的子节点
     * @throws KeeperException
     * @throws InterruptedException
     */
    @Test
    public void getNodes() throws KeeperException, InterruptedException {
        List<String> children = zkClient.getChildren("/qqqq", false);
        for (String child : children) {
            System.out.println(child);
        }
    }

    /**
     * 判断某节点是否存在
     * @throws KeeperException
     * @throws InterruptedException
     */
    @Test
    public void exits() throws KeeperException, InterruptedException {
        Stat stat = zkClient.exists("/qqqq/wocao", false);

        if (null==stat) {
            System.out.println("not exists");
            return;
        }
        System.out.println("node exists");
    }

}