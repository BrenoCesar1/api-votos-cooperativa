package br.com.meta.apivotoscooperativa.domain.entity;

import br.com.meta.apivotoscooperativa.domain.dto.in.DadosCadastroAssociado;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "associados")
@Entity(name = "Associado")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Associado {


        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private Long id;

        @Column(name = "nome")
        private String nome;

        @Column(name = "cpf")
        private String cpf;

        public Associado(@Valid DadosCadastroAssociado dados) {
                this.cpf = dados.cpf();
                this.nome = dados.nome();
        }



}

