    private static __M$Node_UnsafeChildProcess_Unsafe.ChildProcessValue __child(Object value) {
        return (__M$Node_UnsafeChildProcess_Unsafe.ChildProcessValue) value;
    }

    public static Object pidImpl = (java.util.function.Function<Object, Object>) (child) ->
        (java.util.function.Supplier<Object>) () -> {
            Process process = __child(child).process;
            return process == null ? null : process.pid();
        };

    public static Object connectedImpl = (java.util.function.Function<Object, Object>) (child) ->
        (java.util.function.Supplier<Object>) () -> __child(child).connected;

    public static Object exitCodeImpl = (java.util.function.Function<Object, Object>) (child) ->
        (java.util.function.Supplier<Object>) () -> __child(child).exitCode;

    public static Object disconnectImpl = (java.util.function.Function<Object, Object>) (child) ->
        (java.util.function.Supplier<Object>) () -> null;

    private static Object __kill(Object child) {
        __M$Node_UnsafeChildProcess_Unsafe.ChildProcessValue value = __child(child);
        if (value.process == null || !value.process.isAlive()) return false;
        value.process.destroy();
        value.killed = true;
        return true;
    }

    public static Object killImpl = (java.util.function.Function<Object, Object>) (child) ->
        (java.util.function.Supplier<Object>) () -> __kill(child);

    public static Object killStrImpl = (java.util.function.Function<Object, Object>) (child) ->
        (java.util.function.Function<Object, Object>) (signal) ->
            (java.util.function.Supplier<Object>) () -> __kill(child);

    public static Object killedImpl = (java.util.function.Function<Object, Object>) (child) ->
        (java.util.function.Supplier<Object>) () -> __child(child).killed;

    public static Object refImpl = (java.util.function.Function<Object, Object>) (child) ->
        (java.util.function.Supplier<Object>) () -> null;

    public static Object unrefImpl = (java.util.function.Function<Object, Object>) (child) ->
        (java.util.function.Supplier<Object>) () -> null;

    public static Object signalCodeImpl = (java.util.function.Function<Object, Object>) (child) ->
        (java.util.function.Supplier<Object>) () -> __child(child).signalCode;

    public static Object spawnArgs = (java.util.function.Function<Object, Object>) (child) -> new Object[0];

    public static Object spawnFile = (java.util.function.Function<Object, Object>) (child) -> "";

    public static Object stdio = (java.util.function.Function<Object, Object>) (child) -> new Object[0];
