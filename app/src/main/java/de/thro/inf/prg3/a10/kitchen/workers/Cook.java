package de.thro.inf.prg3.a10.kitchen.workers;

import android.util.Log;

import de.thro.inf.prg3.a10.ProgressReporter;
import de.thro.inf.prg3.a10.kitchen.KitchenHatch;
import de.thro.inf.prg3.a10.model.Dish;
import de.thro.inf.prg3.a10.model.Order;

/**
 * @author Peter Kurfer
 */

public final class Cook implements Runnable {

    private static final String LOGGER_TAG = Cook.class.getName();
    private final String name;
    private final KitchenHatch kitchenHatch;
    private final ProgressReporter progressReporter;

    public Cook(String name, KitchenHatch kitchenHatch, ProgressReporter progressReporter) {
        this.name = name;
        this.kitchenHatch = kitchenHatch;
        this.progressReporter = progressReporter;
    }

    @Override
    public void run() {
        Order o;
        do {
            /* try to deque an order */
            o = kitchenHatch.dequeueOrder(2000);
            if(o != null){
                Dish d = new Dish(o.getMealName());
                try {
                    /* 'prepare' the dish */
                    Thread.sleep(d.getCookingTime());
                    Log.i(LOGGER_TAG, String.format("Cook %s prepared meal %s", name, d.getMealName()));
                } catch (InterruptedException e) {
                    Log.e(LOGGER_TAG, "Failed to cook meal", e);
                }
                /* pass the dish to the kitchen hatch */
                kitchenHatch.enqueueDish(d);
                Log.i(LOGGER_TAG, String.format("Cook %s put meal %s into the kitchen hatch", name, d.getMealName()));

                /* update progress - reduce orders progress and optionally enlarge kitchen hatch fill level */
                progressReporter.updateProgress();
            }
        }while (o != null);

        /* notify the progress reporter that the cook is leaving */
        progressReporter.notifyCookLeaving();
    }
}