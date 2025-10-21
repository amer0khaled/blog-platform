package com.amerkhaled.blogplatform.mappers;

import com.amerkhaled.blogplatform.domain.CreatePostRequest;
import com.amerkhaled.blogplatform.domain.UpdatePostRequest;
import com.amerkhaled.blogplatform.domain.dtos.CreatePostRequestDto;
import com.amerkhaled.blogplatform.domain.dtos.PostDto;
import com.amerkhaled.blogplatform.domain.dtos.UpdatePostRequestDto;
import com.amerkhaled.blogplatform.domain.entities.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", unmappedSourcePolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface PostMapper {

    @Mapping(target = "category", source = "category")
    @Mapping(target = "tags", source = "tags")
    @Mapping(target = "author", source = "author")
    PostDto toDto(Post post);

    CreatePostRequest toCreatePostRequest(CreatePostRequestDto dto);
    UpdatePostRequest toUpdatePostRequest(UpdatePostRequestDto dto);

}
