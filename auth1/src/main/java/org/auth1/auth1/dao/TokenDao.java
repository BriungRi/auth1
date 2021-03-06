package org.auth1.auth1.dao;

import org.auth1.auth1.model.entities.PasswordResetToken;
import org.auth1.auth1.model.entities.UserAuthenticationToken;

import java.util.Optional;

public interface TokenDao {

    void saveLoginToken(final UserAuthenticationToken token);

    void savePasswordResetToken(final PasswordResetToken token);

    void logout(final UserAuthenticationToken token);

    Optional<UserAuthenticationToken> getAuthToken(String token);

    Optional<PasswordResetToken> getPasswordResetToken(String token);
}
