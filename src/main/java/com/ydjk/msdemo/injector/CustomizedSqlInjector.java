package com.ydjk.msdemo.injector;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.github.yulichang.injector.MPJSqlInjector;
import com.github.yulichang.method.*;

import java.util.List;

public class CustomizedSqlInjector extends MPJSqlInjector {
    /**
     * 如果只需增加方法，保留mybatis plus自带方法，
     * 可以先获取super.getMethodList()，再添加add
     */
    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass) {
        List<AbstractMethod> methodList = super.getMethodList(mapperClass);

        //多表查询sql注入 从连表插件里移植过来的
        methodList.add(new SelectJoinOne());
        methodList.add(new SelectJoinList());
        methodList.add(new SelectJoinPage());
        methodList.add(new SelectJoinMap());
        methodList.add(new SelectJoinMaps());
        methodList.add(new SelectJoinMapsPage());
        return methodList;
    }
}

