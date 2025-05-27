# Tema04---Lab-PAOJ
The full solution to the tasks listed in the second assignment of the Java Laboratory.

# Topic 4 – Advanced Object-Oriented Programming in Java

## 1. Concurrent Task Manager with Monitoring and Timeout

### Context
Simulate a task manager that runs tasks in parallel, each with a specified execution time. Each task should be monitored with a timeout capability, and its status should be periodically displayed by a watchdog thread.

### Requirements
- Implement each task as a class that implements `Runnable`, taking an ID and a simulated duration in milliseconds.
- On startup, print that the task has started and execute the logic inside a synchronized section using a common monitor object.
- After execution, if the task was not interrupted, print that the task has completed and update its status in a `ConcurrentHashMap<Integer, Status>`.
- If the maximum execution time (`Tmax`) is exceeded, interrupt the task and mark it as `TIMED_OUT`.
- Start a pool of `N` threads and manually apply the timeout logic to each task.
- Implement a watchdog thread that prints the status of all tasks every 500 ms in a tabular format.
- Use `volatile` and `interrupt()` for managing timeouts; **do not** use `ExecutorService`.
- Mark the section where the status is set to `Status.COMPLETED` with `synchronized`.

---

## 2. Producer-Consumer Simulation with Intentional Deadlock

### Context
Simulate a producer-consumer system using two shared buffers and demonstrate how a deadlock can occur. Then, modify the code to resolve the deadlock.

### Requirements
- Declare two shared resources, `BufferA` and `BufferB`, both as synchronized lists.
- Create two producer threads (`P1` and `P2`) that lock resources in reverse order to intentionally cause a deadlock.
- Create two consumer threads (`C1` and `C2`) that read from both buffers, acquiring locks in varying orders.
- Demonstrate the deadlock occurrence with appropriate log messages.
- Resolve the deadlock by:
  - Enforcing a consistent lock acquisition order, or
  - Using `tryLock(timeout)` with `ReentrantLock`.
- Confirm via logs that the deadlock no longer occurs in the corrected version.

---

## 3. Concurrent JDBC Application with Simple Connection Pool

### Context
Deploy a multithreaded application that uses a custom connection pool to insert data into a PostgreSQL database.

### Requirements
- Create a `SimpleConnectionPool` class that pre-creates `M` connections and provides synchronized `getConnection()` and `releaseConnection(Connection)` methods using `wait()` and `notifyAll()`.
- Launch `K` worker threads that:
  - Get a connection,
  - Insert a message into the database,
  - Wait between 100–500 ms,
  - Release the connection.
- At the end, display how many records were inserted into the `Log` table.
- Handle all `SQLException`s and close resources inside `finally` blocks.
- Call a stored procedure using `CallableStatement` to delete entries older than one hour.

---

## 4. Simple P2P Chat using Sockets and Threads per Client

### Context
Build a simple client-server chat system using sockets, where each client is handled by a separate thread.

### Requirements
- Open a `ServerSocket` on port `8888`; for each client connection, start a `ClientHandler` in a new thread.
- Synchronize access to the active client list for safe concurrent access.
- On the client side, run two threads:
  - One for reading user input,
  - One for listening to messages from the server.
- Implement the `/quit` command to allow a client to disconnect.
- Implement the `/shutdown` command, allowed only for an admin client, that shuts down all clients and the server.
- Use `ThreadLocal<Socket>` in the handler to track each client's current socket.

---

## Notes

- Each exercise must be implemented in a **separate file or folder**.
- All code must **compile**.
- Code should be **well-structured**, readable, properly **indented**, and use **descriptive names**.

