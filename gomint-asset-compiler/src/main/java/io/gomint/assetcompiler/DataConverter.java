package io.gomint.assetcompiler;

/**
 * @author geNAZt
 * @version 1.0
 */
public class DataConverter {

    private Pair<Integer, Converter>[] converter = new Pair[256];
    private Pair<Integer, Byte> convertedValue = new Pair<>( 0, (byte) 1 );

    public DataConverter() {
        addConverter( 29, 29, ( b, m ) -> (byte) (m > 7 ? m - 8 : m) );
        addConverter( 33, 33, ( b, m ) -> (byte) (m > 7 ? m - 8 : m) );
        addConverter( 34, 34, ( b, m ) -> (byte) (m > 7 ? m - 8 : m) );

        addConverter( 43, 43, ( b, m ) -> m == 6 ? 7 : m == 7 ? 6 : m );                                                        // Double Slab
        addConverter( 44, 44, ( b, m ) -> m == 6 ? 7 : m == 7 ? 6 : m == 14 ? 15 : m == 15 ? 14 : m );                          // Slab
        addConverter( 84, 25, ( b, m ) -> (byte) 0 );                                                                           // Jukebox
        addConverter( 85, 85, ( b, m ) -> (byte) 0 );                                                                           // Fence
        addConverter( 96, 96, ( b, m ) -> (byte) ( ( ( m & 0x04 ) << 1 ) | ( ( m & 0x08 ) >> 1 ) | ( 3 - ( m & 0x03 ) ) ) );    // Trapdoor
        addConverter( 167, 167, ( b, m ) -> (byte) ( ( ( m & 0x04 ) << 1 ) | ( ( m & 0x08 ) >> 1 ) | ( 3 - ( m & 0x03 ) ) ) );  // Iron trapdoor
        addConverter( 143, 143, ( b, m ) -> {                                                                                   // Wooden button
            // Check if button has been pressed
            boolean pressed = false;
            if ( ( m & 0x08 ) == 0x08 ) {
                pressed = true;
                m ^= 0x08;
            }

            switch ( m ) {
                case 0:
                    return (byte) ( ( pressed ) ? 8 : 0 ); // 0
                case 1:
                    return (byte) ( 5 + ( ( pressed ) ? 8 : 0 ) ); // 5
                case 2:
                    return (byte) ( 4 + ( ( pressed ) ? 8 : 0 ) ); // 4
                case 3:
                    return (byte) ( 3 + ( ( pressed ) ? 8 : 0 ) ); // 3
                case 4:
                    return (byte) ( 2 + ( ( pressed ) ? 8 : 0 ) ); // 2
                case 5:
                    return (byte) ( 1 + ( ( pressed ) ? 8 : 0 ) ); // 1
            }

            return (byte) ( ( pressed ) ? 8 : 0 );
        } );

        addConverter( 77, 77, ( b, m ) -> {                                                                                     // Stone Button
            // Check if button has been pressed
            boolean pressed = false;
            if ( ( m & 0x08 ) == 0x08 ) {
                pressed = true;
                m ^= 0x08;
            }

            switch ( m ) {
                case 0:
                    return (byte) ( ( pressed ) ? 8 : 0 ); // 0
                case 1:
                    return (byte) ( 5 + ( ( pressed ) ? 8 : 0 ) ); // 5
                case 2:
                    return (byte) ( 4 + ( ( pressed ) ? 8 : 0 ) ); // 4
                case 3:
                    return (byte) ( 3 + ( ( pressed ) ? 8 : 0 ) ); // 3
                case 4:
                    return (byte) ( 2 + ( ( pressed ) ? 8 : 0 ) ); // 2
                case 5:
                    return (byte) ( 1 + ( ( pressed ) ? 8 : 0 ) ); // 1
            }

            return (byte) ( ( pressed ) ? 8 : 0 );
        } );

        addConverter( 166, 416, ( b, m ) -> (byte) 0 );                                                                          // Barrier

        // New fences
        addConverter( 188, 0, ( b, m ) -> (byte) 1 );
        addConverter( 189, 0, ( b, m ) -> (byte) 2 );
        addConverter( 190, 0, ( b, m ) -> (byte) 3 );
        addConverter( 191, 0, ( b, m ) -> (byte) 4 );
        addConverter( 192, 0, ( b, m ) -> (byte) 5 );
    }

    private void addConverter( int sourceId, int destId, Converter converter ) {
        this.converter[sourceId] = new Pair<>( destId, converter );
    }

    public boolean hasConverter( int id ) {
        return this.converter[id] != null;
    }

    public Pair<Integer, Byte> convert( int blockId, byte metaData ) {
        Pair<Integer, Converter> converterPair = this.converter[blockId];
        if ( converterPair == null ) {
            return null;
        } else {
            this.convertedValue.setFirst( converterPair.getFirst() );
            this.convertedValue.setSecond( converterPair.getSecond().convert( blockId, metaData ) );
            return this.convertedValue;
        }
    }

    private interface Converter {
        byte convert( int blockId, byte metaData );
    }

}
