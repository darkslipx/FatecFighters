# 🥊 Fatec Fighters

![Java](https://img.shields.io/badge/Java-ED8B00?logo=openjdk&logoColor=white)
![libGDX](https://img.shields.io/badge/libGDX-1.13-E74A45)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?logo=postgresql&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-02303A?logo=gradle&logoColor=white)

Jogo de luta 2D local para dois jogadores, feito em **Java com libGDX**. Tem login de usuário, placar persistido em **PostgreSQL** e ranking de pontuações.

Projeto acadêmico (Projeto Interdisciplinar — Fatec).

## Sumário

- [Funcionalidades](#-funcionalidades)
- [Controles](#-controles)
- [Stack](#️-stack)
- [Arquitetura](#-arquitetura)
- [Como executar](#️-como-executar)
- [Estrutura do projeto](#-estrutura-do-projeto)

## ✨ Funcionalidades

- **Combate 1v1 local** — dois jogadores no mesmo teclado, com movimento, pulo (com gravidade) e três ataques de dano diferente (50, 100 e 200)
- **Detecção de colisão** entre a área do golpe e a hitbox do adversário
- **Barra de vida** (1000 HP) e **temporizador de 3 minutos** por partida
- **Pontuação** — +10 pontos a cada golpe acertado
- **Login e cadastro de usuário** com dados salvos no banco
- **Ranking** — pontuações gravadas ao fim da partida e exibidas em ordem decrescente
- Fluxo de telas: Login → Menu → Partida → Fim de jogo → Ranking

## 🎮 Controles

| Ação | Jogador 1 | Jogador 2 |
|---|---|---|
| Mover | `A` / `D` | `←` / `→` |
| Pular | `W` | `↑` |
| Ataque leve (50) | `Q` | `Num 1` |
| Ataque médio (100) | `E` | `Num 2` |
| Ataque forte (200) | `R` | `Num 3` |

## 🛠️ Stack

| Camada | Tecnologias |
|---|---|
| Jogo | Java, libGDX 1.13 (Scene2D UI, BitmapFont, Texture) |
| Desktop | LWJGL3 |
| Persistência | PostgreSQL via JDBC, padrão DAO |
| Build | Gradle (wrapper incluso), estrutura gerada com gdx-liftoff |

## 🧱 Arquitetura

```
Main (Game)
 ├── TelaLogin        → autenticação/cadastro   ─┐
 ├── TelaMenu         → iniciar partida / ranking │
 ├── TelaJogo         → loop do jogo: input, física, colisão, HUD
 ├── TelaFimJogo      → resultado e gravação da pontuação
 └── TelaPontuacoes   → ranking                  │
                                                  ▼
                    UsuarioDAO / PontuacaoDAO → DatabaseConnection (JDBC) → PostgreSQL
```

Cada tela é um `Screen` do libGDX; o acesso a dados fica isolado em DAOs com `PreparedStatement`.

## ▶️ Como executar

**Pré-requisitos:** JDK 8+ e PostgreSQL. O Gradle é baixado automaticamente pelo wrapper.

**1. Crie o banco**

```sql
CREATE DATABASE jogo;

\c jogo

CREATE TABLE usuarios (
    id            SERIAL PRIMARY KEY,
    nome_usuario  VARCHAR(50) UNIQUE NOT NULL,
    senha         VARCHAR(100) NOT NULL
);

CREATE TABLE pontuacoes (
    id          SERIAL PRIMARY KEY,
    id_usuario  INT REFERENCES usuarios(id),
    pontuacao   INT NOT NULL,
    data        TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

**2. Configure a conexão** em `core/src/main/java/io/github/Inter_Project_FatecFighters/DatabaseConnection.java` (URL, usuário e senha).

**3. Rode o jogo**

```bash
git clone https://github.com/darkslipx/FatecFighters.git
cd FatecFighters
./gradlew lwjgl3:run          # Windows: gradlew.bat lwjgl3:run
```

Para gerar um `.jar` executável: `./gradlew lwjgl3:jar` (saída em `lwjgl3/build/libs`).

## 📁 Estrutura do projeto

```
FatecFighters/
├── assets/          # sprites, fundo, fontes e skin da UI
├── core/            # lógica do jogo (telas, DAOs, conexão)
│   └── src/main/java/io/github/Inter_Project_FatecFighters/
├── lwjgl3/          # launcher desktop
├── build.gradle
└── settings.gradle
```

## 👤 Autor

**Abner Evandro Duarte** — [@darkslipx](https://github.com/darkslipx)

## 📄 Licença

Projeto desenvolvido para fins acadêmicos e de estudo.
