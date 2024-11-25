package io.github.Inter_Project_FatecFighters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.files.FileHandle;

public class TelaJogo extends com.badlogic.gdx.ScreenAdapter {

    private Main jogo;
    SpriteBatch lote;
    Texture fundo;
    Texture jogador1;
    Texture jogador2;
    Texture texturaAtaque1, texturaAtaque2, texturaAtaque3;
    Texture texturaAtaque4, texturaAtaque5, texturaAtaque6;
    ShapeRenderer renderizadorFormas;

    OrthographicCamera camera;
    Viewport viewport;

    // Posições e velocidades dos jogadores
    float jogador1X, jogador1Y;
    float jogador2X, jogador2Y;
    float velocidadeMovimento = 10;

    // Saúde dos jogadores
    float saudeJogador1 = 1000;
    float saudeJogador2 = 1000;
    float saudeMaxima = 1000;

    // Controle de pulo
    float velocidadePulo = 17;
    float gravidade = 0.8f;
    boolean estaPulandoJ1 = false, estaPulandoJ2 = false;
    float velocidadeY1 = 0, velocidadeY2 = 0;

    // Controle de ataques
    private boolean estaAtacandoJ1 = false;
    private boolean estaAtacandoJ2 = false;
    private float temporizadorAtaqueJ1 = 0, temporizadorAtaqueJ2 = 0;
    private final float intervaloAtaque = 0.5f;
    private Texture texturaAtaqueAtualJ1, texturaAtaqueAtualJ2;
    private Rectangle alcanceAtaqueJ1, alcanceAtaqueJ2;

    private float tempoRestante = 180; // Tempo inicial em segundos
    private BitmapFont fonte; // Fonte para o temporizador

    // Controle de direção dos jogadores
    boolean estaOlhandoDireitaJ1 = true, estaOlhandoDireitaJ2 = true;

    // Pontuações
    private int pontuacaoJogador1 = 0;
    private int pontuacaoJogador2 = 0;

    //usuários conectado
    private String nomeUsuario1;
    private String nomeUsuario2;

    private String nomeUsuarioJogador1;
    private String nomeUsuarioJogador2;



    private String nomeUsuario; // Adicione esta variável

    public TelaJogo(Main jogo, String nomeUsuario1, String nomeUsuario2) {
        this.jogo = jogo;
        this.nomeUsuario1 = nomeUsuario1;
        this.nomeUsuario2 = nomeUsuario2;
    }





    @Override
    public void show() {
        fonte = new BitmapFont(); // Fonte padrão
        fonte.getData().setScale(3); // Aumenta o tamanho da fonte

        lote = new SpriteBatch();
        fundo = new Texture("background.png");
        jogador1 = new Texture("Player1.png");
        jogador2 = new Texture("Player1.png");
        texturaAtaque1 = new Texture("Vampire_Attack1.png");
        texturaAtaque2 = new Texture("Vampire_Attack2.png");
        texturaAtaque3 = new Texture("Vampire_Attack3.png");
        texturaAtaque4 = new Texture("Vampire_Attack1.png");
        texturaAtaque5 = new Texture("Vampire_Attack2.png");
        texturaAtaque6 = new Texture("Vampire_Attack3.png");
        renderizadorFormas = new ShapeRenderer();

        camera = new OrthographicCamera();
        viewport = new FitViewport(1920, 1080, camera);
        viewport.apply();
        camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);

        // Ajuste da escala e posições dos jogadores
        float escala = 3.5f;
        jogador1X = 150;
        jogador1Y = 200;
        jogador2X = viewport.getWorldWidth() - jogador1.getWidth() * escala - 150;
        jogador2Y = 200;

