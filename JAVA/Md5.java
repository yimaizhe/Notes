package md5;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Md5 {

    private static BASE64Decoder dncoder = new BASE64Decoder();
    private static BASE64Encoder encoder = new BASE64Encoder();
    static String saltMeij;
    static String digestSMeij;
    protected static final String ALGORITHM = "MD5";

    /**
     * 生成口令字
     * 
     * @param password
     *            口令
     * @throws Exception
     *             口令生成例外
     */
    public static String createPassword(String password) throws Exception {

        // Create a new salt
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[12];
        random.nextBytes(salt);

        // Get a MessageDigest object
        MessageDigest md = MessageDigest.getInstance(ALGORITHM);
        md.update(salt);
        md.update(password.getBytes("UTF8"));

        byte[] digest = md.digest();
        BASE64Encoder encoder = new BASE64Encoder();

        // System.out.println(salt.toString());
        // System.out.println(digest);
        // System.out.println("not encode : " + salt + " length :" +
        // salt.length);

        String saltS = encoder.encode(salt);
        String digestS = encoder.encode(digest);
        // System.out.println("saltS : " + saltS + " length:" + saltS.length());
        // System.out.println("digestSS : " + digestS);
        saltMeij = saltS;
        digestSMeij = digestS;
        return saltS + digestS;
        /*
         * // Open up the password file and write the salt and the digest to it.
         * FileOutputStream fos = new FileOutputStream("password");
         * fos.write(saltS.getBytes()); fos.write(digestS.getBytes());
         * fos.close();
         */
    }

    /**
     * 认证口令
     * 
     * @param password
     *            传入的口令字
     * @param dbpassword
     *            数据库的口令字
     * @param oldsalt
     *            盐值
     * @throws PasswordAuthenticatorException
     *             口令的认证的例外
     * @throws Exception
     *             扔出例外
     */
    public static boolean authenticatePassword(String password,
            String dbpassword, String oldsalt) {

        /*
         * // Read in the byte array from the file "password"
         * ByteArrayOutputStream baos = new ByteArrayOutputStream();
         * FileInputStream fis = new FileInputStream("password"); int theByte =
         * 0; while ((theByte = fis.read()) != -1) { baos.write(theByte); }
         * fis.close(); byte[] hashedPasswordWithSalt = baos.toByteArray();
         * baos.reset(); byte[] salt = new byte[12];
         * System.arraycopy(hashedPasswordWithSalt,0,salt,0,12);
         */
        BASE64Decoder dncoder = new BASE64Decoder();
        byte[] bsalt;
        MessageDigest md = null;
        try {
            bsalt = dncoder.decodeBuffer(oldsalt);

            // byte[] bpassword = dncoder.decodeBuffer(password);
            // Get a message digest and digest the salt and
            // the password that was entered.
            md = MessageDigest.getInstance(ALGORITHM);
            md.update(bsalt);
            md.update(password.getBytes("UTF8"));
        } catch (Exception e) {
        }
        byte[] digest = md.digest();
        BASE64Encoder encoder = new BASE64Encoder();
        String digestS = encoder.encode(digest);
        // System.out.println("digestSS : " + digestS);

        byte[] dbdigest = dbpassword.getBytes();
        byte[] olddigest = digestS.getBytes();

        if (Arrays.equals(olddigest, dbdigest)) {
            // System.out.println("password match");
            return true;
        } else {
            return false;
            // throw new PasswordAuthenticatorException("口令不正确,请确认输入的大小写!!");

            // System.out.println("password is not match");
        }

        // Get the byte array of the hashed password in the file
        // byte[] digestInFile = new byte[hashedPasswordWithSalt.length-12];
        // System.arraycopy(hashedPasswordWithSalt,12,
        // digestInFile,0,hashedPasswordWithSalt.length-12);
        // Now we have both arrays, we need to compare them.

        /*
         * if (Arrays.equals(digest, digestInFile)) {
         * System.out.println("Password matches."); } else {
         * System.out.println("Password does not match"); }
         */
    }

    public static boolean authentication(String password, String dbPassword) {
        if ((password == null) || (dbPassword == null))
            return false;
        String salt = dbPassword.substring(0, 16);
        String encodedDbPassword = dbPassword
                .substring(16, dbPassword.length());
        try {
            String encodedPassword = encode(password, salt);
            System.out.print(Arrays.equals(encodedPassword.getBytes(),
                    encodedDbPassword.getBytes()));
            return Arrays.equals(encodedPassword.getBytes(),
                    encodedDbPassword.getBytes());
        } catch (Exception e) {
        }
        return false;
    }

    private static String encode(String password, String salt) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
            byte[] saltBytes = dncoder.decodeBuffer(salt);
            md.update(saltBytes);
            md.update(password.getBytes("UTF8"));
        } catch (Exception e) {
        }
        byte[] digestBytes = md.digest();
        return encoder.encode(digestBytes);
    }
}