package com.intuit.workcraft.servicecontractapp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "contract_mapping",
        uniqueConstraints = @UniqueConstraint(columnNames = {"contract_worker_id", "service_contract_id"}))
public class ContractMapping implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "contract_worker_id")
    private ContractWorker contractWorker;

    @ManyToOne
    @JoinColumn(name = "service_contract_id")
    private ServiceContract serviceContract;

    @Column(name = "allocation")
    private int allocation = 100;
}
