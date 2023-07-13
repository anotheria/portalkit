package net.anotheria.portalkit.adminapi.resources.account;

public class AccountUpdateRequest {

    private String id;

    private String email;
    private String name;
    private String brand;
    private Integer type;
    private String tenant;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

    @Override
    public String toString() {
        return "AccountUpdateRequest{" +
                "accountId='" + id + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", brand='" + brand + '\'' +
                ", type=" + type +
                ", tenant='" + tenant + '\'' +
                '}';
    }
}
