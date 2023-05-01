package ru.home.application;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import ru.home.logging.model.LoggedClassMetadata;
import ru.home.logging.util.mode.ProxyMode;
import ru.home.logging.util.LoggingProxyFactory;

import java.util.Set;

/*
 * @created 25.04.2023
 * @author alexander
 */
@BenchmarkMode(Mode.Throughput)
@State(Scope.Benchmark)
@Fork(value = 1)
@Measurement(iterations = 3, time = 1)
@Warmup(iterations = 1, time = 1)
public class ProxyBenchmark {

    private final TestObject object = new TestObjectImpl();
    private final TestObject jdkProxy = createProxy(ProxyMode.JDK);
    private final TestObject cglibProxy = createProxy(ProxyMode.CGLIB);
    private final TestObject byteBuddyProxy = createProxy(ProxyMode.BYTE_BUDDY);

    @Benchmark
    public void executeNoProxy(Blackhole bh) {
        bh.consume(object.executeTest());
    }

    @Benchmark
    public void executeByteBuddyProxy(Blackhole bh) {
        bh.consume(byteBuddyProxy.executeTest());
    }

    @Benchmark
    public void executeJdkProxy(Blackhole bh) {
        bh.consume(jdkProxy.executeTest());
    }

    @Benchmark
    public void executeCglibProxy(Blackhole bh) {
        bh.consume(cglibProxy.executeTest());
    }

    public interface TestObject {
        Object executeTest();
    }

    public static class TestObjectImpl implements TestObject {
        @Override
        public Object executeTest() {
            return new Object();
        }
    }

    private TestObject createProxy(ProxyMode mode) {
        try {
            return (TestObject) LoggingProxyFactory.createProxy(
                    object,
                    LoggedClassMetadata.builder()
                            .originalClass(TestObjectImpl.class)
                            .mode(mode)
                            .methods(Set.of(
                                    TestObjectImpl.class.getMethod("executeTest")
                            ))
                            .build()
            );
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
