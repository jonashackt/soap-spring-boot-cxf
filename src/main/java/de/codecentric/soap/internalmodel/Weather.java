package de.codecentric.soap.internalmodel;


public class Weather {
	
	private String postalCode;
	private String flagColor;
	private Product product;
	private User user;
	
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String zipCode) {
		this.postalCode = zipCode;
	}
	public String getFlagColor() {
		return flagColor;
	}
	public void setFlagColor(String flagColor) {
		this.flagColor = flagColor;
	}
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public Product getProduct() {
        return product;
    }
    public void setProduct(Product product) {
        this.product = product;
    }
}
