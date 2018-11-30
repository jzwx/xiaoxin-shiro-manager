package com.xiaoxin.manager.utils;

/**
 * @Author:jzwx
 * @Desicription: PersonA
 * @Date:Created in 2018-11-27 16:39
 * @Modified By:
 */
public class PersonA extends Thread {
    Bank   bank;

    String mode;

    public PersonA(Bank bank, String mode) {
        this.mode = mode;
        this.bank = bank;
    }

    public void run() {
        while (Bank.money >= 100) {
            try {
                bank.outMoney(100,mode);
            }catch (Exception e1){
                e1.printStackTrace();
            }
            try {
                sleep(100);
            }catch (InterruptedException e){
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
