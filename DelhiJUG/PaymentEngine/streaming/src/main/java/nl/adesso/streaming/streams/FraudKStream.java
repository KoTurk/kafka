package nl.adesso.streaming.streams;

import example.avro.Account;
import example.avro.Fraud;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import nl.adesso.streaming.exception.FilterException;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Branched;
import org.apache.kafka.streams.kstream.KStream;

public class FraudKStream {

    public static KStream<Account, Fraud> getFraudKStream(StreamsBuilder builder) {
        // 5.1 Create stream fraud
        KStream<Account, Fraud> fraudKStream = builder.stream("fraud");

        // 5.2 peek
        fraudKStream.peek((account, payment) -> {
            System.out.println("Checking Fraud");
        //            throw new NullPointerException("ooops");
        })

        // 5.3 fILter iban NL63ABNA332454654
        .filter((account, payment) -> {
            try {
                return filterOrThrow(payment);
            } catch (FilterException e) {
                Counter.builder("a.exception.count")
                        .tag("exception", e.getMessage())
                        .register(Metrics.globalRegistry)
                        .increment();
                return false;
            }
        })

        // 5.4 map values
        .mapValues((account, payment) -> new Fraud(payment.getIban()))

        // 5.5 peek
        .peek((account, payment) -> {
            System.out.println("Got a Fraud, don't process");
        })

        // 5.6 split
        .split()

        // 5.7 branch
        .branch((account, payment) -> false, Branched.withConsumer(ks -> ks.to("rewards")))

        // 5.8 defaullbranch
        .defaultBranch(Branched.withConsumer(ks -> ks.to("blacklist")));

        // 5.9 return stream
        return fraudKStream;
    }

    private static boolean analytics(Fraud value) {
        return false;
    }

    private static boolean filterOrThrow(Fraud user) throws FilterException {
        if ("NL63DEUT332454654".equals(user.getIban())) {
            // I MADE A JAVA PROGRAM TO TELL ME MY PURPOSE
            // IT KEEPS SAYING NULLPOINTEREXCEPTION, SO IT WORKS GREAT

            // 5.10 throw exception
            throw new FilterException("oh no, why???");
        } else {
            return true;
        }
    }
}
