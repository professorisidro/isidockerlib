package br.com.isiflix.isiroku.isidockerlib.comands;

public class DockerComands {
    public static final String LIST_RUNNING_CONTAINERS = "stats --no-stream --format \"{{.ID}}\\t{{.Name}}\\t{{.CPUPerc}}\\t{{.MemUsage}}\\t{{.MemPerc}}\"";
    public static final String LIST_ALL_CONTAINERS = "ps -a ";
    public static final String LIST_ALL_IMAGES = "image ls --format \"{{.Repository}}\\t{{.Tag}}\\t{{.ID}}\\t{{.Size}}\"";
    public static final String START_CONTAINER = "run";
    public static final String STOP_CONTAINER = "stop";
    public static final String REMOVE_CONTAINER = "rm";
    public static final String REMOVE_IMAGE = "image rm";
    public static final String GET_CONTAINER_HEALTH = "inspect %s --format {{.State.Health.Status}}";

}
