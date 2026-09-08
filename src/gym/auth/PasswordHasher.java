package gym.auth;

import java.security.GeneralSecurityException; //handle security errors
import java.security.MessageDigest; //nk create hash
import java.security.SecureRandom; //generate random salt
import java.util.Base64; //decode/decode
import java.crypto.SecretKeyFactory; //generate secret code
import java.crypto.spec.PBEKeySpec; //utk simpan pass n setting tuk encrypt
