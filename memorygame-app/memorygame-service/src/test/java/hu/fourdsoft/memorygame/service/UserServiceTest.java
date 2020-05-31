package hu.fourdsoft.memorygame.service;

import hu.fourdsoft.memorygame.common.model.User;
import hu.fourdsoft.memorygame.dao.UserRepository;
import hu.fourdsoft.memorygame.exception.UserAllreadyExistException;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@RunWith(SpringJUnit4ClassRunner.class)
public class UserServiceTest {

	@InjectMocks
	private UserService underTest;

	@Mock
	private UserRepository userRepository;
	private static final String existingUserName = "existingUserName";
	private static final String existingUserEmail = existingUserName + "@example.com";
	private static final String password = "password";
	private static final String notExistingUserName = "notExistingUserName";
	private static final long id = 1;
	private static final User existingUser = new User(id, existingUserName, existingUserEmail, password);

	@Before
	public void setUp() throws Exception {
		when(userRepository.findByUsername(existingUserName)).thenReturn(Optional.of(existingUser));
		when(userRepository.findByUsername(notExistingUserName)).thenReturn(Optional.empty());
	}

	@Test
	public void saveUser() throws UserAllreadyExistException {
		underTest.saveUser(notExistingUserName, password);
		verify(userRepository, times(1)).findByUsername(notExistingUserName);

		Assertions.assertThatExceptionOfType(UserAllreadyExistException.class)
				.isThrownBy(() ->  underTest.saveUser(existingUserName, password))
				.withMessage(UserAllreadyExistException.USER_ALLREADY_EXISTS);
	}

}