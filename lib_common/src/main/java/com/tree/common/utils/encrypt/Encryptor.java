package com.tree.common.utils.encrypt;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public interface Encryptor {


    byte[] encrypt(byte[] orig);
    byte[] decrypt(byte[] encrypted) throws DecryptFailureException;

}
