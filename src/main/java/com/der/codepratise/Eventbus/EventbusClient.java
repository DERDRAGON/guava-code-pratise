package com.der.codepratise.Eventbus;

import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @program: guava-code-pratise
 * @description: 事件总线
 * @author: long
 * @create: 2020-01-06 17:57
 */
public class EventbusClient {

    private List<ChangeEvent> changeEvents = Lists.newArrayList();

    @Getter
    @Setter
    class ChangeEvent {
        private String change;

        public ChangeEvent(String change) {
            this.change = change;
        }
    }

    class CustomerChangeEventListener {
        @Subscribe
        public void eventCustomChange(ChangeEvent e) {
            changeEvents.add(e);
        }
    }

    private ChangeEvent getChangeEvent(){
        return new ChangeEvent("一个事件:" + DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss S").format(LocalDateTime.now()));
    }

    public void addEvent() {
        EventBus eventBus = new EventBus();
        eventBus.register(new CustomerChangeEventListener());
        eventBus.post(getChangeEvent());
    }

    /**
     * see more visit http://ifeve.com/google-guava-eventbus/
     * @param args 不定参
     */
    public static void main(String[] args) {
        EventbusClient client = new EventbusClient();
        client.addEvent();
    }
}
