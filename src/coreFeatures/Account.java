package coreFeatures;
/*
 * Will represent a single account, either checking or savings (or whatever).
 * 
 * Should hold data like account balance, transaction history, and future transactions.
 * 
 * Can partition balance into sections of what you can use, and what is needed to be spent on bills.
 *     It should also be able to hold future paychecks as well.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Scanner;

public class Account {
    String name;
    private BigDecimal balance;
    private Transaction[] transactionHistory;

    private boolean hasInterestRate;
    private BigDecimal interestRate;

    // these headers are the strings that the csv files uses to identify the columns for each data type.
    private String amountCsvHeader;
    private String dateCsvHeader;
    private String descriptionCsvHeader;
    private int dateCsvIndex = -1;
    private int descriptionCsvIndex = -1;
    private int amountCsvIndex = -1;

    public String getAmountCsvHeader() {
        return amountCsvHeader;
    }

    public void setAmountCsvHeader(String amountHeader) {
        this.amountCsvHeader = amountHeader;
    }

    public String getDateCsvHeader() {
        return dateCsvHeader;
    }

    public void setDateCsvHeader(String dateHeader) {
        this.dateCsvHeader = dateHeader;
    }

    public String getDescriptionCsvHeader() {
        return descriptionCsvHeader;
    }

    public void setDescriptionCsvHeader(String descriptionHeader) {
        this.descriptionCsvHeader = descriptionHeader;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getBalance() {
        calculateBalance();
        return balance;
    }

    public void calculateBalance(){
        BigDecimal newBalance = BigDecimal.ZERO;
        for (Transaction transaction : transactionHistory) {
            newBalance = newBalance.add(transaction.getAmount());
        }
        balance = newBalance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void addToBalance(BigDecimal balance) {
        this.balance.add(balance);
    }

    public Transaction[] getTransactionHistory() {
        return transactionHistory;
    }

    public void setTransactionHistory(Transaction[] transactionHistory) {
        this.transactionHistory = transactionHistory;
    }

    /**
     * Creates a new temporary array with one extra index. Adds the new transaction
     * to the last index.
     * 
     * @param transaction - The transaction you would like to add to the accounts transaction history
     */
    public void addTransaction(Transaction transaction) {
        Transaction[] tempTransactionHistory = new Transaction[this.transactionHistory.length + 1];

        for (int i = 0; i < this.transactionHistory.length; ++i) {
            tempTransactionHistory[i] = transactionHistory[i];
        }

        tempTransactionHistory[tempTransactionHistory.length - 1] = transaction;
        transactionHistory = tempTransactionHistory;
    }

    public void removeTransaction(Transaction transaction){
        Transaction[] tempTransactionHistory = new Transaction[this.transactionHistory.length - 1];

        for (int i = 0; i < this.transactionHistory.length; ++i) {
            if(!transaction.equals(getTransactionHistory()[i])){
                tempTransactionHistory[i] = transactionHistory[i];
            }
        }

        transactionHistory = tempTransactionHistory;
    }

    public boolean isHasInterestRate() {
        return hasInterestRate;
    }

    public void setHasInterestRate(boolean hasInterestRate) {
        this.hasInterestRate = hasInterestRate;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public Account(String name, double balance) {
        this.name = name;
        this.balance = BigDecimal.valueOf(balance);
    }

    public Account(String name, double balance, boolean hasInterestRate, double interestRate) {
        this.name = name;
        this.balance = BigDecimal.valueOf(balance);
        this.hasInterestRate = hasInterestRate;
        this.interestRate = BigDecimal.valueOf(interestRate);
    }

    public void readCsvFile(String pathString){
        File file = new File(pathString);
        try (Scanner scanner = new Scanner(file)) {
            String csvHeaders = scanner.nextLine();
            String[] headersArray = csvHeaders.split(",");

            setHeaderIndexes(headersArray);

            addTransactionsFromCsv(scanner);

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

    private void addTransactionsFromCsv(Scanner scanner) {
        while(scanner.hasNextLine()){
            String row = scanner.nextLine();
            String[] rowArray = row.split(",");

            addTransaction(new Transaction(
                BigDecimal.valueOf(Double.parseDouble(rowArray[amountCsvIndex])),
                LocalDate.parse(rowArray[dateCsvIndex]),
                rowArray[descriptionCsvIndex]));
        }
    }

    private void setHeaderIndexes(String[] headersArray) {
        for(int i = 0; i < headersArray.length; i++){
            if(headersArray[i].equals(getAmountCsvHeader())){
                amountCsvIndex = i;
            }
            if(headersArray[i].equals(getDateCsvHeader())){
                dateCsvIndex = i;
            }
            if(headersArray[i].equals(getDescriptionCsvHeader())){
                descriptionCsvIndex = i;
            }
        }
    }
        
}
