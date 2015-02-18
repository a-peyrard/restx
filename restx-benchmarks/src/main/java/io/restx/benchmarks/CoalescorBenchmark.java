package io.restx.benchmarks;

import com.google.common.eventbus.EventBus;

import org.kohsuke.randname.RandomNameGenerator;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.util.Random;
import restx.common.watch.EventCoalescor;
import restx.common.watch.FileWatchEvent;
import restx.common.watch.FileWatchEventCoalescor;

/**
 * @author apeyrard
 */
@State(Scope.Benchmark)
public class CoalescorBenchmark {

	static final int PATH_POOL_SIZE = 50;
	static final RandomNameGenerator rnd = new RandomNameGenerator((int) System.currentTimeMillis());

	static final WatchEvent.Kind<?>[] kinds = new WatchEvent.Kind<?>[] {
			StandardWatchEventKinds.ENTRY_CREATE,
			StandardWatchEventKinds.ENTRY_MODIFY,
			StandardWatchEventKinds.ENTRY_DELETE
	};

	static final Path[] pathsPool = new Path[PATH_POOL_SIZE];
	static {
		for (int i = 0; i < PATH_POOL_SIZE; i++) {
			pathsPool[i] = Paths.get(rnd.next());
		}
	}

	static final Path ROOT = Paths.get("/");

	@State(Scope.Benchmark)
	public static class GlobalContext {
		final EventCoalescor fileWatchEventCoalescor = FileWatchEventCoalescor.create(new EventBus(), 50);
		final EventCoalescor eventCoalescor = EventCoalescor.generic(new EventBus(), 50);
	}

	@State(Scope.Thread)
	public static class ThreadContext {
		final Random random = new Random(System.currentTimeMillis());

		final FileWatchEvent generate() {
			int offset = Math.abs(random.nextInt());
			return FileWatchEvent.newInstance(ROOT, ROOT, pathsPool[offset % PATH_POOL_SIZE], kinds[offset % 3], 1);
		}

	}

	@Benchmark
	public int blank(ThreadContext threadContext) {
		FileWatchEvent event = threadContext.generate();

		// nothing, just common benchmarks code

		return event.hashCode();
	}

	@Benchmark
	public int eventCoalescor(GlobalContext globalContext, ThreadContext threadContext) {
		FileWatchEvent event = threadContext.generate();

		globalContext.eventCoalescor.post(event);

		return event.hashCode();
	}

	@Benchmark
	public int fileWatchEventCoalescor(GlobalContext globalContext, ThreadContext threadContext) {
		FileWatchEvent event = threadContext.generate();

		globalContext.fileWatchEventCoalescor.post(event);

		return event.hashCode();
	}

	@TearDown(Level.Trial)
	public void tearDown(GlobalContext context) {
		try {
			context.eventCoalescor.close();
		} catch (IOException ignored) {}

		try {
			context.fileWatchEventCoalescor.close();
		} catch (IOException ignored) {}
	}
}