        alcanceAtaqueJ1 = new Rectangle();
        alcanceAtaqueJ2 = new Rectangle();
    }

    @Override
    public void render(float delta) {
        tempoRestante -= delta;
        if (tempoRestante <= 0) {
            tempoRestante = 0; // Impede que fique negativo
            finalizarJogo("Tempo esgotado!");
        }

        // Gerenciar cooldown de ataques
        if (estaAtacandoJ1) {
            temporizadorAtaqueJ1 -= delta;
            if (temporizadorAtaqueJ1 <= 0) {
                estaAtacandoJ1 = false;
            }
        }
        if (estaAtacandoJ2) {
            temporizadorAtaqueJ2 -= delta;
            if (temporizadorAtaqueJ2 <= 0) {
                estaAtacandoJ2 = false;
            }
        }

        // Atualização da câmera e viewport
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();

        movimentarJogador1();
        movimentarJogador2();

        // Limpeza da tela
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        lote.setProjectionMatrix(camera.combined);
        lote.begin();
        lote.draw(fundo, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());

        // Renderizar jogador 1
        renderizarJogador(jogador1, jogador1X, jogador1Y, estaOlhandoDireitaJ1, estaAtacandoJ1, texturaAtaqueAtualJ1);
        // Renderizar jogador 2
        renderizarJogador(jogador2, jogador2X, jogador2Y, estaOlhandoDireitaJ2, estaAtacandoJ2, texturaAtaqueAtualJ2);

        // Renderizar informações adicionais
        fonte.draw(lote, String.format("Tempo: %03d", (int) tempoRestante),
            viewport.getWorldWidth() / 2 - 100, viewport.getWorldHeight() - 50);
        fonte.draw(lote, "Pontuação Jogador 1: " + pontuacaoJogador1, 50, 1050);
        fonte.draw(lote, "Pontuação Jogador 2: " + pontuacaoJogador2, 1420, 1050);
        lote.end();

        desenharBarrasDeVida();
        /* desenharHitboxes(); */
    }

    private void renderizarJogador(Texture jogador, float x, float y, boolean olhandoDireita, boolean atacando, Texture texturaAtaque) {
        float escala = 3.5f;
        float largura = jogador.getWidth() * escala;
        float altura = jogador.getHeight() * escala;
        if (atacando && texturaAtaque != null) {
            if (olhandoDireita) {
                lote.draw(texturaAtaque, x, y, largura, altura);
            } else {
                lote.draw(texturaAtaque, x + largura, y, -largura, altura);
            }
        } else {
            if (olhandoDireita) {
                lote.draw(jogador, x, y, largura, altura);
            } else {
                lote.draw(jogador, x + largura, y, -largura, altura);
            }
        }
    }

    private void finalizarJogo(String razao) {
        System.out.println("Finalizando o jogo: " + razao);

        PontuacaoDAO pontuacaoDAO = new PontuacaoDAO();

        // Validações para os usuários
        if (nomeUsuario1 == null || nomeUsuario1.isEmpty()) {
            nomeUsuario1 = "Desconhecido1"; // Define um valor padrão caso o nome esteja ausente
        }
        if (nomeUsuario2 == null || nomeUsuario2.isEmpty()) {
            nomeUsuario2 = "Desconhecido2"; // Define um valor padrão caso o nome esteja ausente
        }

        // Tenta salvar as pontuações
        if (pontuacaoDAO.salvarPontuacao(nomeUsuario1, pontuacaoJogador1)) {
            System.out.println("Pontuação salva para " + nomeUsuario1 + ": " + pontuacaoJogador1);
        } else {
            System.err.println("Erro ao salvar pontuação para " + nomeUsuario1);
        }

        if (pontuacaoDAO.salvarPontuacao(nomeUsuario2, pontuacaoJogador2)) {
            System.out.println("Pontuação salva para " + nomeUsuario2 + ": " + pontuacaoJogador2);
        } else {
            System.err.println("Erro ao salvar pontuação para " + nomeUsuario2);
        }

        // Redireciona para TelaFimJogo com nome do vencedor ou jogador principal
        jogo.setScreen(new TelaFimJogo(jogo, razao, nomeUsuario1));
    }

    private void salvarPontuacoes() {
        PontuacaoDAO pontuacaoDAO = new PontuacaoDAO();

        boolean jogador1Salvo = pontuacaoDAO.salvarPontuacao(nomeUsuario1, pontuacaoJogador1);
        boolean jogador2Salvo = pontuacaoDAO.salvarPontuacao(nomeUsuario2, pontuacaoJogador2);

        if (jogador1Salvo) {
            System.out.println("Pontuação do " + nomeUsuario1 + " salva com sucesso: " + pontuacaoJogador1);
        } else {
            System.out.println("Erro ao salvar pontuação do " + nomeUsuario1);
        }

        if (jogador2Salvo) {
            System.out.println("Pontuação do " + nomeUsuario2 + " salva com sucesso: " + pontuacaoJogador2);
        } else {
            System.out.println("Erro ao salvar pontuação do " + nomeUsuario2);
        }
    }


    private void movimentarJogador1() {
        if (Gdx.input.isKeyPressed(Input.Keys.W)) jogador1Y += 5;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            jogador1X -= velocidadeMovimento;
            estaOlhandoDireitaJ1 = false; // Jogador 1 vira para a esquerda
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            jogador1X += velocidadeMovimento;
            estaOlhandoDireitaJ1 = true; // Jogador 1 vira para a direita
        }

        // Lógica de pulo do Jogador 1
        if (Gdx.input.isKeyPressed(Input.Keys.W) && !estaPulandoJ1) {
            estaPulandoJ1 = true;
            velocidadeY1 = velocidadePulo;
        }

        // Lógica de ataque para o Jogador 1
        if (Gdx.input.isKeyJustPressed(Input.Keys.Q) && !estaAtacandoJ1) {
            iniciarAtaqueJ1(texturaAtaque1, 50);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.E) && !estaAtacandoJ1) {
            iniciarAtaqueJ1(texturaAtaque2, 100);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.R) && !estaAtacandoJ1) {
            iniciarAtaqueJ1(texturaAtaque3, 200);
        }

        // Atualização da física do pulo do Jogador 1
        if (estaPulandoJ1) {
            jogador1Y += velocidadeY1;
            velocidadeY1 -= gravidade;
            if (jogador1Y <= 200) {
                jogador1Y = 200;
                estaPulandoJ1 = false;
                velocidadeY1 = 0;
            }
        }
    }

    private void movimentarJogador2() {
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) jogador2Y += 5;
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            jogador2X -= velocidadeMovimento;
            estaOlhandoDireitaJ2 = false; // Jogador 2 vira para a esquerda
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            jogador2X += velocidadeMovimento;
            estaOlhandoDireitaJ2 = true; // Jogador 2 vira para a direita
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP) && !estaPulandoJ2) {
            estaPulandoJ2 = true;
            velocidadeY2 = velocidadePulo;
        }

        // Lógica de ataque para o Jogador 2
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_1) && !estaAtacandoJ2) {
            iniciarAtaqueJ2(texturaAtaque4, 50);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_2) && !estaAtacandoJ2) {
            iniciarAtaqueJ2(texturaAtaque5, 100);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_3) && !estaAtacandoJ2) {
            iniciarAtaqueJ2(texturaAtaque6, 200);
        }

        // Atualização da física do pulo do Jogador 2
        if (estaPulandoJ2) {
            jogador2Y += velocidadeY2;
            velocidadeY2 -= gravidade;
            if (jogador2Y <= 200) {
                jogador2Y = 200;
                estaPulandoJ2 = false;
                velocidadeY2 = 0;
            }
        }
    }
    private void iniciarAtaqueJ1(Texture textura, float dano) {
        estaAtacandoJ1 = true;
        texturaAtaqueAtualJ1 = textura;

        if (estaOlhandoDireitaJ1) {
            alcanceAtaqueJ1.set(jogador1X + 370, jogador1Y + 350, 50, 50); // Para a direita
        } else {
            alcanceAtaqueJ1.set(jogador1X + 280, jogador1Y + 350, 50, 50); // Para a esquerda
        }

        if (alcanceAtaqueJ1.overlaps(new Rectangle(jogador2X + 150, jogador2Y + 100, jogador2.getWidth() * 2f, jogador2.getHeight() * 2.2f))) {
            aplicarDanoJogador2(dano);
            pontuacaoJogador1 += 10; // Incrementa 10 pontos para Jogador 1
        }

        temporizadorAtaqueJ1 = intervaloAtaque;
    }

    private void iniciarAtaqueJ2(Texture textura, float dano) {
        estaAtacandoJ2 = true;
        texturaAtaqueAtualJ2 = textura;

        if (estaOlhandoDireitaJ2) {
            alcanceAtaqueJ2.set(jogador2X + 370, jogador2Y + 350, 50, 50); // Para a direita
        } else {
            alcanceAtaqueJ2.set(jogador2X + 280, jogador2Y + 350, 50, 50); // Para a esquerda
        }

        if (alcanceAtaqueJ2.overlaps(new Rectangle(jogador1X + 150, jogador1Y + 100, jogador1.getWidth() * 2f, jogador1.getHeight() * 2.2f))) {
            aplicarDanoJogador1(dano);
            pontuacaoJogador2 += 10; // Incrementa 10 pontos para Jogador 2
        }

        temporizadorAtaqueJ2 = intervaloAtaque;
    }

    private void aplicarDanoJogador1(float dano) {
        saudeJogador1 -= dano;
        if (saudeJogador1 < 0) {
            saudeJogador1 = 0; // Impede que a saúde fique negativa
        }
        // Verifica se o Jogador 1 perdeu
        if (saudeJogador1 == 0) {
            finalizarJogo("Jogador 2 venceu!");
        }
    }

    private void aplicarDanoJogador2(float dano) {
        saudeJogador2 -= dano;
        if (saudeJogador2 < 0) {
            saudeJogador2 = 0; // Impede que a saúde fique negativa
        }
        // Verifica se o Jogador 2 perdeu
        if (saudeJogador2 == 0) {
            finalizarJogo("Jogador 1 venceu!");
        }
    }


    private void desenharBarrasDeVida() {
        renderizadorFormas.setProjectionMatrix(camera.combined);
        renderizadorFormas.begin(ShapeRenderer.ShapeType.Filled);
        renderizadorFormas.setColor(1, 0, 0, 1); // Vermelho para Jogador 1
        renderizadorFormas.rect(50, 1050, (saudeJogador1 / saudeMaxima) * 400, 20);
        renderizadorFormas.setColor(0, 0, 1, 1); // Azul para Jogador 2
        renderizadorFormas.rect(1420, 1050, (saudeJogador2 / saudeMaxima) * 400, 20);
        renderizadorFormas.end();
    }

    /* private void desenharHitboxes() {
        renderizadorFormas.setProjectionMatrix(camera.combined);
        renderizadorFormas.begin(ShapeRenderer.ShapeType.Line);

        // Desenhar hitbox do Jogador 1 (Personagem)
        renderizadorFormas.setColor(1, 1, 0, 1); // Amarelo para o Jogador 1
        renderizadorFormas.rect(jogador1X + 150, jogador1Y + 80, jogador1.getWidth() * 2f, jogador1.getHeight() * 2.5f);

        // Desenhar hitbox do Jogador 2 (Personagem)
        renderizadorFormas.setColor(0, 1, 1, 1); // Ciano para o Jogador 2
        renderizadorFormas.rect(jogador2X + 150, jogador2Y + 80, jogador2.getWidth() * 2f, jogador2.getHeight() * 2.5f);

        // Hitbox do ataque do Jogador 1
        renderizadorFormas.setColor(1, 0, 0, 1); // Vermelho
        renderizadorFormas.rect(alcanceAtaqueJ1.x, alcanceAtaqueJ1.y, alcanceAtaqueJ1.width, alcanceAtaqueJ1.height);

        // Hitbox do ataque do Jogador 2
        renderizadorFormas.setColor(0, 1, 0, 1); // Verde
        renderizadorFormas.rect(alcanceAtaqueJ2.x, alcanceAtaqueJ2.y, alcanceAtaqueJ2.width, alcanceAtaqueJ2.height);

        renderizadorFormas.end();
    }
    */


    @Override
    public void dispose() {
        lote.dispose();
        fundo.dispose();
        jogador1.dispose();
        jogador2.dispose();
        texturaAtaque1.dispose();
        texturaAtaque2.dispose();
        texturaAtaque3.dispose();
        texturaAtaque4.dispose();
        texturaAtaque5.dispose();
        texturaAtaque6.dispose();
        renderizadorFormas.dispose();
        fonte.dispose();
    }
}

