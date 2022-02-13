package study.collections.ch11;

import lombok.extern.slf4j.Slf4j;

import java.util.Stack;

@Slf4j
public class MyBrowser {

    private Stack back;
    private Stack forward;

    public MyBrowser() {
        back = new Stack();
        forward = new Stack();
    }

    public void printStatus() {
        log.info("back={}", back);
        log.info("forward={}", forward);
        log.info("현재 화면은 {} 입니다.", back.peek());
    }

    public void goURL(String url) {
        back.push(url);

        if (!forward.empty()) {
            forward.clear();
        }
    }

    public void goForward() {
        if (!forward.empty()) {
            back.push(forward.pop());
        }
    }

    public void goBack() {
        if (!back.empty()) {
            forward.push(back.pop());
        }
    }

}
