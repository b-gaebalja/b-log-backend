package com.bgaebalja.blogbackend.audit;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import static com.bgaebalja.blogbackend.util.EntityConstant.BOOLEAN_DEFAULT_FALSE;
import static jakarta.persistence.GenerationType.IDENTITY;


@EntityListeners(value = AuditingEntityListener.class)
@MappedSuperclass
@Getter
public abstract class BaseGeneralEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = BOOLEAN_DEFAULT_FALSE)
    private boolean deleteYn;

    protected void deleteEntity() {
        deleteYn = true;
    }

    protected void undeleteEntity() {
        deleteYn = false;
    }
}
