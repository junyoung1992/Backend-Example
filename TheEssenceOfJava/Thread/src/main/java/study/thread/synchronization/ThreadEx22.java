package study.thread.synchronization;

public class ThreadEx22 {

    public static void main(String[] args) {
        Runnable r = new RunnableEx22();
        new Thread(r).start();  // ThreadGroup에 의해 참조되므로 gc대상이 아니다.
        new Thread(r).start();
    }

    static class Account {
        private int balance = 1000;

        public int getBalance() {
            return balance;
        }

        // synchronized로 메서드 동기화
        public synchronized void withdraw(int money) {
            if (balance >= money) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {}

                balance -= money;
            }
        }
    }

    static class RunnableEx22 implements Runnable {
        Account acc = new Account();

        @Override
        public void run() {
            while (acc.getBalance() > 0) {
                int money = (int) (Math.random() * 3 + 1) * 100;
                acc.withdraw(money);
                System.out.println("balance:" + acc.getBalance());
            }
        }
    }

}
