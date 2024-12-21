package com.example.rpws.chapters;

import java.io.IOException;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import rx.Subscriber;

public class RxSeeEmitter extends SseEmitter{
    static final long SSE_SESSION_TIMEOUT = 30 * 60 * 1000L;
    private final Subscriber<Temperature> subscriber;

    RxSeeEmitter(){
        super(SSE_SESSION_TIMEOUT);
        this.subscriber = new Subscriber<Temperature>() {
            @Override
            public void onNext(Temperature item) {
                try {
                    RxSeeEmitter.this.send(item);
                } catch (IOException e) {
                   unsubscribe();
                }
            }

            @Override
            public void onError(Throwable throwable) {
            }

            @Override
            public void onCompleted() {
            }
        };
        onCompletion(subscriber::unsubscribe);
        onTimeout(subscriber::unsubscribe);
    }
    Subscriber<Temperature> getSubscriber(){
        return this.subscriber;
    }
    
}
