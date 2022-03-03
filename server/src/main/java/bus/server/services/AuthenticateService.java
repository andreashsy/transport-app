package bus.server.services;

import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bus.server.repositories.UserRepository;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.json.Json;
import jakarta.json.JsonObject;

@Service
public class AuthenticateService {
    @Autowired
	private UserRepository userRepo;

	private SecretKey signKey;

	@PostConstruct
	public void init() {
		String passphrase = System.getenv("JWT_SIGNKEY");

		try {
			signKey = Keys.hmacShaKeyFor(passphrase.getBytes("UTF-8"));
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new IllegalArgumentException("Creating HMAC Sign key", ex);
		}
	}

	public boolean validate(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(signKey).build()
				.parseClaimsJws(token);
			return true;
		} catch (JwtException ex) {
			ex.printStackTrace();
		}
		return false;
	}

	public Optional<JsonObject> authenticate(String username, String password) {

		if (!userRepo.validateUser(username, password))
			return Optional.empty();

		String token = Jwts.builder()
			.setSubject(username)
			.signWith(signKey)
			.compact();

		return Optional.of(Json.createObjectBuilder()
			.add("subject", username)
			.add("token", token)
			.build());
	}
}
