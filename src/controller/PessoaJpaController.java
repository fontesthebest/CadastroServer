
package controller;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import model.Pessoa;
import model.PessoaFisica;
import model.PessoaJuridica;



public class PessoaJpaController implements Serializable {

    private Object pessoaFisica;

    public PessoaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pessoa pessoa) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PessoaJuridica pessoaJuridica = pessoa.getPessoaJuridica();
            if (pessoaJuridica != null) {
                pessoaJuridica = em.getReference(pessoaJuridica.getClass(), pessoaJuridica);
                pessoa.setPessoaJuridica(pessoaJuridica);
            }
            PessoaFisica pessoaFisica = pessoa.getPessoaFisica();
            if (pessoaFisica != null) {
                pessoaFisica = em.getReference(pessoaFisica.getClass(), pessoaFisica.getCpf());
                pessoa.setPessoaFisica(pessoaFisica);
            }
            em.persist(pessoa);
            if (pessoaJuridica != null) {
                Pessoa oldPessoaIDOfPessoaJuridica = pessoaJuridica.getPessoaID();
                if (oldPessoaIDOfPessoaJuridica != null) {
                    oldPessoaIDOfPessoaJuridica.setPessoaJuridica(null);
                    oldPessoaIDOfPessoaJuridica = em.merge(oldPessoaIDOfPessoaJuridica);
                }
                pessoaJuridica.setPessoaID(pessoa);
                pessoaJuridica = em.merge(pessoaJuridica);
            }
            if (pessoaFisica != null) {
                Pessoa oldPessoaIDOfPessoaFisica;
                oldPessoaIDOfPessoaFisica = pessoaFisica.getPessoaID();
                if (oldPessoaIDOfPessoaFisica != null) {
                    oldPessoaIDOfPessoaFisica.setPessoaFisica(null);
                    oldPessoaIDOfPessoaFisica = em.merge(oldPessoaIDOfPessoaFisica);
                }
                pessoaFisica.setPessoaID(pessoa);
                pessoaFisica = em.merge(pessoaFisica);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPessoa(pessoa.getId()) != null) {
                throw new PreexistingEntityException("Pessoa " + pessoa + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pessoa pessoa) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pessoa persistentPessoa = em.find(Pessoa.class, pessoa.getId());
            PessoaJuridica pessoaJuridicaOld = persistentPessoa.getPessoaJuridica();
            PessoaJuridica pessoaJuridicaNew = pessoa.getPessoaJuridica();
            PessoaFisica pessoaFisicaOld = persistentPessoa.getPessoaFisica();
            PessoaFisica pessoaFisicaNew = pessoa.getPessoaFisica();
            if (pessoaJuridicaNew != null) {
                pessoaJuridicaNew = em.getReference(pessoaJuridicaNew.getClass(), pessoaJuridicaNew);
                pessoa.setPessoaJuridica(pessoaJuridicaNew);
            }
            if (pessoaFisicaNew != null) {
                pessoaFisicaNew = em.getReference(pessoaFisicaNew.getClass(), pessoaFisicaNew.getCpf());
                pessoa.setPessoaFisica(pessoaFisicaNew);
            }
            pessoa = em.merge(pessoa);
            if (pessoaJuridicaOld != null && !pessoaJuridicaOld.equals(pessoaJuridicaNew)) {
                pessoaJuridicaOld.setPessoaID(null);
                pessoaJuridicaOld = em.merge(pessoaJuridicaOld);
            }
            if (pessoaJuridicaNew != null && !pessoaJuridicaNew.equals(pessoaJuridicaOld)) {
                Pessoa oldPessoaIDOfPessoaJuridica = pessoaJuridicaNew.getPessoaID();
                if (oldPessoaIDOfPessoaJuridica != null) {
                    oldPessoaIDOfPessoaJuridica.setPessoaJuridica(null);
                    oldPessoaIDOfPessoaJuridica = em.merge(oldPessoaIDOfPessoaJuridica);
                }
                pessoaJuridicaNew.setPessoaID(pessoa);
                pessoaJuridicaNew = em.merge(pessoaJuridicaNew);
            }
            if (pessoaFisicaOld != null && !pessoaFisicaOld.equals(pessoaFisicaNew)) {
                pessoaFisicaOld.setPessoaID(null);
                pessoaFisicaOld = em.merge(pessoaFisicaOld);
            }
            if (pessoaFisicaNew != null && !pessoaFisicaNew.equals(pessoaFisicaOld)) {
                Pessoa oldPessoaIDOfPessoaFisica = pessoaFisicaNew.getPessoaID();
                if (oldPessoaIDOfPessoaFisica != null) {
                    oldPessoaIDOfPessoaFisica.setPessoaFisica(null);
                    oldPessoaIDOfPessoaFisica = em.merge(oldPessoaIDOfPessoaFisica);
                }
                pessoaFisicaNew.setPessoaID(pessoa);
                pessoaFisicaNew = em.merge(pessoaFisicaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = pessoa.getId();
                if (findPessoa(id) == null) {
                    throw new NonexistentEntityException("The pessoa with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pessoa pessoa;
            try {
                pessoa = em.getReference(Pessoa.class, id);
                pessoa.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pessoas with id " + id + " no longer exists.", enfe);
            }
            PessoaJuridica pessoaJuridica = pessoa.getPessoaJuridica();
            if (pessoaJuridica != null) {
                pessoaJuridica.setPessoaID(null);
                pessoaJuridica = em.merge(pessoaJuridica);
            }
            PessoaFisica pessoasFisica = pessoa.getPessoaFisica();
            if (pessoaFisica != null) {
                pessoaFisica = em.merge(pessoaFisica);
            }
            em.remove(pessoa);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Pessoa> findPessoaEntities() {
        return findPessoaEntities(true, -1, -1);
    }

    public List<Pessoa> findPessoaEntities(int maxResults, int firstResult) {
        return findPessoaEntities(false, maxResults, firstResult);
    }

    private List<Pessoa> findPessoaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pessoa.class));
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

    public Pessoa findPessoa(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pessoa.class, id);
        } finally {
            em.close();
        }
    }

    public int getPessoaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pessoa> rt = cq.from(Pessoa.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    private static class PreexistingEntityException extends Exception {

        public PreexistingEntityException() {
        }

        private PreexistingEntityException(String string, Exception ex) {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }
    }

    private static class NonexistentEntityException extends Exception {

        public NonexistentEntityException() {
        }

        private NonexistentEntityException(String string) {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

        private NonexistentEntityException(String string, EntityNotFoundException enfe) {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }
    }
    



}
