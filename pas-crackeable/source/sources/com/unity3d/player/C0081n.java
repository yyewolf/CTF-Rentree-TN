package com.unity3d.player;

/* renamed from: com.unity3d.player.n */
final class C0081n {

    /* renamed from: a */
    private static boolean f237a = false;

    /* renamed from: b */
    private boolean f238b = false;

    /* renamed from: c */
    private boolean f239c = false;

    /* renamed from: d */
    private boolean f240d = true;

    /* renamed from: e */
    private boolean f241e = false;

    C0081n() {
    }

    /* renamed from: a */
    static void m133a() {
        f237a = true;
    }

    /* renamed from: b */
    static void m134b() {
        f237a = false;
    }

    /* renamed from: c */
    static boolean m135c() {
        return f237a;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: a */
    public final void mo249a(boolean z) {
        this.f238b = z;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: b */
    public final void mo250b(boolean z) {
        this.f240d = z;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: c */
    public final void mo251c(boolean z) {
        this.f241e = z;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: d */
    public final void mo252d(boolean z) {
        this.f239c = z;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: d */
    public final boolean mo253d() {
        return this.f240d;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: e */
    public final boolean mo254e() {
        return this.f241e;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: f */
    public final boolean mo255f() {
        return f237a && this.f238b && !this.f240d && !this.f239c;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: g */
    public final boolean mo256g() {
        return this.f239c;
    }

    public final String toString() {
        return super.toString();
    }
}
