# Auth1 HTTP API Documentation

## Endpoints

### Register

Register a new user account.

Method: `POST`

Resource: `/register`

#### Parameters

Name     | Value 
-------: | ---
username | The username of the new user to be created
email    | The email of the new user to be created
password | The raw (unhashed) password of the new user

#### Response

##### Response JSON

Key    | Value 
-----: | ---
result | `RegistrationResult` corresponding to outcome.

##### RegistrationResult

Value                 | Meaning 
--------------------: | ---
SUCCESS               | The account was successfully created and is automatically verified.
SUCCESS_CONFIRM_EMAIL | The account was successfully created, but is not verified.
USERNAME_DUPLICATE    | The account was not created, because an account with this username already exists.
EMAIL_DUPLICATE       | The account was not created, because an account with this email already exists.
EMAIL_REQUIRED        | The account was not created, because an email is required for registration and one was not provided.
USERNAME_REQUIRED     | The account was not created, because a username is required for registration and one was not provided.
PASSWORD_WEAK         | The account was not created, because the password was deemed too weak for account creation.

##### Example Response

```
{'result': 'SUCCESS'}
```

### Login

Login to an existing user account and generate an authentication token to use for subsequent requests.

Method: `POST`

Resource: `/login`

#### Parameters

Name            | Value 
--------------: | ---
usernameOrEmail | The username or email of the user (both will be tried).
username        | The username of the user.
email           | The email of the user.
password        | Password of the user.
totpCode        | Current TOTP code (only required of the user has TOTP set up)

#### Response

##### Response JSON

Key        | Value 
---------: | ---
resultType | `AuthenticationResult` corresponding to outcome.
token      | `ExpiringToken` for the user authentication token if resultType is SUCCESS

##### AuthenticationResult

Value                 | Meaning 
--------------------: | ---
SUCCESS               | The authentication was successful, and a user authentication token is provided.
TOO_MANY_REQUESTS     | Too many authentication requests were made in a recent window of time so the request was not processed.
USER_DOES_NOT_EXIST   | A user did not exist that matched the provided user identifier.
BAD_PASSWORD          | The provided password did not match the password of the user.
BAD_TOTP              | The provided TOTP code did not match the one generated by the TOTP configuration of the user.
TOTP_REQUIRED         | A TOTP code was required but none was provided.
NOT_VERIFIED          | The account exists but is not verified so it cannot be authenticated against.
ACCOUNT_LOCKED        | The account exists but is locked so it cannot be authenticated against.

##### ExpiringToken JSON

Key            | Value 
-------------: | ---
tokenValue     | String value of the user authentication token.
expirationTime | Expiration time of the user authentication token.

##### Example Response

```
{'resultType': 'SUCCESS', 'token': {'tokenValue': 'VXugTbuFdz8hyNsAw3O+Yg==', 'expirationTime': '2019-04-26T18:57:10.79746-04:00'}}
```

### Check Authentication Token

Check the validity an authentication token and get the associated account (if any)

Method: `POST`

Resource: `/checkAuthToken`

#### Parameters

Name  | Value 
----: | ---
token | The string of the user authentication token

##### Response JSON

Key    | Value 
-----: | ---
valid  | 'true' if the token was valid, 'false' otherwise
userId | If the token is valid, the id of the user associated with the token

##### Example Responses

```
{'valid': 'false'}
```

```
{'valid': 'true', 'userId': 1}
```

### Request TOTP Secret

Request a tentative TOTP secret to set up TOTP for this account. After generating, use `/validateTotpSecret` to verify adn apply it.

Method: `POST`

Resource: `/requestTotpSecret`

#### Parameters

Name  | Value 
----: | ---
token | The string of the user authentication token to check.

##### Response JSON

Key                 | Value 
------------------: | ---
type                | `RequestTOTPResult` corresponding to outcome.
tentativeTotpSecret | Base 32 string of the TOTP secret if type is SUCCESS.
expirationTime      | Expiration time of the tenative TOTP configiration if type is SUCCESS.

##### RequestTOTPResult

Value         | Meaning 
------------: | ---
SUCCESS       | A TOTP code was created and must be confirmed to be applied.
INVALID_TOKEN | The authentication token provided was not valid.

### Validate TOTP Secret

Confirm and apply a tentative TOTP secret to an account by providing the TOTP code.

Method: `POST`

Resource: `/validateTotpSecret`

#### Parameters

Name  | Value 
----: | ---
token | The string of the user authentication token.
code  | The string of the 6-digit TOTP code to confirm.

##### Response JSON

Key                 | Value 
------------------: | ---
resultType          | `ValidateTOTPResult` corresponding to the outcome.

##### ValidateTOTPResult

Value                             | Meaning 
--------------------------------: | ---
SUCCESS                           | The TOTP code was confirmed and applied to the account.
TENTATIVE_TOTP_SECRET_NOT_CREATED | There does not exist a tentative TOTP secret to be confirmed. Call `/requestTotpSecret` first.
TENTATIVE_TOTP_SECRET_EXPIRED     | A TOTP secret was created but it has expired at this point. Call `/requestTotpSecret` to generate a new one.
INVALID_CODE                      | The TOTP code provided did not match the tenative TOTP configuration.
INVALID_TOKEN                     | The authentication token provided was not valid.

### Get Password Reset Token

Get a password reset token (which can then be sent to the user by email, text message, etc.)

Method: `POST`

Resource: `/getPasswordResetToken`

#### Parameters

Name            | Value 
--------------: | ---
usernameOrEmail | The username or email of the user (both will be tried).
username        | The username of the user.
email           | The email of the user.

##### Response JSON

Key                 | Value 
------------------: | ---
resultType          | `PasswordResetResult` corresponding to the outcome.
token               | If resultType is SUCCESS, then contains the password reset token.

##### GetPasswordResetTokenResult

Value                  | Meaning 
---------------------: | ---
SUCCESS                | A password reset token was successfully created for the user.
SUCCESS_ALREADY_SENT   | A password reset token has already been generated for the user.
ACCOUNT_DOES_NOT_EXIST | No account exists for the queried user.
ACCOUNT_LOCKED         | The account is locked and cannot get a reset token.

### Reset Password

Use a password reset token received from `/getPasswordResetToken` to reset the password for an account.

Method: `POST`

Resource: `/resetPassword`

#### Parameters

Name            | Value 
--------------: | ---
token           | The password reset token to use to authenticate the password reset request.
newPassword     | New password to set for the user.

##### Response JSON

Key    | Value 
-----: | ---
result | `ResetPasswordResult` corresponding to the outcome

##### ResetPasswordResult

Value               | Meaning 
------------------: | ---
SUCCESS             | The token was valid and the old password was replaced with the new password.
INVALID_TOKEN       | The token was invalid so the password was not reset.
INVALID_PASSWORD    | The new password was invalid as it did not follow the password rules for this Auth1 instance.
USER_DOES_NOT_EXIST | The user does not exist.