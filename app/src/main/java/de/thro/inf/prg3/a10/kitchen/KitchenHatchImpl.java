package de.thro.inf.prg3.a10.kitchen;

import java.util.Deque;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import de.thro.inf.prg3.a10.model.Dish;
import de.thro.inf.prg3.a10.model.Order;

public class KitchenHatchImpl implements KitchenHatch
{
    // maximum amount of dishes that can be in the KitchenHatch. When this capacity is reached the cooks have to wait.
    private final int maxMeals;

    // Queue for the dishes that are currently in the hatch
    private Deque<Dish> dishes;

    // Queue for the orders
    private Deque<Order> orders;

    public KitchenHatchImpl(int maxMeals, Deque<Order> orders)
    {
        this.maxMeals = maxMeals;
        this.orders = orders;

        dishes = new LinkedList<>();
    }

    @Override
    public int getMaxDishes()
    {
        return this.maxMeals;
    }

    @Override
    public Order dequeueOrder(long timeout)
    {
        Order deqOrder;

        synchronized (orders)
        {
            // waiting for the specified amount of time
            try
            {
                orders.wait(timeout);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }

            // Dequeue an order. if the queue is empty just return null
            try
            {
                deqOrder = orders.removeFirst();
            }
            catch (NoSuchElementException e)
            {
                deqOrder = null;
            }

            orders.notifyAll();
        }

        return deqOrder;
    }

    @Override
    public int getOrderCount()
    {
        synchronized (orders)
        {
            return orders.size();
        }
    }

    @Override
    public Dish dequeueDish(long timeout)
    {
        Dish deqDish;

        synchronized (dishes)
        {
            // waiting for the specified amount of time
            try
            {
                dishes.wait(timeout);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }

            // dequeue a dish. if the queue is empty just return null
            try
            {
                deqDish = dishes.removeFirst();
            }
            catch (NoSuchElementException e)
            {
                deqDish = null;
            }

            dishes.notifyAll();
        }

        return deqDish;
    }

    @Override
    public void enqueueDish(Dish m)
    {
        synchronized (dishes)
        {
            // we wait until there is place for our dish
            while (dishes.size() == maxMeals)
            {
                try
                {
                    dishes.wait();
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }

            dishes.addLast(m);

            dishes.notifyAll();
        }

    }

    @Override
    public int getDishesCount()
    {
        synchronized (dishes)
        {
            return dishes.size();
        }
    }
}
