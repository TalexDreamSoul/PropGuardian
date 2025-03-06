package dao.entity;

public class PriceType {
    private int chargeId;
    private String chargeName;
    private double unitPrice;

    // 构造函数
    public PriceType(int chargeId, String chargeName, double unitPrice) {
        this.chargeId = chargeId;
        this.chargeName = chargeName;
        this.unitPrice = unitPrice;
    }

    // getter 和 setter 方法
    public int getChargeId() {
        return chargeId;
    }

    public void setChargeId(int chargeId) {
        this.chargeId = chargeId;
    }

    public String getChargeName() {
        return chargeName;
    }

    public void setChargeName(String chargeName) {
        this.chargeName = chargeName;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    @Override
    public String toString() {
        return "PriceType{" +
                "chargeId=" + chargeId +
                ", chargeName='" + chargeName + '\'' +
                ", unitPrice=" + unitPrice +
                '}';
    }

}
