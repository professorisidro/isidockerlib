# IsiDockerLib
Biblioteca Java Open Source para comunicação com Docker Engine

## Propósito
O principal objetivo desta lib é gerenciar, através de aplicações Java o gerenciamento de contêineres docker. Inicialmente o objetivo era comunicar diretamente com a Docker Engine via a Engine API, porém para Sistemas Operacionais Windows isso estava demorando demais para se resolver. Por isso, foi decidido criar a própria lib que executa comandos Docker.

## Características
Atualmente a IsiDockerLib implementa os seguintes comandos Docker
- `docker run` 
- `docker stats`
- `docker image ls`
- `docker stop`
- `docker rm`
- `docker image rm`

## Uso
 Estamos trabalhando para tornar a IsiDockerLib um artefato Maven (assim que tivermos a publicação no maven repository colocaremos o artefato para ser incluso)
 
## Funcionalidades

A IsiDockerLib ao ser instanciada já reconhece o sistema operacional e, baseado em presets, já verifica o diretório onde o comando docker está disponível.

A partir daí, basta usarmos os seguintes métodos:

`listContainers()` - lista todos os contêineres em execução, exibindo
	- memória consumida (em Mb)
	- % de memória em relação ao sistema
	- % de uso de CPU
	
`listAllImages()` - lista todas as imagens disponíveis
`runContainer(params)` - executa um contêiner 
	- `params` - todos os parâmetros para iniciar o contêiner (desde a imagem, porta, volumes, etc)
	
`stopContainer(container)` - para a execução de um Contêiner
`removeContainer(container)` - remove o contêiner que acabou de ser parado
`stopAndRemoveContainer(container)` - para a execução e remove o contêiner



