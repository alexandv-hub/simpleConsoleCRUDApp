package com.consoleCRUDApp.repository.gson;

import com.consoleCRUDApp.model.Post;
import com.consoleCRUDApp.repository.PostRepository;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;

import java.lang.reflect.Type;
import java.util.List;

@Getter
public class GsonPostRepositoryImpl
                    extends GsonGenericRepositoryImpl<Post, Long>
                    implements PostRepository {

    private final Class<Post> entityClass = Post.class;

    public GsonPostRepositoryImpl(Class<Post> entityClass, String filePath) {
        super(entityClass, filePath);
    }

    @Override
    public Type getEntityTypeToken() {
        return new TypeToken<List<Post>>() {}.getType();
    }

}
