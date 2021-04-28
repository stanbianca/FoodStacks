package Functionalities;

import Classes.*;
import org.javatuples.Triplet;

import java.util.HashMap;
import java.util.List;

public class CartService extends PlatformService {

    private static CartService instance;
    private Audit audit;

    private CartService()
    {
        audit = Audit.getInstance("F:\\Github\\FoodStacks\\Data\\Audit.csv");
    }


    public static CartService getInstance()
    {
        if(instance==null)
        {
            instance = new CartService();
        }
        return instance;
    }
    
    public void removeFromCart(Restaurant restaurant, Dish dish)
    {
        audit.writeToFile();
        if(loggedInUser instanceof Client)
        {
            var cart = ((Client) loggedInUser).getCart();
            HashMap<Restaurant, List<Triplet<Dish, Integer, Double>>> dishes = cart.getDishes();
            List<Triplet<Dish, Integer, Double>> dishesFromRestaurant = dishes.get(restaurant);
            if(dishesFromRestaurant != null)
                for (Triplet<Dish, Integer, Double> triplet : dishesFromRestaurant) {
                    if (dish.equals(triplet.getValue0())) {
                        dishesFromRestaurant.remove(triplet);
                        break;
                    }
                }
            dishes.put(restaurant, dishesFromRestaurant);
            cart.setDishes(dishes);
            ((Client) loggedInUser).setCart(cart);
            System.out.println("Dish "+ dish + " was successfully removed from cart!");
        }
    }

    public void removeAnyRestaurantsWithNoDishes()
    {
        audit.writeToFile();
        if(loggedInUser instanceof Client)
        {
            var cart = ((Client) loggedInUser).getCart();
            var dishes = cart.getDishes();
            for(var restaurant : dishes.keySet())
            {
                if(dishes.get(restaurant).size()==0)
                {
                    dishes.remove(restaurant);
                }
            }
            cart.setDishes(dishes);
            ((Client) loggedInUser).setCart(cart);
        }
    }


    public void increaseNumberFromCart(Cart cart, Restaurant restaurant, Dish dish, Integer by)
    {
        audit.writeToFile();
        if(loggedInUser instanceof Client)
        {
            HashMap<Restaurant, List<Triplet<Dish, Integer, Double>>> dishes = cart.getDishes();
            List<Triplet<Dish, Integer, Double>> dishesFromRestaurant = dishes.get(restaurant);
            for(Triplet<Dish, Integer, Double> triplet : dishesFromRestaurant)
            {
                if(dish.equals(triplet.getValue0()))
                {
                    triplet=triplet.setAt1(triplet.getValue1()-by);
                    if(triplet.getValue1()<=0)
                        dishesFromRestaurant.remove(triplet);
                    break;
                }
            }
            dishes.put(restaurant, dishesFromRestaurant);
            cart.setDishes(dishes);
            System.out.println(by + "portions of "+ dish + " were successfully removed from cart!");
        }
    }
}
