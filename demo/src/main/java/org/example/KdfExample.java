package org.example;

import javax.crypto.KDF;
import javax.crypto.SecretKey;
import javax.crypto.spec.HKDFParameterSpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

public class KdfExample {

    static void main() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        // 创建 HKDF 实例
        KDF hkdf = KDF.getInstance("HKDF-SHA256");

        // 准备初始密钥材料和盐值
        byte[] initialKeyMaterial = "my-secret-key".getBytes();
        byte[] salt = "random-salt".getBytes();
        byte[] info = "application-context".getBytes();

        // 创建 HKDF 参数
        AlgorithmParameterSpec params = HKDFParameterSpec.ofExtract()
                .addIKM(initialKeyMaterial)  // 添加初始密钥材料
                .addSalt(salt)               // 添加盐值
                .thenExpand(info, 32);       // 扩展为 32 字节

        // 派生 AES 密钥
        SecretKey aesKey = hkdf.deriveKey("AES", params);

        // 或者直接获取字节数据
        byte[] derivedData = hkdf.deriveData(params);


    }

}
