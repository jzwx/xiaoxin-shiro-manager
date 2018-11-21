package com.xiaoxin.manager.utils;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author:jzwx
 * @Desicription: DateUtil
 * @Date:Created in 2018-11-19 14:09
 * @Modified By:
 */
public class DateUtil implements AutoCloseable, Serializable {
    private static final long serialVersionUID = -171849725965861058L;
    //饿汉单例
    public static DateUtil    instance         = new DateUtil();

    public DateUtil() {
    }

    //防序列化（杜绝单例对象被反序列化时重新生成对象）
    private Object readResolve() throws ObjectStreamException {
        return instance;
    }

    // SimpleDateFormat线程不安全的类，使用ThreadLocal,
    // 也是将共享变量变为独享，线程独享肯定能比方法独享在并发环境中能减少不少创建对象的开销。如果对性能要求比较高的情况下，一般推荐使用这种方法。
    private static ThreadLocal<DateFormat> threadLocal = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    /**
     *
     * @描述：格式化String转换为Date
     * @创建人：jzwx
     * @创建时间：2017年6月7日 下午6:27:03
     * @param dateStr
     * @return Date
     * @throws ParseException
     */
    public static Date parse(String dateStr) throws ParseException {
        return threadLocal.get().parse(dateStr);
    }

    /**
     *
     * @描述：将date日期转换为string
     * @创建人：jzwx
     * @创建时间：2017年6月7日 下午6:27:14
     * @param date
     * @return 格式：yyyy-MM-dd HH:mm:ss
     */
    public static String format(Date date) {
        return threadLocal.get().format(date);
    }

    @Override
    public void close() throws Exception {

    }
}
