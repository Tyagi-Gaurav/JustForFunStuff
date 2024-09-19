package com.jffs.trade.monitor;

public class QueueFullException extends RuntimeException {
    public QueueFullException(Throwable cause) {
        super(cause);
    }

    public QueueFullException(String message) {
        super(message);
    }
}
