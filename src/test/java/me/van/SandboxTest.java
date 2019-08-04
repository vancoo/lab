package me.van;

import me.van.Sandbox;
import me.van.SandboxCannotCreateObjectException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class SandboxTest {

    @Test
    public void getEnumValue() throws SandboxCannotCreateObjectException {
        //设定重定义的包
        Sandbox sandbox = new Sandbox("com.google.common.collect");

        //获取沙箱内对象，虽然是同名同值，但由于分属沙箱内外，因此预期应该不等
        Enum type = sandbox.getEnumValue(com.google.common.collect.BoundType.CLOSED);
        assertNotEquals(type, com.google.common.collect.BoundType.CLOSED);

        //通过沙箱获取非设定需要重定义的包内对象，预期应该是相等
        Enum property = sandbox.getEnumValue(com.google.common.base.StandardSystemProperty.JAVA_CLASS_PATH);
        assertEquals(property, com.google.common.base.StandardSystemProperty.JAVA_CLASS_PATH);
    }

    @Test
    public void createObject() throws SandboxCannotCreateObjectException, ClassNotFoundException {
        //设定重定义的包
        Sandbox sandbox = new Sandbox("com.google.common.eventbus");

        //获取沙箱内对象，预期中类定义应该与沙箱外的类定义不等
        com.google.common.eventbus.EventBus bus = sandbox.createObject(com.google.common.eventbus.EventBus.class);
        assertNotEquals(bus.getClass(), com.google.common.eventbus.EventBus.class);

        //通过名称获取，如上
        bus = sandbox.createObject("com.google.common.eventbus.EventBus");
        assertNotEquals(bus.getClass(), com.google.common.eventbus.EventBus.class);

        //通过沙箱获取无需重定义的类，预期应该跟沙箱外相等
        List<String> list = sandbox.createObject(ArrayList.class);
        assertEquals(list.getClass(), ArrayList.class);
    }
}