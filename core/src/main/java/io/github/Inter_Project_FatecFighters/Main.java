package io.github.Inter_Project_FatecFighters;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class Main extends ApplicationAdapter {
    SpriteBatch batch;
    Texture background;
    Texture player1;
    Texture player2;
    Texture attackTexture1; // Imagem de ataque 1
    Texture attackTexture2; // Imagem de ataque 2
    Texture attackTexture3; // Imagem de ataque 3
    ShapeRenderer shapeRenderer;

    OrthographicCamera camera;
    Viewport viewport;

    float player1X = -500; // Posição inicial do player 1
    float player1Y = 200; // Posição vertical do player 1
    float player2X; // Posição inicial do player 2
    float player2Y; // Posição vertical do player 2
    float moveSpeed = 10; // Velocidade de movimento dos jogadores

    // Variáveis das vidas
    float player1Health = 100; // Vida do player 1
    float player2Health = 100; // Vida do player 2
    float maxHealth = 100; // Vida máxima para os dois jogadores

    // Configuração do pulo dos jogadores
    float jumpSpeed = 17; // Velocidade do pulo
    float gravity = 0.8f; // Força da gravidade

    // Variáveis para pulo do player 1
    boolean isJumpingP1 = false; // Estado do pulo do player 1
    float velocityY1 = 0; // Velocidade vertical do player 1

    // Variáveis para pulo do player 2
    boolean isJumpingP2 = false; // Estado do pulo do player 2
    float velocityY2 = 0; // Velocidade vertical do player 2

    // Variável de ataque
    private boolean isAttacking = false; // Estado de ataque
    private Texture currentAttackTexture; // Imagem de ataque atual

    @Override
    public void create() {
        batch = new SpriteBatch();
        background = new Texture("background.png");
        player1 = new Texture("Player1.png");
        player2 = new Texture("Player2.png");
        attackTexture1 = new Texture("Attack.png"); // Textura do ataque 1
        attackTexture2 = new Texture("Attack2.png"); // Textura do ataque 2
        attackTexture3 = new Texture("Attack.png"); // Textura do ataque 3
        shapeRenderer = new ShapeRenderer();

        camera = new OrthographicCamera();
        viewport = new FitViewport(1920, 1080, camera);
        viewport.apply();
        camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);

        // Inicializa a posição do player 2
        float scale = 3.5f;
        float player2Width = player2.getWidth() * scale;
        player2X = viewport.getWorldWidth() - player2Width - 350;
        player2Y = player1Y;
    }

    @Override
    public void render() {
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();

        // Movimentação player 1
        handlePlayerMovement();

        // Renderização
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(background, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());

        float scale = 3.5f; // Fator de escala
        float player1Width = player1.getWidth() * scale;
        float player1Height = player1.getHeight() * scale;
        float player2Width = player2.getWidth() * scale;
        float player2Height = player2.getHeight() * scale;

        // Desenhar imagem do ataque ou do jogador normal
        if (isAttacking) {
            batch.draw(currentAttackTexture, player1X, player1Y, player1Width, player1Height);
        } else {
            batch.draw(player1, player1X, player1Y, player1Width, player1Height);
        }

        // Desenhar o player 2
        batch.draw(player2, player2X, player2Y, player2Width, player2Height);
        batch.end();

        // Desenhar as barras de vida
        drawHealthBars();
    }

    private void handlePlayerMovement() {
        // Movimentação player 1
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.W)) {
            player1Y += 5; // Move para cima
        }
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.S)) {
            player1Y -= 5; // Move para baixo
        }
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.A)) {
            player1X -= moveSpeed; // Move para a esquerda
        }
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.D)) {
            player1X += moveSpeed; // Move para a direita
        }

        // Verificação de limites para player 1
        player1X = Math.max(-1050, Math.min(player1X, viewport.getWorldWidth() - (player1.getWidth() * 1.85f)));
        player1Y = Math.max(200, player1Y); // Mantém o player1 acima do chão

        // Pulo Player 1
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.W) && !isJumpingP1) {
            isJumpingP1 = true; // Inicia o pulo
            velocityY1 = jumpSpeed; // Define a velocidade do pulo
        }
        if (isJumpingP1) {
            player1Y += velocityY1; // Aplica a velocidade vertical
            velocityY1 -= gravity; // Aplica a gravidade
            if (player1Y <= 200) { // Verifica se o personagem atingiu o chão
                player1Y = 200; // Reseta a posição
                isJumpingP1 = false; // Permite pular novamente
                velocityY1 = 0; // Reseta a velocidade vertical
            }
        }

        // Verifica se um ataque foi realizado
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.Q) && !isAttacking) {
            isAttacking = true;
            currentAttackTexture = attackTexture1; // Define a textura do ataque 1
        }
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.E) && !isAttacking) {
            isAttacking = true;
            currentAttackTexture = attackTexture2; // Define a textura do ataque 2
        }
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.R) && !isAttacking) {
            isAttacking = true;
            currentAttackTexture = attackTexture3; // Define a textura do ataque 3
        }

        // Resetando o ataque após um pequeno tempo
        if (isAttacking) {
            // Dê um pequeno delay antes de permitir um novo ataque
            // Você pode ajustar o tempo aqui
            if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.Q) ||
                Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.E) ||
                Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.R)) {
                return; // Permite manter o ataque ativo
            }
            isAttacking = false; // Reseta o estado de ataque
        }

        // Movimentação player 2
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.UP)) {
            player2Y += 5; // Move para cima
        }
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.DOWN)) {
            player2Y -= 5; // Move para baixo
        }
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.LEFT)) {
            player2X -= moveSpeed; // Move para a esquerda
        }
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.RIGHT)) {
            player2X += moveSpeed; // Move para a direita;
        }

        player2X = Math.max(-1850, Math.min(player2X, viewport.getWorldWidth() - (player1.getWidth() * 3.1f)));
        player2Y = Math.max(200, player2Y); // Mantém o player2 acima do chão

        // Pulo Player 2
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.UP) && !isJumpingP2) {
            isJumpingP2 = true; // Inicia o pulo
            velocityY2 = jumpSpeed; // Define a velocidade do pulo
        }
        if (isJumpingP2) {
            player2Y += velocityY2; // Aplica a velocidade vertical
            velocityY2 -= gravity; // Aplica a gravidade
            if (player2Y <= 200) { // Verifica se o personagem atingiu o chão
                player2Y = 200; // Reseta a posição
                isJumpingP2 = false; // Permite pular novamente
                velocityY2 = 0; // Reseta a velocidade vertical
            }
        }
    }

    private void drawHealthBars() {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        float healthBarY = viewport.getWorldHeight() - 32; // Posição fixa no topo
        float healthBarHeight = 30; // Altura da barra
        float healthBarWidth = viewport.getWorldWidth() * 0.4f; // 40% da largura da tela

        // Ajustar largura da barra de vida do player 1
        float healthBarWidth1 = Math.min((player1Health / maxHealth) * healthBarWidth, healthBarWidth);
        float healthBarX1 = 50; // Deslocamento à direita
        shapeRenderer.setColor(0, 0, 0, 1); // Cor Preta para a barra de fundo
        shapeRenderer.rect(healthBarX1, healthBarY, healthBarWidth, healthBarHeight); // Barra de fundo
        shapeRenderer.setColor(0, 1, 0, 1); // Cor verde para a vida
        shapeRenderer.rect(healthBarX1, healthBarY, healthBarWidth1, healthBarHeight); // Barra de vida do player 1

        // Posição e dimensões da barra do player 2
        float healthBarX2 = viewport.getWorldWidth() - healthBarWidth1 - 50; // Deslocamento à direita para player 2
        float healthBarWidth2 = Math.min((player2Health / maxHealth) * healthBarWidth1, healthBarWidth1); // Largura ajustada para player 2
        shapeRenderer.setColor(0, 0, 0, 1); // Cor preta para a barra de fundo
        shapeRenderer.rect(healthBarX2, healthBarY, healthBarWidth1, healthBarHeight); // Barra de fundo
        shapeRenderer.setColor(1, 0, 0, 1); // Cor vermelha para a vida
        shapeRenderer.rect(healthBarX2, healthBarY, healthBarWidth2, healthBarHeight); // Barra de vida do player 2

        shapeRenderer.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        background.dispose();
        player1.dispose();
        player2.dispose();
        attackTexture1.dispose();
        attackTexture2.dispose();
        attackTexture3.dispose();
        shapeRenderer.dispose();
    }
}
