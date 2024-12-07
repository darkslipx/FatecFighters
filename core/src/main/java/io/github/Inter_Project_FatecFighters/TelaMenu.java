package io.github.Inter_Project_FatecFighters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class TelaMenu implements Screen {

    private Main jogo;
    private SpriteBatch lote;
    private Texture fundo;
    private BitmapFont fonte;
    private OrthographicCamera camera;
    private Viewport viewport;
    private String nomeUsuario; // Nome do usuário logado

    public TelaMenu(Main jogo, String nomeUsuario) {
        this.jogo = jogo;
        this.nomeUsuario = nomeUsuario;
    }

    @Override
    public void show() {
        lote = new SpriteBatch();
        fonte = new BitmapFont();
        fonte.setColor(Color.WHITE);
        fonte.getData().setScale(3);

        fundo = new Texture("waldir.jpg");

        camera = new OrthographicCamera();
        viewport = new FitViewport(1920, 1080, camera);
        viewport.apply();
        camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);
        camera.update();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        camera.update();

        lote.setProjectionMatrix(camera.combined);
        lote.begin();
        lote.draw(fundo, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());

        // Centralizando o texto
        GlyphLayout layout = new GlyphLayout();
        float centerX = viewport.getWorldWidth() / 2;
        float centerY = viewport.getWorldHeight() / 2;

        // Bem-vindo
        layout.setText(fonte, "Bem-vindo, " + nomeUsuario + "!");
        fonte.draw(lote, layout, centerX - layout.width / 2, centerY + 200);

        // Iniciar Jogo
        layout.setText(fonte, "Iniciar Jogo - Alt");
        fonte.draw(lote, layout, centerX - layout.width / 2, centerY + 100);

        // Ver Pontuações
        layout.setText(fonte, "Rank de Pontuação - R");
        fonte.draw(lote, layout, centerX - layout.width / 2, centerY);

        // Sair
        layout.setText(fonte, "Sair do Jogo - Esc");
        fonte.draw(lote, layout, centerX - layout.width / 2, centerY - 100);

        lote.end();

        tratarEntrada();
    }

    private void tratarEntrada() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ALT_LEFT)) {
            jogo.setScreen(new TelaJogo(jogo, nomeUsuario, "Jogador 2")); // Ajustado para TelaJogo...
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            jogo.setScreen(new TelaPontuacoes(jogo, nomeUsuario));
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }

    @Override
    public void resize(int largura, int altura) {
        viewport.update(largura, altura);
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
        fundo.dispose();
        fonte.dispose();
    }
}
