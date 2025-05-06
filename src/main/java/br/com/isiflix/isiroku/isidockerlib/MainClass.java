package br.com.isiflix.isiroku.isidockerlib;

import java.util.Scanner;

import br.com.isiflix.isiroku.isidockerlib.service.IsiDockerService;

public class MainClass {
	public static void main(String[] args) {

		IsiDockerService dockerService = new IsiDockerService();
		System.out.println(dockerService.GetContainerHealthy("b52090736859"));
		// ACEITA MEU PR AI PROF ISIDRÃO KKKK
				
	}
}
