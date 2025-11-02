import java.util.*;
public class CarRentalSystem {
    private List<Car> cars;
    private List<Customer> customers;
    private List<Rental> rentals;
    public CarRentalSystem() {
        cars = new ArrayList<>();
        customers = new ArrayList<>();
        rentals = new ArrayList<>();
    }
    public void addCar(Car car) {
        cars.add(car);
    }
    public void addCustomer(Customer customer) {
        customers.add(customer);
    }
    public void rentCar(Car car, Customer customer, int days) {
        if (car.isAvailable()) {
            car.rent();
            rentals.add(new Rental(car, customer, days));
            System.out.println("\n✅" + "CAR RENTED SUCCESSFULLY!");
        } else {
            System.out.println("❌"  + "Car is not available for rent.");
        }
    }
    public void returnCar(Car car) {
        car.returnCar();
        Rental rentalToRemove = null;
        for (Rental rental : rentals) {
            if (rental.getCar() == car) {
                rentalToRemove = rental;
                break;
            }
        }
        if (rentalToRemove != null) {
            rentals.remove(rentalToRemove);
            System.out.println("✅" + "CAR RETURNED SUCCESSFULLY!");
        } else {
            System.out.println("❌" + " CAR WAS NOT RENTED.");
        }
    }
    public void menu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n===== CAR RENTAL SYSTEM =====");
            System.out.println("1. RENT A CAR");
            System.out.println("2. RETURN A CAR");
            System.out.println("3. EXIT");
            System.out.print("ENTER YOUR NEED : ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            if (choice == 1) {
                System.out.println("\n== RENT A CAR ==\n");
                System.out.print("ENTER YOUR NAME: ");
                String name = scanner.nextLine();
                System.out.print("ENTER YOUR PHONE NUMBER: ");
                long phone = scanner.nextLong();
                System.out.print("ENTER YOUR ADHAR CARD NUMBER: ");
                long adhar = scanner.nextLong();
                scanner.nextLine();
                Customer newCustomer = new Customer("CUST" + (customers.size() + 1), name, phone, adhar);
                addCustomer(newCustomer);
                System.out.println("\n-- AVAILABLE CARS --");
                for (Car car : cars) {
                    if (car.isAvailable()) {
                        System.out.println(car.getCarId() + " - " + car.getBrand() + " " + car.getModel());
                    }
                }
                System.out.print("\nENTER CAR ID TO RENT: ");
                String carId = scanner.nextLine();
                System.out.print("ENTER RENTAL DAYS: ");
                int days = scanner.nextInt();
                scanner.nextLine();
                Car selectedCar = null;
                for (Car car : cars) {
                    if (car.getCarId().equalsIgnoreCase(carId) && car.isAvailable()) {
                        selectedCar = car;
                        break;
                    }
                }
                if (selectedCar != null) {
                    double total = selectedCar.calculatePrice(days);
                    System.out.println("\n--- RENTAL DETAILS ---");
                    System.out.println("CUSTOMER ID: " + newCustomer.getCustomerId());
                    System.out.println("NAME: " + newCustomer.getName());
                    System.out.println("PHONE: " + newCustomer.getPhoneNumber());
                    System.out.println("ADHAR: " + newCustomer.getAdharCard());
                    System.out.println("CAR: " + selectedCar.getBrand() + " " + selectedCar.getModel());
                    System.out.println("DAYS: " + days);
                    System.out.printf("TOTAL PRICE: ₹%.2f%n", total);
                    System.out.print("\nCONFIRM RENTAL (Y/N): ");
                    String confirm = scanner.nextLine();
                    if (confirm.equalsIgnoreCase("Y")) {
                        rentCar(selectedCar, newCustomer, days);
                    } else {
                        System.out.println("❌" + "RENTAL CANCELLED.");
                        System.out.println("📞"  + "FOR ASSISTANCE, PLEASE CONTACT OUR TOLL-FREE NUMBER: 1800-123-9999");
                    }
                } else {
                    System.out.println("❌" +  "INVALID CAR ID OR CAR NOT AVAILABLE!");
                    System.out.println("📞" + "FOR ASSISTANCE, PLEASE CONTACT OUR TOLL-FREE NUMBER: 1800-123-9999");
                }
            } else if (choice == 2) {
                System.out.println("\n-- RETURN A CAR --\n");
                System.out.print("ENTER CARID TO RETURN : ");
                String carId = scanner.nextLine();
                Car carToReturn = null;
                for (Car car : cars) {
                    if (car.getCarId().equalsIgnoreCase(carId) && !car.isAvailable()) {
                        carToReturn = car;
                        break;
                    }
                }
                if (carToReturn != null) {
                    Customer customer = null;
                    for (Rental rental : rentals) {
                        if (rental.getCar() == carToReturn) {
                            customer = rental.getCustomer();
                            break;
                        }
                    }
                    if (customer != null) {
                        returnCar(carToReturn);
                        System.out.println("CAR RETURNED BY: " + customer.getName());
                    }
                } else {
                    System.out.println("❌" +  "INVALID CAR ID OR CAR NOT RENTED!");
                }

            } else if (choice == 3) {
                System.out.println("\n THANK YOU ^ IF YOU NEED IT'S OURS EXPERIENCE IT!");
                break;
            } else {
                System.out.println("❌" +  "INVALID CHOICE. TRY AGAIN.");
                System.out.println("📞"  + "FOR ASSISTANCE, PLEASE CONTACT OUR TOLL-FREE NUMBER: 1800-123-9999");
            }
        }
    }
}

