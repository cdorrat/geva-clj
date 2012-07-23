package geva.Util.Random;

import java.util.concurrent.atomic.AtomicLong;

/**
 * A Thread safe wrapper for random number geerators.
 * The default implementation uses MersenneTwisterFast, subclass and override createNewRNG to wrap  other implementations.
 * @author cnd
 *
 */
public class ThreadSafeRandomNumberGenerator implements RandomNumberGenerator {
	 
	protected AtomicLong seed;
	ThreadLocal<RandomNumberGenerator> rng = new ThreadLocal<RandomNumberGenerator>() {

		@Override
		protected RandomNumberGenerator initialValue() {
			return createNewRNG();
		}
		
	};
	
	protected RandomNumberGenerator createNewRNG() {
		return (seed == null) ? new MersenneTwisterFast() : new MersenneTwisterFast(seed.getAndIncrement());
	}
	
	
	@Override
	public boolean nextBoolean() {
		return rng.get().nextBoolean();
	}

	@Override
	public boolean nextBoolean(double d) {
		return rng.get().nextBoolean(d);
	}

	@Override
	public char nextChar() {
		return rng.get().nextChar();
	}

	@Override
	public double nextDouble() {
		return rng.get().nextDouble();
	}

	@Override
	public int nextInt(int n) {
		return rng.get().nextInt(n);
	}

	@Override
	public int nextInt() {
		return rng.get().nextInt();
	}

	@Override
	public short nextShort() {
		return rng.get().nextShort();
	}

	@Override
	public void setSeed(long l) {
		if (seed == null) {
			 seed = new AtomicLong();
		}
		seed.set(l);
		rng.get().setSeed(l);
	}

	@Override
	public void setSeed(int[] aI) {
		// This doesn't seem to be used anywhere 
		// and it's not clear how to make sensible new seeds 
		// for other threads.
		throw new UnsupportedOperationException();
	}

}
