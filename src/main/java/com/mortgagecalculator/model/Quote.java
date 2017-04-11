package com.mortgagecalculator.model;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import javax.persistence.*;

@Entity
@Table(name = "Quotes")
public class Quote {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private long id;

    //we might want to provide quotes that aren't par value, for some reason; so a quote should have a daily rate instead
    //of a par value daily rate
    @OneToOne
    @PrimaryKeyJoinColumn
    private DailyRate dailyRate;

    @Column(nullable = false)
    private long loanAmountCents;

    @Column(nullable = false)
    private long monthlyPaymentAmountCents;

    //applicable date is also a first-order concept for a quote; so storing it here as well, instead of having to
    //join on daily rate
    @Column(nullable = false)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    private LocalDate applicableDate;

    @Column(nullable = false)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime createdDateTime;

    //needed by hibernate
    protected Quote()  {

    }

    public Quote(DailyRate dailyRate, long loanAmountCents, long monthlyPaymentAmountCents) {
        this.dailyRate = dailyRate;
        this.loanAmountCents = loanAmountCents;
        this.monthlyPaymentAmountCents = monthlyPaymentAmountCents;
        this.applicableDate = dailyRate.getApplicableDate();
        this.createdDateTime = DateTime.now();
    }

    public DailyRate getDailyRate() {
        return dailyRate;
    }

    public long getLoanAmountCents() {
        return loanAmountCents;
    }

    public long getMonthlyPaymentAmountCents() {
        return monthlyPaymentAmountCents;
    }

    public LocalDate getApplicableDate() {
        return applicableDate;
    }

    public DateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public boolean isFixed() {
        return getDailyRate().getMortgageProductType().isFixed();
    }

    public int getFixedYears() {
        return getDailyRate().getMortgageProductType().getFixedYears();
    }

    @Override
    public String toString() {
        return "Quote{" +
                "id=" + id +
                ", dailyRate=" + dailyRate +
                ", loanAmountCents=" + loanAmountCents +
                ", monthlyPaymentAmountCents=" + monthlyPaymentAmountCents +
                ", applicableDate=" + applicableDate +
                ", createdDateTime=" + createdDateTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Quote quote = (Quote) o;

        if (getLoanAmountCents() != quote.getLoanAmountCents()) return false;
        if (getMonthlyPaymentAmountCents() != quote.getMonthlyPaymentAmountCents()) return false;
        if (!getDailyRate().equals(quote.getDailyRate())) return false;
        return getApplicableDate().equals(quote.getApplicableDate());
    }

    @Override
    public int hashCode() {
        int result = getDailyRate().hashCode();
        result = 31 * result + (int) (getLoanAmountCents() ^ (getLoanAmountCents() >>> 32));
        result = 31 * result + (int) (getMonthlyPaymentAmountCents() ^ (getMonthlyPaymentAmountCents() >>> 32));
        result = 31 * result + getApplicableDate().hashCode();
        return result;
    }
}
