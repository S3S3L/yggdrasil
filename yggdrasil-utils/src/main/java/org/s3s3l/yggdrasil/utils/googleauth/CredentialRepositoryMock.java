package org.s3s3l.yggdrasil.utils.googleauth;

import java.util.List;

import com.warrenstrange.googleauth.ICredentialRepository;

/**
 * <p>
 * </p>
 * Date: Sep 12, 2019 4:46:03 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class CredentialRepositoryMock implements ICredentialRepository {
    /**
     * Name of the environment property used by this mock to retrieve the fake
     * secret key returned by <code>#getSecretKey</code>.
     */
    public static final String MOCK_SECRET_KEY_NAME = "org.s3s3l.yggdrasil.utils.googleauth.CredentialRepositoryMock.secret.name";

    /**
     * This method returns the value of the system property named
     * <code>#MOCK_SECRET_KEY_NAME</code>.
     *
     * @param userName
     *            the user whose private key shall be retrieved.
     * @return the value of the environment property named
     *         <code>#MOCK_SECRET_KEY_NAME</code>.
     */
    @Override
    public String getSecretKey(String userName) {
        return System.getProperty(MOCK_SECRET_KEY_NAME);
    }

    /**
     * This method does nothing.
     *
     * @param userName
     *            the user whose data shall be saved.
     * @param secretKey
     *            the generated key.
     * @param validationCode
     *            the validation code.
     * @param scratchCodes
     *            the list of scratch codes.
     */
    @Override
    public void saveUserCredentials(String userName, String secretKey, int validationCode, List<Integer> scratchCodes) {
        // Do nothing.
    }
}
