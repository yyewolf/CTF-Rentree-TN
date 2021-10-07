package com.unity3d.player;

import android.app.Activity;
import android.content.Context;
import com.unity3d.player.C0084p;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/* renamed from: com.unity3d.player.q */
final class C0087q {
    /* access modifiers changed from: private */

    /* renamed from: a */
    public UnityPlayer f277a = null;
    /* access modifiers changed from: private */

    /* renamed from: b */
    public Context f278b = null;

    /* renamed from: c */
    private C0094a f279c;
    /* access modifiers changed from: private */

    /* renamed from: d */
    public final Semaphore f280d = new Semaphore(0);
    /* access modifiers changed from: private */

    /* renamed from: e */
    public final Lock f281e = new ReentrantLock();
    /* access modifiers changed from: private */

    /* renamed from: f */
    public C0084p f282f = null;
    /* access modifiers changed from: private */

    /* renamed from: g */
    public int f283g = 2;

    /* renamed from: h */
    private boolean f284h = false;
    /* access modifiers changed from: private */

    /* renamed from: i */
    public boolean f285i = false;

    /* renamed from: com.unity3d.player.q$a */
    public interface C0094a {
        /* renamed from: a */
        void mo144a();
    }

    C0087q(UnityPlayer unityPlayer) {
        this.f277a = unityPlayer;
    }

    /* access modifiers changed from: private */
    /* renamed from: d */
    public void m162d() {
        C0084p pVar = this.f282f;
        if (pVar != null) {
            this.f277a.removeViewFromPlayer(pVar);
            this.f285i = false;
            this.f282f.destroyPlayer();
            this.f282f = null;
            C0094a aVar = this.f279c;
            if (aVar != null) {
                aVar.mo144a();
            }
        }
    }

    /* renamed from: a */
    public final void mo288a() {
        this.f281e.lock();
        C0084p pVar = this.f282f;
        if (pVar != null) {
            if (this.f283g == 0) {
                pVar.CancelOnPrepare();
            } else if (this.f285i) {
                boolean a = pVar.mo261a();
                this.f284h = a;
                if (!a) {
                    this.f282f.pause();
                }
            }
        }
        this.f281e.unlock();
    }

    /* renamed from: a */
    public final boolean mo289a(Context context, String str, int i, int i2, int i3, boolean z, long j, long j2, C0094a aVar) {
        this.f281e.lock();
        this.f279c = aVar;
        this.f278b = context;
        this.f280d.drainPermits();
        this.f283g = 2;
        final String str2 = str;
        final int i4 = i;
        final int i5 = i2;
        final int i6 = i3;
        final boolean z2 = z;
        final long j3 = j;
        final long j4 = j2;
        runOnUiThread(new Runnable() {
            public final void run() {
                if (C0087q.this.f282f != null) {
                    C0068g.Log(5, "Video already playing");
                    int unused = C0087q.this.f283g = 2;
                    C0087q.this.f280d.release();
                    return;
                }
                C0084p unused2 = C0087q.this.f282f = new C0084p(C0087q.this.f278b, str2, i4, i5, i6, z2, j3, j4, new C0084p.C0085a() {
                    /* renamed from: a */
                    public final void mo285a(int i) {
                        C0087q.this.f281e.lock();
                        int unused = C0087q.this.f283g = i;
                        if (i == 3 && C0087q.this.f285i) {
                            C0087q.this.runOnUiThread(new Runnable() {
                                public final void run() {
                                    C0087q.this.m162d();
                                    C0087q.this.f277a.resume();
                                }
                            });
                        }
                        if (i != 0) {
                            C0087q.this.f280d.release();
                        }
                        C0087q.this.f281e.unlock();
                    }
                });
                if (C0087q.this.f282f != null) {
                    C0087q.this.f277a.addView(C0087q.this.f282f);
                }
            }
        });
        boolean z3 = false;
        try {
            this.f281e.unlock();
            this.f280d.acquire();
            this.f281e.lock();
            if (this.f283g != 2) {
                z3 = true;
            }
        } catch (InterruptedException unused) {
        }
        runOnUiThread(new Runnable() {
            public final void run() {
                C0087q.this.f277a.pause();
            }
        });
        runOnUiThread((!z3 || this.f283g == 3) ? new Runnable() {
            public final void run() {
                C0087q.this.m162d();
                C0087q.this.f277a.resume();
            }
        } : new Runnable() {
            public final void run() {
                if (C0087q.this.f282f != null) {
                    C0087q.this.f277a.addViewToPlayer(C0087q.this.f282f, true);
                    boolean unused = C0087q.this.f285i = true;
                    C0087q.this.f282f.requestFocus();
                }
            }
        });
        this.f281e.unlock();
        return z3;
    }

    /* renamed from: b */
    public final void mo290b() {
        this.f281e.lock();
        C0084p pVar = this.f282f;
        if (pVar != null && this.f285i && !this.f284h) {
            pVar.start();
        }
        this.f281e.unlock();
    }

    /* renamed from: c */
    public final void mo291c() {
        this.f281e.lock();
        C0084p pVar = this.f282f;
        if (pVar != null) {
            pVar.updateVideoLayout();
        }
        this.f281e.unlock();
    }

    /* access modifiers changed from: protected */
    public final void runOnUiThread(Runnable runnable) {
        Context context = this.f278b;
        if (context instanceof Activity) {
            ((Activity) context).runOnUiThread(runnable);
        } else {
            C0068g.Log(5, "Not running from an Activity; Ignoring execution request...");
        }
    }
}
