package br.com.isiflix.isiroku.isidockerlib.resolver;

import br.com.isiflix.isiroku.isidockerlib.platform.OperatingSystem;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class DockerPathResolver {

    private static String DOCKER_PATH = resolveDockerBinary();

    public static String getDockerPath() {
        if (DOCKER_PATH == null) {
            DOCKER_PATH = resolveDockerBinary();
        }
        return DOCKER_PATH;
    }

    private static String resolveDockerBinary() {
        List<String> commonPaths = OperatingSystem.isLinux() || OperatingSystem.isMac() ?
                List.of(
                        "/opt/homebrew/bin/docker",
                        "/usr/bin/docker",
                        "/usr/local/bin/docker",
                        "/bin/docker"
                ):
                List.of(
                        "C:\\Program Files\\Docker\\Docker\\resources\\bin\\docker.exe",
                        "C:\\ProgramData\\DockerDesktop\\version-bin\\docker.exe"
                );

        for (String path: commonPaths) {
            if (Files.isExecutable(Paths.get(path))){
                return path;
            }
        }
        throw new RuntimeException("Docker command not found");
    }

}
