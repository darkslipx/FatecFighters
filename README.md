# InterProject2

**InterProject2** é um jogo 2D desenvolvido com o framework LibGDX. Este projeto utiliza a estrutura **gdx-liftoff** para facilitar o desenvolvimento de jogos com suporte multiplataforma. O jogo possui uma interface gráfica simples e pode ser executado em diferentes plataformas, como desktop, usando o **LWJGL3**.

## Plataformas

O projeto está dividido em dois módulos principais:

- **core**: Módulo principal que contém a lógica do jogo, compartilhada entre todas as plataformas.
- **lwjgl3**: Plataforma principal de desktop utilizando **LWJGL3**, anteriormente chamada de 'desktop'. Este módulo contém os detalhes específicos de implementação para desktop.

## Estrutura de Diretórios

O projeto está organizado da seguinte maneira:

- `core/`: Lógica do jogo compartilhada entre todas as plataformas.
- `lwjgl3/`: Implementação específica para a plataforma desktop com LWJGL3.
- `assets/`: Contém recursos como imagens, sons, fontes, etc.
- `build.gradle`: Arquivo de configuração do Gradle.

## Requisitos de Sistema

- **Java JDK 8 ou superior**
- **Gradle 6.x ou superior**
- **LibGDX** (incluído no projeto via Gradle)

## Agradecimentos

- **LibGDX**: Framework utilizado para o desenvolvimento do jogo.
- **gdx-liftoff**: Template utilizado para gerar a estrutura do projeto.
- **Font Awesome**: Usado para os ícones no jogo.


## Como Executar

Siga estas etapas para executar o projeto localmente:

1. Clone o repositório:
    ```bash
    git clone https://github.com/darkslipx/FatecFighters.git
    ```

2. Acesse o diretório do projeto:
    ```bash
    cd FatecFighters
    ```

3. Para compilar e executar o jogo no ambiente **desktop (lwjgl3)**, execute o seguinte comando:
    ```bash
    ./gradlew lwjgl3:run
    ```

4. Para compilar o projeto em um arquivo JAR executável:
    ```bash
    ./gradlew lwjgl3:jar
    ```

## Instalação do Gradle

Se o **Gradle** não estiver instalado na sua máquina, siga estas etapas para instalá-lo:

### Para Windows:

1. Baixe o **Gradle** em [https://gradle.org/install/](https://gradle.org/install/).
2. Extraia o conteúdo do arquivo ZIP em um diretório de sua escolha.
3. Adicione o caminho do Gradle à variável de ambiente `PATH`:
    - Clique com o botão direito em **Este PC** ou **Meu Computador** e selecione **Propriedades**.
    - Selecione **Configurações avançadas do sistema**.
    - Clique em **Variáveis de ambiente**.
    - Na seção "Variáveis de sistema", localize a variável `Path` e clique em **Editar**.
    - Adicione o caminho para a pasta `bin` do Gradle (por exemplo, `C:\gradle\bin`).
4. Verifique a instalação com:
    ```bash
    gradle -v
    ```

## FAQ

**Q: Como eu faço para rodar o jogo em uma plataforma diferente do desktop?**

**A:** O suporte para plataformas móveis e web ainda não foi implementado. No momento, o jogo funciona apenas no desktop usando o LWJGL3.

