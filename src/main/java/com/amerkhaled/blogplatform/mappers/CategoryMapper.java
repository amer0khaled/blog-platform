package com.amerkhaled.blogplatform.mappers;

import com.amerkhaled.blogplatform.domain.PostStatus;
import com.amerkhaled.blogplatform.domain.dtos.CategoryDto;
import com.amerkhaled.blogplatform.domain.dtos.CreateCategoryRequestDto;
import com.amerkhaled.blogplatform.domain.entities.Category;
import com.amerkhaled.blogplatform.domain.entities.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {

    @Mapping(target = "postCount", source = "posts", qualifiedByName = "calculatePostCount")
    CategoryDto toDto(Category category);

    Category toEntity(CreateCategoryRequestDto categoryRequest);

    @Named("calculatePostCount")
    default long calculatePostCount(List<Post> posts) {
        if (posts==null || posts.isEmpty()) {
            return 0;
        }
        return posts.stream()
                .filter(post -> post.getStatus().equals(PostStatus.PUBLISHED))
                .count();
    }

}
