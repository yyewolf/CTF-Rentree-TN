package org.fmod;

import android.media.AudioRecord;
import android.util.Log;
import java.nio.ByteBuffer;

/* renamed from: org.fmod.a */
final class C0095a implements Runnable {

    /* renamed from: a */
    private final FMODAudioDevice f310a;

    /* renamed from: b */
    private final ByteBuffer f311b;

    /* renamed from: c */
    private final int f312c;

    /* renamed from: d */
    private final int f313d;

    /* renamed from: e */
    private final int f314e = 2;

    /* renamed from: f */
    private volatile Thread f315f;

    /* renamed from: g */
    private volatile boolean f316g;

    /* renamed from: h */
    private AudioRecord f317h;

    /* renamed from: i */
    private boolean f318i;

    C0095a(FMODAudioDevice fMODAudioDevice, int i, int i2) {
        this.f310a = fMODAudioDevice;
        this.f312c = i;
        this.f313d = i2;
        this.f311b = ByteBuffer.allocateDirect(AudioRecord.getMinBufferSize(i, i2, 2));
    }

    /* renamed from: d */
    private void m173d() {
        AudioRecord audioRecord = this.f317h;
        if (audioRecord != null) {
            if (audioRecord.getState() == 1) {
                this.f317h.stop();
            }
            this.f317h.release();
            this.f317h = null;
        }
        this.f311b.position(0);
        this.f318i = false;
    }

    /* renamed from: a */
    public final int mo306a() {
        return this.f311b.capacity();
    }

    /* renamed from: b */
    public final void mo307b() {
        if (this.f315f != null) {
            mo308c();
        }
        this.f316g = true;
        this.f315f = new Thread(this);
        this.f315f.start();
    }

    /* renamed from: c */
    public final void mo308c() {
        while (this.f315f != null) {
            this.f316g = false;
            try {
                this.f315f.join();
                this.f315f = null;
            } catch (InterruptedException unused) {
            }
        }
    }

    public final void run() {
        int i = 3;
        while (this.f316g) {
            if (!this.f318i && i > 0) {
                m173d();
                AudioRecord audioRecord = new AudioRecord(1, this.f312c, this.f313d, this.f314e, this.f311b.capacity());
                this.f317h = audioRecord;
                int state = audioRecord.getState();
                boolean z = true;
                if (state != 1) {
                    z = false;
                }
                this.f318i = z;
                if (z) {
                    this.f311b.position(0);
                    this.f317h.startRecording();
                    i = 3;
                } else {
                    Log.e("FMOD", "AudioRecord failed to initialize (status " + this.f317h.getState() + ")");
                    i += -1;
                    m173d();
                }
            }
            if (this.f318i && this.f317h.getRecordingState() == 3) {
                AudioRecord audioRecord2 = this.f317h;
                ByteBuffer byteBuffer = this.f311b;
                this.f310a.fmodProcessMicData(this.f311b, audioRecord2.read(byteBuffer, byteBuffer.capacity()));
                this.f311b.position(0);
            }
        }
        m173d();
    }
}
