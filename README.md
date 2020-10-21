<h1 align="center">Nosso Banco Digital - Zup Bootcamp 2020</h1>

## Descrição do Projeto
<p align="center">Projeto de Banco digital para processo seletivo Bootcamp de Tecnologia ZUP</p>

Tabela de conteúdos
=================
<!--ts-->
   * [Tabela de Conteudo](#tabela-de-conteudo)
   * [Sobre](#Sobre-o-projeto)
   * [Funcionalidades](#Funcionalidades)
   * [Modelo de Dados](#Modelo-De-Dados)
   * [Tecnologias Utilizadas](#Tecnologias-Utilizadas)
   * [Executando o aplicativo localmente](#Executando-o-aplicativo-localmente)
   * [Testes](#Testes)
   * [Questionamentos](#Questionamentos)
   * [Autor](#Autor)
<!--te-->

## Sobre o projeto

Nosso Banco Digital foi desenvolvido como parte do processo seletivo para o **Bootcamp de Tecnologia ZUP**. 

## Funcionalidades

- [x] Um client que deseja abrir uma account no banco pode abrir uma proposal enviando:
  - [x] seus dados básicos;
  - [x] seu endereço;
  - [x] uma cópia do CPF;
- [x] Após criar a proposal, o client revisa a mesma podendo aceitar ou não.
  - [x] Após aceitar a proposal, a mesma é enviada para uma API externa que pode aprovar ou não a abertura de account para o client.
  - [x] Após recusar a proposal, um e-mail é enviado ao client solicitando que ele aprove.
- [x] Caso a API externa aprove a proposal uma account no banco é aberta ao client.
- [x] Possibilidade de receber transferências de valores para as contas.

## Modelo de Dados

O modelo de dados final da aplicação pode ser visto abaixo:

![modelo de dados](https://user-images.githubusercontent.com/11140125/96477243-a27de800-120c-11eb-9c6e-dbdf4afcf85b.png)

## Tecnologias Utilizadas

### Data

* 	[MySQL](https://www.mysql.com/) - Open-Source Relational Database Management System
* 	[H2 Database Engine](https://www.h2database.com/html/main.html) - Java SQL database. Embedded and server modes; in-memory databases

### Desenvolvimento

* 	[JDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) - Java™ Platform, Standard Edition Development Kit
* 	[Spring Boot](https://spring.io/projects/spring-boot) - Framework to ease the bootstrapping and development of new Spring Applications
* 	[Maven](https://maven.apache.org/) - Dependency Management

### Bibliotecas e Plugins
* 	[Swagger](https://swagger.io/) - Open-Source software framework backed by a large ecosystem of tools that helps developers design, build, document, and consume RESTful Web services.

### Outros
* 	[git](https://git-scm.com/) - Free and Open-Source distributed version control system

### Ferramentas externas
*   [Docker](https://www.docker.com/) - Docker is an open platform for developing, shipping, and running applications.
* 	[Postman](https://www.getpostman.com/) - API Development Environment (Testing Docmentation)

## Executando o aplicativo localmente

* O role ativo padrão é **`test`**. No role **`test`**, o aplicativo usa o banco de dados **H2**.

* Em desenvolvimento, o aplicativo utiliza o **MySQL** como banco de dados padrão. Para instanciar o banco de dados em desenvolvimento basta executar o seguinte comando:

```shell
$ docker-compose up
```

Alternativamente, você pode configurar manualmente com os seguintes passos:

* Você precisa ter o **MySQL** instalado em sua máquina para executar o aplicativo no role **`dev`**. Usando o `MySQL Workbench` ou em qualquer outro client/console MySQL, crie um banco de dados chamado **nossobancodigital**

~~~sql
create database nossobancodigital;
~~~

* Posteriormente, basta configurar no arquivo `application-dev.properties` a `url`, o `usuário` e a `senha`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/nossobancodigital
spring.datasource.username=root
spring.datasource.password=
```

### Executando a aplicação com uma IDE

Existem várias maneiras de executar um aplicativo Spring Boot em sua máquina local. Uma maneira é executar o método `main` na classe` com.jocivaldias.nossobancodigital.NossoBancoDigitalApplication` em seu IDE. 

* Baixe o zip ou clone o repositório Git.
* Descompacte o arquivo zip (se você baixou um)
* Abra o prompt de comando e mude o diretório (cd) para a pasta que contém pom.xml
* Abra o Eclipse
* Arquivo -> Importar -> Projeto Maven Existente -> Navegue até a pasta onde você descompactou o zip
* Selecione o projeto
* Escolha o arquivo do aplicativo Spring Boot (procure @SpringBootApplication)
* Clique com o botão direito no arquivo e execute como aplicativo Java

### Executando a aplicação com Maven

Alternativamente, você pode usar o [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html):

```shell
$ git clone https://github.com/jocivaldias/nosso-banco-digital.git
$ cd nosso-banco-digital
$ mvn spring-boot:run
```

## Testes

### Testando com Maven

*	Para executar os testes:
```shell
$ mvn clean test
```

## Documentação

Todos os endpoints foram documentados através do `swagger` e pode ser visto acessando a seguinte url: 

* 	[Swagger](http://localhost:8080/swagger-ui.html) - `http://localhost:8080/swagger-ui.html`- Documentation & Testing

![endpoints](https://user-images.githubusercontent.com/11140125/96477268-ab6eb980-120c-11eb-9d2a-3b760bbd2349.png)

## Questionamentos

*Será que existe algum jeito melhor de tratar esses uploads do que fazer o arquivo chegar pela aplicação?*

Supondo a situação em que o armazenamento será realizado em um bucket S3 da Amazon e você terá que receber em seu backend para posteriormente enviá-lo ao S3, claramente existe um gargalo que provavelmente será a sua aplicação backend. Neste caso, pode ser gerado uma URL AWS S3 previamente assinada para que seus usuários consigam enviar o arquivo diretamente ao S3. Conforme pode ser visto na referência [1], o ganho de desempenho é substancial (além de reduzir consideravelmente o overhead nos servidores do backend).  Um esquema desse processo pode ser visto na figura [2] abaixo:

![esquema](https://user-images.githubusercontent.com/11140125/96661486-8d937880-1322-11eb-8c84-b4411b780f6c.png)

[1] https://softwareontheroad.com/aws-s3-secure-direct-upload/

[2] https://medium.com/@ashan.fernando/upload-files-to-aws-s3-using-signed-urls-fa0a0cf489db

## Autor

Feito por Jocival Dias [Entre em contato!](https://www.linkedin.com/in/jocival-dias-b7941494/)
