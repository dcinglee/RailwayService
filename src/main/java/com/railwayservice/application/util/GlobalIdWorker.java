package com.railwayservice.application.util;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.SecureRandom;

/**
 * 可独立运行的全局ID生成器，可保持趋势递增。
 * 44位(自动扩展)毫秒+1标志+48Mac地址+1标志+13位累加随机数。
 * 每秒最少可生成819200个ID，平均情况每秒是1638400个。
 * 使用32位10进制存储可以使用到2300年之后，可以扩展长度。
 *
 * @author Ewing
 */
public class GlobalIdWorker implements IdentifierGenerator {
    // Mac地址掩码（48个1）
    private final static long macAddressMask = ~(-1L << 48L);
    // Mac标志位 保证长度一定是48+1位
    private final static long macAddressFlag = 1L << 48L;

    // 序号掩码（13个1）也是最大值
    private final static long sequenceMask = ~(-1L << 13L);
    // 序号标志位 保证长度一定是13+1位
    private final static long sequenceFlag = 1L << 13L;

    private static long lastTimestamp = System.currentTimeMillis();

    private static long sequence = 0L;

    private static String macAddressBit;

    /**
     * 初始化worker
     */
    static {
        try {
            long macAddress = macAddressLong();
            macAddressBit = Long.toBinaryString((macAddress & macAddressMask) | macAddressFlag);
        } catch (IOException e) {
            throw new RuntimeException("Init mac address fail.", e);
        }
    }

    /**
     * 获取机器的Mac地址（48位）
     */
    private static long macAddressLong() throws IOException {
        byte[] macs = NetworkInterface.getByInetAddress(InetAddress.getLocalHost()).getHardwareAddress();
        int shift = 0;
        long macLong = 0;
        for (int i = 0; i < macs.length; i++) {
            macLong = (macLong << shift) | (macs[i] & 0xFF);
            shift += 8;
        }
        return macLong;
    }

    /**
     * 生成唯一ID
     */
    public static synchronized BigInteger nextBigInteger() {
        long timestamp = timeGen();
        if (timestamp < lastTimestamp) {
            tilNextMillis(lastTimestamp);
        }

        if (lastTimestamp == timestamp) {
            // 当前毫秒内，则随机增加，避免尾数太集中
            sequence = sequence + new SecureRandom().nextInt(10) + 1;
            if (sequence > sequenceMask) {
                // 当前毫秒内计数满了，则等待下一毫秒
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0;
        }

        lastTimestamp = timestamp;

        // ID偏移组合生成最终的ID，并返回ID
        String idBit = Long.toBinaryString(timestamp) + macAddressBit +
                Long.toBinaryString(sequence | sequenceFlag);

        return new BigInteger(idBit, 2);
    }

    /**
     * 循环到下一毫秒
     */
    private static long tilNextMillis(final long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * 获取时间
     */
    private static long timeGen() {
        return System.currentTimeMillis();
    }

    /**
     * 获取BigDecimal类型的ID
     */
    public static BigDecimal nextBigDecimal() {
        return new BigDecimal(nextBigInteger());
    }

    @Override
    public Serializable generate(SessionImplementor sessionImplementor, Object o) throws HibernateException {
        return GlobalIdWorker.nextBigInteger().toString();
    }

    /**
     测试方法。
     使用到2300年时的值是：
     96339286759182815130258985975807

     1000个线程线程各生成1000个共：1000000 个无重复

     生成1000000（百万）个用时：1844 毫秒
     */
    /*public static void main(String[] args) throws Exception {
        // 使用到2300年的长度测试
        Long time = new SimpleDateFormat("yyyyMMdd").parse("23001230").getTime();
        String idBit = Long.toBinaryString(time) + Long.toBinaryString(~(-1L << 49L)) + Long.toBinaryString(~(-1L << 14L));
        System.out.println("使用到2300年时的值是：\n" + new BigInteger(idBit, 2) + "\n");

        for (int i = 0; i < 10; i++) {
            System.out.println(GlobalIdWorker.nextBigInteger());
        }

        // ID无重复测试 1000个线程 每个线程生成1000个
        CountDownLatch latch = new CountDownLatch(1000);
        Map<BigInteger, String> ids = new ConcurrentHashMap<>(1000000);
        for (int i = 0; i < 1000; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int n = 0; n < 1000; n++)
                        ids.put(GlobalIdWorker.nextBigInteger(), "");
                    latch.countDown();
                }
            }).start();
        }
        latch.await();
        System.out.println("1000个线程线程各生成1000个共：" + ids.size() + " 个无重复\n");

        // 生成效率测试
        time = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            GlobalIdWorker.nextBigInteger();
        }
        System.out.println("生成1000000（百万）个用时：" + (System.currentTimeMillis() - time) + " 毫秒");
    }*/
} 