package com.mortgagecalculator.model;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import javax.persistence.*;

@Entity
@Table(name = "ParValueDailyRates")
//modeled this separately, in the case that we'd want to store other metadata associated with a par value rate
public class ParValueDailyRate {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private long id;

    @OneToOne
    @PrimaryKeyJoinColumn
    private DailyRate dailyRate;

    //applicable date is also a first-order concept for a par value; so storing it here as well, instead of having to
    //join on daily rate
    @Column(nullable = false)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    private LocalDate applicableDate;

    @Column(nullable = false)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime createdDateTime;

    //needed for hibernate
    protected ParValueDailyRate() {

    }

    public static ParValueDailyRate fromDailyRate(DailyRate dailyRate) {
        ParValueDailyRate parValueDailyRate = new ParValueDailyRate();
        parValueDailyRate.dailyRate = dailyRate;
        parValueDailyRate.applicableDate = dailyRate.getApplicableDate();
        parValueDailyRate.createdDateTime = DateTime.now();
        return parValueDailyRate;
    }

    public DailyRate getDailyRate() {
        return dailyRate;
    }

    @Override
    public String toString() {
        return "ParValueDailyRate{" +
                "id=" + id +
                ", dailyRate=" + dailyRate +
                ", applicableDate=" + applicableDate +
                ", createdDateTime=" + createdDateTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ParValueDailyRate that = (ParValueDailyRate) o;

        if (!getDailyRate().equals(that.getDailyRate())) return false;
        return applicableDate.equals(that.applicableDate);
    }

    @Override
    public int hashCode() {
        int result = getDailyRate().hashCode();
        result = 31 * result + applicableDate.hashCode();
        return result;
    }
}
