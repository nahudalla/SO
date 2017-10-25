package p4.e5;

public class Semaphore {

    private static abstract class ThreadSem implements Runnable {
        protected final int i;
        protected final Semaphore semaphore;

        public ThreadSem(int i, Semaphore semaphore) {
            this.i = i;
            this.semaphore = semaphore;
        }
    }

    private static class ThreadSemA extends ThreadSem {
        public ThreadSemA(int i, Semaphore semaphore) {
            super(i, semaphore);
        }

        @Override
        public void run() {
            if(this.i < 3) {
                this.semaphore.wait_a();
                System.out.printf("Thread %d signaled\n", this.i);
            } else {
                this.semaphore.signal_a();
                System.out.printf("Thread %d sent signal\n", this.i);
            }
        }
    }

    private static class ThreadSemB extends ThreadSem {
        public ThreadSemB(int i, Semaphore semaphore) {
            super(i, semaphore);
        }

        @Override
        public void run() {
            if(this.i < 3) {
                boolean wait = this.semaphore.wait_b();
                if(wait) {
                    System.out.printf("Thread %d signaled\n", this.i);
                } else {
                    System.out.printf("Thread %d could not wait\n", this.i);
                }
            } else {
                this.semaphore.signal_b();
                System.out.printf("Thread %d sent signal\n", this.i);
            }
        }
    }

    public static void main(String[] args) {
        Thread threads[] = new Thread[5];
        final Semaphore semaphore = new Semaphore(2);

        for (int i = 0; i < 5; i++) {
            int finalI = i;
            Thread thread = new Thread(
                    new ThreadSemA(finalI, semaphore)
                    //new ThreadSemB(finalI, semaphore)
            );
            thread.start();
            threads[i] = thread;
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private int count;

    private final Object monitor = new Object();

    public Semaphore(int initCount) {
        this.count = initCount;
    }

    public synchronized void wait_a() {
        this.count--;
        if(this.count < 0) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                // Should not happen
                e.printStackTrace();
            }
        }
    }

    public synchronized void signal_a() {
        this.count++;
        if(this.count <= 0) {
            this.notify();
        }
    }

    public synchronized boolean wait_b() {
        if(this.count > 0) {
            this.count--;
            try {
                this.wait();
                return true;
            } catch (InterruptedException e) {
                // Should not happen
                this.count++;
                e.printStackTrace();
                return false;
            }
        }

        return false;
    }

    public synchronized void signal_b() {
        this.count++;
        this.notify();
    }
}
