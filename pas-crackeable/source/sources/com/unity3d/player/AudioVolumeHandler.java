package com.unity3d.player;

import android.content.Context;
import com.unity3d.player.C0053a;

public class AudioVolumeHandler implements C0053a.C0055b {

    /* renamed from: a */
    private C0053a f12a;

    AudioVolumeHandler(Context context) {
        C0053a aVar = new C0053a(context);
        this.f12a = aVar;
        aVar.mo198a(this);
    }

    /* renamed from: a */
    public final void mo19a() {
        this.f12a.mo197a();
        this.f12a = null;
    }

    public final native void onAudioVolumeChanged(int i);
}
