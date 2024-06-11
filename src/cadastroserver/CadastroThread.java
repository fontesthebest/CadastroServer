
package cadastroserver;


import controller.MovimentoJpaController;
import controller.PessoaJpaController;
import controller.ProdutoJpaController;
import controller.UsuarioJpaController;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Movimento;
import model.Pessoa;
import model.Produto;
import model.Usuario;


public class CadastroThread extends Thread {
    private final ProdutoJpaController ctrlProduto;
    private final UsuarioJpaController ctrlUsuario;
    private final MovimentoJpaController ctrlMovimento;
    private final PessoaJpaController ctrlPessoa;
    private final Socket soc1;
    public CadastroThread(ProdutoJpaController ctrlProduto, UsuarioJpaController ctrlUsuario,
            MovimentoJpaController ctrlMovimento, PessoaJpaController ctrlPessoa, Socket soc1) {
        this.ctrlProduto = ctrlProduto;
        this.ctrlUsuario = ctrlUsuario;
        this.ctrlMovimento = ctrlMovimento;
        this.ctrlPessoa = ctrlPessoa;
        this.soc1 = soc1;
    }
    @Override
    public void run() {
        try (
                ObjectOutputStream saida = new ObjectOutputStream(soc1.getOutputStream());
                ObjectInputStream entrada = new ObjectInputStream(soc1.getInputStream())) {
            String login = (String) entrada.readObject();
            String senha = (String) entrada.readObject();
            Usuario usuario = ctrlUsuario.findUsuariosenha(login, senha);
            if (usuario == null) {
                System.out.println("Usuário inválido. Conexão encerrada.");
                return;
            }
            while (true) {
                String comando = (String) entrada.readObject();
                if ("L".equals(comando)) {
                    List<Produto> produtos = ctrlProduto.findProdutoEntities();
                    saida.writeObject(produtos);

                } else if ("E".equalsIgnoreCase(comando)) {
                    if (EntradaMovimento(entrada, usuario)) {
                        saida.writeObject("EntradaMovimento realizada com sucesso.");
                    } else {
                        saida.writeObject("Erro ao realizar entrada.");
                    }
                } else if ("S".equalsIgnoreCase(comando)) {
                    if (SaidaMovimento(entrada, usuario)) {
                        saida.writeObject("Saída realizada com sucesso.");
                    } else {
                        saida.writeObject("Erro ao realizar saída.");
                    }
                } else if ("X".equals(comando)) {
                    saida.writeObject("SAINDO");
                }
            }
        } catch (IOException | ClassNotFoundException e) {
        } catch (Exception ex) {
            Logger.getLogger(CadastroThread.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                soc1.close();
            } catch (IOException e) {
            }
        }
    }
    private boolean EntradaMovimento(ObjectInputStream entrada, Usuario usuario, Movimento movimento) throws IOException, ClassNotFoundException {
        Integer idPessoaObj = (Integer) entrada.readObject();
        Integer idProdutoObj = (Integer) entrada.readObject();
        Integer quantidadeObj = (Integer) entrada.readObject();
        Double valorUnitarioObj = (Double) entrada.readObject();
        int idPessoa = idPessoaObj;
        int idProduto = idProdutoObj;
        int quantidade = quantidadeObj;
        double valorUnitario = valorUnitarioObj;
        Pessoa pessoa = ctrlPessoa.findPessoa(idPessoa);
        Produto produto = ctrlProduto.findProduto(idProduto);
        if (pessoa == null || produto == null) {
            System.out.println("Pessoa ou Produto não encontrado. Movimento não registrado.");
            return false;
        }
        if (quantidade <= 0) {
            System.out.println("Quantidade inválida. Movimento não registrado.");
            return false;
        }
        Movimento movimentacao = new Movimento();
        movimentacao.setIdUsuario(usuario);
        movimentacao.setTipo("E");
        movimentacao.setIdPessoa(pessoa);
        movimentacao.setIdProduto(produto);
        movimentacao.setQuantidade(quantidade);
        movimentacao.setValorUnitario(valorUnitario);
        int novaQuantidade = produto.getQuantidade() + quantidade;
        try {
            produto.setQuantidade(novaQuantidade);
            ctrlProduto.edit(produto);
        } catch (Exception ex) {
            System.out.println("Erro ao realizar a persistencia em produto.");
            ex.printStackTrace();
            return false;
        }
        try {
            ctrlMovimento.create(movimento);
            return true;
        } catch (Exception ex) {
            System.out.println("Erro ao realizar a persistencia em movimento.");
            ex.printStackTrace();
            return false;
        }
    }
    private boolean SaidaMovimento(ObjectInputStream entrada, Usuario usuario, Movimento movimento) throws IOException, ClassNotFoundException {
        Integer idPessoaObj = (Integer) entrada.readObject();
        Integer idProdutoObj = (Integer) entrada.readObject();
        Integer quantidadeObj = (Integer) entrada.readObject();
        Double valorUnitarioObj = (Double) entrada.readObject();
        int idPessoa = idPessoaObj;
        int idProduto = idProdutoObj;
        int quantidade = quantidadeObj;
        double valorUnitario = valorUnitarioObj;
        Pessoa pessoa = ctrlPessoa.findPessoa(idPessoa);
        Produto produto = ctrlProduto.findProduto(idProduto);
        if (pessoa == null || produto == null) {
            System.out.println("Pessoa ou Produto não encontrado. Movimento não registrado.");
            return false;
        }
        if (quantidade <= 0) {
            System.out.println("Quantidade inválida. Movimento não registrado.");
            return false;
        }
        int novaQuantidade = produto.getQuantidade() - quantidade;
        if (novaQuantidade >= 0) {
            Movimento movimentacao = new Movimento();
            movimentacao.setIdUsuario(usuario);
            movimentacao.setTipo("S");
            movimentacao.setIdPessoa(pessoa);
            movimentacao.setIdProduto(produto);
            movimentacao.setIdMovimento(quantidade);
            movimentacao.setValorUnitario(valorUnitario);
            try {
                produto.setQuantidade(novaQuantidade);
                ctrlProduto.edit(produto);
            } catch (Exception ex) {
                System.out.println("Erro ao realizar a persistencia em produto.");
                return false;
            }
            try {
                ctrlMovimento.create(movimento);
                return true;
            } catch (Exception ex) {
                System.out.println("Erro ao realizar a persistencia em movimento.");
                return false;
            }
        } else {
            System.out.println("Estoque insuficiente para a saída.");
            return false;
        }
    }

    private boolean EntradaMovimento(ObjectInputStream entrada, Usuario usuario) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private boolean SaidaMovimento(ObjectInputStream entrada, Usuario usuario) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}