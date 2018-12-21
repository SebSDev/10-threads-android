package de.thro.inf.prg3.a10.kitchen.workers;

import de.thro.inf.prg3.a10.ProgressReporter;
import de.thro.inf.prg3.a10.kitchen.KitchenHatch;
import de.thro.inf.prg3.a10.model.Dish;
import de.thro.inf.prg3.a10.model.Order;

public class Cook implements Runnable
{
    private String name;
    private ProgressReporter progressReporter;
    private KitchenHatch kitchenHatch;

    public Cook (String name, KitchenHatch kitchenHatch, ProgressReporter progressReporter)
    {
        this.name = name;
        this.progressReporter = progressReporter;
        this.kitchenHatch = kitchenHatch;
    }

    @Override
    public void run()
    {
        Order order;

        // get orders as long as there are orders
        while (kitchenHatch.getOrderCount() > 0)
        {
            //!!! using timeout 1 because with 0 it just locks forever
            order = kitchenHatch.dequeueOrder(1);

            // prepare a new meal and cook it
            Dish dish = new Dish (order.getMealName());
            try
            {
                Thread.sleep(dish.getCookingTime());
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }

            // dish is finished. we can put it in the kitchen hatch
            kitchenHatch.enqueueDish(dish);

            progressReporter.updateProgress();
        }

        progressReporter.notifyCookLeaving();
    }
}
