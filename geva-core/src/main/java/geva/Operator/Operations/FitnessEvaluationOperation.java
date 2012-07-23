/*
Grammatical Evolution in Java
Release: GEVA-v1.2.zip
Copyright (C) 2008 Michael O'Neill, Erik Hemberg, Anthony Brabazon, Conor Gilligan 
Contributors Patrick Middleburgh, Eliott Bartley, Jonathan Hugosson, Jeff Wrigh

Separate licences for asm, bsf, antlr, groovy, jscheme, commons-logging, jsci is included in the lib folder. 
Separate licence for rieps is included in src/com folder.

This licence refers to GEVA-v1.2.

This software is distributed under the terms of the GNU General Public License.


This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
/>.
*/

/*
 * FitnessEvaluationOperation.java
 *
 * Created on April 17, 2007, 11:16 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package geva.Operator.Operations;


import geva.FitnessEvaluation.FitnessFunction;
import geva.Individuals.Individual;
import geva.Mapper.GEGrammar;
import geva.Util.Constants;
import geva.Util.Statistics.StatCatcher;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryNotificationInfo;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryType;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import javax.management.Notification;
import javax.management.NotificationEmitter;
import javax.management.NotificationListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * Operation for evaluating the fitness. Has a FitnessFunction.
 * Map the individual. If vallid evaluate.
 * Set fitness.
 * @author erikhemberg
 */
public class FitnessEvaluationOperation implements Operation{
	private static Log logger = LogFactory.getLog(FitnessEvaluationOperation.class);

    private FitnessFunction fitnessFunction;
    private boolean evaluateEverytime = false;
    private HashMap<String, CacheItem> fitnessCache = new HashMap<String, CacheItem>();
    private int currentAge;
    private int originalPopulationSize;
    private int populationSize;
    private static final int GENERATIONS_SURVIVAL = 10;

    /** Creates a new instance of FitnessEvaluationOperation
     * @param ff fitness function
     */
    public FitnessEvaluationOperation(FitnessFunction ff) {
        this.fitnessFunction = ff;
        new CacheMemoryMonitor();
    }

    public int getPopulationSize() {
        return populationSize;
    }

    public void setPopulationSize(int populationSize) {
        this.populationSize = populationSize;
    }

    HashMap<String, CacheItem> getFitnessCache() {
        return fitnessCache;
    }

    /**
     * Evaluates geva.Individuals that are not alreadt evaluated.
     * Map the individual.
     * If valid call the fitnessFunction.getFitness(Individual operand)
     * to evaluate and set the fitness. invalid individuals get the
     * default fitness.
     * Mark the individual as evaluated.
     * @param operand Individual to evaluate
     **/
    public void doOperation(Individual operand) {
        //	System.out.println(this.getClass().getName()+".doOperation("+operand+") ENTRY");
        operand.setEvaluated(false);
        if (!operand.isEvaluated()) {
            if (operand.getGenotype() == null && operand.getPhenotype() != null) {
                // This can happen when individual's phenotype is constructed directly,
                // eg by NGramEDAReproductionOperation. No need to map. Can't interact with cache.
                if (operand.isValid()) {
                    this.fitnessFunction.getFitness(operand);
                } else {
                    operand.getFitness().setDefault();
                }
                return;
            }

            //Map individual
            operand.map(0);

            boolean cache = fitnessFunction.canCache();
            //cache = false;
            if (cache == false // Short-circuit, won't getFitnessFromCache if cache==false
                    || getFitnessFromCache(operand) == false) {
                if (operand.isValid()) {
                    this.fitnessFunction.getFitness(operand);
                } else {
                    operand.getFitness().setDefault();
                }
                if (cache == true) {
                    addFitnessToCache(operand);
                }
            }
            if (this.evaluateEverytime) {
                operand.setEvaluated(false);
            } else {
                operand.setEvaluated(true);
            }
        }
    //	System.out.println(this.getClass().getName()+".doOperation("+operand+") EXIT");
    //	System.out.println(this.getClass().getName()+".doOperation("+operand+") fit:"+operand.getFitness().getDouble());
    }

    /**
     * Get the fitness value from the cache if it exists. If a fitness is taken
     *  from the cache, it is reborn (age reset to [relative] 0) and is given
     *  an extra life (will survive one more clean up). This must not be called
     *  if the fitness function doesn't support caching (if canCache() == false)
     * @return true if the value was taken from the cache, false otherwise
     */
    protected boolean getFitnessFromCache(Individual operand) {
        assert fitnessFunction.canCache() == true;

        String fitnessName = ((GEGrammar) operand.getMapper()).getName();
        synchronized (fitnessCache) {
            if (fitnessCache.containsKey(fitnessName) == true) {
                CacheItem cacheItem = fitnessCache.get(fitnessName);
                cacheItem.age = currentAge++;
                cacheItem.lives++;
                operand.getFitness().setDouble(cacheItem.value);
                return true;
            }
        }

        return false;

    }

