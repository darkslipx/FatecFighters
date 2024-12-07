package io.github.Inter_Project_FatecFighters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class TelaFimJogo implements Screen {
    private Main jogo;
    private String mensagemVencedor;
    private String nomeUsuario;
    private SpriteBatch lote;
    private BitmapFont fonte;
    private OrthographicCamera camera;
    private Viewport viewport;

    public TelaFimJogo(Main jogo, String mensagemVencedor, String nomeUsuario) {
        this.jogo = jogo;
        this.mensagemVencedor = mensagemVencedor;
        this.nomeUsuario = nomeUsuario;
    }

    @Override
    public void show() {
        lote = new SpriteBatch();
        fonte = new BitmapFont();
        fonte.getData().setScale(3); // Escala ajustável para o texto
        fonte.setColor(Color.WHITE); // Definir cor do texto para melhorar contraste

        camera = new OrthographicCamera();
        viewport = new FitViewport(1920, 1080, camera); // Configuuração padrão
        viewport.apply();
        camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);
        camera.update();

        System.out.println("TelaFimJogo inicializada. Usuário: " + nomeUsuario);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        camera.update();

        lote.setProjectionMatrix(camera.combined);
        lote.begin();

        // Centralizando o texto "mensagemVencedor"
        GlyphLayout layout = new GlyphLayout();
        layout.setText(fonte, mensagemVencedor);
        float mensagemX = (viewport.getWorldWidth() - layout.width) / 2;
        float mensagemY = (viewport.getWorldHeight() + layout.height) / 2;
        fonte.draw(lote, mensagemVencedor, mensagemX, mensagemY);

        // Colocando "Voltar - Shift" no canto inferior direito
        layout.setText(fonte, "Voltar - Shift");
        float voltarX = viewport.getWorldWidth() - layout.width - 50;
        float voltarY = 50 + layout.height; // Um pouco acima do canto inferior
        fonte.draw(lote, "Voltar - Shift", voltarX, voltarY);

        lote.end();

        // Verifica se a tecla SHIFT foi pressionada para voltar ao menu
        if (Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_LEFT)) {
            System.out.println("SHIFT pressionado. Retornando ao menu...");
            jogo.setScreen(new TelaMenu(jogo, nomeUsuario));
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true); // Atualiza o viewport quando a tela é redimensionada
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
