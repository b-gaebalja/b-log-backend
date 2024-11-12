package com.bgaebalja.blogbackend.share.domain;

import com.bgaebalja.blogbackend.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Share")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Share extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    @Column(name = "sharer_id", nullable = false)
    private Long sharerId;

    @Column(name = "writer_id", nullable = false)
    private Long writerId;

    @Column(name = "post_id", nullable = false)
    private Long postId;

    @Column(nullable = false)
    private String url;

    public Share(Long postId, Long writerId, Long sharerId, String url) {
        this.postId = postId;
        this.writerId = writerId;
        this.sharerId = sharerId;
        this.url = url;
    }
}
