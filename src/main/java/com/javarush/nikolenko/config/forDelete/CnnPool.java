package com.javarush.nikolenko.config.forDelete;
import com.javarush.nikolenko.config.Cnn;
import lombok.SneakyThrows;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class CnnPool {
    public static final int SIZE = 10;
    private static final BlockingQueue<Connection> proxies = new ArrayBlockingQueue<>(SIZE);
    private static final List<Connection> realConnections = new ArrayList<>(SIZE);
    private static final Cnn cnn = new Cnn();

    private static void init() {
        for (int i = 0; i < SIZE; i++) {
            Connection realConnection = cnn.get();
            realConnections.add(realConnection);

            //catch close() method by proxy and add connection back to the queue or execute the method
            Object newProxyInstance = Proxy.newProxyInstance(CnnPool.class.getClassLoader(),
                    new Class[]{Connection.class},
                    (proxy, method, args) -> {
                        if("close".equals(method.getName())) {
                            proxies.put((Connection) proxy);
                            return true;
                        } else {
                            return method.invoke(realConnection, args);
                        }
                    }
            );

            proxies.add((Connection) newProxyInstance);
        }
    }

    @SneakyThrows
    public static Connection get() {
        if(realConnections.isEmpty()) {
            synchronized(realConnections){
                if(realConnections.isEmpty()) {
                    synchronized(realConnections){
                        init();
                    }
                }
            }
        }
        return proxies.take();
    }

    public static void destroy() {
        realConnections.forEach(connection -> {
            try {
                if(connection.getAutoCommit()) connection.rollback();
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
