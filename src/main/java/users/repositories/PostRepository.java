package users.repositories;

import java.util.Set;

import users.models.Post;
import users.models.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface PostRepository extends CrudRepository<Post, Long> {
    public Page<Post> findByAuthorInOrderByCreatedAtDesc(Set<User> users, Pageable pageable);
}
