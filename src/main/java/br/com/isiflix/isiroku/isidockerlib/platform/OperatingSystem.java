package br.com.isiflix.isiroku.isidockerlib.platform;

public enum OperatingSystem {
    WINDOWS, MAC, LINUX, OTHER;

    private static final OperatingSystem CURRENT_OS;

    static {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win")) {
            CURRENT_OS = WINDOWS;
        } else if (osName.contains("mac")) {
            CURRENT_OS = MAC;
        } else if (osName.contains("nix") || osName.contains("nux") || osName.contains("aix")) {
            CURRENT_OS = LINUX;
        } else {
            CURRENT_OS = OTHER;
        }
    }

    public static OperatingSystem get() {
        return CURRENT_OS;
    }

    public static boolean isWindows() {
        return CURRENT_OS == WINDOWS;
    }

    public static boolean isMac() {
        return CURRENT_OS == MAC;
    }

    public static boolean isLinux() {
        return CURRENT_OS == LINUX;
    }

    public static boolean isOther() {
        return CURRENT_OS == OTHER;
    }
}