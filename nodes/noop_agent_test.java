package nodes;

import axiom.AxiomContext;
import gen.Messages.NoopInput;
import gen.Messages.NoopOutput;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

// TESTS — delete this block when done ─────────────────────────────────────────
// Tests are required to publish this package. The publish pipeline runs your
// tests as a quality gate — a package will not be published if tests fail or
// do not meet the minimum requirements.
//
// Requirements checked before publishing:
//   - At least one test per node
//   - All tests must pass
//   - Output fields must be meaningfully asserted — not just null-checked
//
// The generated test below is a starting point. Replace the TODO comment with
// real assertions that verify your node returns correct data for known inputs.
// Think: given a specific input, what should the output fields contain?
//
// Run your tests locally at any time:
//   axiom test

public class NoopAgentTest {

    // A no-op AxiomContext a node author edits to drive a specific scenario.
    // Memory returns empty (the test does not connect to the sidecar proxy),
    // reflection exposes an empty graph, mutation is a sink. Implement only what
    // your assertions need.
    static final class TestContext implements AxiomContext {
        public Logger log() {
            return new Logger() {
                public void debug(String m, Map<String, String> a) {}
                public void info(String m, Map<String, String> a)  {}
                public void warn(String m, Map<String, String> a)  {}
                public void error(String m, Map<String, String> a) {}
            };
        }
        public Secrets secrets() { return name -> Optional.empty(); }
        public Agent agent() {
            return () -> new AgentMemory() {
                public SessionMemory session(String sessionId) {
                    return new SessionMemory() {
                        public List<MemoryEntry> search(String q, int limit) { return List.of(); }
                        public String write(String content, double importance) { return ""; }
                        public SessionHistory history() {
                            return new SessionHistory() {
                                public List<ConversationTurn> last(int n) { return List.of(); }
                                public void append(String role, String content) {}
                            };
                        }
                        public void end() {}
                    };
                }
                public List<MemoryEntry> search(String q, int limit) { return List.of(); }
                public String write(String content, double importance) { return ""; }
            };
        }
        public String executionId() { return "test-execution-id"; }
        public String flowId() { return "test-flow-id"; }
        public String tenantId() { return "test-tenant-id"; }
        public Reflection reflection() {
            return () -> new FlowReflection() {
                public List<ReflectionNode> nodes() { return List.of(); }
                public List<ReflectionEdge> edges() { return List.of(); }
                public List<ReflectionEdge> loopEdges() { return List.of(); }
                public FlowPosition position() { return new FlowPosition(0, 0, Map.of(), List.of()); }
                public String graphId() { return ""; }
            };
        }
        public Mutation mutation() {
            return () -> new FlowMutation() {
                public int addNode(String pkg, String ver, CanvasPosition pos) { return 0; }
                public void addEdge(int src, int dst, EdgeCondition cond) {}
            };
        }
    }

    @Test
    public void testNoopAgent() {
        AxiomContext ax = new TestContext();
        NoopInput input = NoopInput.newBuilder()
            .setExampleString("hello")
            .setExampleInt(7)
            .build();
        NoopOutput result = NoopAgent.noopAgent(ax, input);
        // Field mapping: the string echoes through unchanged (catches a node that
        // drops or swaps fields).
        assertEquals("hello", result.getExampleString());
        // Memory wiring: the mock's agent memory is empty, so after the (no-op)
        // append the history reads back 0 turns (catches ax.agent().memory()
        // .session().history() throwing or being wired to the wrong call).
        assertEquals(0, result.getExampleInt());
    }
}
