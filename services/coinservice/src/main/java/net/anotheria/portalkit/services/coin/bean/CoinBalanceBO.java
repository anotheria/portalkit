package net.anotheria.portalkit.services.coin.bean;

import java.io.Serializable;

public class CoinBalanceBO implements Serializable {

    private String accountId;
    private Integer amount;

    public CoinBalanceBO() {
    }

    public CoinBalanceBO(String accountId, Integer amount) {
        this.accountId = accountId;
        this.amount = amount;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CoinBalanceBO that = (CoinBalanceBO) o;

        return accountId != null ? accountId.equals(that.accountId) : that.accountId == null;
    }

    @Override
    public int hashCode() {
        return accountId != null ? accountId.hashCode() : 0;
    }
}
