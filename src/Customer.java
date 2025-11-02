public class Customer {
    private String customerId;
    private String name;
    private long phoneNumber;
    private long adharCard;
    public Customer(String customerId, String name, long phoneNumber, long adharCard) {
        this.customerId = customerId;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.adharCard = adharCard;
    }
    public String getCustomerId() {
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

