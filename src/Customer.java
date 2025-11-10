public class Customer {
    private int customerId;
    private String name;
    private long phoneNumber;
    private long adharCard;
    public Customer(int customerId, String name, long phoneNumber, long adharCard) {
        this.customerId = customerId;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.adharCard = adharCard;
    }
    public int getCustomerId() {
        return customerId;
    }
    public String getName() {
        return name;
    }
    public long getPhoneNumber() {
        return phoneNumber;
    }
    public long getAdharCard() {
        return adharCard;
    }
}
