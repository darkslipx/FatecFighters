package io.github.Inter_Project_FatecFighters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.InputAdapter;

public class TelaLogin extends ScreenAdapter {

    private Main jogo;
    private SpriteBatch lote;
    private BitmapFont fonte;
    private String nomeUsuario = "";
    private String senha = "";
    private String mensagem = "";
    private boolean criandoConta = false;
    private boolean digitandoSenha = false;
    private boolean alterandoSenha = false;
    private boolean excluindoConta = false;

    private OrthographicCamera camera;
    private Viewport viewport;
    private GlyphLayout layout;

    // Instância do DAO
    private UsuarioDAO usuarioDAO;

    public TelaLogin(Main jogo) {
        this.jogo = jogo;
        layout = new GlyphLayout();
        usuarioDAO = new UsuarioDAO();
    }

    @Override
    public void show() {
        lote = new SpriteBatch();
        fonte = new BitmapFont();
        fonte.getData().setScale(3);
        fonte.setColor(Color.BLACK);

        camera = new OrthographicCamera();
        viewport = new FitViewport(1920, 1080, camera);
        viewport.apply();
        camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);
        camera.update();

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyTyped(char character) {
                if (character == '\b') { // Backspace
                    if (digitandoSenha) {
                        if (senha.length() > 0) {
                            senha = senha.substring(0, senha.length() - 1);
                        }
                    } else {
                        if (nomeUsuario.length() > 0) {
                            nomeUsuario = nomeUsuario.substring(0, nomeUsuario.length() - 1);
                        }
                    }
                } else if (character == '\r' || character == '\n') { // Enter
                    if (criandoConta) {
                        if (usuarioDAO.criarUsuario(nomeUsuario, senha)) {
                            mensagem = "Conta criada com sucesso! Pressione L para logar.";
                            nomeUsuario = "";
                            senha = "";
                            criandoConta = false;
                        } else {
                            mensagem = "Conta já existe! Tente novamente.";
                        }
                    } else if (excluindoConta) {
                        if (usuarioDAO.excluirUsuario(nomeUsuario)) {
                            mensagem = "Conta excluída com sucesso!";
                        } else {
                            mensagem = "Falha ao excluir conta! Verifique suas credenciais.";
                        }
                        excluindoConta = false;
                        nomeUsuario = "";
                        senha = "";
                    } else if (alterandoSenha) {
                        if (usuarioDAO.alterarSenha(nomeUsuario, senha)) {
                            mensagem = "Senha alterada com sucesso!";
                        } else {
                            mensagem = "Falha ao alterar senha! Usuário não encontrado.";
                        }
                        alterandoSenha = false;
                        nomeUsuario = "";
                        senha = "";
                    } else if (usuarioDAO.autenticarUsuario(nomeUsuario, senha)) {
                        mensagem = "Login bem-sucedido! Redirecionando para o menu principal...";
                        jogo.setScreen(new TelaMenu(jogo, nomeUsuario)); // Passa o nome do usuário logado para TelaMenu
                    } else {
                        mensagem = "Credenciais inválidas! Tente novamente.";
                    }
                } else if (character != '\t') { // Ignora a tecla Tab
                    if (digitandoSenha) {
                        senha += character;
                    } else {
                        nomeUsuario += character;
                    }
                }
                return true;
            }

            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.TAB) {
                    digitandoSenha = !digitandoSenha;
                    mensagem = digitandoSenha ? "Digitando senha" : "Digitando nome de usuário";
                    return true;
                } else if (keycode == Input.Keys.C) {
                    criandoConta = true;
                    nomeUsuario = "";
                    senha = "";
                    mensagem = "Criando uma conta. Insira um nome de usuário e senha.";
                    return true;
                } else if (keycode == Input.Keys.L) {
                    criandoConta = false;
                    nomeUsuario = "";
                    senha = "";
                    mensagem = "Logando. Insira seu nome de usuário e senha.";
                    return true;
                } else if (keycode == Input.Keys.P) {
                    alterandoSenha = true;
                    nomeUsuario = "";
                    senha = "";
                    mensagem = "Alterar senha. Insira seu nome de usuário e nova senha.";
                    return true;
                } else if (keycode == Input.Keys.D) {
                    excluindoConta = true;
                    nomeUsuario = "";
                    senha = "";
                    mensagem = "Excluir conta. Insira seu nome de usuário e senha.";
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void render(float delta) {
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);

        Gdx.gl.glClearColor(0.9f, 0.9f, 0.9f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        lote.setProjectionMatrix(camera.combined);

        lote.begin();

        // Posição centralizada para nome e senha
        float centerX = viewport.getWorldWidth() / 2;
        float centerY = viewport.getWorldHeight() / 2;

        layout.setText(fonte, "Nome de usuário: " + nomeUsuario);
        fonte.draw(lote, layout, centerX - layout.width / 2, centerY + 100);

        layout.setText(fonte, "Senha: " + (digitandoSenha ? senha.replaceAll(".", "*") : ""));
        fonte.draw(lote, layout, centerX - layout.width / 2, centerY);

        layout.setText(fonte, mensagem);
        fonte.draw(lote, layout, centerX - layout.width / 2, centerY - 100);

        // Lista de opções no canto inferior direito
        float optionsX = viewport.getWorldWidth() - 350; // Ajuste horizontal
        float optionsY = 150; // Ajuste vertical para o canto inferior
        fonte.draw(
            lote,
            "Opções:\nCriar conta - C\nLogar - L\nMudar senha - P\nExcluir conta - D",
            optionsX,
            optionsY + 150
        );

        lote.end();
    }

    @Override
    public void resize(int largura, int altura) {
        viewport.update(largura, altura, true);
    }

    @Override
    public void dispose() {
        lote.dispose();
        fonte.dispose();
    }
}
