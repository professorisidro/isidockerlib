package io.isiflix.isiroku.isidockerlib.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import io.isiflix.isiroku.isidockerlib.dto.ContainerData;
import io.isiflix.isiroku.isidockerlib.dto.ContainerInfo;
import io.isiflix.isiroku.isidockerlib.dto.ImageInfo;

public class IsiDockerService {
	
	public static final String LIST_RUNNING_CONTAINERS="docker stats --no-stream --format \"{{.ID}}\\t{{.Name}}\\t{{.CPUPerc}}\\t{{.MemUsage}}\\t{{.MemPerc}}\"";
	public static final String LIST_ALL_CONTAINERS = "docker ps -a ";
	public static final String LIST_ALL_IMAGES = "docker image ls --format \"{{.Repository}}\\t{{.Tag}}\\t{{.ID}}\\t{{.Size}}\"";
	public static final String START_CONTAINER = "docker run";
	public static final String STOP_CONTAINER = "docker stop";
	public static final String REMOVE_CONTAINER = "docker rm";
	public static final String REMOVE_IMAGE = "docker image rm";
	
	public List<ContainerInfo> listContainers(){
		String dockerOtput = runDockerCommand(LIST_RUNNING_CONTAINERS);
		String lines[] = dockerOtput.split("\n");
		List<ContainerInfo> containerInfo = new ArrayList<>();
		for (String result: lines) {
			String resultFields[] = result.split("\t");
			containerInfo.add(new ContainerInfo(resultFields[0],
					                            resultFields[1],
					                            resultFields[2],
					                            resultFields[3],
					                            resultFields[4]));
		}
		return containerInfo;
	}
	
	public List<ImageInfo> listAllImages(){
		String dockerOtput = runDockerCommand(LIST_ALL_IMAGES);
		String lines[] = dockerOtput.split("\n");
		List<ImageInfo> imageInfo = new ArrayList<>();
		for (String result: lines) {
			String resultFields[] = result.split("\t");
			imageInfo.add(new ImageInfo(resultFields[0],
					                    resultFields[1],
					                    resultFields[2],
					                    resultFields[3]));

		}
		return imageInfo;
	}
	
	public ContainerData runContainer(String params) {
		String outpString = runDockerCommand(START_CONTAINER + " "+params);
		return new ContainerData(outpString);
	}
	
	public ContainerData stopContainer(String container) {
		System.out.println("Stopping Container "+container);
		String output = runDockerCommand(STOP_CONTAINER+ " "+container);
		return new ContainerData(output);
	}
	public ContainerData removeContainer(String container) {
		System.out.println("Removing Container "+container);
		String output = runDockerCommand(REMOVE_CONTAINER+ " "+container);
		return new ContainerData(output);
	}
	public ContainerData stopAndRemoveContainer(String container) {
		ContainerData data = stopContainer(container);
		data = removeContainer(container);
		return data;
	}
	
	
	public String runDockerCommand(String dockerCommand) {
		ProcessBuilder processBuilder = new ProcessBuilder(dockerCommand.split(" "));
		processBuilder.redirectErrorStream(true);
		try {
			Process process = processBuilder.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			StringBuilder output = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println("DEBUG - "+line);
				output.append(line).append("\n");
			}
			int exitCode = process.waitFor();
			if (exitCode != 0) {
				throw new RuntimeException("Comando Docker falhou com erro "+exitCode);
			} 
			return output.toString();
		}
		catch(Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException("Erro ao executar "+dockerCommand);
		}
	}
}
