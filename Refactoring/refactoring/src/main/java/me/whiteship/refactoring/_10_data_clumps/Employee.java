package me.whiteship.refactoring._10_data_clumps;

public class Employee {

    private String name;
    private TelephoneNumber personalPhoneNumber;

    public Employee(String name, TelephoneNumber personalPhoneNumber) {
        this.name = name;
        this.personalPhoneNumber = personalPhoneNumber;
    }

    public String personalPhoneNumber() {
        return personalPhoneNumber.toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TelephoneNumber getPersonalPhoneNumber() {
        return personalPhoneNumber;
    }

    public void setPersonalPhoneNumber(TelephoneNumber personalPhoneNumber) {
        this.personalPhoneNumber = personalPhoneNumber;
    }

}
