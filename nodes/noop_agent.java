package nodes;

import axiom.AxiomContext;
import gen.Messages.NoopInput;
import gen.Messages.NoopOutput;
import java.util.Map;

public class NoopAgent {

    /**
     * Exercises the live agent-memory client (ADR-002): appends the input string
     * as a conversation turn on a fixed session, then reads the history back and
     * returns its length. Across repeated invocations on the same session the
     * turn count grows, proving the append + read round-trip through the sidecar
     * MemoryProxyService. The input string is echoed so field mapping stays
     * observable on the wire.
     *
     * @param ax    The AxiomContext: logging, secrets, agent memory, reflection, mutation.
     * @param input The decoded NoopInput for this invocation.
     */
    public static NoopOutput noopAgent(AxiomContext ax, NoopInput input) {
        ax.log().info("noopAgent handling", Map.of("text", input.getExampleString()));

        // Live memory: append this turn, then read the session history back.
        var session = ax.agent().memory().session("java-noop-demo");
        session.history().append("user", input.getExampleString());
        int turns = session.history().last(100).size();

        return NoopOutput.newBuilder()
            .setExampleString(input.getExampleString())
            .setExampleInt(turns)
            .build();
    }
}
