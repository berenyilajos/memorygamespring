package hu.fourdsoft.memorygame.service;

import hu.fourdsoft.memorygame.common.dto.UserDTO;
import hu.fourdsoft.memorygame.common.dto.helper.DtoHelper;
import hu.fourdsoft.memorygame.common.model.User;
import hu.fourdsoft.memorygame.dao.UserRepository;
import hu.fourdsoft.memorygame.exception.UserAllreadyExistException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;

import javax.transaction.Transactional;
import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<UserDTO> findAll() {
        return DtoHelper.usersToDTO(userRepository.findAll());
    }

	public UserDTO getUserByUsernameAndPassword(String username, String password) {
    	return DtoHelper.toDTOWithoutResults(userRepository.findOneByUsernameAndPassword(username, getMD5(password)));
	}

	@Transactional
	public void saveUser(String username, String password) throws UserAllreadyExistException {
    	Optional<User> existsUuser = userRepository.findByUsername(username);
    	if (existsUuser.isPresent()) {
    		throw new UserAllreadyExistException();
		}
		User user = new User();
		user.setUsername(username);
		user.setEmail(username + "@example.com");
		user.setPassword(getMD5(password));
		userRepository.saveAndFlush(user);
	}

	private static String getMD5(String rawPass) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch(NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}

		md.update(rawPass.getBytes());
		byte[] mdBytes = md.digest();

		// only java 8
		// String passEncrypted = Base64.getEncoder().encodeToString(mdBytes);
		String passEncrypted = DatatypeConverter.printBase64Binary(mdBytes);

		return passEncrypted;
	}

}
