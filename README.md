# Desafio Técnico SICREDI

Este projeto tem como proposito demonstrar habilidades de programação, desenvolvido com as tecnologias:

* Linguagem Java 21
* Framework Spring Boot
* Arquitetura adaptada de Clean Architecture
* Gerenciador de Dependencias Gradle
* Documentação de API Springdoc OpenAPI
* Banco de Dados Postgresql v16
* Virtualização Docker
* Testes Automatizados com JUnit e Mockito

## 1. Pré-requisitos

Antes de começar, verifique se você possui as seguintes ferramentas instaladas:

- **Java 21** (JDK 21 ou superior)
- **Git** (para versionamento de código)

## 2. Rodando o projeto localmente

### Variáveis de Ambiente

Para rodar o projeto localmente, você precisa configurar algumas variáveis de ambiente para a conexão com o banco de dados.

Você pode configurar as variáveis de ambiente de acordo com o seu sistema operacional ou direto no comando start do projeto

Para setar variáveis de ambiente no Mac ou Linux
```bash
export DB_URL=jdbc:postgresql://seu_host:5432/seu_banco
export DB_USERNAME=seu_usuario
export DB_PASSWORD=sua_senha
```

Para setar variáveis de ambiente no Windows
```bash
set DB_URL=jdbc:postgresql://seu_host:5432/seu_banco
set DB_USERNAME=seu_usuario
set DB_PASSWORD=sua_senha
```

**Observação:** Substitua `seu_host`, `seu_banco`, `seu_usuario` e `sua_senha` pelos valores reais de seu banco de dados.

### Rodando o aplicativo

Com as variáveis de ambiente configuradas, você pode rodar o projeto localmente com o seguinte comando:

Comando para Mac e Linux
```bash
./gradlew bootRun
```

Comando para Windows
```bash
./gradlew.bat bootRun
```

Isso iniciará o servidor localmente na porta `8080`, você pode acessar API a partir do endereço `http://localhost:8080`.

### Configurações adicionais

Para demais configurações do projeto, você pode ajustar no arquivo `src/main/resources/application.yml`.

## 3. Construindo o projeto

Para gerar o pacote executável (JAR) do projeto, use o seguinte comando:

```bash
./gradlew build
```

Este comando irá compilar o projeto e gerar o arquivo JAR na pasta `build/libs/`.

Após isso, você pode rodar o arquivo JAR gerado da seguinte maneira:

```bash
java -jar build/libs/cooperative-vote-{version}.jar
```

## 4. Rodando os testes e checagem

Para rodar os testes automatizados e demais checks do projeto, use o comando:

```bash
./gradlew check
```

Isso executará todos os testes locais e exibirá os resultados no terminal. Caso algum teste ou check falhe, o Gradle mostrará detalhes sobre a falha.

## 5. Verificando a cobertura dos testes

Se o projeto estiver configurado para gerar um relatório de cobertura de testes com o **JaCoCo**, você pode verificar a cobertura executando o seguinte comando:

```bash
./gradlew test jacocoTestReport
```

Após a execução, o relatório de cobertura será gerado na pasta `build/reports/jacoco/test/html/`. Abra o arquivo `index.html` em um navegador para visualizar o relatório de cobertura.

## 6. Visualizando a documentação da API

Com o projeto rodando, acesse a documentação da API em:

- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8080/v3/api-docs`

## 7. Outras operações úteis

### Limpeza do projeto

Caso você precise limpar os artefatos de build anteriores, use:

```bash
./gradlew clean
```

### Gerar um relatório detalhado de dependências

Para gerar um relatório de dependências do Gradle, use o comando:

```bash
./gradlew dependencies
```

## Licença

Este projeto está licenciado sob a licença MIT - veja o arquivo LICENSE para mais detalhes.
