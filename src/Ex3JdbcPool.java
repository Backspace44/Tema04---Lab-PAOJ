import java.sql.*;
import java.util.*;

class SimpleConnectionPool {
    private final List<Connection> pool = new ArrayList<>();
    private final int maxSize;

    public SimpleConnectionPool(String url, String user, String pass, int size) throws SQLException {
        this.maxSize = size;
        for (int i = 0; i < size; i++) {
            pool.add(DriverManager.getConnection(url, user, pass));
        }
    }

    public synchronized Connection getConnection() throws InterruptedException {
        while (pool.isEmpty()) wait();
        return pool.remove(0);
    }

    public synchronized void releaseConnection(Connection c) {
        pool.add(c);
        notifyAll();
    }
}

class JdbcWorker extends Thread {
    private final SimpleConnectionPool pool;

    public JdbcWorker(SimpleConnectionPool pool) {
        this.pool = pool;
    }

    @Override
    public void run() {
        Connection conn = null;
        try {
            conn = pool.getConnection();
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO log(message) VALUES(?)");
            stmt.setString(1, "Message from thread " + Thread.currentThread().getName());
            stmt.executeUpdate();
            stmt.close();
            Thread.sleep(100 + new Random().nextInt(400));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) pool.releaseConnection(conn);
        }
    }
}
