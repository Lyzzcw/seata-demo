package com.lzy.seata.util;

import java.security.SecureRandom;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: lzy
 * Date: 2022/5/10
 * Time: 9:23
 * Description: 使用默认字母表每秒可生成超过 220 万个唯一 ID，使用自定义字母表每秒可生成超过 180 万个唯一 ID。
 * 与 UUID 不同，NanoID 的大小要小 4.5 倍，并且没有任何依赖关系。此外，大小限制已用于将大小从另外 35% 减小。
 * 在大多数随机生成器中，它们使用不安全的 Math.random()。但是，NanoID 使用 crypto module 和 Web Crypto API，意味着 NanoID 更安全
 * 由于内存分配的技巧，NanoID 比 UUID 快 60%。与 UUID 字母表中的 36 个字符不同，NanoID 只有 21 个字符。
 */
public class NanoIdUtils {
    public static final SecureRandom DEFAULT_NUMBER_GENERATOR = new SecureRandom();
    public static final char[] DEFAULT_ALPHABET = "_-0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    public static final int DEFAULT_SIZE = 21;
    private NanoIdUtils() {
        //Do Nothing
    }
    public static String randomNanoId() {
        return randomNanoId(DEFAULT_NUMBER_GENERATOR, DEFAULT_ALPHABET, DEFAULT_SIZE);
    }

    public static String randomNanoId(Random random, char[] alphabet, int size) {
        if (random == null) {
            throw new IllegalArgumentException("random cannot be null.");
        } else if (alphabet == null) {
            throw new IllegalArgumentException("alphabet cannot be null.");
        } else if (alphabet.length != 0 && alphabet.length < 256) {
            if (size <= 0) {
                throw new IllegalArgumentException("size must be greater than zero.");
            } else {
                int mask = (2 << (int) Math.floor(Math.log((double) (alphabet.length - 1)) / Math.log(2.0D))) - 1;
                int step = (int) Math.ceil(1.6D * (double) mask * (double) size / (double) alphabet.length);
                StringBuilder idBuilder = new StringBuilder();

                while (true) {
                    byte[] bytes = new byte[step];
                    random.nextBytes(bytes);

                    for (int i = 0; i < step; ++i) {
                        int alphabetIndex = bytes[i] & mask;
                        if (alphabetIndex < alphabet.length) {
                            idBuilder.append(alphabet[alphabetIndex]);
                            if (idBuilder.length() == size) {
                                return idBuilder.toString();
                            }
                        }
                    }
                }
            }
        } else {
            throw new IllegalArgumentException("alphabet must contain between 1 and 255 symbols.");
        }
    }

    public static void main(String[] args) {
        String nanoId = NanoIdUtils.randomNanoId();
        System.out.println(nanoId);
        System.out.println(System.currentTimeMillis());
        System.out.println(System.nanoTime());
    }
}

