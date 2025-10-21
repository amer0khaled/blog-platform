package com.amerkhaled.blogplatform.mappers;

import com.amerkhaled.blogplatform.domain.PostStatus;
import com.amerkhaled.blogplatform.domain.dtos.CreateTagsRequestDto;
import com.amerkhaled.blogplatform.domain.dtos.TagDto;
import com.amerkhaled.blogplatform.domain.entities.Post;
import com.amerkhaled.blogplatform.domain.entities.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Set;

@Mapper(componentModel = "spring", unmappedSourcePolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface TagMapper {

    @Mapping(target = "postCount", source = "posts", qualifiedByName = "calculatePostCount")
    TagDto toTagResponse(Tag tag);

    Tag toEntity(CreateTagsRequestDto createTagsRequestDto);

    @Named("calculatePostCount")
    default long calculatePostCount(Set<Post> posts) {
        if (posts==null || posts.isEmpty()) {
            return 0;
        }
        return posts.stream()
                .filter(post -> post.getStatus().equals(PostStatus.PUBLISHED))
                .count();
    }

}
