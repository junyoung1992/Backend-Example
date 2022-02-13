package study.thread.wait;

import java.util.ArrayList;

public class ThreadWaitEx2 {

    public static void main(String[] args) throws Exception {
        Table table = new Table();  // 여러 쓰레드가 공유하는 객체

        new Thread(new Cook(table), "COOK1").start();
        new Thread(new Customer(table, "donut"), "CUST1").start();
        new Thread(new Customer(table, "burger"), "CUST2").start();

        Thread.sleep(100);
        System.exit(0);
    }

    static class Customer implements Runnable {
        private final Table table;
        private final String food;

        Customer(Table table, String food) {
            this.table = table;
            this.food = food;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                String name = Thread.currentThread().getName();
                if (eatFood()) {
                    System.out.println(name + " ate a " + food);
                } else {
                    System.out.println(name + " failed to eat. :(");
                }
            }
        }

        boolean eatFood() {
            return table.remove(food);
        }
    }

    static class Cook implements Runnable {
        private Table table;

        Cook(Table table) {
            this.table = table;
        }

        @Override
        public void run() {
            while (true) {
                // 임의의 요리를 하나 선택해서 table에 추가한다.
                int idx = (int) (Math.random() * table.dishNum());
                table.add(table.dishNames[idx]);
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class Table {
        String[] dishNames = { "donut", "donut", "burger" };
        final int MAX_FOOD = 6;

        private final ArrayList<String> dishes = new ArrayList<>();

        public synchronized void add(String dish) {
            // 테이블에 음식이 가득찼으면, 테이블에 음식을 추가하지 않는다.
            if (dishes.size() >= MAX_FOOD) {
                return;
            }
            dishes.add(dish);
            System.out.println("Dishes: " + dishes.toString());
        }

        public boolean remove(String dishName) {
            synchronized(this) {
                while(dishes.size() == 0) {
                    String name = Thread.currentThread().getName();
                    System.out.println(name + " is waiting.");
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                for (int i = 0; i < dishes.size(); i++) {
                    if (dishName.equals(dishes.get(i))) {
                        dishes.remove(i);
                        return true;
                    }
                }
            }

            return false;
        }

        public int dishNum() {
            return dishNames.length;
        }
    }

}
