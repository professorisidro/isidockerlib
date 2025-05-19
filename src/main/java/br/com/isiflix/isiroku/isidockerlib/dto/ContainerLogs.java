package br.com.isiflix.isiroku.isidockerlib.dto;

public record ContainerLogs(
        String containerId,
        String logContent,
        Long timeStamp
) {
}