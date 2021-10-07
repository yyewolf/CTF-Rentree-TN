package com.unity3d.player;

import java.lang.reflect.Method;
import java.util.HashMap;

/* renamed from: com.unity3d.player.o */
final class C0082o {

    /* renamed from: a */
    private HashMap f242a = new HashMap();

    /* renamed from: b */
    private Class f243b = null;

    /* renamed from: c */
    private Object f244c = null;

    /* renamed from: com.unity3d.player.o$a */
    class C0083a {

        /* renamed from: a */
        public Class[] f245a;

        /* renamed from: b */
        public Method f246b = null;

        public C0083a(Class[] clsArr) {
            this.f245a = clsArr;
        }
    }

    public C0082o(Class cls, Object obj) {
        this.f243b = cls;
        this.f244c = obj;
    }

    /* renamed from: a */
    private void m144a(String str, C0083a aVar) {
        try {
            aVar.f246b = this.f243b.getMethod(str, aVar.f245a);
        } catch (Exception e) {
            C0068g.Log(6, "Exception while trying to get method " + str + ". " + e.getLocalizedMessage());
            aVar.f246b = null;
        }
    }

    /* renamed from: a */
    public final Object mo258a(String str, Object... objArr) {
        StringBuilder sb;
        if (!this.f242a.containsKey(str)) {
            sb = new StringBuilder("No definition for method ");
            sb.append(str);
            str = " can be found";
        } else {
            C0083a aVar = (C0083a) this.f242a.get(str);
            if (aVar.f246b == null) {
                m144a(str, aVar);
            }
            if (aVar.f246b == null) {
                sb = new StringBuilder("Unable to create method: ");
            } else {
                try {
                    return objArr.length == 0 ? aVar.f246b.invoke(this.f244c, new Object[0]) : aVar.f246b.invoke(this.f244c, objArr);
                } catch (Exception e) {
                    C0068g.Log(6, "Error trying to call delegated method " + str + ". " + e.getLocalizedMessage());
                    return null;
                }
            }
        }
        sb.append(str);
        C0068g.Log(6, sb.toString());
        return null;
    }

    /* renamed from: a */
    public final void mo259a(String str, Class[] clsArr) {
        this.f242a.put(str, new C0083a(clsArr));
    }
}
