package br.com.isiflix.isiroku.isidockerlib;

import java.util.Scanner;

import br.com.isiflix.isiroku.isidockerlib.dto.ContainerLogs;
import br.com.isiflix.isiroku.isidockerlib.service.IsiDockerService;

public class MainClass {
	public static void main(String[] args) {
		Scanner teclado = new Scanner(System.in);
		
		
		IsiDockerService dockerService = new IsiDockerService();
		
		
		//String params = "-d -it --name meuMysql -e MYSQL_ROOT_PASSWORD=12345 -p3310:3306 mysql:8.4";
//		System.out.println(dockerService.runContainer(params));
		
//		System.out.println(dockerService.listContainers());
//		teclado.nextLine();
//		System.out.println(dockerService.listAllImages());

		ContainerLogs logs = dockerService.getContainerLogs("01c23fd04352");
		System.out.println("Id do container:" + logs.containerId());
		System.out.println("Logs do container:" + logs.logContent());
		System.out.println("Logs coletado em: " + logs.timeStamp());


//		System.out.println("Digite o ID do container para parar");
//		String id = teclado.nextLine();
//		
//		System.out.println(dockerService.stopContainer(id));
//		teclado.nextLine();
//		System.out.println(dockerService.removeContainer(id));
				
	}
}
