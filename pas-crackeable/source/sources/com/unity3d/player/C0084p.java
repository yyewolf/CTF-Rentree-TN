package com.unity3d.player;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.MediaController;

/* renamed from: com.unity3d.player.p */
public final class C0084p extends FrameLayout implements MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnVideoSizeChangedListener, SurfaceHolder.Callback, MediaController.MediaPlayerControl {
    /* access modifiers changed from: private */

    /* renamed from: a */
    public static boolean f248a = false;

    /* renamed from: b */
    private final Context f249b;

    /* renamed from: c */
    private final SurfaceView f250c;

    /* renamed from: d */
    private final SurfaceHolder f251d;

    /* renamed from: e */
    private final String f252e;

    /* renamed from: f */
    private final int f253f;

    /* renamed from: g */
    private final int f254g;

    /* renamed from: h */
    private final boolean f255h;

    /* renamed from: i */
    private final long f256i;

    /* renamed from: j */
    private final long f257j;

    /* renamed from: k */
    private final FrameLayout f258k;

    /* renamed from: l */
    private final Display f259l;

    /* renamed from: m */
    private int f260m;

    /* renamed from: n */
    private int f261n;

    /* renamed from: o */
    private int f262o;

    /* renamed from: p */
    private int f263p;

    /* renamed from: q */
    private MediaPlayer f264q;

    /* renamed from: r */
    private MediaController f265r;

    /* renamed from: s */
    private boolean f266s = false;

    /* renamed from: t */
    private boolean f267t = false;

    /* renamed from: u */
    private int f268u = 0;

    /* renamed from: v */
    private boolean f269v = false;

    /* renamed from: w */
    private boolean f270w = false;

    /* renamed from: x */
    private C0085a f271x;

    /* renamed from: y */
    private C0086b f272y;

    /* renamed from: z */
    private volatile int f273z = 0;

    /* renamed from: com.unity3d.player.p$a */
    public interface C0085a {
        /* renamed from: a */
        void mo285a(int i);
    }

    /* renamed from: com.unity3d.player.p$b */
    public class C0086b implements Runnable {

        /* renamed from: b */
        private C0084p f275b;

        /* renamed from: c */
        private boolean f276c = false;

        public C0086b(C0084p pVar) {
            this.f275b = pVar;
        }

        /* renamed from: a */
        public final void mo286a() {
            this.f276c = true;
        }

