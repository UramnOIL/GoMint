/*
 * Copyright (c) 2017, GoMint, BlackyPaw and geNAZt
 *
 * This code is licensed under the BSD license found in the
 * LICENSE file in the root directory of this source tree.
 */

package io.gomint.server.world;

import io.gomint.math.MathUtils;
import io.gomint.server.entity.EntityPlayer;
import io.gomint.server.util.Values;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.function.Consumer;

/**
 * @author BlackyPaw
 * @version 1.0
 */
public class ChunkCache {

    private static final Logger LOGGER = LoggerFactory.getLogger( ChunkCache.class );

    // CHECKSTYLE:OFF
    // ==================================== FIELDS ==================================== //
    private final WorldAdapter world;
    private final Long2ObjectMap<ChunkAdapter> cachedChunks;
    private boolean enableAutoSave;
    private long autoSaveInterval;

    // Ticking helper
    private float lastFullTickDT = 0;
    private LongSet alreadyTicked = new LongOpenHashSet();
    private final LongSet[] tempHashes = { new LongOpenHashSet(), new LongOpenHashSet() }; // 0 => not to delete chunks, 1 => to delete chunks

    public ChunkCache( WorldAdapter world ) {
        this.world = world;
        this.cachedChunks = new Long2ObjectOpenHashMap<>();
        this.enableAutoSave = world.getConfig().isAutoSave();
        this.autoSaveInterval = world.getConfig().getAutoSaveInterval();
    }
    // CHECKSTYLE:ON

    /**
     * Ticking for Chunk GC
     *
     * @param currentTimeMS The current time in milliseconds. Used to reduce the number of calls to System#currentTimeMillis()
     */
    public synchronized void tick( long currentTimeMS ) {
        // Check for gc
        int spawnXChunk = CoordinateUtils.fromBlockToChunk( (int) this.world.getSpawnLocation().getX() );
        int spawnZChunk = CoordinateUtils.fromBlockToChunk( (int) this.world.getSpawnLocation().getZ() );

        int spawnAreaSize = this.world.getConfig().getAmountOfChunksForSpawnArea();

        // Clear temp sets
        this.tempHashes[0].clear();
        this.tempHashes[1].clear();

        // Copy over the current loaded chunk hashes
        for ( EntityPlayer player : this.world.getPlayers0().keySet() ) {
            LongIterator chunkIterator = player.getConnection().getPlayerChunks().iterator();
            while ( chunkIterator.hasNext() ) {
                this.tempHashes[0].add( chunkIterator.nextLong() );
            }

            chunkIterator = player.getConnection().getLoadingChunks().iterator();
            while ( chunkIterator.hasNext() ) {
                this.tempHashes[0].add( chunkIterator.nextLong() );
            }
        }

        boolean checkChunkSave = this.isAutosaveEnabled() &&
            this.autoSaveInterval > 0;

        Long2ObjectMap.FastEntrySet<ChunkAdapter> fastEntrySet = (Long2ObjectMap.FastEntrySet<ChunkAdapter>) this.cachedChunks.long2ObjectEntrySet();
        ObjectIterator<Long2ObjectMap.Entry<ChunkAdapter>> iterator = fastEntrySet.fastIterator();
        while ( iterator.hasNext() ) {
            Long2ObjectMap.Entry<ChunkAdapter> entry = iterator.next();
            ChunkAdapter chunk = entry.getValue();
            if ( checkChunkSave && chunk.isNeedsPersistance() && currentTimeMS - chunk.getLastSavedTimestamp() >= this.autoSaveInterval ) {
                chunk.setLastSavedTimestamp( currentTimeMS );
                this.world.saveChunkAsynchronously( chunk );
            }

            int currentX = (int) ( entry.getLongKey() >> 32 );
            int currentZ = (int) ( entry.getLongKey() ) + Integer.MIN_VALUE;

            // Check if this is part of the spawn
            if ( spawnAreaSize > 0 &&
                ( Math.abs( currentX ) - spawnXChunk <= spawnAreaSize &&
                    Math.abs( currentZ ) - spawnZChunk <= spawnAreaSize ) ) {
                continue;
            }

            // Calculate the hashes which are used by players view distances
            if ( this.tempHashes[0].contains( entry.getLongKey() ) ) {
                continue;
            }

            if ( chunk == null ) {
                chunk = this.cachedChunks.get( entry.getLongKey() );
            }

            // Ask this chunk if he wants to be gced
            if ( !chunk.canBeGCed( currentTimeMS ) ) {
                continue;
            }

            LOGGER.debug( "Cleaning up chunk @ {} {}", currentX, currentZ );

            // Ask this chunk if he wants to be gced
            this.tempHashes[1].add( entry.getLongKey() );
        }

        if ( !this.tempHashes[1].isEmpty() ) {
            LongIterator toRemoveCursor = this.tempHashes[1].iterator();
            while ( toRemoveCursor.hasNext() ) {
                this.cachedChunks.remove( toRemoveCursor.nextLong() );
            }
        }
    }

