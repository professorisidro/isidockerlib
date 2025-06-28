package br.com.isiflix.isiroku.isidockerlib.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import br.com.isiflix.isiroku.isidockerlib.dto.*;
import br.com.isiflix.isiroku.isidockerlib.platform.OperatingSystem;

public class IsiDockerService {

	private static String DOCKER_PATH="";
	public static final String LIST_RUNNING_CONTAINERS = "stats --no-stream --format \"{{.ID}}\\t{{.Name}}\\t{{.CPUPerc}}\\t{{.MemUsage}}\\t{{.MemPerc}}\"";
	public static final String LIST_ALL_CONTAINERS = "ps -a ";
	public static final String LIST_ALL_IMAGES = "image ls --format \"{{.Repository}}\\t{{.Tag}}\\t{{.ID}}\\t{{.Size}}\"";
	public static final String START_CONTAINER = "run";
	public static final String STOP_CONTAINER = "stop";
	public static final String REMOVE_CONTAINER = "rm";
	public static final String REMOVE_IMAGE = "image rm";
	public static final String LOG_CONTAINER = "logs";
	
	
	public IsiDockerService() {
		// TODO - Identificar o sistema operacional e o caminho onde o DOCKER está instalado
		DOCKER_PATH = locateDockerBinary();
//		System.out.println(DOCKER_PATH);
	}

	public List<ContainerInfo> listContainers() {
		String dockerOutput = runDockerCommand(DOCKER_PATH +" "+ LIST_RUNNING_CONTAINERS);
		String lines[] = dockerOutput.split("\n");
		List<ContainerInfo> containerInfo = new ArrayList<>();
		for (String result : lines) {
			if (!result.isBlank() && !result.isEmpty()) {
				result = result.replaceAll("\"", "");
				String resultFields[] = result.split("\t");
				containerInfo.add(new ContainerInfo(resultFields[0], resultFields[1], resultFields[2], resultFields[3],
						resultFields[4]));
			}
		}
		return containerInfo;
	}

	public List<ImageInfo> listAllImages() {
		String dockerOtput = runDockerCommand(DOCKER_PATH + " "+ LIST_ALL_IMAGES);
		String lines[] = dockerOtput.split("\n");
		List<ImageInfo> imageInfo = new ArrayList<>();
		for (String result : lines) {
			if (!result.isBlank() && !result.isEmpty()) {
				result = result.replaceAll("\"", "");
				String resultFields[] = result.split("\t");
					imageInfo.add(new ImageInfo(resultFields[0], resultFields[1], resultFields[2], resultFields[3]));
			}
		}
		System.out.println(imageInfo);
		return imageInfo;
	}

	public ContainerData runContainer(String params) {
		String outpString = runDockerCommand(DOCKER_PATH+" "+START_CONTAINER + " " + params);
		return new ContainerData(outpString);
	}

	public ContainerData stopContainer(String container) {
		System.out.println("Stopping Container " + container);
		String output = runDockerCommand(DOCKER_PATH+" "+STOP_CONTAINER + " " + container);
		return new ContainerData(output);
	}

	public ContainerData removeContainer(String container) {
		String output = runDockerCommand(DOCKER_PATH+" "+REMOVE_CONTAINER + " " + container);
		return new ContainerData(output);
	}

	public ContainerLogs getContainerLogs(String containerId) {
		String output = runDockerCommand(DOCKER_PATH + " " + LOG_CONTAINER + " " + containerId);
		if (output.toLowerCase().contains("no such container")) {
			throw new IllegalArgumentException("Container Não encontrado");
		}

		return new ContainerLogs(
				containerId,
				output,
				System.currentTimeMillis()
		);
	}


	public ContainerData stopAndRemoveContainer(String container) {
		ContainerData data = stopContainer(container);
		data = removeContainer(container);
		return data;
	}
	
	public ImageData removeImage(String image) {
		String imageData = runDockerCommand(DOCKER_PATH+" "+REMOVE_IMAGE+ " "+image);
		return new ImageData(imageData);
	}

	public String runDockerCommand(String command) {
		ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
		processBuilder.redirectErrorStream(true);
		try {
			Process process = processBuilder.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			StringBuilder output = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				output.append(line).append("\n");
			}
			int exitCode = process.waitFor();
			
//			if (exitCode != 0) {
//				throw new RuntimeException("Comando falhou com erro " + exitCode);
//			}
			return output.toString();
		} catch (Exception ex) {
			//System.out.println(ex.getMessage());
			ex.printStackTrace();
			throw new RuntimeException("Erro ao executar " + command);
		}
	}
	
	private String locateDockerBinary() {
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