    /**
     * Add the fitness value of an individual to the cache. After adding, this
     *  also preforms clean up (if the cache reaches its maximum size). If after
     *  clean-up the cache doesn't go below a certain treshold, the maximum size
     *  is increased (it means the cache is getting good use, so increase it
     *  rather than remove the entries are making hits). A memory monitor also
     *  watches for low-memory conditions and cleans out the cache without bias
     *  towards each items usefulness, so the cache will never grow out of
     *  control
     */
    protected void addFitnessToCache(Individual operand) {
        assert fitnessFunction.canCache() == true;

        synchronized (fitnessCache) {

            fitnessCache.put(((GEGrammar) operand.getMapper()).getName(),
                    new CacheItem(operand.getFitness().getDouble(),
                    currentAge++));

            // If the cache gets too large, remove all the oldest entries. Old
            //  entries are those that haven't been 'hit' the longest
            if (fitnessCache.size() >= populationSize * GENERATIONS_SURVIVAL) {

                Set<Entry<String, CacheItem>> entries = fitnessCache.entrySet();
                Iterator<Entry<String, CacheItem>> entry = entries.iterator();

                while (entry.hasNext() == true) {
                    CacheItem item = entry.next().getValue();
                    if (item.age - currentAge < -populationSize * GENERATIONS_SURVIVAL / 2) {
                        item.lives--;
                        if (item.lives <= 0) {
                            entry.remove();
                        }
                    }
                }

                // If the number of remaining entries in the cache exceed half
                //  the original cache size, double the size of the used cache.
                //  This allows the cache to grow if it is being used quite
                //  often, but not allow it to shrink below the original cache
                //  size
                if (fitnessCache.size() > originalPopulationSize * GENERATIONS_SURVIVAL / 2) {
                    populationSize = fitnessCache.size() * 2 / GENERATIONS_SURVIVAL;
                }

            }

        }

    }

    /**
     * Get the size of the population, and use this to determine the size of the
     *  cache. The cache is set to GENERATIONS_SURVIVAL times the size of the
     *  population
     * @param p object containing properties
     */
    public void setProperties(Properties p) {
        String value = p.getProperty(Constants.POPULATION_SIZE);
        try {
            populationSize = Integer.parseInt(value);
            originalPopulationSize = populationSize;
        } catch (NumberFormatException e) {
            populationSize = Integer.parseInt(Constants.DEFAULT_POPULATION_SIZE);
        }
    }

    public int getOriginalPopulationSize() {
        return originalPopulationSize;
    }

    public void doOperation(List<Individual> operands) {
        Individual ind;
        for (Individual operand : operands) {
            ind = operand;
            this.doOperation(ind);
        }
    }

    /**
     * Get the fitness function
     * @return fitness function
     */
    public FitnessFunction getFitnessFunction() {
        return this.fitnessFunction;
    }

    /**
     * Cache values from the fitness-function so the fitness-function won't have
     *  to re-evalute if a genotype that was considerded before is cached. From
     *  testing, most of the items that will likely to be used from the cache
     *  have quite short life-times. This caching mechanism gives cached items
     *  an age, as well as extra lives, so cache clean-up is preformed by
     *  removing old entries that have lost all their lives. Old means the item
     *  hasn't been hit in a long time, and lives are gained when an item is hit,
     *  and lost when an item survives a clean-up (so if an item is hit several
     *  times between clean-ups, it will survive that many clean-ups in the
     *  future regardless of how old it becomes)
     * @author eliott bartley
     */
    private class CacheItem {

        /**
         * The value of the cached item
         */
        public double value;
        /**
         * Every time the item is hit, the item's age is set to the 'youngest'
         *  age (the closest to the 'currentAge'). Only items older (furthest
         *  from the 'currentAge') by a certain amount are considered for
         *  removal during clean up (i.e., items that haven't got a cache hit in
         *  a long time)
         */
        public int age;
        /**
         * Every item is given one life when it's added to the cache. Every time
         *  the item is hit it is given an extra life. During clean up, every
         *  item that is very old loses a life. When an item has 0 lives, it is
         *  removed from the cache. The lives allow old items to live a bit
         *  longer if that item had a very active life
         */
        public int lives;

        public CacheItem(double value, int age) {
            this.value = value;
            this.age = age;
            this.lives = 1;
        }
    }

    /**
     * Watch for low memory situations and remove half of all entries from the
     *  cache. This simply removes the first half of the hash's set, so being
     *  removed depends on the position of the entries in this set, not on age
     *  or lives, just a simple path of destruction
     * @author eliott bartley
     */
    private class CacheMemoryMonitor {

        /**
         * Portion of memory that must be used before low-memory notification is
         *  reported
         */
        private final static float memoryThreshold = 0.75f;

        public CacheMemoryMonitor() {
            initialiseThreshold();
            initialiseMonitor();
        }

        /**
         * Set up threshold-notifications on all heaps that support it to
         *  generate notifications when memory usage exceeds the
         *  <var>memoryThreshold</var>
         */
        private void initialiseThreshold() {
            for (MemoryPoolMXBean pool : ManagementFactory.getMemoryPoolMXBeans()) {
                if (pool.getType() == MemoryType.HEAP && pool.isUsageThresholdSupported() == true) {
                    pool.setUsageThreshold((long) (pool.getUsage().getMax() * memoryThreshold));
                }
            }
        }

        /**
         * Set up the listener to respond to low-memory notifications (when
         *  memory usage exceeds the <var>memoryThreshold</var>
         */
        private void initialiseMonitor() {
            MemoryMXBean memoryMonitor = ManagementFactory.getMemoryMXBean();
            ((NotificationEmitter) memoryMonitor).addNotificationListener(new NotificationListener() {

                public void handleNotification(Notification notification,
                        Object userData) {
                    if (notification.getType().equals(MemoryNotificationInfo.MEMORY_THRESHOLD_EXCEEDED)) {
                        preformCleanup();
                    }
                }
            },
                    null,
                    null);
        }

        /**
         * Go through half the hash map and remove those entries. Unbiased
         *  towards age and life, but very biased towards position in hash map
         */
        private void preformCleanup() {
            int size = fitnessCache.size() / 2;

            synchronized (fitnessCache) {

                Set<Entry<String, CacheItem>> entries = fitnessCache.entrySet();
                Iterator<Entry<String, CacheItem>> entry = entries.iterator();

                while (entry.hasNext() == true && size > 0) {
                    entry.next();
                    entry.remove();
                    size--;
                }

            }

        }
    }
}
