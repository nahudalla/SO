package p4.e7_Readers_Writers;

import p4.e5.Semaphore;

public class RWLock {
    private Semaphore rw_mutex = new Semaphore(1);
    private Semaphore mutex = new Semaphore(1);
    private long cant_readers = 0;

    public void acquire_write() {
        this.rw_mutex.wait_a();
    }

    public void release_write() {
        this.rw_mutex.signal_a();
    }

    public void acquire_read() {
        this.mutex.wait_a();
        this.cant_readers++;
        if(this.cant_readers == 1)
            this.rw_mutex.wait_a();
        this.mutex.signal_a();
    }

    public void release_read() {
        this.mutex.wait_a();
        this.cant_readers--;
        if(this.cant_readers <= 0)
            this.rw_mutex.signal_a();
        this.mutex.signal_a();
    }
}
