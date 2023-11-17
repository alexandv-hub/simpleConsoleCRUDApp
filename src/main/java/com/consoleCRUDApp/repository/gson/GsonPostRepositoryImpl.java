package com.consoleCRUDApp.repository.gson;

import com.consoleCRUDApp.model.Post;
import com.consoleCRUDApp.repository.PostRepository;
import lombok.Getter;

@Getter
public class GsonPostRepositoryImpl
                    extends GsonGenericRepositoryImpl<Post, Long>
                    implements PostRepository {

    private static final Class<Post> entityClass = Post.class;
    private static final String POSTS_FILE_PATH = "src/main/resources/data/posts.json";

    public GsonPostRepositoryImpl() {
        super(entityClass, POSTS_FILE_PATH);
    }
}
