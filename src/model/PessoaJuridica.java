package model;
import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
@Entity
@Table(name = "PessoaJuridica")
@NamedQueries({
    @NamedQuery(name = "PessoaJuridica.findAll", query = "SELECT p FROM PessoaJuridica p"),
    @NamedQuery(name = "PessoaJuridica.findByCnpj", query = "SELECT p FROM PessoaJuridica p WHERE p.cnpj = :cnpj")})
public class PessoaJuridica implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "CNPJ")
    private String cnpj;
    @JoinColumn(name = "PessoaID", referencedColumnName = "ID")
    @OneToOne
    private Pessoa pessoaID;
    @OneToMany(mappedBy = "iDPessoaJuridica")
    private Collection<MovimentoCompra> movimentoCollection;
    private Collection<Movimento> movimentoCollection1;
    public PessoaJuridica() {
    }
    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }
    public Pessoa getPessoaID() {
        return pessoaID;
    }
    public void setPessoaID(Pessoa pessoaID) {
        this.pessoaID = pessoaID;
    }
    public Collection<MovimentoCompra> getMovimentoCollection() {
        return movimentoCollection;
    }
    public void setMovimentoCollection(Collection<Movimento> movimentoCollection) {
        this.movimentoCollection1 = movimentoCollection;
    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cnpj != null ? cnpj.hashCode() : 0);
        return hash;
    }
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PessoaJuridica)) {
            return false;
        }
        PessoaJuridica other = (PessoaJuridica) object;
        if ((this.cnpj == null && other.cnpj != null) || (this.cnpj != null && !this.cnpj.equals(other.cnpj))) {
            return false;
        }
        return true;
    }
    @Override
    public String toString() {
        return "model.PessoaJuridica[ cnpj=" + cnpj + " ]";
    }
    public Pessoa getIdPessoa() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    public void setIdPessoa(Pessoa idPessoa) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    public Integer getIdPessoaJuridica() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    private static class MovimentoCompra {
        public MovimentoCompra() {
        }
    }   
}
