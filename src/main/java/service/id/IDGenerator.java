/**
 * 实现ID随机生成功能
 */

package service.id;

import java.util.Random;

public class IDGenerator
{
    public static String generateID()
    {
        // 前13位是当前毫秒时间, 后8位是随机数
        final int BOUND = 100000000;
        long millis = System.currentTimeMillis();
        long randNum = new Random().nextInt(BOUND);
        return String.format("%13d%08d", millis, randNum);
    }
}
