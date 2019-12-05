import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

public class Publisher {
    private String connectStr = "192.168.6.122:2181,192.168.6.122:2183";
    private int sessionTimeout = 20000;
    private ZooKeeper zkCli;

    public void getConnect() throws IOException {
        zkCli = new ZooKeeper(connectStr, sessionTimeout, watchedEvent -> {
            System.out.println(watchedEvent.getState());
        });
    }

    public void register(String serverName,String hostName) throws KeeperException, InterruptedException {
        String node = zkCli.create("/Services/ServerA/" + serverName,hostName.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println(hostName + "服务上线");
    }

    public static void main(String[] args) {
        Publisher publisher = new Publisher();
        try {
            publisher.getConnect();
            publisher.register(args[0],args[1]);
            Thread.sleep(30000000000000000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}