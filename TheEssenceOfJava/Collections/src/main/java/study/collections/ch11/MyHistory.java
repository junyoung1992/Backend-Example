package study.collections.ch11;

import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Queue;

@Slf4j
public class MyHistory {

    public static final int MAX_SIZE = 5;

    private Queue q;

    public MyHistory() {
        q = new LinkedList();
    }

    public void save(String input) {
        // queue에 저장한다.
        if (!"".equals(input)) {
            q.offer(input);
        }

        // queue의 최대 크기를 넘으면 제일 처음 입력된 것을 삭제한다.
        if(q.size() > MAX_SIZE) {
            q.remove();
        }
    }

    public void showHistory() {
        LinkedList tmp = (LinkedList) q;
        ListIterator it = tmp.listIterator();

        int i = 0;
        while(it.hasNext()) {
            System.out.println(++i + "." + it.next());
        }
    }

}
