package org.fmod;

import android.media.AudioTrack;
import android.util.Log;
import java.nio.ByteBuffer;

public class FMODAudioDevice implements Runnable {

    /* renamed from: h */
    private static int f299h = 0;

    /* renamed from: i */
    private static int f300i = 1;

    /* renamed from: j */
    private static int f301j = 2;

    /* renamed from: k */
    private static int f302k = 3;

    /* renamed from: a */
    private volatile Thread f303a = null;

    /* renamed from: b */
    private volatile boolean f304b = false;

    /* renamed from: c */
    private AudioTrack f305c = null;

    /* renamed from: d */
    private boolean f306d = false;

    /* renamed from: e */
    private ByteBuffer f307e = null;

    /* renamed from: f */
    private byte[] f308f = null;

    /* renamed from: g */
    private volatile C0095a f309g;

    private native int fmodGetInfo(int i);

    private native int fmodProcess(ByteBuffer byteBuffer);

    private void releaseAudioTrack() {
        AudioTrack audioTrack = this.f305c;
        if (audioTrack != null) {
            if (audioTrack.getState() == 1) {
                this.f305c.stop();
            }
            this.f305c.release();
            this.f305c = null;
        }
        this.f307e = null;
        this.f308f = null;
        this.f306d = false;
    }

    public synchronized void close() {
        stop();
    }

    /* access modifiers changed from: package-private */
    public native int fmodProcessMicData(ByteBuffer byteBuffer, int i);

    public boolean isRunning() {
        return this.f303a != null && this.f303a.isAlive();
    }

    public void run() {
        int i = 3;
        while (this.f304b) {
            if (!this.f306d && i > 0) {
                releaseAudioTrack();
                int fmodGetInfo = fmodGetInfo(f299h);
                int round = Math.round(((float) AudioTrack.getMinBufferSize(fmodGetInfo, 12, 2)) * 1.1f) & -4;
                int fmodGetInfo2 = fmodGetInfo(f300i);
                int fmodGetInfo3 = fmodGetInfo(f301j) * fmodGetInfo2 * 4;
                AudioTrack audioTrack = new AudioTrack(3, fmodGetInfo, 12, 2, fmodGetInfo3 > round ? fmodGetInfo3 : round, 1);
                this.f305c = audioTrack;
                boolean z = audioTrack.getState() == 1;
                this.f306d = z;
                if (z) {
                    ByteBuffer allocateDirect = ByteBuffer.allocateDirect(fmodGetInfo2 * 2 * 2);
                    this.f307e = allocateDirect;
                    this.f308f = new byte[allocateDirect.capacity()];
                    this.f305c.play();
                    i = 3;
                } else {
                    Log.e("FMOD", "AudioTrack failed to initialize (status " + this.f305c.getState() + ")");
                    releaseAudioTrack();
                    i += -1;
                }
            }
            if (this.f306d) {
                if (fmodGetInfo(f302k) == 1) {
                    fmodProcess(this.f307e);
                    ByteBuffer byteBuffer = this.f307e;
                    byteBuffer.get(this.f308f, 0, byteBuffer.capacity());
                    this.f305c.write(this.f308f, 0, this.f307e.capacity());
                    this.f307e.position(0);
                } else {
                    releaseAudioTrack();
                }
            }
        }
        releaseAudioTrack();
    }

    public synchronized void start() {
        if (this.f303a != null) {
            stop();
        }
        this.f303a = new Thread(this, "FMODAudioDevice");
        this.f303a.setPriority(10);
        this.f304b = true;
        this.f303a.start();
        if (this.f309g != null) {
            this.f309g.mo307b();
        }
    }

    public synchronized int startAudioRecord(int i, int i2, int i3) {
        if (this.f309g == null) {
            this.f309g = new C0095a(this, i, i2);
            this.f309g.mo307b();
        }
        return this.f309g.mo306a();
    }

    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Missing exception handler attribute for start block: B:1:0x0001 */
    /* JADX WARNING: Removed duplicated region for block: B:1:0x0001 A[LOOP:0: B:1:0x0001->B:16:0x0001, LOOP_START, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void stop() {
        /*
            r1 = this;
            monitor-enter(r1)
        L_0x0001:
            java.lang.Thread r0 = r1.f303a     // Catch:{ all -> 0x001c }
            if (r0 == 0) goto L_0x0011
            r0 = 0
            r1.f304b = r0     // Catch:{ all -> 0x001c }
            java.lang.Thread r0 = r1.f303a     // Catch:{ InterruptedException -> 0x0001 }
            r0.join()     // Catch:{ InterruptedException -> 0x0001 }
            r0 = 0
            r1.f303a = r0     // Catch:{ InterruptedException -> 0x0001 }
            goto L_0x0001
        L_0x0011:
            org.fmod.a r0 = r1.f309g     // Catch:{ all -> 0x001c }
            if (r0 == 0) goto L_0x001a
            org.fmod.a r0 = r1.f309g     // Catch:{ all -> 0x001c }
            r0.mo308c()     // Catch:{ all -> 0x001c }
        L_0x001a:
            monitor-exit(r1)
            return
        L_0x001c:
            r0 = move-exception
            monitor-exit(r1)
            goto L_0x0020
        L_0x001f:
            throw r0
        L_0x0020:
            goto L_0x001f
        */
        throw new UnsupportedOperationException("Method not decompiled: org.fmod.FMODAudioDevice.stop():void");
    }

    public synchronized void stopAudioRecord() {
        if (this.f309g != null) {
            this.f309g.mo308c();
            this.f309g = null;
        }
    }
}
