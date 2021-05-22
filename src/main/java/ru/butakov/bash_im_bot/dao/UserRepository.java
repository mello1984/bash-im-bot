package ru.butakov.bash_im_bot.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.butakov.bash_im_bot.entity.state.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
