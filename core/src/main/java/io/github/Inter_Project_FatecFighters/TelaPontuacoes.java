package io.github.Inter_Project_FatecFighters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.Input;

import java.util.List;

public class TelaPontuacoes implements Screen {

    private Main jogo;
    private String nomeUsuario;
    private SpriteBatch lote;
    private BitmapFont fonte;
    private OrthographicCamera camera;
    private Viewport viewport;

    private List<String> pontuacoes; // Lista de pontuações no formato "nome_usuario: pontuação"
    private PontuacaoDAO pontuacaoDAO; // DAO para carregar as pontuações

    public TelaPontuacoes(Main jogo, String nomeUsuario) {
        this.jogo = jogo;
        this.nomeUsuario = nomeUsuario;
        this.pontuacaoDAO = new PontuacaoDAO(); // Inicializa o DAO
    }

    @Override
    public void show() {
        lote = new SpriteBatch();
        fonte = new BitmapFont();
        fonte.setColor(Color.WHITE);
        fonte.getData().setScale(3);

        camera = new OrthographicCamera();
        viewport = new FitViewport(1920, 1080, camera);
        viewport.apply();
        camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);
        camera.update();

        // Carrega as pontuações do banco de dados no formato "nome_usuario: pontuação"
        pontuacoes = pontuacaoDAO.listarPontuacoesPorUsuario(nomeUsuario);
        System.out.println("Pontuações carregadas: " + pontuacoes);
    }

    @Override
    public void render(float delta) {
        // Limpa a tela
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Configura a câmera
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();
        lote.setProjectionMatrix(camera.combined);

        lote.begin();

        // Centraliza o texto
        float centerX = viewport.getWorldWidth() / 2;
        float startY = viewport.getWorldHeight() - 200;

        // Exibe as pontuações no formato "Pontuação: [valor]"
        if (pontuacoes.isEmpty()) {
            fonte.draw(lote, "Nenhuma pontuação encontrada.", centerX - 200, startY);
        } else {
            fonte.draw(lote, "Rank de Pontuação:", centerX - 300, startY);
            startY -= 50;
            for (String pontuacao : pontuacoes) {
                fonte.draw(lote, nomeUsuario + ":" + pontuacao, centerX - 200, startY);
                startY -= 50; // Ajusta a próxima linha
            }
        }

        // Instrução para voltar ao menu no canto inferior direito
        fonte.draw(lote, "Voltar ao Menu - Shift", viewport.getWorldWidth() - 400, 50);

        lote.end();

        // Trata entrada do usuário
        tratarEntrada();
    }

    private void tratarEntrada() {
        // Volta para o menu ao pressionar Shift
        if (Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_LEFT)) {
            System.out.println("Tecla Shift pressionada. Voltando ao menu...");
            jogo.setScreen(new TelaMenu(jogo, nomeUsuario)); // Volta para o menu passando o nome do usuário
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        lote.dispose();
        fonte.dispose();
    }
}
