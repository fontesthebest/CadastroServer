
package controller;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import model.Movimento;
import model.Pessoa;
import model.PessoaFisica;




public class PessoaFisicaJpaController implements Serializable {

    private static Object em;

    private Object pessoaFisica;
    private String id;
    private Object movimentoCollection;

    public PessoaFisicaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PessoaFisica pessoasFisica, PessoaFisica pessoaFisica, Object oldIDPessoaFisicaOfMovimentoCollection) throws PreexistingEntityException, Exception {
        if (pessoaFisica.getMovimentoCollection() == null) {
            pessoaFisica.setMovimentoCollection(new ArrayList<>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pessoa pessoaID = pessoaFisica.getPessoaID();
            if (pessoaID != null) {
                pessoaID = em.getReference(pessoaID.getClass(), pessoaID.getId());
                pessoaFisica.setPessoaID(pessoaID);
            }
            Collection<Movimento> attachedMovimentoCollection = new ArrayList<>();
            for (Movimento movimentoCollectionMovimentoToAttach : pessoaFisica.getMovimentoCollection()) {
                movimentoCollectionMovimentoToAttach = em.getReference(movimentoCollectionMovimentoToAttach.getClass(), movimentoCollectionMovimentoToAttach);
                attachedMovimentoCollection.add(movimentoCollectionMovimentoToAttach);
            }
            pessoaFisica.setMovimentoCollection(attachedMovimentoCollection);
            em.persist(pessoaFisica);
            if (pessoaID != null) {
                PessoaFisica oldPessoaFisicaOfPessoaID = pessoaID.getPessoaFisica();
                if (oldPessoaFisicaOfPessoaID != null) {
                    oldPessoaFisicaOfPessoaID.setPessoaID(null);
                    oldPessoaFisicaOfPessoaID = em.merge(oldPessoaFisicaOfPessoaID);
                }
                pessoaID.setPessoaFisica(pessoaFisica);
                pessoaID = em.merge(pessoaID);
            }
            for (Movimento movimentoCollectionMovimento : pessoaFisica.getMovimentoCollection()) {
                movimentoCollectionMovimento.setIDPessoaFisica(pessoaFisica);
                
                if (oldIDPessoaFisicaOfMovimentoCollection != null) {
                    oldIDPessoaFisicaOfMovimentoCollection();
                    oldIDPessoaFisicaOfMovimentoCollection = em.merge(oldIDPessoaFisicaOfMovimentoCollection);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPessoaFisica(pessoaFisica.getCpf()) != null) {
                throw new PreexistingEntityException("PessoaFisica " + pessoaFisica + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PessoaFisica pessoaFisica) throws NonexistentEntityException, Exception {
        EntityManager em = null;
         {
            em = getEntityManager();
            em.getTransaction().begin();
            PessoaFisica persistentPessoaFisica = em.find(PessoaFisica.class, pessoaFisica.getCpf());
            Pessoa pessoaIDOld = persistentPessoaFisica.getPessoaID();
            Pessoa pessoaIDNew = pessoaFisica.getPessoaID();
            Collection<Movimento> movimentoCollectionOld = persistentPessoaFisica.getMovimentoCollection();
            Collection<Movimento> movimentoCollectionNew = pessoaFisica.getMovimentoCollection();
            if (pessoaIDNew != null) {
                pessoaIDNew = em.getReference(pessoaIDNew.getClass(), pessoaIDNew.getId());
                pessoaFisica.setPessoaID(pessoaIDNew);
            }
            Collection<Movimento> attachedMovimentoCollectionNew = new ArrayList<Movimento>();
            for (Movimento movimentoCollectionNewMovimentoToAttach : movimentoCollectionNew) {
                movimentoCollectionNewMovimentoToAttach = em.getReference(movimentoCollectionNewMovimentoToAttach.getClass(), movimentoCollectionNewMovimentoToAttach);
                attachedMovimentoCollectionNew.add(movimentoCollectionNewMovimentoToAttach);
            }
            movimentoCollectionNew = attachedMovimentoCollectionNew;
            pessoaFisica.setMovimentoCollection(movimentoCollectionNew);
            pessoaFisica = em.merge(pessoaFisica);
            if (pessoaIDOld != null && !pessoaIDOld.equals(pessoaIDNew)) {
                pessoaIDOld.setPessoaFisica(null);
                pessoaIDOld = em.merge(pessoaIDOld);
            }
            if (pessoaIDNew != null && !pessoaIDNew.equals(pessoaIDOld)) {
                PessoaFisica oldPessoaFisicaOfPessoaID = pessoaIDNew.getPessoaFisica();
                if (oldPessoaFisicaOfPessoaID != null) {
                    oldPessoaFisicaOfPessoaID.setPessoaID(null);
                    oldPessoaFisicaOfPessoaID = em.merge(oldPessoaFisicaOfPessoaID);
                }
                pessoaIDNew.setPessoaFisica(pessoaFisica);
                pessoaIDNew = em.merge(pessoaIDNew);
            }
            for (Movimento movimentoCollectionOldMovimento : movimentoCollectionOld) {
                if (!movimentoCollectionNew.contains(movimentoCollectionOldMovimento)) {
                    movimentoCollectionOldMovimento.setIDPessoaFisica(null);
                    movimentoCollectionOldMovimento = em.merge(movimentoCollectionOldMovimento);
                }
            }
            for (Movimento movimentoVendaCollectionNewMovimento : movimentoCollectionNew) {
                if (!movimentoCollectionOld.contains(movimentoCollection)) {
                    PessoaFisica oldIDPessoaFisicaOfMovimentoCollectionNewMovimento;
                    movimentoCollection = em.merge(movimentoCollection);
                    }
                }
            }
            em.getTransaction().commit();
            
        try {
            String msg = getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
             
                if (findPessoaFisica(id) == null) {
                    
                }
            }
            
        } catch (Exception ex) {
            Logger.getLogger(PessoaFisicaJpaController.class.getName()).log(Level.SEVERE, null, ex);
        }
      
            if (em != null) {
                em.close();
            }
        }

    private Object findPessoaFisica(String id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    

      private Object oldIDPessoaFisicaOfMovimentoCollection() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private String getLocalizedMessage() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private static class PreexistingEntityException extends Exception {

        public PreexistingEntityException() {
        }


    PreexistingEntityException(String string, Exception ex) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    }

@SuppressWarnings("serial")
     class NonexistentEntityException extends Exception {

        public NonexistentEntityException() {
        }

        private NonexistentEntityException(String string) {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }
    }
    
}
