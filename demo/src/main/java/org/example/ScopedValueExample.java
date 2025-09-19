package org.example;


import java.util.concurrent.StructuredTaskScope;

public class ScopedValueExample {

    private static final ScopedValue<String> USER_ID = ScopedValue.newInstance();
    private static final ScopedValue<String> CONTEXT = ScopedValue.newInstance();

    public void processRequest(String userId) {
        ScopedValue.where(USER_ID, userId)
                .run(() -> IO.println("Processing request for user: " + USER_ID.get()));
    }

    public void multiThreadProcessRequest() {
        ScopedValue<String> scopedValue = ScopedValue.newInstance();
        ScopedValue.where(scopedValue, "userData")
                .run(() -> {
                    for (int i = 0; i < 10; i++) {
                        Thread.ofVirtual().start(() -> {
                            IO.println("Processing request for user: " + scopedValue.get());
                        });
                    }
                });
    }

    public String processWithResult(String input) {
        return ScopedValue.where(CONTEXT, input)
                .call(() -> {
                    // do something
                    return "result " + CONTEXT.get();
                });
    }

    public static void outerMethod() {
        ScopedValue.where(CONTEXT, "hello").run(() -> {
            IO.println(CONTEXT.get());
            ScopedValue.where(CONTEXT, "world").run(() -> {
                IO.println(CONTEXT.get());
            });
            IO.println(CONTEXT.get());
        });
    }

    void handleRequest() {
        ScopedValue.where(USER_ID, "userId")
                .run(() -> {
                    // 其中open传入的参数是结构性并发策略
                    try (var scope = StructuredTaskScope.open()) {
                        var userTask = scope.fork(() -> "");      // 子线程可以访问 USER_ID
                        var ordersTask = scope.fork(() -> "");  // 子线程可以访问 USER_ID
                        scope.join();
                        // return new Response(userTask.get(), ordersTask.get());
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });
    }



    static void main() {
        outerMethod();
    }

}
