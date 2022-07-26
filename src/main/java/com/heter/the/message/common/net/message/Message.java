package com.heter.the.message.common.net.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.heter.the.message.common.net.annotation.MessageMeta;

@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class Message {

    /**
     * messageMeta, module of message
     *
     * @return
     */
    public short getModule() {
        MessageMeta annotation = getClass().getAnnotation(MessageMeta.class);
        if (annotation != null) {
            return annotation.module();
        }
        return 0;
    }

    /**
     * messageMeta, subType of module
     *
     * @return
     */
    public byte getCmd() {
        MessageMeta annotation = getClass().getAnnotation(MessageMeta.class);
        if (annotation != null) {
            return annotation.cmd();
        }
        return 0;
    }

    public String key() {
        return this.getModule() + "_" + this.getCmd();
    }

}
