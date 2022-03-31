package us.fjj.spring.learning.databaseseparateusage.base;

/**
 * 内部有个ThreadLocal，用来记录当前走主库还是从库，将这个标志放在dsTypeThreadLocal中
 */
public class DsTypeHolder {
    private static ThreadLocal<DsType> dsTypeThreadLocal = new ThreadLocal<>();

    public static void master() {
        dsTypeThreadLocal.set(DsType.MASTER);
    }

    public static void slaver() {
        dsTypeThreadLocal.set(DsType.SLAVER);
    }

    public static DsType getDsType() {
        return dsTypeThreadLocal.get();
    }

    public static void  clearDsType() {
        dsTypeThreadLocal.remove();
    }
}
