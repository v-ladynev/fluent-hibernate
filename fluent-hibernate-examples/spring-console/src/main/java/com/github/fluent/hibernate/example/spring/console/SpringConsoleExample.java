package com.github.fluent.hibernate.example.spring.console;

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
        createTransactions();
        loadTransactionsWithAllProperties();
        loadTransactionsWithPartProperties();

        addSomeCustomers();
        addPrimaryCustomersTo("Doe", H.<Customer> request(Customer.class).list());
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

    private void addSomeCustomers() {
        H.save(Customer.create("Mister"));
        H.save(Customer.create("Twister"));
    }

    private void addPrimaryCustomersTo(String merchantName, List<Customer> primaryCustomers) {
        Merchant merchant = H.<Merchant> request(Merchant.class).eq("name", merchantName).first();
        Assert.notNull(merchant,
                String.format("Can't find a merchant with name '%s'", merchantName));
        merchant.setPrimaryCustomers(primaryCustomers);
        H.saveOrUpdate(merchant);
    }

}
