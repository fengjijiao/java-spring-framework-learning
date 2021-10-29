package us.fjj.spring.learning;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class JavaCollection {
    List list;
    Set set;
    Map map;
    Properties props;

    public List getList() {
        System.out.println("list: "+list);
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    public Set getSet() {
        System.out.println("set: "+set);
        return set;
    }

    public void setSet(Set set) {
        this.set = set;
    }

    public Map getMap() {
        System.out.println("map: "+map);
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public Properties getProps() {
        System.out.println("props: "+props);
        return props;
    }

    public void setProps(Properties props) {
        this.props = props;
    }
}
