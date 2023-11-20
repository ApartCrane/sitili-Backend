package mp.sitili.modules.payment_cc.entities;

import mp.sitili.modules.user.entities.User;
import javax.persistence.*;

@Entity
@Table(name = "payments_cc")
public class PaymentCC {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "cc", nullable = false, length = 50)
    private String cc;

    @Column(name = "day", nullable = false, length = 50)
    private String day;

    @Column(name = "month", nullable = false, length = 50)
    private String month;

    @Column(name = "year", nullable = false, length = 50)
    private String year;

    @Column(name = "cvv", nullable = false, length = 50)
    private String cvv;

    public PaymentCC(Integer id, User user, String cc, String day, String month, String year, String cvv) {
        this.id = id;
        this.user = user;
        this.cc = cc;
        this.day = day;
        this.month = month;
        this.year = year;
        this.cvv = cvv;
    }

    public PaymentCC() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }
}
