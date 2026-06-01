package nodes;

// REGISTRY DOCS — delete this block when done ─────────────────────────────────
// The Javadoc comment directly above the method is extracted at publish time
// and shown in the Axiom registry as this node's description.
// Note: standard Java convention requires the public class name to match the
// filename. Consider renaming this file to NoopAgent.java to align with
// that convention.

import axiom.AxiomContext;
import gen.Messages.NoopInput;
import gen.Messages.NoopOutput;
import java.util.Map;

public class NoopAgent {

    /**
     * TODO: replace with your description — this Javadoc is extracted at
     * publish time and shown in the Axiom registry as this node's documentation.
     *
     * <p>{@code ax} is the AxiomContext (ADR-001): every platform capability is
     * reached through it — {@code ax.log()}, {@code ax.secrets()},
     * {@code ax.agent()}, {@code ax.reflection()}, {@code ax.mutation()}. Node
     * code never talks to the platform directly.
     *
     * @param ax    The AxiomContext: logging, secrets, agent memory, reflection, mutation.
     * @param input The decoded NoopInput for this invocation.
     */
    public static NoopOutput noopAgent(AxiomContext ax, NoopInput input) {
        ax.log().info("noopAgent handling", Map.of("text", input.getExampleString()));

        // Reflection (ADR-050/055): observe the running graph from inside the node.
        int nodeCount = ax.reflection().flow().nodes().size();

        // Echo the input through, annotating the int with the reflected node count
        // so the transformation is observable end-to-end.
        return NoopOutput.newBuilder()
            .setExampleString(input.getExampleString())
            .setExampleInt(input.getExampleInt() + nodeCount)
            .build();
    }
}
