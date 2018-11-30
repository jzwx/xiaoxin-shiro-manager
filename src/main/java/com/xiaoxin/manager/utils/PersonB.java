package com.xiaoxin.manager.utils;

/**
 * @Author:jzwx
 * @Desicription: PersonB
 * @Date:Created in 2018-11-27 16:47
 * @Modified By:
 */
public class PersonB extends Thread {
    Bank   bank;

    String mode;

    public PersonB(Bank bank, String mode) {
        this.bank = bank;
        this.mode = mode;
    }

    public void run() {
        while (Bank.money >= 200) {
            try {
                bank.outMoney(200, mode);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            try {
                sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
