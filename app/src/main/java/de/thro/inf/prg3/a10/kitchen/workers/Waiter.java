package de.thro.inf.prg3.a10.kitchen.workers;

import de.thro.inf.prg3.a10.ProgressReporter;
import de.thro.inf.prg3.a10.kitchen.KitchenHatch;
import de.thro.inf.prg3.a10.model.Dish;

public class Waiter implements Runnable
{
    private String name;
    private ProgressReporter progressReporter;
    private KitchenHatch kitchenHatch;

    public Waiter (String name, KitchenHatch kitchenHatch, ProgressReporter progressReporter)
    {
        this.name = name;
        this.progressReporter = progressReporter;
        this.kitchenHatch = kitchenHatch;
    }

    @Override
    public void run()
    {
        Dish dish;

        // get orders as long as there are orders
        while (kitchenHatch.getDishesCount() > 0 || kitchenHatch.getOrderCount() > 0)
        {
            dish = kitchenHatch.dequeueDish();

            if (dish != null)
            {
                // simulating serving the dish by sleeping a random amount of time
                try
                {
                    Thread.sleep((int) (Math.random() * 1000));
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }

                progressReporter.updateProgress();
            }

        }

        progressReporter.notifyWaiterLeaving();
    }
}
