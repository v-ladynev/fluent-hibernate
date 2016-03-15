package com.github.fluent.hibernate.example.spring.console;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.Assert;

import com.github.fluent.hibernate.H;
import com.github.fluent.hibernate.example.spring.console.persistent.Customer;
import com.github.fluent.hibernate.example.spring.console.persistent.Merchant;
import com.github.fluent.hibernate.example.spring.console.persistent.RegionalCountry;
import com.github.fluent.hibernate.example.spring.console.persistent.Student;
import com.github.fluent.hibernate.example.spring.console.persistent.Transaction;
import com.github.fluent.hibernate.factory.HibernateSessionFactory;

/**
 *
 * @author V.Ladynev
 */
public class SpringConsoleExample {

    public static void main(String[] args) {
        try {
            new ClassPathXmlApplicationContext("classpath:hibernate-context.xml")
                    .registerShutdownHook();

            new SpringConsoleExample().doSomeDatabaseStuff();
        } finally {
            HibernateSessionFactory.closeSessionFactory();
        }
    }

    private void doSomeDatabaseStuff() {
        Student a = H.save(new Student("John"));
        Student b = H.save(new Student("Kelly"));
        H.save(new Student("Mary"));

        b.getFriends().add(a);
        H.saveOrUpdate(b);

        Student x = H.getById(Student.class, b.getId());
        System.out.println(x.getFriends().get(0).getName());

        /*
                dealWithTaransactions();
                dealWithMerchantsAndCustomers();

                dealWithCheck();
         */
        /*
                final UserDetails user = new UserDetails();
                final UserDetails user2 = new UserDetails();
                user.setUserName("Faisal");
                user2.setUserName("Raza");

                final Vehicle vehicle = new Vehicle();
                final Vehicle vehicle2 = new Vehicle();
                final Vehicle vehicle3 = new Vehicle();

                vehicle.setVehicleName("Bullet ThunderBird");
                vehicle2.setVehicleName("yamaha");
                vehicle3.setVehicleName("bullet");

                // Didirectional relationship
                user.getVehicle().add(vehicle);
                vehicle.getUserList().add(user);

                user.getVehicle().add(vehicle2);
                vehicle2.getUserList().add(user);

                user2.getVehicle().add(vehicle3);
                vehicle3.getUserList().add(user2);

                HibernateSessionFactory.doInTransaction(new IRequest<Void>() {
                    @Override
                    public Void doInTransaction(Session session) {
                        session.save(user);
                        session.save(user2);

                        session.save(vehicle);
                        session.save(vehicle2);
                        session.save(vehicle3);

                        return null;
                    }

                });
         */
    }

    private void dealWithCheck() {
        List<RegionalCountry> list = H.<RegionalCountry> request(RegionalCountry.class)
                .eq("countryCode", "xxx").list();
    }

    private void dealWithMerchantsAndCustomers() {
        createCustomers("Mister", "Twister");

        addPrimaryCustomersTo(getMerchantByName("Doe"), getCustomersByNames("Mister", "Twister"));

        System.out.println("primary customesrs of Doe " + getPrimaryCustomersOf("Doe"));

        addFriendsTo(getMerchantByName("Doe"),
                createCustomers("Doe's friend Ann", "Doe's friend Ta"));

        System.out.println("primary customesrs of Doe " + getPrimaryCustomersOf("Doe"));
        System.out.println("friends of Doe " + getFriendsOf("Doe"));
    }

    private void dealWithTaransactions() {
        createTransactions();
        loadTransactionsWithAllProperties();
        loadTransactionsWithPartProperties();
    }

    private List<Customer> getPrimaryCustomersOf(String merchantName) {
        Merchant result = H.<Merchant> request(Merchant.class).eq("name", merchantName)
                .fetchJoin("primaryCustomers").first();
        return result.getPrimaryCustomers();
    }

    private List<Customer> getFriendsOf(String merchantName) {
        Merchant result = H.<Merchant> request(Merchant.class).eq("name", merchantName)
                .fetchJoin("friends").first();
        return result.getFriends();
    }

    private void loadTransactionsWithAllProperties() {
        List<Transaction> transactions = H.<Transaction> request(Transaction.class).list();
        System.out.println(transactions);
    }

    private void loadTransactionsWithPartProperties() {
        List<Transaction> transactions = H.<Transaction> request(Transaction.class)
                .innerJoin("customer").innerJoin("merchant").proj("customer.name")
                .proj("merchant.name").proj("amountDue").transform(Transaction.class).list();
        System.out.println(transactions);
    }

    private void createTransactions() {
        Customer customer = H.save(Customer.create("Jhon"));
        Merchant merchant = H.save(Merchant.create("Doe"));
        Transaction t1000 = Transaction.create(customer, merchant);
        t1000.setAmountDue(1000L);

        Transaction t2000 = Transaction.create(customer, merchant);
        t2000.setAmountDue(2000L);

        H.saveAll(Arrays.asList(t1000, t2000));
    }

    private List<Customer> createCustomers(String... names) {
        List<Customer> result = new ArrayList<Customer>();
        for (String name : names) {
            result.add(H.save(Customer.create(name)));
        }

        return result;
    }

    private void addPrimaryCustomersTo(Merchant merchant, List<Customer> primaryCustomers) {
        merchant.setPrimaryCustomers(primaryCustomers);
        H.saveOrUpdate(merchant);
    }

    private void addFriendsTo(Merchant merchant, List<Customer> friends) {
        merchant.setFriends(friends);
        H.saveOrUpdate(merchant);
    }

    private Merchant getMerchantByName(String merchantName) {
        Merchant result = H.<Merchant> request(Merchant.class).eq("name", merchantName).first();
        Assert.notNull(result, String.format("Can't find a merchant with name '%s'", merchantName));
        return result;
    }

    private List<Customer> getCustomersByNames(String... names) {
        return H.<Customer> request(Customer.class).in("name", names).list();
    }

}
