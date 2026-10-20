    public static Object showKillSignal = (java.util.function.Function<Object, Object>) (signal) ->
        String.valueOf(signal);

    public static Object showShell = (java.util.function.Function<Object, Object>) (shell) ->
        String.valueOf(shell);

    public static Object fromKillSignalImpl = (java.util.function.Function<Object, Object>) (fromInt) ->
        (java.util.function.Function<Object, Object>) (fromString) ->
        (java.util.function.Function<Object, Object>) (signal) -> {
            if (signal instanceof Number) return ((java.util.function.Function<Object, Object>) fromInt)
                .apply(((Number) signal).intValue());
            return ((java.util.function.Function<Object, Object>) fromString).apply(String.valueOf(signal));
        };
