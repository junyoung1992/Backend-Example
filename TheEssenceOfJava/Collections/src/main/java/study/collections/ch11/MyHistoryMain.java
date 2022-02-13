package study.collections.ch11;

import java.util.Scanner;

public class MyHistoryMain {

    public static void main(String[] args) {
        MyHistory history = new MyHistory();

        System.out.println("help를 입력하면 도움말을 볼 수 있습니다.");

        while (true) {
            System.out.print(">>");
            try {
                // 화면으로부터 라인 단위로 입력받는다.
                Scanner s = new Scanner(System.in);
                String input = s.nextLine().trim();

                if ("".equals(input)) {
                    continue;
                }

                if (input.equalsIgnoreCase("q")) {
                    System.exit(0);
                } else if (input.equalsIgnoreCase("help")) {
                    System.out.println(" help - 도움말을 보여줍니다.");
                    System.out.println(" q 또는 Q - 프로그램을 종료합니다.");
                    System.out.println(" history - 최근 입력한 명령어를 " + MyHistory.MAX_SIZE + "개 보여줍니다.");
                } else if (input.equalsIgnoreCase("history")) {
                    history.save(input);
                    history.showHistory();
                } else {
                    history.save(input);
                    System.out.println(input);
                }

            } catch (Exception e) {
                System.out.println("입력 오류입니다.");
            }
        }
    }

}
