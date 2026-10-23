    public static final class ChildProcessValue extends __M$Node_EventEmitter.EmitterBase {
        public Process process;
        public __M$Node_Stream.ReadableStream stdout = new __M$Node_Stream.ReadableStream();
        public __M$Node_Stream.ReadableStream stderr = new __M$Node_Stream.ReadableStream();
        public __M$Node_Stream.WritableStream stdin = new __M$Node_Stream.WritableStream();
        public Integer exitCode = null;
        public String signalCode = null;
        public boolean connected = true;
        public boolean killed = false;
        public String spawnFile = "";
        public Object[] spawnArgs = new Object[0];
        private final java.util.List<Object[]> history = new java.util.ArrayList<>();

        @Override
        public boolean fire(String name, Object... args) {
            boolean delivered = super.fire(name, args);
            history.add(new Object[] { name, args });
            if (history.size() > 64) history.remove(0);
            return delivered;
        }

        @Override
        public void addListener(String name, Object listener, boolean prepend) {
            super.addListener(name, listener, prepend);
            for (Object[] event : new java.util.ArrayList<>(history)) {
                if (name.equals(event[0])) invoke(listener, (Object[]) event[1]);
            }
        }
    }

    private static void __append(__M$Node_Stream.ReadableStream stream, byte[] chunk) {
        byte[] joined = java.util.Arrays.copyOf(stream.data, stream.data.length + chunk.length);
        System.arraycopy(stream.data, 0, joined, 0, stream.data.length);
        System.arraycopy(chunk, 0, joined, stream.data.length, chunk.length);
        stream.data = joined;
        stream.fire("data", __M$Node_Buffer.__wrap(chunk));
        stream.fire("readable");
    }

    private static void __pump(java.io.InputStream input, __M$Node_Stream.ReadableStream target) {
        try {
            byte[] buffer = new byte[8192];
            int count;
            while ((count = input.read(buffer)) > 0) {
                __append(target, java.util.Arrays.copyOf(buffer, count));
            }
        } catch (java.io.IOException ignored) {
        } finally {
            target.ended = true;
            target.fire("end");
        }
    }

    private static Object __spawn(String command, Object argsObj, Object options) {
        ChildProcessValue child = new ChildProcessValue();
        java.util.List<String> commandLine = new java.util.ArrayList<>();
        commandLine.add(command);
        child.spawnFile = command;
        child.spawnArgs = argsObj instanceof Object[] ? ((Object[]) argsObj).clone() : new Object[0];
        if (argsObj instanceof Object[]) for (Object arg : (Object[]) argsObj) commandLine.add(String.valueOf(arg));
        try {
            java.lang.ProcessBuilder builder = new java.lang.ProcessBuilder(commandLine);
            if (options instanceof java.util.Map) {
                java.util.Map<?, ?> opts = (java.util.Map<?, ?>) options;
                Object cwd = opts.get("cwd");
                if (cwd instanceof String) builder.directory(new java.io.File((String) cwd));
                Object env = opts.get("env");
                if (env instanceof java.util.Map) {
                    builder.environment().clear();
                    for (java.util.Map.Entry<?, ?> entry : ((java.util.Map<?, ?>) env).entrySet()) {
                        builder.environment().put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
                    }
                }
                Object shell = opts.get("shell");
                if (Boolean.TRUE.equals(shell)) {
                    java.util.List<String> shellLine = new java.util.ArrayList<>();
                    shellLine.add("/bin/sh");
                    shellLine.add("-c");
                    shellLine.add(String.join(" ", commandLine));
                    builder.command(shellLine);
                }
            }
            builder.redirectInput(java.lang.ProcessBuilder.Redirect.from(new java.io.File("/dev/null")));
            child.process = builder.start();
            Thread out = new Thread(() -> __pump(child.process.getInputStream(), child.stdout));
            Thread err = new Thread(() -> __pump(child.process.getErrorStream(), child.stderr));
            out.setDaemon(true); err.setDaemon(true); out.start(); err.start();
            Thread waiter = new Thread(() -> {
                try {
                    boolean finished = child.process.waitFor(60, java.util.concurrent.TimeUnit.SECONDS);
                    int code;
                    if (!finished) {
                        child.process.descendants().forEach(ProcessHandle::destroyForcibly);
                        child.process.destroyForcibly();
                        code = 124;
                    } else {
                        code = child.process.exitValue();
                    }
                    child.exitCode = code;
                    child.connected = false;
                    child.fire("exit", code, null);
                    child.fire("close", code, null);
                } catch (Throwable failed) {
                    failed.printStackTrace();
                    child.fire("error", failed);
                }
            });
            waiter.setDaemon(true); waiter.start();
        } catch (Throwable failure) {
            child.connected = false;
            child.fire("error", failure);
        }
        return child;
    }

    public static Object spawnImpl = (java.util.function.Function<Object, Object>) (command) ->
        (java.util.function.Function<Object, Object>) (args) ->
            (java.util.function.Supplier<Object>) () -> __spawn((String) command, args, null);

    public static Object spawnOptsImpl = (java.util.function.Function<Object, Object>) (command) ->
        (java.util.function.Function<Object, Object>) (args) ->
        (java.util.function.Function<Object, Object>) (options) ->
            (java.util.function.Supplier<Object>) () -> __spawn((String) command, args, options);

    public static Object unsafeStdin = (java.util.function.Function<Object, Object>) (child) ->
        ((ChildProcessValue) child).stdin;

    public static Object unsafeStdout = (java.util.function.Function<Object, Object>) (child) ->
        ((ChildProcessValue) child).stdout;

    public static Object unsafeStderr = (java.util.function.Function<Object, Object>) (child) ->
        ((ChildProcessValue) child).stderr;

    private static java.util.function.Supplier<Object> __unsupported(String api) {
        return (java.util.function.Supplier<Object>) () -> {
            throw new UnsupportedOperationException(
                "node-child-process: " + api + " is not implemented on JVM");
        };
    }

    public static Object execSyncImpl = (java.util.function.Function<Object, Object>) (command) -> __unsupported("execSync");
    public static Object execSyncOptsImpl = (java.util.function.Function<Object, Object>) (command) ->
        (java.util.function.Function<Object, Object>) (opts) -> __unsupported("execSync(options)");
    public static Object execImpl = (java.util.function.Function<Object, Object>) (command) -> __unsupported("exec");
    public static Object execOptsImpl = (java.util.function.Function<Object, Object>) (command) ->
        (java.util.function.Function<Object, Object>) (opts) -> __unsupported("exec(options)");
    public static Object execCbImpl = (java.util.function.Function<Object, Object>) (command) ->
        (java.util.function.Function<Object, Object>) (callback) -> __unsupported("exec(callback)");
    public static Object execOptsCbImpl = (java.util.function.Function<Object, Object>) (command) ->
        (java.util.function.Function<Object, Object>) (opts) ->
        (java.util.function.Function<Object, Object>) (callback) -> __unsupported("exec(options, callback)");
    public static Object execFileSyncImpl = (java.util.function.Function<Object, Object>) (file) ->
        (java.util.function.Function<Object, Object>) (args) -> __unsupported("execFileSync");
    public static Object execFileSyncOptsImpl = (java.util.function.Function<Object, Object>) (file) ->
        (java.util.function.Function<Object, Object>) (args) ->
        (java.util.function.Function<Object, Object>) (opts) -> __unsupported("execFileSync(options)");
    public static Object execFileImpl = (java.util.function.Function<Object, Object>) (file) ->
        (java.util.function.Function<Object, Object>) (args) -> __unsupported("execFile");
    public static Object execFileOptsImpl = (java.util.function.Function<Object, Object>) (file) ->
        (java.util.function.Function<Object, Object>) (args) ->
        (java.util.function.Function<Object, Object>) (opts) -> __unsupported("execFile(options)");
    public static Object execFileCbImpl = (java.util.function.Function<Object, Object>) (file) ->
        (java.util.function.Function<Object, Object>) (args) ->
        (java.util.function.Function<Object, Object>) (callback) -> __unsupported("execFile(callback)");
    public static Object execFileOptsCbImpl = (java.util.function.Function<Object, Object>) (file) ->
        (java.util.function.Function<Object, Object>) (args) ->
        (java.util.function.Function<Object, Object>) (opts) ->
        (java.util.function.Function<Object, Object>) (callback) -> __unsupported("execFile(options, callback)");
    public static Object spawnSyncImpl = (java.util.function.Function<Object, Object>) (command) ->
        (java.util.function.Function<Object, Object>) (args) -> __unsupported("spawnSync");
    public static Object spawnSyncOptsImpl = (java.util.function.Function<Object, Object>) (command) ->
        (java.util.function.Function<Object, Object>) (args) ->
        (java.util.function.Function<Object, Object>) (opts) -> __unsupported("spawnSync(options)");
    public static Object forkImpl = spawnImpl;
    public static Object forkOptsImpl = spawnOptsImpl;
    public static Object sendImpl = (java.util.function.Function<Object, Object>) (child) ->
        (java.util.function.Function<Object, Object>) (message) ->
        (java.util.function.Function<Object, Object>) (handle) -> __unsupported("send");
    public static Object sendOptsImpl = (java.util.function.Function<Object, Object>) (child) ->
        (java.util.function.Function<Object, Object>) (message) ->
        (java.util.function.Function<Object, Object>) (handle) ->
        (java.util.function.Function<Object, Object>) (opts) -> __unsupported("send(options)");
    public static Object sendCbImpl = (java.util.function.Function<Object, Object>) (child) ->
        (java.util.function.Function<Object, Object>) (message) ->
        (java.util.function.Function<Object, Object>) (handle) ->
        (java.util.function.Function<Object, Object>) (callback) -> __unsupported("send(callback)");
    public static Object sendOptsCbImpl = (java.util.function.Function<Object, Object>) (child) ->
        (java.util.function.Function<Object, Object>) (message) ->
        (java.util.function.Function<Object, Object>) (handle) ->
        (java.util.function.Function<Object, Object>) (opts) ->
        (java.util.function.Function<Object, Object>) (callback) -> __unsupported("send(options, callback)");
    public static Object unsafeChannelRefImpl = (java.util.function.Function<Object, Object>) (child) ->
        __unsupported("channel.ref");
    public static Object unsafeChannelUnrefImpl = (java.util.function.Function<Object, Object>) (child) ->
        __unsupported("channel.unref");
