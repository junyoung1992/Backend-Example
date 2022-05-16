package me.whiteship.refactoring._24_comments._43_introduce_assertion;

public class Customer {

    private Double discountRate;

    public double applyDiscount(double amount) {
        return (this.discountRate != null) ? amount - (this.discountRate * amount) : amount;
    }

    public Double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(Double discountRate) {
//        assert discountRate != null && discountRate > 0;
        // assertion 문이 예외처리 기능을 해줄 수 없다.
        // 실제 프로그램 실행될 때는 동작하지 않는다.
        if (discountRate == null || discountRate <= 0) {
            throw new IllegalArgumentException(discountRate + " can't be minus.");
        }
        this.discountRate = discountRate;
    }
}
