package com.bgaebalja.blogbackend.image.service;

import com.bgaebalja.blogbackend.image.domain.AddImageRequest;
import com.bgaebalja.blogbackend.image.domain.Image;
import com.bgaebalja.blogbackend.image.domain.TargetType;

import java.util.List;

public interface ImageService {
    Image createImage(AddImageRequest addImageRequest);

    List<Image> getImages(TargetType targetType, Long targetId);
}
