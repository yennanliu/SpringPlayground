package spring.basic;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class ControllerSpring {
    //

    /**
     * @ AtomicLong
     * @Function: The AtomicLong class provides you with a long variable
     * which can be read and written atomically, and
     * which also contains advanced atomic operations like compareAndSet().
     * @Proccess: Function is updating id, as well.
     **/
    private static final String template = "Hello, %s !";
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/hey")
    public HelloSpring Greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return new HelloSpring(counter.incrementAndGet(), String.format(template, name));
    }
}
