package users.repositories;

import java.util.List;

import users.models.User;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
	
	User findByLogin(String login);
	
	List<User> findFirst10ByIdNotIn(List<Long> users);
}
