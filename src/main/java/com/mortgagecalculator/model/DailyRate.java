package com.mortgagecalculator.model;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import javax.persistence.*;

@Entity
@Table(name = "DailyRates")
public class DailyRate {

    //lender, mortgageProductType, interest_rate, price
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private long id;

    @Column(nullable = false)
    private String lenderName;

    @Column(nullable = false)
    private MortgageProductType mortgageProductType;

    //float precision seems sufficient for rates and prices

    @Column(nullable = false)
    private float interestRate;

    @Column(nullable = false)
    private float price;

    //the date for which the quote applies
    @Column(nullable = false)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    private LocalDate applicableDate;

    @Column(nullable = false)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime createdDateTime;

    //needed by hibernate
    protected DailyRate() {

    }

    public DailyRate(String lenderName, MortgageProductType mortgageProductType, float interestRate, float price,
                     LocalDate applicableDate) {
        this.lenderName = lenderName;
        this.mortgageProductType = mortgageProductType;
        this.interestRate = interestRate;
        this.price = price;
        this.applicableDate = applicableDate;
        this.createdDateTime = DateTime.now();
    }

    public long getId() {
        return id;
    }

    public String getLenderName() {
        return lenderName;
    }

    public MortgageProductType getMortgageProductType() {
        return mortgageProductType;
    }

    public float getInterestRate() {
        return interestRate;
    }

    public float getPrice() {
        return price;
    }

    public LocalDate getApplicableDate() {
        return applicableDate;
    }

    public DateTime getCreatedDateTime() {
        return createdDateTime;
    }

    @Override
    public String toString() {
        return "DailyRate{" +
                "id=" + id +
                ", lenderName='" + lenderName + '\'' +
                ", mortgageProductType=" + mortgageProductType +
                ", interestRate=" + interestRate +
                ", price=" + price +
                ", applicableDate=" + applicableDate +
                ", createdDateTime=" + createdDateTime +
                '}';
    }
}
