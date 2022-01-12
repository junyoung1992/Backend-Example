package hello.proxy.pureproxy.decorator.code;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Decorator {

    protected final Component component;

}
