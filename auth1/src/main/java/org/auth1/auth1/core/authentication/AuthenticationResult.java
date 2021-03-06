package org.auth1.auth1.core.authentication;

import javax.annotation.Nullable;

/**
 * Result of calling {@link AuthenticationManager#authenticate(UserIdentifier, String, String)}.
 * See the constants of {@link ResultType} for details on the various states this class can take on.
 */
public class AuthenticationResult {
    public static final AuthenticationResult USER_DOES_NOT_EXIST
            = new AuthenticationResult(ResultType.USER_DOES_NOT_EXIST, null, null, null);
    public static final AuthenticationResult BAD_TOTP
            = new AuthenticationResult(ResultType.BAD_TOTP, null, null, null);
    public static final AuthenticationResult TOTP_REQUIRED
            = new AuthenticationResult(ResultType.TOTP_REQUIRED, null, null, null);
    public static final AuthenticationResult BAD_PASSWORD
            = new AuthenticationResult(ResultType.BAD_PASSWORD, null, null, null);
    public static final AuthenticationResult NOT_VERIFIED
            = new AuthenticationResult(ResultType.NOT_VERIFIED, null, null, null);
    public static final AuthenticationResult ACCOUNT_LOCKED
            = new AuthenticationResult(ResultType.ACCOUNT_LOCKED, null, null, null);


    public enum ResultType {
        /**
         * The authentication was successful, and {@link AuthenticationResult#getUserAuthenticationToken()}
         * returns a token object.
         */
        SUCCESS,
        /**
         * Too many authentication requests were made in a recent window of time so the request was not processed.
         */
        TOO_MANY_REQUESTS,
        /**
         * A user did not exist that matched the provided user identifier.
         */
        USER_DOES_NOT_EXIST,
        /**
         * The provided password did not match the password of the user.
         */
        BAD_PASSWORD,
        /**
         * The provided TOTP code did not match the one generated by the TOTP configuration of the user.
         */
        BAD_TOTP,
        /**
         * A TOTP code was required but none was provided.
         */
        TOTP_REQUIRED,
        /**
         * The account exists but is not verified so it cannot be authenticated against.
         */
        NOT_VERIFIED,
        /**
         * The account exists but is locked so it cannot be authenticated against.
         */
        ACCOUNT_LOCKED
    }

    private final ResultType type;
    private final @Nullable ExpiringToken userAuthenticationToken;
    private final @Nullable Double rateLimit; // number of logins per period/window (milliseconds)
    private final @Nullable Double ratePeriod; // milliseconds of the login period/window

    private AuthenticationResult(ResultType type, @Nullable ExpiringToken userAuthenticationToken, @Nullable Double rateLimit, @Nullable Double ratePeriod) {
        this.type = type;
        this.userAuthenticationToken = userAuthenticationToken;
        this.rateLimit = rateLimit;
        this.ratePeriod = ratePeriod;
    }

    public static AuthenticationResult forSuccess(ExpiringToken userAuthenticationToken) {
        return new AuthenticationResult(ResultType.SUCCESS, userAuthenticationToken, null, null);
    }

    public static AuthenticationResult forTooManyRequests(double rateLimit, double ratePeriod) {
        return new AuthenticationResult(ResultType.TOO_MANY_REQUESTS, null, rateLimit, ratePeriod);
    }


    public ResultType getType() {
        return type;
    }

    @Nullable
    public ExpiringToken getUserAuthenticationToken() {
        return userAuthenticationToken;
    }

    @Nullable
    public Double getRateLimit() {
        return rateLimit;
    }

    @Nullable
    public Double getRatePeriod() {
        return ratePeriod;
    }
}
