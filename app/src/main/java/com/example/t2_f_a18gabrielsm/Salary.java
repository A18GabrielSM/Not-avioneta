package com.example.t2_f_a18gabrielsm;

public class Salary {

    String month;
    float salary_total;

    public Salary() {
    }

    public Salary(String month, float salary_total) {
        this.month = month;
        this.salary_total = salary_total;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public float getSalary_total() {
        return salary_total;
    }

    public void setSalary_total(float salary_total) {
        this.salary_total = salary_total;
    }

    @Override
    public String toString() {
        return "Salary{" +
                "month='" + month + '\'' +
                ", salary_total=" + salary_total +
                '}';
    }
}