    // ==================================== CHUNK CACHE ==================================== //

    /**
     * Attempts to get the specified chunk.
     *
     * @param x The x-coordinate of the chunk
     * @param z The z-coordinate of the chunk
     * @return The chunk if it is loaded or null otherwise
     */
    public synchronized ChunkAdapter getChunk( int x, int z ) {
        long chunkHash = CoordinateUtils.toLong( x, z );
        return this.getChunkInternal( chunkHash );
    }

    /**
     * Puts the specified chunk into the cache thus making it available to the outside
     *
     * @param chunk The chunk to put into the cache
     */
    public synchronized void putChunk( ChunkAdapter chunk ) {
        long key = CoordinateUtils.toLong( chunk.getX(), chunk.getZ() );
        this.cachedChunks.put( key, chunk );
    }

    // ==================================== AUTOSAVE ==================================== //

    /**
     * Checks whether or not the auto-save feature is enabled.
     *
     * @return Whether or not the auto-save feature is enabled
     */
    public boolean isAutosaveEnabled() {
        return this.enableAutoSave;
    }

    /**
     * Gets the interval in milliseconds in which chunks get saved automatically.
     *
     * @return The auto-save interval
     */
    public long getAutoSaveInterval() {
        return this.autoSaveInterval;
    }

    /**
     * Get a chunk based on his hash
     *
     * @param chunkHash which should be get
     * @return chunk adapter for the given hash or null when the hash has no chunk attached
     */
    synchronized ChunkAdapter getChunkInternal( long chunkHash ) {
        return this.cachedChunks.get( chunkHash );
    }

    synchronized long[] getTickingChunks( float dT ) {
        this.lastFullTickDT += dT;
        if ( Values.CLIENT_TICK_RATE - this.lastFullTickDT < 0.005f ) {
            // We need to tick all chunks which haven't been ticked until now
            long[] returnVal = new long[this.cachedChunks.size()];
            int index = 0;

            Long2ObjectMap.FastEntrySet<ChunkAdapter> set = (Long2ObjectMap.FastEntrySet<ChunkAdapter>) this.cachedChunks.long2ObjectEntrySet();
            ObjectIterator<Long2ObjectMap.Entry<ChunkAdapter>> iterator = set.fastIterator();
            while ( iterator.hasNext() ) {
                long l = iterator.next().getLongKey();
                if ( !this.alreadyTicked.contains( l ) ) {
                    returnVal[index++] = l;
                }
            }

            this.lastFullTickDT = 0;
            this.alreadyTicked.clear();
            return Arrays.copyOf( returnVal, index );
        } else {
            // Check how many chunks we need to tick
            int max = this.cachedChunks.size();
            if ( max == 0 ) {
                return new long[0];
            }

            int needCurrent = MathUtils.fastFloor( max * ( this.lastFullTickDT / Values.CLIENT_TICK_RATE ) );

            // This only happens on first tick though
            if ( this.alreadyTicked.size() == needCurrent ) {
                return new long[0];
            }

            int needed = needCurrent - this.alreadyTicked.size();
            if ( needed <= 0 ) {
                return new long[0];
            }

            long[] returnVal = new long[needed];
            int index = 0;

            Long2ObjectMap.FastEntrySet<ChunkAdapter> set = (Long2ObjectMap.FastEntrySet<ChunkAdapter>) this.cachedChunks.long2ObjectEntrySet();
            ObjectIterator<Long2ObjectMap.Entry<ChunkAdapter>> iterator = set.fastIterator();
            while ( iterator.hasNext() ) {
                long l = iterator.next().getLongKey();
                if ( !this.alreadyTicked.contains( l ) ) {
                    returnVal[index++] = l;
                    this.alreadyTicked.add( l );

                    if ( index == needed ) {
                        break;
                    }
                }
            }

            if ( index == needed ) {
                return returnVal;
            }

            return Arrays.copyOf( returnVal, index );
        }
    }

    /**
     * Save all chunks and persist them to disk
     */
    synchronized void saveAll() {
        for ( long l : this.cachedChunks.keySet() ) {
            ChunkAdapter chunkAdapter = this.cachedChunks.get( l );
            this.world.saveChunk( chunkAdapter );
            chunkAdapter.setLastSavedTimestamp( this.world.getServer().getCurrentTickTime() );
        }
    }

    public synchronized void iterateAll( Consumer<ChunkAdapter> chunkConsumer ) {
        for ( long l : this.cachedChunks.keySet() ) {
            chunkConsumer.accept( this.cachedChunks.get( l ) );
        }
    }

    public synchronized int size() {
        return this.cachedChunks.size();
    }

}