        public final void run() {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException unused) {
                Thread.currentThread().interrupt();
            }
            if (!this.f276c) {
                if (C0084p.f248a) {
                    C0084p.m149b("Stopping the video player due to timeout.");
                }
                this.f275b.CancelOnPrepare();
            }
        }
    }

    protected C0084p(Context context, String str, int i, int i2, int i3, boolean z, long j, long j2, C0085a aVar) {
        super(context);
        this.f271x = aVar;
        this.f249b = context;
        this.f258k = this;
        SurfaceView surfaceView = new SurfaceView(context);
        this.f250c = surfaceView;
        SurfaceHolder holder = surfaceView.getHolder();
        this.f251d = holder;
        holder.addCallback(this);
        this.f258k.setBackgroundColor(i);
        this.f258k.addView(this.f250c);
        this.f259l = ((WindowManager) this.f249b.getSystemService("window")).getDefaultDisplay();
        this.f252e = str;
        this.f253f = i2;
        this.f254g = i3;
        this.f255h = z;
        this.f256i = j;
        this.f257j = j2;
        if (f248a) {
            m149b("fileName: " + this.f252e);
        }
        if (f248a) {
            m149b("backgroundColor: " + i);
        }
        if (f248a) {
            m149b("controlMode: " + this.f253f);
        }
        if (f248a) {
            m149b("scalingMode: " + this.f254g);
        }
        if (f248a) {
            m149b("isURL: " + this.f255h);
        }
        if (f248a) {
            m149b("videoOffset: " + this.f256i);
        }
        if (f248a) {
            m149b("videoLength: " + this.f257j);
        }
        setFocusable(true);
        setFocusableInTouchMode(true);
    }

    /* renamed from: a */
    private void m147a(int i) {
        this.f273z = i;
        C0085a aVar = this.f271x;
        if (aVar != null) {
            aVar.mo285a(this.f273z);
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: b */
    public static void m149b(String str) {
        Log.i("Video", "VideoPlayer: " + str);
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:17|18|19|20|21) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:20:0x007d */
    /* renamed from: c */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void m151c() {
        /*
            r8 = this;
            android.media.MediaPlayer r0 = r8.f264q
            if (r0 == 0) goto L_0x001c
            android.view.SurfaceHolder r1 = r8.f251d
            r0.setDisplay(r1)
            boolean r0 = r8.f269v
            if (r0 != 0) goto L_0x001b
            boolean r0 = f248a
            if (r0 == 0) goto L_0x0016
            java.lang.String r0 = "Resuming playback"
            m149b(r0)
        L_0x0016:
            android.media.MediaPlayer r0 = r8.f264q
            r0.start()
        L_0x001b:
            return
        L_0x001c:
            r0 = 0
            r8.m147a((int) r0)
            r8.doCleanUp()
            android.media.MediaPlayer r0 = new android.media.MediaPlayer     // Catch:{ Exception -> 0x00cc }
            r0.<init>()     // Catch:{ Exception -> 0x00cc }
            r8.f264q = r0     // Catch:{ Exception -> 0x00cc }
            boolean r1 = r8.f255h     // Catch:{ Exception -> 0x00cc }
            if (r1 == 0) goto L_0x003a
            android.content.Context r1 = r8.f249b     // Catch:{ Exception -> 0x00cc }
            java.lang.String r2 = r8.f252e     // Catch:{ Exception -> 0x00cc }
            android.net.Uri r2 = android.net.Uri.parse(r2)     // Catch:{ Exception -> 0x00cc }
            r0.setDataSource(r1, r2)     // Catch:{ Exception -> 0x00cc }
            goto L_0x008e
        L_0x003a:
            long r0 = r8.f257j     // Catch:{ Exception -> 0x00cc }
            r2 = 0
            int r4 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r4 == 0) goto L_0x005a
            java.io.FileInputStream r0 = new java.io.FileInputStream     // Catch:{ Exception -> 0x00cc }
            java.lang.String r1 = r8.f252e     // Catch:{ Exception -> 0x00cc }
            r0.<init>(r1)     // Catch:{ Exception -> 0x00cc }
            android.media.MediaPlayer r2 = r8.f264q     // Catch:{ Exception -> 0x00cc }
            java.io.FileDescriptor r3 = r0.getFD()     // Catch:{ Exception -> 0x00cc }
            long r4 = r8.f256i     // Catch:{ Exception -> 0x00cc }
            long r6 = r8.f257j     // Catch:{ Exception -> 0x00cc }
            r2.setDataSource(r3, r4, r6)     // Catch:{ Exception -> 0x00cc }
        L_0x0056:
            r0.close()     // Catch:{ Exception -> 0x00cc }
            goto L_0x008e
        L_0x005a:
            android.content.res.Resources r0 = r8.getResources()     // Catch:{ Exception -> 0x00cc }
            android.content.res.AssetManager r0 = r0.getAssets()     // Catch:{ Exception -> 0x00cc }
            java.lang.String r1 = r8.f252e     // Catch:{ IOException -> 0x007d }
            android.content.res.AssetFileDescriptor r0 = r0.openFd(r1)     // Catch:{ IOException -> 0x007d }
            android.media.MediaPlayer r1 = r8.f264q     // Catch:{ IOException -> 0x007d }
            java.io.FileDescriptor r2 = r0.getFileDescriptor()     // Catch:{ IOException -> 0x007d }
            long r3 = r0.getStartOffset()     // Catch:{ IOException -> 0x007d }
            long r5 = r0.getLength()     // Catch:{ IOException -> 0x007d }
            r1.setDataSource(r2, r3, r5)     // Catch:{ IOException -> 0x007d }
            r0.close()     // Catch:{ IOException -> 0x007d }
            goto L_0x008e
        L_0x007d:
            java.io.FileInputStream r0 = new java.io.FileInputStream     // Catch:{ Exception -> 0x00cc }
            java.lang.String r1 = r8.f252e     // Catch:{ Exception -> 0x00cc }
            r0.<init>(r1)     // Catch:{ Exception -> 0x00cc }
            android.media.MediaPlayer r1 = r8.f264q     // Catch:{ Exception -> 0x00cc }
            java.io.FileDescriptor r2 = r0.getFD()     // Catch:{ Exception -> 0x00cc }
            r1.setDataSource(r2)     // Catch:{ Exception -> 0x00cc }
            goto L_0x0056
        L_0x008e:
            android.media.MediaPlayer r0 = r8.f264q     // Catch:{ Exception -> 0x00cc }
            android.view.SurfaceHolder r1 = r8.f251d     // Catch:{ Exception -> 0x00cc }
            r0.setDisplay(r1)     // Catch:{ Exception -> 0x00cc }
            android.media.MediaPlayer r0 = r8.f264q     // Catch:{ Exception -> 0x00cc }
            r1 = 1
            r0.setScreenOnWhilePlaying(r1)     // Catch:{ Exception -> 0x00cc }
            android.media.MediaPlayer r0 = r8.f264q     // Catch:{ Exception -> 0x00cc }
            r0.setOnBufferingUpdateListener(r8)     // Catch:{ Exception -> 0x00cc }
            android.media.MediaPlayer r0 = r8.f264q     // Catch:{ Exception -> 0x00cc }
            r0.setOnCompletionListener(r8)     // Catch:{ Exception -> 0x00cc }
            android.media.MediaPlayer r0 = r8.f264q     // Catch:{ Exception -> 0x00cc }
            r0.setOnPreparedListener(r8)     // Catch:{ Exception -> 0x00cc }
            android.media.MediaPlayer r0 = r8.f264q     // Catch:{ Exception -> 0x00cc }
            r0.setOnVideoSizeChangedListener(r8)     // Catch:{ Exception -> 0x00cc }
            android.media.MediaPlayer r0 = r8.f264q     // Catch:{ Exception -> 0x00cc }
            r1 = 3
            r0.setAudioStreamType(r1)     // Catch:{ Exception -> 0x00cc }
            android.media.MediaPlayer r0 = r8.f264q     // Catch:{ Exception -> 0x00cc }
            r0.prepareAsync()     // Catch:{ Exception -> 0x00cc }
            com.unity3d.player.p$b r0 = new com.unity3d.player.p$b     // Catch:{ Exception -> 0x00cc }
            r0.<init>(r8)     // Catch:{ Exception -> 0x00cc }
            r8.f272y = r0     // Catch:{ Exception -> 0x00cc }
            java.lang.Thread r0 = new java.lang.Thread     // Catch:{ Exception -> 0x00cc }
            com.unity3d.player.p$b r1 = r8.f272y     // Catch:{ Exception -> 0x00cc }
            r0.<init>(r1)     // Catch:{ Exception -> 0x00cc }
            r0.start()     // Catch:{ Exception -> 0x00cc }
            return
        L_0x00cc:
            r0 = move-exception
            boolean r1 = f248a
            if (r1 == 0) goto L_0x00e9
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r2 = "error: "
            r1.<init>(r2)
            java.lang.String r2 = r0.getMessage()
            r1.append(r2)
            r1.append(r0)
            java.lang.String r0 = r1.toString()
            m149b(r0)
        L_0x00e9:
            r0 = 2
            r8.m147a((int) r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.unity3d.player.C0084p.m151c():void");
    }

    /* renamed from: d */
    private void m152d() {
        if (!isPlaying()) {
            m147a(1);
            if (f248a) {
                m149b("startVideoPlayback");
            }
            updateVideoLayout();
            if (!this.f269v) {
                start();
            }
        }
    }

    public final void CancelOnPrepare() {
        m147a(2);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: a */
    public final boolean mo261a() {
        return this.f269v;
    }

    public final boolean canPause() {
        return true;
    }

    public final boolean canSeekBackward() {
        return true;
    }

    public final boolean canSeekForward() {
        return true;
    }

    /* access modifiers changed from: protected */
    public final void destroyPlayer() {
        if (f248a) {
            m149b("destroyPlayer");
        }
        if (!this.f269v) {
            pause();
        }
        doCleanUp();
    }

    /* access modifiers changed from: protected */
    public final void doCleanUp() {
        C0086b bVar = this.f272y;
        if (bVar != null) {
            bVar.mo286a();
            this.f272y = null;
        }
        MediaPlayer mediaPlayer = this.f264q;
        if (mediaPlayer != null) {
            mediaPlayer.release();
            this.f264q = null;
        }
        this.f262o = 0;
        this.f263p = 0;
        this.f267t = false;
        this.f266s = false;
    }

    public final int getAudioSessionId() {
        MediaPlayer mediaPlayer = this.f264q;
        if (mediaPlayer == null) {
            return 0;
        }
        return mediaPlayer.getAudioSessionId();
    }

    public final int getBufferPercentage() {
        if (this.f255h) {
            return this.f268u;
        }
        return 100;
    }

    public final int getCurrentPosition() {
        MediaPlayer mediaPlayer = this.f264q;
        if (mediaPlayer == null) {
            return 0;
        }
        return mediaPlayer.getCurrentPosition();
    }

    public final int getDuration() {
        MediaPlayer mediaPlayer = this.f264q;
        if (mediaPlayer == null) {
            return 0;
        }
        return mediaPlayer.getDuration();
    }

    public final boolean isPlaying() {
        boolean z = this.f267t && this.f266s;
        MediaPlayer mediaPlayer = this.f264q;
        return mediaPlayer == null ? !z : mediaPlayer.isPlaying() || !z;
    }

    public final void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
        if (f248a) {
            m149b("onBufferingUpdate percent:" + i);
        }
        this.f268u = i;
    }

    public final void onCompletion(MediaPlayer mediaPlayer) {
        if (f248a) {
            m149b("onCompletion called");
        }
        destroyPlayer();
        m147a(3);
    }

    public final boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i == 4 || (this.f253f == 2 && i != 0 && !keyEvent.isSystem())) {
            destroyPlayer();
            m147a(3);
            return true;
        }
        MediaController mediaController = this.f265r;
        return mediaController != null ? mediaController.onKeyDown(i, keyEvent) : super.onKeyDown(i, keyEvent);
    }

    public final void onPrepared(MediaPlayer mediaPlayer) {
        if (f248a) {
            m149b("onPrepared called");
        }
        C0086b bVar = this.f272y;
        if (bVar != null) {
            bVar.mo286a();
            this.f272y = null;
        }
        int i = this.f253f;
        if (i == 0 || i == 1) {
            MediaController mediaController = new MediaController(this.f249b);
            this.f265r = mediaController;
            mediaController.setMediaPlayer(this);
            this.f265r.setAnchorView(this);
            this.f265r.setEnabled(true);
            Context context = this.f249b;
            if (context instanceof Activity) {
                this.f265r.setSystemUiVisibility(((Activity) context).getWindow().getDecorView().getSystemUiVisibility());
            }
            this.f265r.show();
        }
        this.f267t = true;
        if (1 != 0 && this.f266s) {
            m152d();
        }
    }

    public final boolean onTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction() & 255;
        if (this.f253f == 2 && action == 0) {
            destroyPlayer();
            m147a(3);
            return true;
        }
        MediaController mediaController = this.f265r;
        return mediaController != null ? mediaController.onTouchEvent(motionEvent) : super.onTouchEvent(motionEvent);
    }

    public final void onVideoSizeChanged(MediaPlayer mediaPlayer, int i, int i2) {
        if (f248a) {
            m149b("onVideoSizeChanged called " + i + "x" + i2);
        }
        if (i != 0 && i2 != 0) {
            this.f266s = true;
            this.f262o = i;
            this.f263p = i2;
            if (this.f267t && 1 != 0) {
                m152d();
            }
        } else if (f248a) {
            m149b("invalid video width(" + i + ") or height(" + i2 + ")");
        }
    }

    public final void pause() {
        MediaPlayer mediaPlayer = this.f264q;
        if (mediaPlayer != null) {
            if (this.f270w) {
                mediaPlayer.pause();
            }
            this.f269v = true;
        }
    }

    public final void seekTo(int i) {
        MediaPlayer mediaPlayer = this.f264q;
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(i);
        }
    }

    public final void start() {
        if (f248a) {
            m149b("Start");
        }
        MediaPlayer mediaPlayer = this.f264q;
        if (mediaPlayer != null) {
            if (this.f270w) {
                mediaPlayer.start();
            }
            this.f269v = false;
        }
    }

    public final void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
        if (f248a) {
            m149b("surfaceChanged called " + i + " " + i2 + "x" + i3);
        }
        if (this.f260m != i2 || this.f261n != i3) {
            this.f260m = i2;
            this.f261n = i3;
            if (this.f270w) {
                updateVideoLayout();
            }
        }
    }

    public final void surfaceCreated(SurfaceHolder surfaceHolder) {
        if (f248a) {
            m149b("surfaceCreated called");
        }
        this.f270w = true;
        m151c();
    }

    public final void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if (f248a) {
            m149b("surfaceDestroyed called");
        }
        this.f270w = false;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x004d, code lost:
        if (r5 <= r3) goto L_0x004f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0053, code lost:
        r0 = (int) (((float) r1) * r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x005d, code lost:
        if (r5 >= r3) goto L_0x004f;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void updateVideoLayout() {
        /*
            r8 = this;
            boolean r0 = f248a
            if (r0 == 0) goto L_0x0009
            java.lang.String r0 = "updateVideoLayout"
            m149b(r0)
        L_0x0009:
            android.media.MediaPlayer r0 = r8.f264q
            if (r0 != 0) goto L_0x000e
            return
        L_0x000e:
            int r0 = r8.f260m
            if (r0 == 0) goto L_0x0016
            int r0 = r8.f261n
            if (r0 != 0) goto L_0x0034
        L_0x0016:
            android.content.Context r0 = r8.f249b
            java.lang.String r1 = "window"
            java.lang.Object r0 = r0.getSystemService(r1)
            android.view.WindowManager r0 = (android.view.WindowManager) r0
            android.util.DisplayMetrics r1 = new android.util.DisplayMetrics
            r1.<init>()
            android.view.Display r0 = r0.getDefaultDisplay()
            r0.getMetrics(r1)
            int r0 = r1.widthPixels
            r8.f260m = r0
            int r0 = r1.heightPixels
            r8.f261n = r0
        L_0x0034:
            int r0 = r8.f260m
            int r1 = r8.f261n
            boolean r2 = r8.f266s
            if (r2 == 0) goto L_0x0065
            int r2 = r8.f262o
            float r3 = (float) r2
            int r4 = r8.f263p
            float r5 = (float) r4
            float r3 = r3 / r5
            float r5 = (float) r0
            float r6 = (float) r1
            float r5 = r5 / r6
            int r6 = r8.f254g
            r7 = 1
            if (r6 != r7) goto L_0x0058
            int r2 = (r5 > r3 ? 1 : (r5 == r3 ? 0 : -1))
            if (r2 > 0) goto L_0x0053
        L_0x004f:
            float r1 = (float) r0
            float r1 = r1 / r3
            int r1 = (int) r1
            goto L_0x006e
        L_0x0053:
            float r0 = (float) r1
            float r0 = r0 * r3
            int r0 = (int) r0
            goto L_0x006e
        L_0x0058:
            r7 = 2
            if (r6 != r7) goto L_0x0060
            int r2 = (r5 > r3 ? 1 : (r5 == r3 ? 0 : -1))
            if (r2 < 0) goto L_0x0053
            goto L_0x004f
        L_0x0060:
            if (r6 != 0) goto L_0x006e
            r0 = r2
            r1 = r4
            goto L_0x006e
        L_0x0065:
            boolean r2 = f248a
            if (r2 == 0) goto L_0x006e
            java.lang.String r2 = "updateVideoLayout: Video size is not known yet"
            m149b(r2)
        L_0x006e:
            int r2 = r8.f260m
            if (r2 != r0) goto L_0x0076
            int r2 = r8.f261n
            if (r2 == r1) goto L_0x00a1
        L_0x0076:
            boolean r2 = f248a
            if (r2 == 0) goto L_0x0093
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            java.lang.String r3 = "frameWidth = "
            r2.<init>(r3)
            r2.append(r0)
            java.lang.String r3 = "; frameHeight = "
            r2.append(r3)
            r2.append(r1)
            java.lang.String r2 = r2.toString()
            m149b(r2)
        L_0x0093:
            android.widget.FrameLayout$LayoutParams r2 = new android.widget.FrameLayout$LayoutParams
            r3 = 17
            r2.<init>(r0, r1, r3)
            android.widget.FrameLayout r0 = r8.f258k
            android.view.SurfaceView r1 = r8.f250c
            r0.updateViewLayout(r1, r2)
        L_0x00a1:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.unity3d.player.C0084p.updateVideoLayout():void");
    }
}
