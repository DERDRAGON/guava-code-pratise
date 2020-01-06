package com.der.codepratise.Eventbus;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import javax.swing.event.ChangeEvent;

/**
 * @program: guava-code-pratise
 * @description: 事件总线
 * @author: long
 * @create: 2020-01-06 17:57
 */
public class EventbusClient {

    static class CustomerChangeEventListener {
        @Subscribe
        public void eventCustomChange(ChangeEvent e) {
            System.out.println(e.getSource());
        }
    }

    public static void main(String[] args) {
        EventBus eventBus = new EventBus();
        eventBus.register(new CustomerChangeEventListener());
    }
}
