package org.example;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;

public abstract class JSConsole implements JSObject {
    @JSBody(params = "msg",script = "console.log(msg);")
    public static native void log(String msg);

    @JSBody(params = "obj",script = "console.log(obj);")
    public static native void log(Object obj);
}
