
package controller;

import controller.PessoaFisicaJpaController.NonexistentEntityException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.Movimento;
import model.Produto;


public class ProdutoJpaController implements Serializable {

    private EntityManager movimentoCollectionMovimento;
    private Iterable<Movimento> movimentoCollectionNew;
    private Object oldIdProdutoOfMovimentoCollectionMovimentacao;
    private Iterable<Movimento> movimentoCollectionOld;
    private Object movimentacaoCollectionOld;

    public ProdutoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Produto produto) {
        if (produto.getMovimentacaoCollection() == null) {
            produto.setMovimentoCollection(new ArrayList<Movimento>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Movimento> attachedMovimentacaoCollection = new ArrayList<Movimento>();
            for (Movimento movimentacaoCollectionMovimentacaoToAttach : produto.getMovimentoCollection()) {
                movimentacaoCollectionMovimentacaoToAttach = em.getReference(movimentacaoCollectionMovimentacaoToAttach.getClass(), movimentacaoCollectionMovimentacaoToAttach.getIdMovimento());
                attachedMovimentacaoCollection.add(movimentacaoCollectionMovimentacaoToAttach);
            }
            produto.setMovimentoCollection(attachedMovimentacaoCollection);
            em.persist(produto);
            for (Movimento movimentoCollectionMovimentacao : produto.getMovimentoCollection()) {
                Produto oldIdProdutoOfMovimentacaoCollectionMovimentacao = movimentoCollectionMovimentacao.getIdProduto();
              
                movimentoCollectionMovimento = em.merge(movimentoCollectionMovimento);
                if (oldIdProdutoOfMovimentoCollectionMovimentacao != null) {
                    oldIdProdutoOfMovimentacaoCollectionMovimentacao.getMovimentacaoCollection();
                    oldIdProdutoOfMovimentacaoCollectionMovimentacao = em.merge(oldIdProdutoOfMovimentacaoCollectionMovimentacao);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Produto produto) throws NonexistentEntityException, Exception {
        EntityManager em = null;
         {
            em = getEntityManager();
            em.getTransaction().begin();
            Produto persistentProduto;
            persistentProduto = em.find(Produto.class, produto.getIdProduto());
            Iterable<Movimento> movimentacaoCollectionOld = persistentProduto.getMovimentoCollection();
            Object movimentacaoCollectionNew = produto.getMovimentacaoCollection();
            Collection<Movimento> attachedMovimentacaoCollectionNew = new ArrayList<Movimento>();
            for (Movimento movimentacaoCollectionNewMovimentacaoToAttach : movimentoCollectionNew) {
                movimentacaoCollectionNewMovimentacaoToAttach = em.getReference(movimentacaoCollectionNewMovimentacaoToAttach.getClass(), movimentacaoCollectionNewMovimentacaoToAttach.getIdMovimento());
                attachedMovimentacaoCollectionNew.add(movimentacaoCollectionNewMovimentacaoToAttach);
            }
            movimentacaoCollectionNew = attachedMovimentacaoCollectionNew;
            produto.setMovimentacaoCollection(movimentacaoCollectionNew);
            produto = em.merge(produto);
            for (Movimento movimentacaoCollectionOldMovimentacao : movimentoCollectionOld) {
             
                    movimentacaoCollectionOldMovimentacao.setIdProduto(null);
                    movimentacaoCollectionOldMovimentacao = em.merge(movimentacaoCollectionOldMovimentacao);
                }
            }
            for (Movimento movimentoCollectionNewMovimento : movimentoCollectionNew) {
               
                    Produto oldIdProdutoOfMovimentacaoCollectionNewMovimentacao = movimentoCollectionNewMovimento.getIdProduto();
                    movimentoCollectionNewMovimento.setIdProduto(produto);
                    movimentoCollectionNewMovimento = em.merge(movimentoCollectionNewMovimento);
                    if (oldIdProdutoOfMovimentacaoCollectionNewMovimentacao != null && !oldIdProdutoOfMovimentacaoCollectionNewMovimentacao.equals(produto)) {
                        oldIdProdutoOfMovimentacaoCollectionNewMovimentacao.getMovimentoCollection();
                        oldIdProdutoOfMovimentacaoCollectionNewMovimentacao = em.merge(oldIdProdutoOfMovimentacaoCollectionNewMovimentacao);
                    
                } else {}               
        }   
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Produto produto = null;
         
                produto = em.getReference(Produto.class, id);
                produto.getIdProduto();
             
          
            Iterable<Movimento> movimentoCollection = produto.getMovimentoCollection();
            for (Movimento movimentoCollectionMovimento : movimentoCollection) {
                movimentoCollectionMovimento.setIdProduto(null);
                movimentoCollectionMovimento = em.merge(movimentoCollectionMovimento);
            }
            em.remove(produto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Produto> findProdutoEntities() {
        return findProdutoEntities(true, -1, -1);
    }

    public List<Produto> findProdutoEntities(int maxResults, int firstResult) {
        return findProdutoEntities(false, maxResults, firstResult);
    }

    private List<Produto> findProdutoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Produto.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Produto findProduto(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Produto.class, id);
        } finally {
            em.close();
        }
    }

    public int getProdutoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Produto> rt = cq.from(Produto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    private List<Produto> findProdutosEntities(boolean all, int maxResults, int firstResult) {
    EntityManager em = getEntityManager();
    try {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Produto.class));
        Query q = em.createQuery(cq);
        if (!all) {
        q.setMaxResults(maxResults);
        q.setFirstResult(firstResult);
        }
        return q.getResultList();
    } finally {
        em.close();
    }
    }

     public List<Produto> getListaProdutos() {
    EntityManager em = getEntityManager();
    try {
        CriteriaQuery<Produto> cq = em.getCriteriaBuilder().createQuery(Produto.class);
        cq.select(cq.from(Produto.class));
        TypedQuery<Produto> query = em.createQuery(cq);
        return query.getResultList();
    } finally {
        em.close();
    }
}

    private static class movimentoCollectionMovimento {

        private static void setIdProduto(Produto produto) {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

        public movimentoCollectionMovimento() {
        }
    }

    private static class ex {

        public ex() {
        }
    }
    
}
