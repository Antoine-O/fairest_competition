package com.qc.competition.service.structure;

public enum Version {
    VERSION_1_0(1, 0), VERSION_2_0(2, 0);
    int major;
    int minor;

    Version(int major, int minor) {
        this.major = major;
        this.minor = minor;
    }

    public static Version parse(String versionString) {
        int major = Integer.parseInt(versionString.substring(0, versionString.indexOf(".")));
        int minor = Integer.parseInt(versionString.substring(versionString.indexOf(".") + 1));
        Version versionResult = null;
        for (Version version : Version.values()) {
            if (version.minor == minor && version.major == major) {
                versionResult = version;
            }
        }
        return versionResult;
    }

    @Override
    public String toString() {
        return major + "." + minor;
    }
}
