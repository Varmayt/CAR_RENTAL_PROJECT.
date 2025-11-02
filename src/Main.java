public class Main {
    public static void main(String[] args) {
        CarRentalSystem system = new CarRentalSystem();
        system.addCar(new Car("C001", "TOYOTA", "CAMRY", 3000.0));
        system.addCar(new Car("C002", "HONDA", "ACCORD", 4000.0));
        system.addCar(new Car("C003", "MAHINDRA", "THAR", 4500.0));
        system.addCar(new Car("C004", "POESCHE", "TAYCAN", 30000));
        system.menu();
    }
}



