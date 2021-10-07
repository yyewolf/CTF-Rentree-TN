package com.unity3d.player;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.TypedValue;
import android.view.InputEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import com.unity3d.player.C0077l;
import com.unity3d.player.C0087q;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class UnityPlayer extends FrameLayout implements IUnityPlayerLifecycleEvents, C0067f {
    private static final int ANR_TIMEOUT_SECONDS = 4;
    private static final String ARCORE_ENABLE_METADATA_NAME = "unity.arcore-enable";
    private static final int RUN_STATE_CHANGED_MSG_CODE = 2269;
    private static final String SPLASH_ENABLE_METADATA_NAME = "unity.splash-enable";
    private static final String SPLASH_MODE_METADATA_NAME = "unity.splash-mode";
    public static Activity currentActivity;
    /* access modifiers changed from: private */
    public Context mContext;
    /* access modifiers changed from: private */
    public SurfaceView mGlView;
    /* access modifiers changed from: private */
    public int mInitialScreenOrientation;
    private boolean mIsFullscreen;
    private BroadcastReceiver mKillingIsMyBusiness;
    /* access modifiers changed from: private */
    public boolean mMainDisplayOverride;
    /* access modifiers changed from: private */
    public int mNaturalOrientation;
    private OrientationEventListener mOrientationListener;
    private boolean mProcessKillRequested;
    /* access modifiers changed from: private */
    public boolean mQuitting;
    C0072k mSoftInputDialog;
    private C0081n mState;
    /* access modifiers changed from: private */
    public C0087q mVideoPlayerProxy;
    private GoogleARCoreApi m_ARCoreApi;
    private boolean m_AddPhoneCallListener;
    private AudioVolumeHandler m_AudioVolumeHandler;
    private Camera2Wrapper m_Camera2Wrapper;
    private ClipboardManager m_ClipboardManager;
    private final ConcurrentLinkedQueue m_Events;
    private C0046a m_FakeListener;
    private HFPStatus m_HFPStatus;
    C0050e m_MainThread;
    private NetworkConnectivity m_NetworkConnectivity;
    private C0048c m_PhoneCallListener;
    /* access modifiers changed from: private */
    public C0077l m_SplashScreen;
    private TelephonyManager m_TelephonyManager;
    /* access modifiers changed from: private */
    public IUnityPlayerLifecycleEvents m_UnityPlayerLifecycleEvents;
    private Uri m_launchUri;

    /* renamed from: com.unity3d.player.UnityPlayer$a */
    class C0046a implements SensorEventListener {
        C0046a() {
        }

        public final void onAccuracyChanged(Sensor sensor, int i) {
        }

        public final void onSensorChanged(SensorEvent sensorEvent) {
        }
    }

    /* renamed from: com.unity3d.player.UnityPlayer$b */
    enum C0047b {
        ;

        static {
            f134d = new int[]{1, 2, 3};
        }
    }

    /* renamed from: com.unity3d.player.UnityPlayer$c */
    private class C0048c extends PhoneStateListener {
        private C0048c() {
        }

        /* synthetic */ C0048c(UnityPlayer unityPlayer, byte b) {
            this();
        }

        public final void onCallStateChanged(int i, String str) {
            UnityPlayer unityPlayer = UnityPlayer.this;
            boolean z = true;
            if (i != 1) {
                z = false;
            }
            unityPlayer.nativeMuteMasterAudio(z);
        }
    }

    /* renamed from: com.unity3d.player.UnityPlayer$d */
    enum C0049d {
        PAUSE,
        RESUME,
        QUIT,
        SURFACE_LOST,
        SURFACE_ACQUIRED,
        FOCUS_LOST,
        FOCUS_GAINED,
        NEXT_FRAME,
        URL_ACTIVATED,
        ORIENTATION_ANGLE_CHANGE
    }

    /* renamed from: com.unity3d.player.UnityPlayer$e */
    private class C0050e extends Thread {

        /* renamed from: a */
        Handler f147a;

        /* renamed from: b */
        boolean f148b;

        /* renamed from: c */
        boolean f149c;

        /* renamed from: d */
        int f150d;

        /* renamed from: e */
        int f151e;

        /* renamed from: f */
        int f152f;

        /* renamed from: g */
        int f153g;

        /* renamed from: h */
        int f154h;

        private C0050e() {
            this.f148b = false;
            this.f149c = false;
            this.f150d = C0047b.f132b;
            this.f151e = 0;
            this.f154h = 5;
        }

        /* synthetic */ C0050e(UnityPlayer unityPlayer, byte b) {
            this();
        }

        /* renamed from: a */
        private void m54a(C0049d dVar) {
            Handler handler = this.f147a;
            if (handler != null) {
                Message.obtain(handler, UnityPlayer.RUN_STATE_CHANGED_MSG_CODE, dVar).sendToTarget();
            }
        }

        /* renamed from: a */
        public final void mo169a() {
            m54a(C0049d.QUIT);
        }

        /* renamed from: a */
        public final void mo170a(int i, int i2) {
            this.f152f = i;
            this.f153g = i2;
            m54a(C0049d.ORIENTATION_ANGLE_CHANGE);
        }

        /* renamed from: a */
        public final void mo171a(Runnable runnable) {
            if (this.f147a != null) {
                m54a(C0049d.PAUSE);
                Message.obtain(this.f147a, runnable).sendToTarget();
            }
        }

        /* renamed from: b */
        public final void mo172b() {
            m54a(C0049d.RESUME);
        }

        /* renamed from: b */
        public final void mo173b(Runnable runnable) {
            if (this.f147a != null) {
                m54a(C0049d.SURFACE_LOST);
                Message.obtain(this.f147a, runnable).sendToTarget();
            }
        }

        /* renamed from: c */
        public final void mo174c() {
            m54a(C0049d.FOCUS_GAINED);
        }

        /* renamed from: c */
        public final void mo175c(Runnable runnable) {
            Handler handler = this.f147a;
            if (handler != null) {
                Message.obtain(handler, runnable).sendToTarget();
                m54a(C0049d.SURFACE_ACQUIRED);
            }
        }

        /* renamed from: d */
        public final void mo176d() {
            m54a(C0049d.FOCUS_LOST);
        }

        /* renamed from: d */
        public final void mo177d(Runnable runnable) {
            Handler handler = this.f147a;
            if (handler != null) {
                Message.obtain(handler, runnable).sendToTarget();
            }
        }

        /* renamed from: e */
        public final void mo178e() {
            m54a(C0049d.URL_ACTIVATED);
        }

        public final void run() {
            setName("UnityMain");
            Looper.prepare();
            this.f147a = new Handler(new Handler.Callback() {
                /* renamed from: a */
                private void m65a() {
                    if (C0050e.this.f150d == C0047b.f133c && C0050e.this.f149c) {
                        UnityPlayer.this.nativeFocusChanged(true);
                        C0050e.this.f150d = C0047b.f131a;
                    }
                }

                public final boolean handleMessage(Message message) {
                    if (message.what != UnityPlayer.RUN_STATE_CHANGED_MSG_CODE) {
                        return false;
                    }
                    C0049d dVar = (C0049d) message.obj;
                    if (dVar == C0049d.NEXT_FRAME) {
                        C0050e.this.f151e--;
                        UnityPlayer.this.executeGLThreadJobs();
                        if (!C0050e.this.f148b || !C0050e.this.f149c) {
                            return true;
                        }
                        if (C0050e.this.f154h >= 0) {
                            if (C0050e.this.f154h == 0 && UnityPlayer.this.getSplashEnabled()) {
                                UnityPlayer.this.DisableStaticSplashScreen();
                            }
                            C0050e.this.f154h--;
                        }
                        if (!UnityPlayer.this.isFinishing() && !UnityPlayer.this.nativeRender()) {
                            UnityPlayer.this.finish();
                        }
                    } else if (dVar == C0049d.QUIT) {
                        Looper.myLooper().quit();
                    } else if (dVar == C0049d.RESUME) {
                        C0050e.this.f148b = true;
                    } else if (dVar == C0049d.PAUSE) {
                        C0050e.this.f148b = false;
                    } else if (dVar == C0049d.SURFACE_LOST) {
                        C0050e.this.f149c = false;
                    } else {
                        if (dVar == C0049d.SURFACE_ACQUIRED) {
                            C0050e.this.f149c = true;
                        } else if (dVar == C0049d.FOCUS_LOST) {
                            if (C0050e.this.f150d == C0047b.f131a) {
                                UnityPlayer.this.nativeFocusChanged(false);
                            }
                            C0050e.this.f150d = C0047b.f132b;
                        } else if (dVar == C0049d.FOCUS_GAINED) {
                            C0050e.this.f150d = C0047b.f133c;
                        } else if (dVar == C0049d.URL_ACTIVATED) {
                            UnityPlayer.this.nativeSetLaunchURL(UnityPlayer.this.getLaunchURL());
                        } else if (dVar == C0049d.ORIENTATION_ANGLE_CHANGE) {
                            UnityPlayer.this.nativeOrientationChanged(C0050e.this.f152f, C0050e.this.f153g);
                        }
                        m65a();
                    }
                    if (C0050e.this.f148b && C0050e.this.f151e <= 0) {
                        Message.obtain(C0050e.this.f147a, UnityPlayer.RUN_STATE_CHANGED_MSG_CODE, C0049d.NEXT_FRAME).sendToTarget();
                        C0050e.this.f151e++;
                    }
                    return true;
                }
            });
            Looper.loop();
        }
    }

    /* renamed from: com.unity3d.player.UnityPlayer$f */
    private abstract class C0052f implements Runnable {
        private C0052f() {
        }

        /* synthetic */ C0052f(UnityPlayer unityPlayer, byte b) {
            this();
        }

        /* renamed from: a */
        public abstract void mo141a();

        public final void run() {
            if (!UnityPlayer.this.isFinishing()) {
                mo141a();
            }
        }
    }

    static {
        new C0080m().mo247a();
        try {
            System.loadLibrary("main");
        } catch (UnsatisfiedLinkError e) {
            C0068g.Log(6, "Failed to load 'libmain.so', the application will terminate.");
            throw e;
        }
    }

    public UnityPlayer(Context context) {
        this(context, (IUnityPlayerLifecycleEvents) null);
    }

    public UnityPlayer(Context context, IUnityPlayerLifecycleEvents iUnityPlayerLifecycleEvents) {
        super(context);
        this.mInitialScreenOrientation = -1;
        this.mMainDisplayOverride = false;
        this.mIsFullscreen = true;
        this.mState = new C0081n();
        this.m_Events = new ConcurrentLinkedQueue();
        this.mKillingIsMyBusiness = null;
        this.mOrientationListener = null;
        this.m_MainThread = new C0050e(this, (byte) 0);
        this.m_AddPhoneCallListener = false;
        this.m_PhoneCallListener = new C0048c(this, (byte) 0);
        this.m_ARCoreApi = null;
        this.m_FakeListener = new C0046a();
        this.m_Camera2Wrapper = null;
        this.m_HFPStatus = null;
        this.m_AudioVolumeHandler = null;
        this.m_launchUri = null;
        this.m_NetworkConnectivity = null;
        this.m_UnityPlayerLifecycleEvents = null;
        this.mProcessKillRequested = true;
        this.mSoftInputDialog = null;
        this.m_UnityPlayerLifecycleEvents = iUnityPlayerLifecycleEvents == null ? this : iUnityPlayerLifecycleEvents;
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            currentActivity = activity;
            this.mInitialScreenOrientation = activity.getRequestedOrientation();
            this.m_launchUri = currentActivity.getIntent().getData();
        }
        EarlyEnableFullScreenIfVrLaunched(currentActivity);
        this.mContext = context;
        this.mNaturalOrientation = getNaturalOrientation(getResources().getConfiguration().orientation);
        if (currentActivity != null && getSplashEnabled()) {
            C0077l lVar = new C0077l(this.mContext, C0077l.C0079a.m131a()[getSplashMode()]);
            this.m_SplashScreen = lVar;
            addView(lVar);
        }
        loadNative(this.mContext.getApplicationInfo());
        if (!C0081n.m135c()) {
            AlertDialog create = new AlertDialog.Builder(this.mContext).setTitle("Failure to initialize!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public final void onClick(DialogInterface dialogInterface, int i) {
                    UnityPlayer.this.finish();
                }
            }).setMessage("Your hardware does not support this application, sorry!").create();
            create.setCancelable(false);
            create.show();
            return;
        }
        initJni(context);
        this.mState.mo251c(true);
        SurfaceView CreateGlView = CreateGlView();
        this.mGlView = CreateGlView;
        CreateGlView.setContentDescription(GetGlViewContentDescription(context));
        addView(this.mGlView);
        bringChildToFront(this.m_SplashScreen);
        this.mQuitting = false;
        hideStatusBar();
        this.m_TelephonyManager = (TelephonyManager) this.mContext.getSystemService("phone");
        this.m_ClipboardManager = (ClipboardManager) this.mContext.getSystemService("clipboard");
        this.m_Camera2Wrapper = new Camera2Wrapper(this.mContext);
        this.m_HFPStatus = new HFPStatus(this.mContext);
        this.m_MainThread.start();
    }

    /* access modifiers changed from: private */
    public SurfaceView CreateGlView() {
        SurfaceView surfaceView = new SurfaceView(this.mContext);
        surfaceView.setId(this.mContext.getResources().getIdentifier("unitySurfaceView", "id", this.mContext.getPackageName()));
        if (IsWindowTranslucent()) {
            surfaceView.getHolder().setFormat(-3);
            surfaceView.setZOrderOnTop(true);
        } else {
            surfaceView.getHolder().setFormat(-1);
        }
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            public final void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
                UnityPlayer.this.updateGLDisplay(0, surfaceHolder.getSurface());
                UnityPlayer.this.sendSurfaceChangedEvent();
            }

            public final void surfaceCreated(SurfaceHolder surfaceHolder) {
                UnityPlayer.this.updateGLDisplay(0, surfaceHolder.getSurface());
            }

            public final void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                UnityPlayer.this.updateGLDisplay(0, (Surface) null);
            }
        });
        surfaceView.setFocusable(true);
        surfaceView.setFocusableInTouchMode(true);
        return surfaceView;
    }

    /* access modifiers changed from: private */
    public void DisableStaticSplashScreen() {
        runOnUiThread(new Runnable() {
            public final void run() {
                UnityPlayer unityPlayer = UnityPlayer.this;
                unityPlayer.removeView(unityPlayer.m_SplashScreen);
                C0077l unused = UnityPlayer.this.m_SplashScreen = null;
            }
        });
    }

    private void EarlyEnableFullScreenIfVrLaunched(Activity activity) {
        View decorView;
        if (activity != null && activity.getIntent().getBooleanExtra("android.intent.extra.VR_LAUNCH", false) && activity.getWindow() != null && (decorView = activity.getWindow().getDecorView()) != null) {
            decorView.setSystemUiVisibility(7);
        }
    }

    private String GetGlViewContentDescription(Context context) {
        return context.getResources().getString(context.getResources().getIdentifier("game_view_content_description", "string", context.getPackageName()));
    }

    private boolean IsWindowTranslucent() {
        if (currentActivity == null) {
            return false;
        }
        TypedValue typedValue = new TypedValue();
        return currentActivity.getTheme().resolveAttribute(16842840, typedValue, true) && typedValue.type == 18 && typedValue.data == 1;
    }

    public static void UnitySendMessage(String str, String str2, String str3) {
        if (!C0081n.m135c()) {
            C0068g.Log(5, "Native libraries not loaded - dropping message for " + str + "." + str2);
            return;
        }
        try {
            nativeUnitySendMessage(str, str2, str3.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException unused) {
        }
    }

    private void checkResumePlayer() {
        if (this.mState.mo255f()) {
            this.mState.mo252d(true);
            queueGLThreadEvent((Runnable) new Runnable() {
                public final void run() {
                    UnityPlayer.this.nativeResume();
                }
            });
            this.m_MainThread.mo172b();
        }
    }

    /* access modifiers changed from: private */
    public void finish() {
        Context context = this.mContext;
        if ((context instanceof Activity) && !((Activity) context).isFinishing()) {
            ((Activity) this.mContext).finish();
        }
    }

    private boolean getARCoreEnabled() {
        try {
            return getApplicationInfo().metaData.getBoolean(ARCORE_ENABLE_METADATA_NAME);
        } catch (Exception unused) {
            return false;
        }
    }

    private ApplicationInfo getApplicationInfo() {
        return this.mContext.getPackageManager().getApplicationInfo(this.mContext.getPackageName(), 128);
    }

    private int getNaturalOrientation(int i) {
        int rotation = ((WindowManager) this.mContext.getSystemService("window")).getDefaultDisplay().getRotation();
        if ((rotation == 0 || rotation == 2) && i == 2) {
            return 0;
        }
        return ((rotation == 1 || rotation == 3) && i == 1) ? 0 : 1;
    }

    /* access modifiers changed from: private */
    public boolean getSplashEnabled() {
        try {
            return getApplicationInfo().metaData.getBoolean(SPLASH_ENABLE_METADATA_NAME);
        } catch (Exception unused) {
            return false;
        }
    }

    private void hideStatusBar() {
        Context context = this.mContext;
        if (context instanceof Activity) {
            ((Activity) context).getWindow().setFlags(1024, 1024);
        }
    }

    private final native void initJni(Context context);

    protected static boolean loadLibraryStatic(String str) {
        StringBuilder sb;
        try {
            System.loadLibrary(str);
            return true;
        } catch (UnsatisfiedLinkError unused) {
            sb = new StringBuilder("Unable to find ");
            sb.append(str);
            C0068g.Log(6, sb.toString());
            return false;
        } catch (Exception e) {
            sb = new StringBuilder("Unknown error ");
            sb.append(e);
            C0068g.Log(6, sb.toString());
            return false;
        }
    }

    private static void loadNative(ApplicationInfo applicationInfo) {
        if (NativeLoader.load(applicationInfo.nativeLibraryDir)) {
            C0081n.m133a();
        } else {
            C0068g.Log(6, "NativeLoader.load failure, Unity libraries were not loaded.");
        }
    }

    private final native void nativeApplicationUnload();

    private final native boolean nativeDone();

    /* access modifiers changed from: private */
    public final native void nativeFocusChanged(boolean z);

    private final native boolean nativeInjectEvent(InputEvent inputEvent);

    /* access modifiers changed from: private */
    public final native boolean nativeIsAutorotationOn();

    /* access modifiers changed from: private */
    public final native void nativeLowMemory();

    /* access modifiers changed from: private */
    public final native void nativeMuteMasterAudio(boolean z);

    /* access modifiers changed from: private */
    public final native void nativeOrientationChanged(int i, int i2);

    /* access modifiers changed from: private */
    public final native boolean nativePause();

    /* access modifiers changed from: private */
    public final native void nativeRecreateGfxState(int i, Surface surface);

    /* access modifiers changed from: private */
    public final native boolean nativeRender();

    /* access modifiers changed from: private */
    public final native void nativeReportKeyboardConfigChanged();

    private final native void nativeRestartActivityIndicator();

    /* access modifiers changed from: private */
    public final native void nativeResume();

    /* access modifiers changed from: private */
    public final native void nativeSendSurfaceChangedEvent();

    /* access modifiers changed from: private */
    public final native void nativeSetInputArea(int i, int i2, int i3, int i4);

    /* access modifiers changed from: private */
    public final native void nativeSetInputSelection(int i, int i2);

    /* access modifiers changed from: private */
    public final native void nativeSetInputString(String str);

    /* access modifiers changed from: private */
    public final native void nativeSetKeyboardIsVisible(boolean z);

    /* access modifiers changed from: private */
    public final native void nativeSetLaunchURL(String str);

    /* access modifiers changed from: private */
    public final native void nativeSoftInputCanceled();

    /* access modifiers changed from: private */
    public final native void nativeSoftInputClosed();

    /* access modifiers changed from: private */
    public final native void nativeSoftInputLostFocus();

    private static native void nativeUnitySendMessage(String str, String str2, byte[] bArr);

    private void pauseUnity() {
        reportSoftInputStr((String) null, 1, true);
        if (this.mState.mo256g()) {
            if (C0081n.m135c()) {
                final Semaphore semaphore = new Semaphore(0);
                this.m_MainThread.mo171a(isFinishing() ? new Runnable() {
                    public final void run() {
                        UnityPlayer.this.shutdown();
                        semaphore.release();
                    }
                } : new Runnable() {
                    public final void run() {
                        if (UnityPlayer.this.nativePause()) {
                            boolean unused = UnityPlayer.this.mQuitting = true;
                            UnityPlayer.this.shutdown();
                            semaphore.release(2);
                            return;
                        }
                        semaphore.release();
                    }
                });
                try {
                    if (!semaphore.tryAcquire(4, TimeUnit.SECONDS)) {
                        C0068g.Log(5, "Timeout while trying to pause the Unity Engine.");
                    }
                } catch (InterruptedException unused) {
                    C0068g.Log(5, "UI thread got interrupted while trying to pause the Unity Engine.");
                }
                if (semaphore.drainPermits() > 0) {
                    destroy();
                }
            }
            this.mState.mo252d(false);
            this.mState.mo250b(true);
            if (this.m_AddPhoneCallListener) {
                this.m_TelephonyManager.listen(this.m_PhoneCallListener, 0);
            }
        }
    }

    private void queueGLThreadEvent(C0052f fVar) {
        if (!isFinishing()) {
            queueGLThreadEvent((Runnable) fVar);
        }
    }

    private void queueGLThreadEvent(Runnable runnable) {
        if (C0081n.m135c()) {
            if (Thread.currentThread() == this.m_MainThread) {
                runnable.run();
            } else {
                this.m_Events.add(runnable);
            }
        }
    }

    /* access modifiers changed from: private */
    public void sendSurfaceChangedEvent() {
        if (C0081n.m135c() && this.mState.mo254e()) {
            this.m_MainThread.mo177d(new Runnable() {
                public final void run() {
                    UnityPlayer.this.nativeSendSurfaceChangedEvent();
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void shutdown() {
        this.mProcessKillRequested = nativeDone();
        this.mState.mo251c(false);
    }

    /* JADX WARNING: type inference failed for: r2v0, types: [android.view.ViewParent] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void swapViews(android.view.View r5, android.view.View r6) {
        /*
            r4 = this;
            com.unity3d.player.n r0 = r4.mState
            boolean r0 = r0.mo253d()
            r1 = 0
            if (r0 != 0) goto L_0x000e
            r4.pause()
            r0 = 1
            goto L_0x000f
        L_0x000e:
            r0 = 0
        L_0x000f:
            if (r5 == 0) goto L_0x0030
            android.view.ViewParent r2 = r5.getParent()
            boolean r3 = r2 instanceof com.unity3d.player.UnityPlayer
            if (r3 == 0) goto L_0x001e
            r3 = r2
            com.unity3d.player.UnityPlayer r3 = (com.unity3d.player.UnityPlayer) r3
            if (r3 == r4) goto L_0x0030
        L_0x001e:
            boolean r3 = r2 instanceof android.view.ViewGroup
            if (r3 == 0) goto L_0x0027
            android.view.ViewGroup r2 = (android.view.ViewGroup) r2
            r2.removeView(r5)
        L_0x0027:
            r4.addView(r5)
            r4.bringChildToFront(r5)
            r5.setVisibility(r1)
        L_0x0030:
            if (r6 == 0) goto L_0x0040
            android.view.ViewParent r5 = r6.getParent()
            if (r5 != r4) goto L_0x0040
            r5 = 8
            r6.setVisibility(r5)
            r4.removeView(r6)
        L_0x0040:
            if (r0 == 0) goto L_0x0045
            r4.resume()
        L_0x0045:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.unity3d.player.UnityPlayer.swapViews(android.view.View, android.view.View):void");
    }

    private static void unloadNative() {
        if (C0081n.m135c()) {
            if (NativeLoader.unload()) {
                C0081n.m134b();
                return;
            }
            throw new UnsatisfiedLinkError("Unable to unload libraries from libmain.so");
        }
    }

    private boolean updateDisplayInternal(final int i, final Surface surface) {
        if (!C0081n.m135c() || !this.mState.mo254e()) {
            return false;
        }
        final Semaphore semaphore = new Semaphore(0);
        C003423 r1 = new Runnable() {
            public final void run() {
                UnityPlayer.this.nativeRecreateGfxState(i, surface);
                semaphore.release();
            }
        };
        if (i == 0) {
            C0050e eVar = this.m_MainThread;
            if (surface == null) {
                eVar.mo173b(r1);
            } else {
                eVar.mo175c(r1);
            }
        } else {
            r1.run();
        }
        if (surface != null || i != 0) {
            return true;
        }
        try {
            if (semaphore.tryAcquire(4, TimeUnit.SECONDS)) {
                return true;
            }
            C0068g.Log(5, "Timeout while trying detaching primary window.");
            return true;
        } catch (InterruptedException unused) {
            C0068g.Log(5, "UI thread got interrupted while trying to detach the primary window from the Unity Engine.");
            return true;
        }
    }

    /* access modifiers changed from: private */
    public void updateGLDisplay(int i, Surface surface) {
        if (!this.mMainDisplayOverride) {
            updateDisplayInternal(i, surface);
        }
    }

    /* access modifiers changed from: protected */
    public void addPhoneCallListener() {
        this.m_AddPhoneCallListener = true;
        this.m_TelephonyManager.listen(this.m_PhoneCallListener, 32);
    }

    public boolean addViewToPlayer(View view, boolean z) {
        swapViews(view, z ? this.mGlView : null);
        boolean z2 = true;
        boolean z3 = view.getParent() == this;
        boolean z4 = z && this.mGlView.getParent() == null;
        boolean z5 = this.mGlView.getParent() == this;
        if (!z3 || (!z4 && !z5)) {
            z2 = false;
        }
        if (!z2) {
            if (!z3) {
                C0068g.Log(6, "addViewToPlayer: Failure adding view to hierarchy");
            }
            if (!z4 && !z5) {
                C0068g.Log(6, "addViewToPlayer: Failure removing old view from hierarchy");
            }
        }
        return z2;
    }

    public void configurationChanged(Configuration configuration) {
        SurfaceView surfaceView = this.mGlView;
        if (surfaceView instanceof SurfaceView) {
            surfaceView.getHolder().setSizeFromLayout();
        }
        C0087q qVar = this.mVideoPlayerProxy;
        if (qVar != null) {
            qVar.mo291c();
        }
        GoogleVrProxy b = GoogleVrApi.m9b();
        if (b != null) {
            b.mo45c();
        }
    }

    public void destroy() {
        if (GoogleVrApi.m9b() != null) {
            GoogleVrApi.m7a();
        }
        Camera2Wrapper camera2Wrapper = this.m_Camera2Wrapper;
        if (camera2Wrapper != null) {
            camera2Wrapper.mo21a();
            this.m_Camera2Wrapper = null;
        }
        HFPStatus hFPStatus = this.m_HFPStatus;
        if (hFPStatus != null) {
            hFPStatus.mo65a();
            this.m_HFPStatus = null;
        }
        NetworkConnectivity networkConnectivity = this.m_NetworkConnectivity;
        if (networkConnectivity != null) {
            networkConnectivity.mo72b();
            this.m_NetworkConnectivity = null;
        }
        this.mQuitting = true;
        if (!this.mState.mo253d()) {
            pause();
        }
        this.m_MainThread.mo169a();
        try {
            this.m_MainThread.join(4000);
        } catch (InterruptedException unused) {
            this.m_MainThread.interrupt();
        }
        BroadcastReceiver broadcastReceiver = this.mKillingIsMyBusiness;
        if (broadcastReceiver != null) {
            this.mContext.unregisterReceiver(broadcastReceiver);
        }
        this.mKillingIsMyBusiness = null;
        if (C0081n.m135c()) {
            removeAllViews();
        }
        if (this.mProcessKillRequested) {
            this.m_UnityPlayerLifecycleEvents.onUnityPlayerQuitted();
            kill();
        }
        unloadNative();
    }

    /* access modifiers changed from: protected */
    public void disableLogger() {
        C0068g.f213a = true;
    }

    public boolean displayChanged(int i, Surface surface) {
        if (i == 0) {
            this.mMainDisplayOverride = surface != null;
            runOnUiThread(new Runnable() {
                public final void run() {
                    if (UnityPlayer.this.mMainDisplayOverride) {
                        UnityPlayer unityPlayer = UnityPlayer.this;
                        unityPlayer.removeView(unityPlayer.mGlView);
                        return;
                    }
                    UnityPlayer unityPlayer2 = UnityPlayer.this;
                    unityPlayer2.addView(unityPlayer2.mGlView);
                }
            });
        }
        return updateDisplayInternal(i, surface);
    }

    /* access modifiers changed from: protected */
    public void executeGLThreadJobs() {
        while (true) {
            Runnable runnable = (Runnable) this.m_Events.poll();
            if (runnable != null) {
                runnable.run();
            } else {
                return;
            }
        }
    }

    /* access modifiers changed from: protected */
    public String getClipboardText() {
        ClipData primaryClip = this.m_ClipboardManager.getPrimaryClip();
        return primaryClip != null ? primaryClip.getItemAt(0).coerceToText(this.mContext).toString() : "";
    }

    /* access modifiers changed from: protected */
    public String getKeyboardLayout() {
        C0072k kVar = this.mSoftInputDialog;
        if (kVar == null) {
            return null;
        }
        return kVar.mo228a();
    }

    /* access modifiers changed from: protected */
    public String getLaunchURL() {
        Uri uri = this.m_launchUri;
        if (uri != null) {
            return uri.toString();
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public int getNetworkConnectivity() {
        if (!C0071j.f216c) {
            return 0;
        }
        if (this.m_NetworkConnectivity == null) {
            this.m_NetworkConnectivity = new NetworkConnectivity(this.mContext);
        }
        return this.m_NetworkConnectivity.mo71a();
    }

    public String getNetworkProxySettings(String str) {
        String str2;
        String str3;
        if (str.startsWith("http:")) {
            str2 = "http.proxyHost";
            str3 = "http.proxyPort";
        } else {
            if (str.startsWith("https:")) {
                str2 = "https.proxyHost";
                str3 = "https.proxyPort";
            }
            return null;
        }
        String property = System.getProperties().getProperty(str2);
        if (property != null && !"".equals(property)) {
            StringBuilder sb = new StringBuilder(property);
            String property2 = System.getProperties().getProperty(str3);
            if (property2 != null && !"".equals(property2)) {
                sb.append(":");
                sb.append(property2);
            }
            String property3 = System.getProperties().getProperty("http.nonProxyHosts");
            if (property3 != null && !"".equals(property3)) {
                sb.append(10);
                sb.append(property3);
            }
            return sb.toString();
        }
        return null;
    }

    public Bundle getSettings() {
        return Bundle.EMPTY;
    }

    /* access modifiers changed from: protected */
    public int getSplashMode() {
        try {
            return getApplicationInfo().metaData.getInt(SPLASH_MODE_METADATA_NAME);
        } catch (Exception unused) {
            return 0;
        }
    }

    public View getView() {
        return this;
    }

    /* access modifiers changed from: protected */
    public void hideSoftInput() {
        postOnUiThread(new Runnable() {
            public final void run() {
                UnityPlayer.this.reportSoftInputArea(new Rect());
                UnityPlayer.this.reportSoftInputIsVisible(false);
                if (UnityPlayer.this.mSoftInputDialog != null) {
                    UnityPlayer.this.mSoftInputDialog.dismiss();
                    UnityPlayer.this.mSoftInputDialog = null;
                    UnityPlayer.this.nativeReportKeyboardConfigChanged();
                }
            }
        });
    }

    public void init(int i, boolean z) {
    }

    /* access modifiers changed from: protected */
    public boolean initializeGoogleAr() {
        if (this.m_ARCoreApi != null || currentActivity == null || !getARCoreEnabled()) {
            return false;
        }
        GoogleARCoreApi googleARCoreApi = new GoogleARCoreApi();
        this.m_ARCoreApi = googleARCoreApi;
        googleARCoreApi.initializeARCore(currentActivity);
        if (this.mState.mo253d()) {
            return false;
        }
        this.m_ARCoreApi.resumeARCore();
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean initializeGoogleVr() {
        final GoogleVrProxy b = GoogleVrApi.m9b();
        if (b == null) {
            GoogleVrApi.m8a(this);
            b = GoogleVrApi.m9b();
            if (b == null) {
                C0068g.Log(6, "Unable to create Google VR subsystem.");
                return false;
            }
        }
        final Semaphore semaphore = new Semaphore(0);
        final C002414 r3 = new Runnable() {
            public final void run() {
                UnityPlayer.this.injectEvent(new KeyEvent(0, UnityPlayer.ANR_TIMEOUT_SECONDS));
                UnityPlayer.this.injectEvent(new KeyEvent(1, UnityPlayer.ANR_TIMEOUT_SECONDS));
            }
        };
        runOnUiThread(new Runnable() {
            public final void run() {
                if (!b.mo43a(UnityPlayer.currentActivity, UnityPlayer.this.mContext, UnityPlayer.this.CreateGlView(), r3)) {
                    C0068g.Log(6, "Unable to initialize Google VR subsystem.");
                }
                if (UnityPlayer.currentActivity != null) {
                    b.mo41a(UnityPlayer.currentActivity.getIntent());
                }
                semaphore.release();
            }
        });
        try {
            if (semaphore.tryAcquire(4, TimeUnit.SECONDS)) {
                return b.mo42a();
            }
            C0068g.Log(5, "Timeout while trying to initialize Google VR.");
            return false;
        } catch (InterruptedException e) {
            C0068g.Log(5, "UI thread was interrupted while initializing Google VR. " + e.getLocalizedMessage());
            return false;
        }
    }

    public boolean injectEvent(InputEvent inputEvent) {
        if (!C0081n.m135c()) {
            return false;
        }
        return nativeInjectEvent(inputEvent);
    }

    /* access modifiers changed from: protected */
    public boolean isFinishing() {
        if (!this.mQuitting) {
            Context context = this.mContext;
            boolean z = (context instanceof Activity) && ((Activity) context).isFinishing();
            this.mQuitting = z;
            return z;
        }
    }

    /* access modifiers changed from: protected */
    public void kill() {
        Process.killProcess(Process.myPid());
    }

    /* access modifiers changed from: protected */
    public boolean loadLibrary(String str) {
        return loadLibraryStatic(str);
    }

    public void lowMemory() {
        if (C0081n.m135c()) {
            queueGLThreadEvent((Runnable) new Runnable() {
                public final void run() {
                    UnityPlayer.this.nativeLowMemory();
                }
            });
        }
    }

    public void newIntent(Intent intent) {
        this.m_launchUri = intent.getData();
        this.m_MainThread.mo178e();
    }

    public boolean onGenericMotionEvent(MotionEvent motionEvent) {
        return injectEvent(motionEvent);
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        return injectEvent(keyEvent);
    }

    public boolean onKeyLongPress(int i, KeyEvent keyEvent) {
        return injectEvent(keyEvent);
    }

    public boolean onKeyMultiple(int i, int i2, KeyEvent keyEvent) {
        return injectEvent(keyEvent);
    }

    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        return injectEvent(keyEvent);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        return injectEvent(motionEvent);
    }

    public void onUnityPlayerQuitted() {
    }

    public void onUnityPlayerUnloaded() {
    }

    public void pause() {
        GoogleARCoreApi googleARCoreApi = this.m_ARCoreApi;
        if (googleARCoreApi != null) {
            googleARCoreApi.pauseARCore();
        }
        C0087q qVar = this.mVideoPlayerProxy;
        if (qVar != null) {
            qVar.mo288a();
        }
        GoogleVrProxy b = GoogleVrApi.m9b();
        if (b != null) {
            b.pauseGvrLayout();
        }
        AudioVolumeHandler audioVolumeHandler = this.m_AudioVolumeHandler;
        if (audioVolumeHandler != null) {
            audioVolumeHandler.mo19a();
            this.m_AudioVolumeHandler = null;
        }
        pauseUnity();
    }

    /* access modifiers changed from: protected */
    public void pauseJavaAndCallUnloadCallback() {
        runOnUiThread(new Runnable() {
            public final void run() {
                UnityPlayer.this.pause();
                UnityPlayer.this.windowFocusChanged(false);
                UnityPlayer.this.m_UnityPlayerLifecycleEvents.onUnityPlayerUnloaded();
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void postOnUiThread(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }

    public void quit() {
        destroy();
    }

    public void removeViewFromPlayer(View view) {
        swapViews(this.mGlView, view);
        boolean z = true;
        boolean z2 = view.getParent() == null;
        boolean z3 = this.mGlView.getParent() == this;
        if (!z2 || !z3) {
            z = false;
        }
        if (!z) {
            if (!z2) {
                C0068g.Log(6, "removeViewFromPlayer: Failure removing view from hierarchy");
            }
            if (!z3) {
                C0068g.Log(6, "removeVireFromPlayer: Failure agging old view to hierarchy");
            }
        }
    }

    public void reportError(String str, String str2) {
        C0068g.Log(6, str + ": " + str2);
    }

    /* access modifiers changed from: protected */
    public void reportSoftInputArea(final Rect rect) {
        queueGLThreadEvent((C0052f) new C0052f() {
            /* renamed from: a */
            public final void mo141a() {
                UnityPlayer.this.nativeSetInputArea(rect.left, rect.top, rect.right, rect.bottom);
            }
        });
    }

    /* access modifiers changed from: protected */
    public void reportSoftInputIsVisible(final boolean z) {
        queueGLThreadEvent((C0052f) new C0052f() {
            /* renamed from: a */
            public final void mo141a() {
                UnityPlayer.this.nativeSetKeyboardIsVisible(z);
            }
        });
    }

    /* access modifiers changed from: protected */
    public void reportSoftInputSelection(final int i, final int i2) {
        queueGLThreadEvent((C0052f) new C0052f() {
            /* renamed from: a */
            public final void mo141a() {
                UnityPlayer.this.nativeSetInputSelection(i, i2);
            }
        });
    }

    /* access modifiers changed from: protected */
    public void reportSoftInputStr(final String str, final int i, final boolean z) {
        if (i == 1) {
            hideSoftInput();
        }
        queueGLThreadEvent((C0052f) new C0052f() {
            /* renamed from: a */
            public final void mo141a() {
                if (z) {
                    UnityPlayer.this.nativeSoftInputCanceled();
                } else {
                    String str = str;
                    if (str != null) {
                        UnityPlayer.this.nativeSetInputString(str);
                    }
                }
                if (i == 1) {
                    UnityPlayer.this.nativeSoftInputClosed();
                }
            }
        });
    }

    /* access modifiers changed from: protected */
    public void requestUserAuthorization(String str) {
        if (C0071j.f215b && str != null && !str.isEmpty() && currentActivity != null) {
            C0071j.f217d.mo224a(currentActivity, str);
        }
    }

    public void resume() {
        GoogleARCoreApi googleARCoreApi = this.m_ARCoreApi;
        if (googleARCoreApi != null) {
            googleARCoreApi.resumeARCore();
        }
        this.mState.mo250b(false);
        C0087q qVar = this.mVideoPlayerProxy;
        if (qVar != null) {
            qVar.mo290b();
        }
        checkResumePlayer();
        nativeRestartActivityIndicator();
        GoogleVrProxy b = GoogleVrApi.m9b();
        if (b != null) {
            b.mo44b();
        }
        this.m_AudioVolumeHandler = new AudioVolumeHandler(this.mContext);
    }

    /* access modifiers changed from: package-private */
    public void runOnAnonymousThread(Runnable runnable) {
        new Thread(runnable).start();
    }

    /* access modifiers changed from: package-private */
    public void runOnUiThread(Runnable runnable) {
        Context context = this.mContext;
        if (context instanceof Activity) {
            ((Activity) context).runOnUiThread(runnable);
        } else {
            C0068g.Log(5, "Not running Unity from an Activity; ignored...");
        }
    }

    /* access modifiers changed from: protected */
    public void setCharacterLimit(final int i) {
        runOnUiThread(new Runnable() {
            public final void run() {
                if (UnityPlayer.this.mSoftInputDialog != null) {
                    UnityPlayer.this.mSoftInputDialog.mo229a(i);
                }
            }
        });
    }

    /* access modifiers changed from: protected */
    public void setClipboardText(String str) {
        this.m_ClipboardManager.setPrimaryClip(ClipData.newPlainText("Text", str));
    }

    /* access modifiers changed from: protected */
    public void setHideInputField(final boolean z) {
        runOnUiThread(new Runnable() {
            public final void run() {
                if (UnityPlayer.this.mSoftInputDialog != null) {
                    UnityPlayer.this.mSoftInputDialog.mo232a(z);
                }
            }
        });
    }

    /* access modifiers changed from: protected */
    public void setSelection(final int i, final int i2) {
        runOnUiThread(new Runnable() {
            public final void run() {
                if (UnityPlayer.this.mSoftInputDialog != null) {
                    UnityPlayer.this.mSoftInputDialog.mo230a(i, i2);
                }
            }
        });
    }

    /* access modifiers changed from: protected */
    public void setSoftInputStr(final String str) {
        runOnUiThread(new Runnable() {
            public final void run() {
                if (UnityPlayer.this.mSoftInputDialog != null && str != null) {
                    UnityPlayer.this.mSoftInputDialog.mo231a(str);
                }
            }
        });
    }

    /* access modifiers changed from: protected */
    public void showSoftInput(String str, int i, boolean z, boolean z2, boolean z3, boolean z4, String str2, int i2, boolean z5) {
        final String str3 = str;
        final int i3 = i;
        final boolean z6 = z;
        final boolean z7 = z2;
        final boolean z8 = z3;
        final boolean z9 = z4;
        final String str4 = str2;
        final int i4 = i2;
        final boolean z10 = z5;
        postOnUiThread(new Runnable() {
            public final void run() {
                UnityPlayer.this.mSoftInputDialog = new C0072k(UnityPlayer.this.mContext, this, str3, i3, z6, z7, z8, str4, i4, z10);
                UnityPlayer.this.mSoftInputDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    public final void onCancel(DialogInterface dialogInterface) {
                        UnityPlayer.this.nativeSoftInputLostFocus();
                        UnityPlayer.this.reportSoftInputStr((String) null, 1, false);
                    }
                });
                UnityPlayer.this.mSoftInputDialog.show();
                UnityPlayer.this.nativeReportKeyboardConfigChanged();
            }
        });
    }

    /* access modifiers changed from: protected */
    public boolean showVideoPlayer(String str, int i, int i2, int i3, boolean z, int i4, int i5) {
        if (this.mVideoPlayerProxy == null) {
            this.mVideoPlayerProxy = new C0087q(this);
        }
        boolean a = this.mVideoPlayerProxy.mo289a(this.mContext, str, i, i2, i3, z, (long) i4, (long) i5, new C0087q.C0094a() {
            /* renamed from: a */
            public final void mo144a() {
                C0087q unused = UnityPlayer.this.mVideoPlayerProxy = null;
            }
        });
        if (a) {
            runOnUiThread(new Runnable() {
                public final void run() {
                    if (UnityPlayer.this.nativeIsAutorotationOn() && (UnityPlayer.this.mContext instanceof Activity)) {
                        ((Activity) UnityPlayer.this.mContext).setRequestedOrientation(UnityPlayer.this.mInitialScreenOrientation);
                    }
                }
            });
        }
        return a;
    }

    /* access modifiers changed from: protected */
    public boolean skipPermissionsDialog() {
        if (!C0071j.f215b || currentActivity == null) {
            return false;
        }
        return C0071j.f217d.mo225a(currentActivity);
    }

    public boolean startOrientationListener(int i) {
        String str;
        if (this.mOrientationListener != null) {
            str = "Orientation Listener already started.";
        } else {
            C002919 r0 = new OrientationEventListener(this.mContext, i) {
                public final void onOrientationChanged(int i) {
                    UnityPlayer.this.m_MainThread.mo170a(UnityPlayer.this.mNaturalOrientation, i);
                }
            };
            this.mOrientationListener = r0;
            if (r0.canDetectOrientation()) {
                this.mOrientationListener.enable();
                return true;
            }
            str = "Orientation Listener cannot detect orientation.";
        }
        C0068g.Log(5, str);
        return false;
    }

    public boolean stopOrientationListener() {
        OrientationEventListener orientationEventListener = this.mOrientationListener;
        if (orientationEventListener == null) {
            C0068g.Log(5, "Orientation Listener was not started.");
            return false;
        }
        orientationEventListener.disable();
        this.mOrientationListener = null;
        return true;
    }

    /* access modifiers changed from: protected */
    public void toggleGyroscopeSensor(boolean z) {
        SensorManager sensorManager = (SensorManager) this.mContext.getSystemService("sensor");
        Sensor defaultSensor = sensorManager.getDefaultSensor(11);
        if (z) {
            sensorManager.registerListener(this.m_FakeListener, defaultSensor, 1);
        } else {
            sensorManager.unregisterListener(this.m_FakeListener);
        }
    }

    public void unload() {
        nativeApplicationUnload();
    }

    public void windowFocusChanged(boolean z) {
        this.mState.mo249a(z);
        if (this.mState.mo254e()) {
            if (z) {
                this.m_MainThread.mo174c();
            } else {
                this.m_MainThread.mo176d();
            }
            checkResumePlayer();
        }
    }
}
