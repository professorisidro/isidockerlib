package br.com.isiflix.isiroku.isidockerlib.enums;

public enum ContainerHealthEnum {

    NONE("none"),
    STARTING("starting"),
    HEALTHY("healthy"),
    UNHEALTHY("unhealthy"),
    UNKNOWN("unknown");

    private final String status;

    ContainerHealthEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public static ContainerHealthEnum from(String value) {
        for (ContainerHealthEnum e : values()) {
            if (e.status.equalsIgnoreCase(value)) {
                return e;
            }
        }
        return NONE;
    }

}
