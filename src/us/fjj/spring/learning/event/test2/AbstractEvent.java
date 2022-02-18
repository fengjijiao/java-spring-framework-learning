package us.fjj.spring.learning.event.test2;

/**
 * 事件对象
 * 表示所有事件的父类，内部有个source字段，表示事件源；我们自定义的事件需要继承这个类。
 */
public abstract class AbstractEvent {
    //事件源
    protected Object source;

    public Object getSource() {
        return source;
    }

    public void setSource(Object source) {
        this.source = source;
    }

    public AbstractEvent(Object source) {
        this.source = source;
    }
}
