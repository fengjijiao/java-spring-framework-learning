package us.fjj.spring.learning.databaseseparateusage.base;

/**
 * 表示数据源类型，有2个值，用来区分是主库还是从库。
 */
public enum DsType {
    MASTER, SLAVER;
}
