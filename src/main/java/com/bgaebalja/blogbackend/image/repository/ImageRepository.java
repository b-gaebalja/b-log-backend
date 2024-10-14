package com.bgaebalja.blogbackend.image.repository;

import com.bgaebalja.blogbackend.image.domain.Image;
import com.bgaebalja.blogbackend.image.domain.TargetType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByTargetTypeAndTargetIdAndDeleteYnFalse(TargetType targetType, Long id);

    Optional<Image> findByIdAndDeleteYnFalse(Long id);

    Optional<Image> findByTargetTypeAndTargetIdAndRepresentativeYnTrueAndDeleteYnFalse(
            TargetType targetType, Long targetId
    );

    Optional<Image> findFirstByTargetTypeAndTargetIdAndDeleteYnFalseOrderByCreatedAt(
            TargetType targetType, Long targetId
    );

    Optional<Image> findByRepresentativeYnTrueAndDeleteYnFalse();

    // 추후 삭제
    List<Image> findByTargetId(Long targetId);
}
