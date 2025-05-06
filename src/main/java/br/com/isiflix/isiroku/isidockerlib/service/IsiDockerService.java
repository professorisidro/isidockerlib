package br.com.isiflix.isiroku.isidockerlib.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import br.com.isiflix.isiroku.isidockerlib.comands.DockerComands;
import br.com.isiflix.isiroku.isidockerlib.dto.ContainerData;
import br.com.isiflix.isiroku.isidockerlib.dto.ContainerInfo;
import br.com.isiflix.isiroku.isidockerlib.dto.ImageInfo;
import br.com.isiflix.isiroku.isidockerlib.enums.ContainerHealthEnum;
import br.com.isiflix.isiroku.isidockerlib.platform.OperatingSystem;
import br.com.isiflix.isiroku.isidockerlib.resolver.DockerPathResolver;

public class IsiDockerService {

	private static String DOCKER_PATH="";

	public IsiDockerService() {
		DOCKER_PATH = DockerPathResolver.getDockerPath();
		System.out.printf("Docker path resolved - Path: %s%n", DOCKER_PATH);
	}

	public List<ContainerInfo> listContainers() {
		String dockerOutput = runDockerCommand(DOCKER_PATH +" "+ DockerComands.LIST_RUNNING_CONTAINERS);
		String lines[] = dockerOutput.split("\n");
		List<ContainerInfo> containerInfo = new ArrayList<>();
		for (String result : lines) {
			if (!result.isBlank() && !result.isEmpty()) {
				String resultFields[] = result.split("\t");
				containerInfo.add(new ContainerInfo(resultFields[0], resultFields[1], resultFields[2], resultFields[3],
						resultFields[4]));
			}
		}
		return containerInfo;
	}

	public List<ImageInfo> listAllImages() {
		String dockerOtput = runDockerCommand(DOCKER_PATH + " "+ DockerComands.LIST_ALL_IMAGES);
		String lines[] = dockerOtput.split("\n");
		List<ImageInfo> imageInfo = new ArrayList<>();
		for (String result : lines) {
			if (!result.isBlank() && !result.isEmpty()) {
				String resultFields[] = result.split("\t");
				imageInfo.add(new ImageInfo(resultFields[0], resultFields[1], resultFields[2], resultFields[3]));
			}
		}
		return imageInfo;
	}

	public ContainerData runContainer(String params) {
		String outpString = runDockerCommand(DOCKER_PATH+" "+ DockerComands.START_CONTAINER + " " + params);
		return new ContainerData(outpString);
	}

	public ContainerData stopContainer(String container) {
		System.out.println("Stopping Container " + container);
		String output = runDockerCommand(DOCKER_PATH+" "+ DockerComands.STOP_CONTAINER + " " + container);
		return new ContainerData(output);
	}

	public ContainerData removeContainer(String container) {
		String output = runDockerCommand(DOCKER_PATH+" "+ DockerComands.REMOVE_CONTAINER + " " + container);
		return new ContainerData(output);
	}

	public ContainerHealthEnum GetContainerHealthy(String container) {
		String output = runDockerCommand(DOCKER_PATH+" " + String.format(DockerComands.GET_CONTAINER_HEALTH, container));
		output = output.replace("\n", "");
		return ContainerHealthEnum.from(output);
	}


	public ContainerData stopAndRemoveContainer(String container) {
		ContainerData data = stopContainer(container);
		data = removeContainer(container);
		return data;
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
			System.out.println(ex.getMessage());
			ex.printStackTrace();
			throw new RuntimeException("Erro ao executar " + command);
		}
	}
}
