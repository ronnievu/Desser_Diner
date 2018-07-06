package cli;

import static cli.AdminAndManager.con;
import static cli.Tiger.sc;
import java.sql.Connection;
import java.util.ArrayList;


import domain.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Scanner;
import services.CardService;


import services.MenuServices;
import services.OrderService;
import services.UserService;

public class ServiceWrapper {
	
	Connection con;
	
	
	
	public ServiceWrapper(Connection con) {
		super();
		this.con = con;

	}
        
        public boolean emailTaken(String email) {
            UserService us = new UserService(con);
            User candidate = us.getByEmail(email);
            return email == null;
        }

	public User login(String email, String password){
		
		UserService us = new UserService(con);
		User candidate = us.getByEmail(email);
		System.out.println(candidate.getFirstName());
		if(password.equals(candidate.getPassword())) return candidate;
		else return null;
	}
	
	public User register(String firstName, String lastName, String phone, String email, String password){
		//, String street, String city, String state, String country, String zip, String userStatus
		boolean result = false;
		String userId = Double.toString(Math.random()* 10001);
		String userStatusId = "1";

		User user = new User(userId,firstName,lastName,phone, email,password,userStatusId);
		UserService us = new UserService(con);
		result =  us.add(user);
		return user;
	}

	public static void printOptions(ArrayList<String> options){
		options.add("Go back");
		int count = 0;
		for(String option : options) {
			count++;
			System.out.println(count + ". " + option);
		}

	}
	
	public static void printMenuItems(ArrayList<Menu> menus){
		int count = 0;
		for(Menu menu: menus){
			count++;
			System.out.println(count + ". $" + menu.getPrice() + " " + menu.getName());
		}
		System.out.println(++count + ". Go Back");
	}

	public static void printOrders(ArrayList<Order> orders){
		int count = 0;
		for(Order order: orders){
			count++;
			System.out.println(count + ". " + order.getPlaced_timestamp());
		}
		System.out.println(count++ + ". Go Back");
	}

	public void cancelOrder(Order order) {
		order.setDelivery_status_id("3");
		OrderService os = new OrderService(con);
		os.update(order);
	}

	public void submitOrder(Order currentOrder) {
		// TODO Auto-generated method stub
		
		currentOrder.setDelivery_status_id("0");
		OrderService os = new OrderService(con);
		os.add(currentOrder);
		
	}

	public ArrayList<Menu> getMenuItems(ArrayList<String> itemIds) {
		
		MenuServices ms = new MenuServices(con);
		ArrayList<Menu> items = new ArrayList<Menu>();
		
		
		for (String itemId:itemIds){
			items.add(ms.getById(itemId));
		}

		return items;
	}

	public int calculateTotalPrice(ArrayList<String> item_ids) {
		int total = 0;
		ServiceWrapper sw = new ServiceWrapper(con);
		ArrayList<Menu> items = sw.getMenuItems(item_ids);
		for(Menu item: items){
			total += item.getPrice();
		}
		return total;
	}
        
    public void addCreditCard(
            String userId, 
            String cardNumber, 
            String securityCode, 
            Date expiryDate) 
    {
        String cardId = Double.toString(Math.random() * 10001);
        CardService cardService = new CardService(con);
        Card newCard = new Card(
                cardId,
                userId,
                cardNumber,
                expiryDate,
                securityCode);
        cardService.add(newCard);
    }
}
