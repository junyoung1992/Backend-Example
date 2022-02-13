package study.collections.ch11;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class MyBrowserTest {

    @Test
    public void test() {
        MyBrowser browser = new MyBrowser();

        browser.goURL("https://www.google.com");
        browser.goURL("https://www.naver.com");
        browser.goURL("https://www.daum.net");

        browser.printStatus();

        browser.goBack();
        log.info("= '뒤로' 버튼을 누른 후 =");
        browser.printStatus();

        browser.goBack();
        log.info("= '뒤로' 버튼을 누른 후 =");
        browser.printStatus();

        browser.goForward();
        log.info("= '앞으로' 버튼을 누른 후 =");
        browser.printStatus();

        browser.goURL("https://www.junyoung.xyz");
        log.info("= 새로운 주소로 이동 후 =");
        browser.printStatus();
    }

}
