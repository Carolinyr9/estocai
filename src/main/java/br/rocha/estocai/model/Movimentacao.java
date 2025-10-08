package br.rocha.estocai.model;

import java.util.Date;
import br.rocha.estocai.model.enums.DescricaoMovimentacao;
import br.rocha.estocai.model.enums.TipoMovimentacao;
import jakarta.annotation.Nonnull;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "movimentacao")
public class Movimentacao {

    @Id
    @GeneratedValue
    private Long id;
    
    @Nonnull
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Nonnull
    @Enumerated(EnumType.STRING)
    private TipoMovimentacao tipo;

    @Nonnull
    @Enumerated(EnumType.STRING)
    private DescricaoMovimentacao descricao;
    //private User user

    @ManyToOne
    @JoinColumn(name = "produto_id")
    private Produto produto;

}
