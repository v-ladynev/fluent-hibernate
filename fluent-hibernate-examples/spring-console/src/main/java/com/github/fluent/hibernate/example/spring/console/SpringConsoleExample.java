package com.github.fluent.hibernate.example.spring.console;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.Assert;

import com.github.fluent.hibernate.H;
import com.github.fluent.hibernate.example.spring.console.persistent.Customer;
import com.github.fluent.hibernate.example.spring.console.persistent.Merchant;
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
        /*
                dealWithTaransactions();
                dealWithMerchantsAndCustomers();

                dealWithCheck();
         */
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
