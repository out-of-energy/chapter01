package com.example.rpws.chapters;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import rx.Observable;

//import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class TemperatureSensor {
    //private final ApplicationEventPublisher publisher;
    private final Random rnd = new Random();
    //private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

   /* public TemperatureSensor(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    } */

   /* 
   @PostConstruct
    public void startProcessing() {
        this.executor.schedule(this::probe, 1, TimeUnit.SECONDS);
    }

    private void probe() {
        double temperature = 16 + rnd.nextGaussian() * 10;
        publisher.publishEvent(new Temperature(temperature));
        executor.schedule(this::probe, rnd.nextInt(5000), TimeUnit.MILLISECONDS);
    }

    @PreDestroy
    public void shutdown() {
        executor.shutdown();
    }
   */ 
  private final Observable<Temperature> dataStream = 
      Observable.range(0, Integer.MAX_VALUE)
      .concatMap(tick -> Observable.just(tick)
      .delay(rnd.nextInt(5000), TimeUnit.MILLISECONDS)
      .map(tickValue -> this.probe()))
      .publish().refCount();
      private Temperature probe() {
          return new Temperature(16 + rnd.nextGaussian() * 10);
      }

      public Observable<Temperature> temperatureStream() {
          return this.dataStream;
      }
}